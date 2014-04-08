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
