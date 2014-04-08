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
package com.gomoob.archiver.component.glacier.command;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.gomoob.archiver.component.AbstractCommand;
import com.gomoob.archiver.component.glacier.configuration.store.GlacierAdditionalConfiguration;
import com.gomoob.archiver.configuration.credentials.Credentials;
import com.gomoob.archiver.configuration.store.Store;

/**
 * Abstract class common to all Glacier commands.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public abstract class AbstractGlacierCommand extends AbstractCommand {

    /**
     * The configured Amazon Glacier client.
     */
    private AmazonGlacierClient amazonGlacierClient;

    /**
     * The configured AWS credentials.
     */
    private AWSCredentials awsCredentials;

    protected Store findAmazonGlacierStoreById(String storeId) {

        // Try to find a store having the specified identifier
        Store store = this.findStoreById(storeId);

        // The store must be a glacier store
        if (!store.getType().equals("glacier")) {

            throw new IllegalStateException("The store having an identifier equal to '" + storeId
                    + "' is not a glacier store !");

        }

        return store;

    }

    /**
     * Gets the Amazon Glacier Client which have been configured with the Archiver configuration file or the command
     * line parameters.
     * <p>
     * This function scans the Archiver configuration file and the command line arguments to automatically create the
     * Amazon Glacier store which is configured.
     * </p>
     * 
     * @return the configured Amazon Glacier Client.
     */
    protected AmazonGlacierClient getAmazonGlacierClient() {

        // If the Amazon Glacier Client has not been created
        if (this.amazonGlacierClient == null) {

            this.amazonGlacierClient = new AmazonGlacierClient(this.getAWSCredentials());
            this.amazonGlacierClient.setEndpoint(this.getEndpoint());

        }

        return this.amazonGlacierClient;

    }

    /**
     * Gets the Amazon AWS Credentials which have been configured with the Archiver configuration file or the command
     * line parameters.
     * <p>
     * This function scans the Archiver configuration file and the command line arguments to automatically create the
     * AWS Credentials.
     * </p>
     * 
     * @return the configured AWS Credentials.
     */
    protected AWSCredentials getAWSCredentials() {

        // The AWS credentials have not been created
        if (this.awsCredentials == null) {

            // Gets the generic credentials
            Credentials credentials = this.getCredentials();

            this.awsCredentials = new BasicAWSCredentials(credentials.getKey(), credentials.getSecret());

        }

        return this.awsCredentials;

    }

    /**
     * Gets the Amazon Glacier Store which have been configured with the Archiver configuration file or the command line
     * parameters.
     * <p>
     * This function scans the Archiver configuration file and the command line arguments to automatically create the
     * Amazon Glacier store which is configured.
     * </p>
     * 
     * @return the configured Amazon Glacier Store.
     */
    protected Store getAmazonGlacierStore() {

        return this.findAmazonGlacierStoreById(this.getCommandLine().getOptionValue("a-store-id"));

    }

    /**
     * Gets the Amazon Glacier Endpoint which have been configured with the Archiver configuration file or the command
     * line parameters.
     * <p>
     * This function scans the Archiver configuration file and the command line arguments to automatically create the
     * Amazon Glacier vault wich is configured.
     * </p>
     * <p>
     * NOTE: This function can return a <code>null</code> value, in this case the default Amazon Glacier Endpoint
     * "glacier.us-east-1.amazonaws.com" will be used.
     * </p>
     * 
     * @return the configured Amazon Glacier Endpoint or <code>null</code>.
     */
    protected String getEndpoint() {

        // The default Amazon Glacier endpoint (this is taken from the AmazonGlacierClient class and is the default
        // provided by the Amazon AWS SDK)
        String endpoint = "glacier.us-east-1.amazonaws.com";

        if (this.commandLine.hasOption("a-store-id")) {

            // Gets the configured Amazon Glacier Endpoint
            Store store = this.getAmazonGlacierStore();

            endpoint = ((GlacierAdditionalConfiguration) store.getAdditionalConfiguration()).getEndpoint();

        }
        

        return endpoint;

    }

    /**
     * Gets the Amazon Glacier Vault name which have been configured with the Archiver configuration file or the command
     * line parameters.
     * <p>
     * This function scans the Archiver configuration file and the command line arguments to automatically create the
     * Amazon Glacier vault name which is configured.
     * </p>
     * 
     * @return the configured Amazon Glacier Vault Name.
     */
    protected String getVaultName() {

        // Gets the configured Amazon Glacier Endpoint
        Store store = this.getAmazonGlacierStore();

        return ((GlacierAdditionalConfiguration) store.getAdditionalConfiguration()).getVaultName();

    }

}
