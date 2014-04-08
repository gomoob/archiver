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
import com.gomoob.archiver.configuration.store.IAdditionalConfiguration;
import com.gomoob.archiver.configuration.store.Store;

/**
 * Command used to post / upload an archive into an Amazon Glacier vault in a single operation.
 * <p>
 * WARNNIG: It is not advised to use this command for large files because it does not allow to stop / resume your
 * uploads and will not allow you to inspect the upload progression.
 * </p>
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public class PostArchiveCommand extends AbstractGlacierCommand {

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

        // The upload is performed using the archiver configuration
        if (commandLine.hasOption("a-store-id") || commandLine.hasOption("a-archive-id")) {

            // Gets the archive file to upload
            File archiveFile = null;

            try {

                archiveFile = this.getArchiveLocator().locate(commandLine);

            } catch (IOException e1) {

                // TODO Auto-generated catch block
                e1.printStackTrace();

            }

            //@formatter:off
                ArchiveTransferManager archiveTransferManager = new ArchiveTransferManager(
                    this.getAmazonGlacierClient(),
                    this.getAWSCredentials());
                //@formatter:on

            try {

                //@formatter:off
                    UploadResult uploadResult = archiveTransferManager.upload(
                        this.getVaultName(), 
                        "Mon archive",
                        archiveFile
                    );
                    //@formatter:on

                System.out.println(uploadResult.getArchiveId());

            } catch (AmazonServiceException ase) {

                // TODO Auto-generated catch block
                ase.printStackTrace();

            } catch (AmazonClientException ace) {

                // TODO Auto-generated catch block
                ace.printStackTrace();

            } catch (FileNotFoundException fnfe) {

                // TODO Auto-generated catch block
                fnfe.printStackTrace();

            }

        }

    }

}
