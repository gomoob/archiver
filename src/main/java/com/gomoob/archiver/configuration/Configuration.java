package com.gomoob.archiver.configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gomoob.archiver.configuration.archive.Archive;
import com.gomoob.archiver.configuration.credentials.Credentials;
import com.gomoob.archiver.configuration.store.Store;

/**
 * Class which represents a global configuration of the archiver.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public class Configuration {

    /**
     * The list of archives configured in the archiver.
     */
    private List<Archive> archives;
    
    /**
     * A mape used to quickly find archives by identifier.
     */
    private Map<String, Archive> archivesMap = new HashMap<String, Archive>();

    /**
     * The list of credentials configured in the archiver.
     */
    private List<Credentials> credentialsList;

    /**
     * A map used t quickly find credentials by credentials identifier.
     */
    private Map<String, Credentials> credentialsMap = new HashMap<String, Credentials>();

    /**
     * The list of stores configured in the archiver.
     */
    private List<Store> stores;

    /**
     * A map used to quickly find stores by store identifier.
     */
    private Map<String, Store> storesMap = new HashMap<String, Store>();

    /**
     * Function used to find an archive by identifier.
     * 
     * @param archiveId the identifier of the archive to find.
     * 
     * @return the found archives or <code>null</code>.
     */
    public Archive findArchiveById(String archiveId) {
        
        return this.archivesMap.get(archiveId);
        
    }
    
    /**
     * Function used to find credentials by identifier.
     * 
     * @param credentialsId the identifier of the credentials to find.
     * 
     * @return the found credentials or <code>null</code>.
     */
    public Credentials findCredentialsById(String credentialsId) {
        
        return this.credentialsMap.get(credentialsId);
        
    }
    
    /**
     * Function used to find a store by identifier.
     * 
     * @param storeId the identifier of the store to find.
     * @return the found store or <code>null</code>.
     */
    public Store findStoreById(String storeId) {

        return this.storesMap.get(storeId);

    }

    /**
     * Gets the list of archives configured in the archiver.
     * 
     * @return the list of archives archives created in the archiver.
     */
    public List<Archive> getArchives() {

        return this.archives;

    }

    /**
     * Gets the list of credentials configured in the archiver.
     * 
     * @return the list of credentials configured in the archiver.
     */
    public List<Credentials> getCredentialsList() {

        return this.credentialsList;

    }

    /**
     * Gets the list of stores configured in the archiver.
     * 
     * @return the list of stores configured in the archiver.
     */
    public List<Store> getStores() {

        return this.stores;

    }

    /**
     * Sets the list of archives configured in the archiver.
     * 
     * @param archives the list of archives configured in the archiver.
     */
    public void setArchives(List<Archive> archives) {

        this.archives = archives;
        
        // Updates the archives map
        this.archivesMap.clear();

        for (Archive archive : archives) {

            this.archivesMap.put(archive.getId(), archive);

        }

    }

    /**
     * Sets the list of credentials configured in the archiver.
     * 
     * @param credentialsList the list of credentials configured in the archiver.
     */
    public void setCredentialsList(List<Credentials> credentialsList) {

        this.credentialsList = credentialsList;

        // Updates the credentials map
        this.credentialsMap.clear();

        for (Credentials credentials : credentialsList) {

            this.credentialsMap.put(credentials.getId(), credentials);

        }

    }

    /**
     * Sets the list of stores configured in the archiver.
     * 
     * @param stores the list of stores configured in the archiver.
     */
    public void setStores(List<Store> stores) {

        this.stores = stores;

        // Updates the stores map
        this.storesMap.clear();

        for (Store store : stores) {

            this.storesMap.put(store.getId(), store);

        }

    }

}
