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
     * Utility function used to create an Amazon Glacier Client from credentials.
     * 
     * @param credentials the credentials used to create the Amazon Glacier Client.
     * @return the resulting Amazon Glacier Client.
     */
    protected AmazonGlacierClient createAmazonGlacierClient(Credentials credentials) {

        return new AmazonGlacierClient(this.createAWSCredentials(credentials));

    }

    /**
     * Utility function used to create an Amazon Glacier client from the informations of a store.
     * 
     * @param store the store from which one to get informations to create an Amazon Glacier Client.
     * @return the resulting Amazon Glacier client.
     */
    protected AmazonGlacierClient createAmazonGlacierClient(Store store) {

        AmazonGlacierClient amazonGlacierClient = this.createAmazonGlacierClient(store.getCredentials());
        amazonGlacierClient.setEndpoint(((GlacierAdditionalConfiguration) store.getAdditionalConfiguration())
                .getEndpoint());

        return amazonGlacierClient;

    }
    
    /**
     * Utility function used to create AWS Credentials from credentials.
     * 
     * @param credentials the credentials used to create the AWS credentials.
     * 
     * @return the resulting AWS credentials.
     */
    protected AWSCredentials createAWSCredentials(Credentials credentials) {
        
        return new BasicAWSCredentials(credentials.getKey(), credentials.getSecret());
        
    }

}
