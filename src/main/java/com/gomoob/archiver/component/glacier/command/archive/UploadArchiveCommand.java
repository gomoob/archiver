package com.gomoob.archiver.component.glacier.command.archive;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.glacier.transfer.UploadResult;
import com.gomoob.archiver.component.glacier.command.AbstractGlacierCommand;
import com.gomoob.archiver.component.glacier.configuration.store.GlacierAdditionalConfiguration;
import com.gomoob.archiver.compressor.ICompressor;
import com.gomoob.archiver.configuration.archive.Archive;
import com.gomoob.archiver.configuration.store.Store;

/**
 * Command used to upload an archive into an Amazon Glacier vault.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public class UploadArchiveCommand extends AbstractGlacierCommand {

    /**
     * {@inheritDoc}
     */
    @Override
    public void processCommand(String[] args) {

        Options options = new Options();
        options.addOption(this.createAArchiveIdOption());
        options.addOption(this.createAStoreIdOption());
        options.addOption(this.createHelpOption());
        
        CommandLineParser commandLineParser = new PosixParser();

        try {

            CommandLine commandLine = commandLineParser.parse(options, args);

            if (commandLine.getOptions().length == 1 && commandLine.hasOption("help")) {

                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("archiver --glacier --upload", options);

            }

            // The upload is performed using the archiver configuration
            if (commandLine.hasOption("a-store-id") || commandLine.hasOption("a-archive-id")) {

                String archiveId = commandLine.getOptionValue("a-archive-id");
                String storeId = commandLine.getOptionValue("a-store-id");

                Archive archive = this.configuration.findArchiveById(archiveId);
                Store store = this.configuration.findStoreById(storeId);
                
                File zippedArchive = null;
                ICompressor compressor = this.getCompressor(archive.getType());
                
                try {
                    
                    zippedArchive = compressor.compress(archive);
                    
                } catch (IOException ioException) {
                    
                    // TODO Auto-generated catch block
                    ioException.printStackTrace();
                
                }
                
                GlacierAdditionalConfiguration gac = (GlacierAdditionalConfiguration) store.getAdditionalConfiguration();
                
                AmazonGlacierClient amazonGlacierClient = this.createAmazonGlacierClient(store);
                AWSCredentials awsCredentials = this.createAWSCredentials(store.getCredentials());
                ArchiveTransferManager archiveTransferManager = new ArchiveTransferManager(amazonGlacierClient, awsCredentials);

                try {
                    
                    UploadResult uploadResult = archiveTransferManager.upload(gac.getVaultName(), "Mon archive", zippedArchive);
                    
                    System.out.println(uploadResult.getArchiveId());

                } catch (AmazonServiceException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (AmazonClientException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
