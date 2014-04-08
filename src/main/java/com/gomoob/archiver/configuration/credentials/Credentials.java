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
package com.gomoob.archiver.configuration.credentials;

public class Credentials {

    /**
     * The uniq identifier of the credentials.
     */
    private String id;
    
    /**
     * The key / login.
     */
    private String key;
    
    /**
     * The secret / password.
     */
    private String secret;
    
    /**
     * Gets the uniq identifier of the credentials.
     * 
     * @return the uniq identifier of the credentials.
     */
    public String getId() {
        
        return this.id;
        
    }

    /**
     * Gets the key / login.
     * 
     * @return the key / login.
     */
    public String getKey() {

        return this.key;

    }

    /**
     * Gets the secret / password.
     * 
     * @return the secret / password.
     */
    public String getSecret() {

        return this.secret;

    }

    /**
     * Sets the uniq identifier of the credentials.
     * 
     * @param id the uniq identifier of the credentials.
     */
    public void setId(String id) {
        
        this.id = id;
        
    }

    /**
     * Sets the key / login.
     * 
     * @param key the key / login.
     */
    public void setKey(String key) {

        this.key = key;

    }

    /**
     * Sets the secret / password.
     * 
     * @param secret the secret / password.
     */
    public void setSecret(String secret) {

        this.secret = secret;

    }

}
