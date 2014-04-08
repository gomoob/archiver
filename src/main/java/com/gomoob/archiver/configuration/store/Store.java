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
package com.gomoob.archiver.configuration.store;

import com.gomoob.archiver.configuration.credentials.Credentials;

/**
 * Class which represents the configuration of a store.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public class Store {

    /**
     * An additional configuration attached to this store.
     */
    private IAdditionalConfiguration additionalConfiguration;

    /**
     * The credentials used to access to the store.
     */
    private Credentials credentials;
    
    /**
     * The uniq identifier of the store.
     */
    private String id;
    
    /**
     * The type of the store.
     */
    private String type;

    /**
     * Gets the additional configuration attached to this store.
     * 
     * @return the additional configuration attached to this store.
     */
    public IAdditionalConfiguration getAdditionalConfiguration() {
        
        return this.additionalConfiguration;
        
    }

    /**
     * Gets the credentials used to access to the store.
     * 
     * @return the credentials used to access to the store.
     */
    public Credentials getCredentials() {

        return this.credentials;

    }

    /**
     * Gets the uniq identfier of the store.
     * 
     * @return the uniq identifier of the store.
     */
    public String getId() {

        return this.id;

    }

    /**
     * Gets the type of the store.
     * 
     * @return the type of the store.
     */
    public String getType() {

        return this.type;

    }

    /**
     * Sets the additional configuration attached to this store.
     * 
     * @param additionalConfiguration the additional configuration attached to this store.
     */
    public void setAdditionalConfiguration(IAdditionalConfiguration additionalConfiguration) {
        
        this.additionalConfiguration = additionalConfiguration;
        
    }

    /**
     * Sets the credentials used to access to the store.
     * 
     * @param credentials the credentials used to access to the store.
     */
    public void setCredentials(Credentials credentials) {

        this.credentials = credentials;

    }

    /**
     * Sets the uniq identifier of the store.
     */
    public void setId(String id) {

        this.id = id;

    }

    /**
     * Sets the type of the store.
     * 
     * @param type the type of the store.
     */
    public void setType(String type) {

        this.type = type;

    }

}
