package com.gomoob.archiver.glacier.command.multipartupload;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.glacier.TreeHashGenerator;
import com.amazonaws.services.glacier.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.glacier.model.CompleteMultipartUploadResult;
import com.amazonaws.services.glacier.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.glacier.model.InitiateMultipartUploadResult;
import com.amazonaws.services.glacier.model.UploadMultipartPartRequest;
import com.amazonaws.services.glacier.model.UploadMultipartPartResult;
import com.amazonaws.util.BinaryUtils;
import com.gomoob.archiver.ArchiveFile;
import com.gomoob.archiver.glacier.command.AbstractGlacierCommand;

/**
 * Glacier command used to perform multipart uploads.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public class MultipartUploadCommand extends AbstractGlacierCommand {

    /**
     * The archive file to upload.
     */
    private ArchiveFile archiveFile;

    /**
     * The default part size used for multipart uploads (1MB).
     */
    private String partSize = "1048576";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doConfigureOptions(Options options) {

        options.addOption(this.createAArchiveIdOption());
        options.addOption(this.createAStoreIdOption());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doExecute(CommandLine commandLine) {

        try {

            // The upload is performed using the archiver configuration
            if (commandLine.hasOption("a-store-id") || commandLine.hasOption("a-archive-id")) {

                // Gets the archive file to upload
                this.archiveFile = this.getArchiveFile();

                // Creates the archive dst name
                String archiveDstName = this.createArchiveDstName(this.archiveFile);

                System.out.println("Destination archive name will be '" + archiveDstName + "'.");
                
                //@formatter:off
                String uploadId = this.initiateMultipartUpload(
                    archiveDstName,
                    this.getPartSize()
                );

                String checksum = this.uploadParts(uploadId);
                
                 this.completeMultiPartUpload(uploadId, checksum);
                // @formatter:on

            }

        } catch (AmazonServiceException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (AmazonClientException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {

            // If the archive file has to be delete after processing we delete it
            if (this.archiveFile != null && this.archiveFile.isToBeDeletedAfterProcess()) {

                this.archiveFile.delete();

            }

        }

    }

    private String completeMultiPartUpload(String uploadId, String checksum) throws NoSuchAlgorithmException,
            IOException {

        //@formatter:off
        CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest()
                .withVaultName(this.getVaultName())
                .withUploadId(uploadId)
                .withChecksum(checksum)
                .withArchiveSize(String.valueOf(this.archiveFile.length()));
        //@formatter:on

        CompleteMultipartUploadResult compResult = this.getAmazonGlacierClient().completeMultipartUpload(compRequest);

        return compResult.getLocation();

    }

    /**
     * Gets the part size which have been configured.
     * 
     * @return the part size which have been configured.
     */
    private String getPartSize() {

        return this.partSize;

    }

    /**
     * Function used to initiate a new Multipart Upload.
     * 
     * @param archiveDescription the description of the archive to upload.
     * @param partSize the part size of the parts to upload.
     * @return the Amazon Glacier identifier of the archive to upload.
     */
    private String initiateMultipartUpload(String archiveDescription, String partSize) {

        //@formatter:off
        InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest()
            .withVaultName(this.getVaultName())
            .withArchiveDescription(archiveDescription)
            .withPartSize(partSize);
        //@formatter:on

        InitiateMultipartUploadResult initiateMultipartUploadResult = this.getAmazonGlacierClient()
                .initiateMultipartUpload(initiateMultipartUploadRequest);

        System.out.println("Location : " + initiateMultipartUploadResult.getLocation());
        System.out.println("ArchiveID : " + initiateMultipartUploadResult.getUploadId());

        return initiateMultipartUploadResult.getUploadId();

    }

    /**
     * @param uploadId
     * @return
     * @throws AmazonServiceException
     * @throws NoSuchAlgorithmException
     * @throws AmazonClientException
     * @throws IOException
     */
    private String uploadParts(String uploadId) throws AmazonServiceException, NoSuchAlgorithmException,
            AmazonClientException, IOException {

        // A position or index of the current byte from which one to read data in the archive to upload, the archive
        // reading end when this position reach the archive file length in bytes.
        long currentPosition = 0;

        // A byte buffer which has a size which is equal to the multipart part size in bytes
        byte[] buffer = new byte[Integer.valueOf(this.getPartSize())];

        List<byte[]> binaryChecksums = new LinkedList<byte[]>();

        double percentage = 0.0;
        int roundedPercentage = 0;

        FileInputStream fileToUpload = new FileInputStream(this.archiveFile);
        String contentRange;
        int read = 0;

        while (currentPosition < this.archiveFile.length()) {

            read = fileToUpload.read(buffer, 0, buffer.length);

            if (read == -1) {
                break;
            }

            byte[] bytesRead = Arrays.copyOf(buffer, read);

            contentRange = String.format("bytes %s-%s/*", currentPosition, currentPosition + read - 1);
            String checksum = TreeHashGenerator.calculateTreeHash(new ByteArrayInputStream(bytesRead));
            byte[] binaryChecksum = BinaryUtils.fromHex(checksum);
            binaryChecksums.add(binaryChecksum);

            System.out.println(contentRange);

            //@formatter:off
            UploadMultipartPartRequest partRequest = new UploadMultipartPartRequest()
                .withVaultName(this.getVaultName())
                .withBody(new ByteArrayInputStream(bytesRead))
                .withChecksum(checksum)
                .withRange(contentRange)
                .withUploadId(uploadId);
            //formatter:on
            
            UploadMultipartPartResult partResult = this.getAmazonGlacierClient().uploadMultipartPart(partRequest);

            System.out.println("Part uploaded, checksum: " + partResult.getChecksum());

            currentPosition = currentPosition + read;
            
            // Updates the percentage
            percentage = (double)currentPosition / (double) this.archiveFile.length() * 100;
            
            // If the rounded percentage is greater than the previous rounded percentage we display the updated 
            // percentage
            if((int) percentage > roundedPercentage) {

                roundedPercentage = (int) percentage;
                System.out.println(new Date().toString() + " - " + roundedPercentage + "%");
                
            }
            
        }
        
        fileToUpload.close();
        
        String checksum = TreeHashGenerator.calculateTreeHash(binaryChecksums);
        
        return checksum;
        
    }


}
