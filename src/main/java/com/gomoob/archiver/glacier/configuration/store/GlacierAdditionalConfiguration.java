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
package com.gomoob.archiver.glacier.configuration.store;

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
