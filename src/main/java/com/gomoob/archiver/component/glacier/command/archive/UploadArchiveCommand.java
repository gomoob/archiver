//@formatter:off

/**
 * (C) Copyright 2014, GOMOOB SARL (http://gomoob.com), All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General 
 * Public License as published by the Free Software Foundation; either version 3.0 of the License, or (at your option) 
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more 
 * details. You should have received a copy of the GNU Lesser General Public License along with this library.
 */

//@formatter:on
package com.gomoob.archiver.component.glacier.command.archive;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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

        try {

            CommandLine commandLine = this.parseCommandLine(options, args);

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

                GlacierAdditionalConfiguration gac = (GlacierAdditionalConfiguration) store
                        .getAdditionalConfiguration();

                AmazonGlacierClient amazonGlacierClient = this.createAmazonGlacierClient(store);
                AWSCredentials awsCredentials = this.createAWSCredentials(store.getCredentials());
                ArchiveTransferManager archiveTransferManager = new ArchiveTransferManager(amazonGlacierClient,
                        awsCredentials);

                try {

                    UploadResult uploadResult = archiveTransferManager.upload(gac.getVaultName(), "Mon archive",
                            zippedArchive);

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

    /**
     * Gets the archive file to upload.
     * 
     * @return the archive file to upload.
     * @throws IOException TODO: A documenter
     */
    private File getArchivePath() throws IOException {

        File archiveFile = null;
        
        // Providing both the 'a-archive-id' and 'archive-file' options is forbidden
        if (commandLine.hasOption("a-archive-id") && commandLine.hasOption("archive-file")) {

            throw new IllegalArgumentException(
                    "The options 'a-archive-id' and 'archive-file' cannot be provided together !");

        }

        // Build the archive path using the identifier of an archive described in an archiver configuration file
        else if (commandLine.hasOption("a-archive-id")) {

            String archiveId = commandLine.getOptionValue("a-archive-id");
            Archive archive = this.configuration.findArchiveById(archiveId);

            // No archive configuration has been found for the provided 'a-archive-id' parameter
            if (archive == null) {

                throw new IllegalArgumentException(
                        "No archive configuration has been found for the provided 'a-archive-id="
                                + commandLine.getOptionValue("a-archive-id") + "' parameter !");

            }
            
            ICompressor compressor = this.getCompressor(archive.getType());
            archiveFile = compressor.compress(archive);

        }

        // Build the archive path using an archive file path provided at command line
        else if (commandLine.hasOption("archive-file")) {

            archiveFile = new File(commandLine.getOptionValue("archive-file"));

            // The archive file must exist
            if (!archiveFile.exists()) {

                throw new IOException("The file '" + archiveFile.getAbsolutePath() + "' does not exist !");

            }

        }

        // Nothing allows us to create an archive file path
        else {

        }

        return archiveFile;

    }

}
