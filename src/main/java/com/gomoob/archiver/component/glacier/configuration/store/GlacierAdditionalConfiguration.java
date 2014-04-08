package com.gomoob.archiver.component.glacier.configuration.store;

import com.gomoob.archiver.configuration.store.IAdditionalConfiguration;

public class GlacierAdditionalConfiguration implements IAdditionalConfiguration {

    /**
     * The endpoint associated to the Amazon Glacier store.
     */
    private String endpoint;
    
    /**
     * The vault name associated to the Amazon Glacier store.
     */
    private String vaultName;
    
    /**
     * Gets the endpoint associated to the Amazon Glacier store.
     * 
     * @return the endpoint associated to the Amazon Glacier store.
     */
    public String getEndpoint() {
        
        return this.endpoint;
        
    }
    
    /**
     * Gets the vault name associated to the Amazon Glacier store.
     * 
     * @return the vault name associated to the Amazon Glacier store.
     */
    public String getVaultName() {
        
        return this.vaultName;
        
    }
    
    /**
     * Sets the endpoint associated to the Amazon Glacier store.
     * 
     * @param endpoint the endpoint associated to the Amazon Glacier store.
     */
    public void setEndpoint(String endpoint) {
        
        this.endpoint = endpoint;
        
    }
    
    /**
     * Sets the vault name associated to the Amazon Glacier store.
     * 
     * @param vaultName the vault name associated to the Amazon Glacier store.
     */
    public void setVaultName(String vaultName) {
        
        this.vaultName = vaultName;
        
    }
    
}
