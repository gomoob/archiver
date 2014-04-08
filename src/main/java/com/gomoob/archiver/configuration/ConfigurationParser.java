package com.gomoob.archiver.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.utils.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gomoob.archiver.component.glacier.configuration.store.GlacierAdditionalConfigurationParser;
import com.gomoob.archiver.configuration.archive.Archive;
import com.gomoob.archiver.configuration.archive.Src;
import com.gomoob.archiver.configuration.archive.Type;
import com.gomoob.archiver.configuration.credentials.Credentials;
import com.gomoob.archiver.configuration.store.IAdditionalConfiguration;
import com.gomoob.archiver.configuration.store.IAdditionalConfigurationParser;
import com.gomoob.archiver.configuration.store.Store;

/**
 * Parser used to parse an archiver configuration file.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public class ConfigurationParser {

    /**
     * A map which maps credentials identifiers to credentials.
     */
    private Map<String, Credentials> credentialsMap = new HashMap<String, Credentials>();
    
    public void parse(File configurationFile) {

    }

    /**
     * Function used to parse a configuration file as an input stream.
     * 
     * @param configurationStream the input stream to parse.
     * @return the resulting configuration object.
     * @throws ParseException
     * @throws IOException
     */
    public Configuration parse(InputStream configurationStream) throws ParseException, IOException {

        String configurationString = new String(IOUtils.toByteArray(configurationStream));

        return this.parse(configurationString);

    }

    /**
     * Function used to parse a configuration file as a string.
     * 
     * @param configurationString the content of the configuration file to parse.
     * @return the resulting configuration object.
     * @throws ParseException
     */
    public Configuration parse(String configurationString) throws ParseException {

        Configuration configuration = new Configuration();

        JSONObject jsonConfiguration = new JSONObject(configurationString);

        // Parse the 'credentials'
        List<Credentials> credentialsList = this.parseCredentialsArray(jsonConfiguration);
        configuration.setCredentialsList(credentialsList);
        
        // Parse the 'stores'
        List<Store> stores = this.parseStores(jsonConfiguration);
        configuration.setStores(stores);

        // Parse the 'archives'
        List<Archive> archives = this.parseArchives(jsonConfiguration);
        configuration.setArchives(archives);

        return configuration;

    }

    private Archive parseArchive(JSONObject archiveJsonObject) throws ParseException {

        Archive archive = new Archive();

        try {

            archive.setCwd(archiveJsonObject.getString("cwd"));

        } catch (JSONException jsonException) {

            throw new ParseException("The 'cwd' attribute is 'null' or not provided !", jsonException);

        }

        try {

            archive.setDst(archiveJsonObject.getString("dst"));

        } catch (JSONException jsonException) {

            throw new ParseException("The 'dst' attribute is 'null' or not provided !", jsonException);

        }
        
        try {

            archive.setId(archiveJsonObject.getString("id"));

        } catch (JSONException jsonException) {

            throw new ParseException("The 'id' attribute is 'null' or not provided !", jsonException);

        }

        Object srcObject = null;
        Src src = new Src();

        try {

            srcObject = archiveJsonObject.get("src");

            if (srcObject instanceof String) {

                src.setGlobbingPattern((String) srcObject);

            } else if (srcObject instanceof JSONArray) {

                JSONArray jsonArray = (JSONArray) srcObject;
                List<String> globbingPatterns = new ArrayList<String>();

                for (int i = 0; i < jsonArray.length(); ++i) {

                    globbingPatterns.add(jsonArray.getString(i));

                }

                src.setGlobbingPatterns(globbingPatterns);

            } else {

            }

            archive.setSrc(src);

        } catch (JSONException jsonException) {

            throw new ParseException("The 'src' attribute is 'null' or not provided !", jsonException);

        }

        String type = null;

        try {

            type = archiveJsonObject.getString("type");

            if (type.toLowerCase().trim() == "zip") {

                archive.setType(Type.ZIP);

            } else if (type.toLowerCase().trim() == "tar.gz") {

                archive.setType(Type.TAR_GZ);

            } else {

                // TODO: Exception...

            }

        } catch (JSONException jsonException) {

            throw new ParseException("The 'type' attribute is 'null' or not provided !", jsonException);

        }

        return archive;

    }

    /**
     * Function used to parse the 'archives' declaration in the configuration file.
     * 
     * @throws ParseException
     */
    private List<Archive> parseArchives(JSONObject jsonConfiguration) throws ParseException {

        List<Archive> archives = new ArrayList<Archive>();

        // If an 'archives' declaration is available
        if (jsonConfiguration.has("archives")) {

            JSONArray archivesJsonArray = jsonConfiguration.getJSONArray("archives");

            for (int i = 0; i < archivesJsonArray.length(); ++i) {

                JSONObject archiveJsonObject = archivesJsonArray.getJSONObject(i);

                Archive archive = null;

                try {

                    archive = this.parseArchive(archiveJsonObject);

                } catch (ParseException parseException) {

                    throw new ParseException("Error encountered in archive declaration at position \'" + (i + 1)
                            + "\' !", parseException);

                }

                archives.add(archive);

            }

        }

        return archives;

    }

    private Credentials parseCredentials(JSONObject credentialsJsonObject) throws ParseException {

        Credentials credentials = new Credentials();

        try {

            credentials.setId(credentialsJsonObject.getString("id"));

        } catch (JSONException jsonException) {

            throw new ParseException("The 'id' attribute is 'null' or not provided !", jsonException);

        }
        
        try {

            credentials.setKey(credentialsJsonObject.getString("key"));

        } catch (JSONException jsonException) {

            throw new ParseException("The 'key' attribute is 'null' or not provided !", jsonException);

        }

        try {

            credentials.setSecret(credentialsJsonObject.getString("secret"));

        } catch (JSONException jsonException) {

            throw new ParseException("The 'secret' attribute is 'null' or not provided !", jsonException);

        }

        return credentials;

    }

    private List<Credentials> parseCredentialsArray(JSONObject jsonConfiguration) throws ParseException {

        // Clears the credentials map
        this.credentialsMap.clear();
        
        List<Credentials> credentialsArray = new ArrayList<Credentials>();

        // If an 'credentials' declaration is available
        if (jsonConfiguration.has("credentials")) {

            JSONArray credentialsJsonArray = jsonConfiguration.getJSONArray("credentials");

            for (int i = 0; i < credentialsJsonArray.length(); ++i) {

                JSONObject credentialsJsonObject = credentialsJsonArray.getJSONObject(i);

                Credentials credentials = null;

                try {

                    credentials = this.parseCredentials(credentialsJsonObject);

                } catch (ParseException parseException) {

                    throw new ParseException("Error encountered in credentials declaration at position \'" + (i + 1)
                            + "\' !", parseException);

                }

                credentialsArray.add(credentials);
                
                this.credentialsMap.put(credentials.getId(), credentials);

            }

        }

        return credentialsArray;

    }

    private Store parseStore(JSONObject storeJsonObject) throws ParseException {

        Store store = new Store();

        if(storeJsonObject.has("credentials")) {
            
            Object credentialsObject = storeJsonObject.get("credentials");

            if(credentialsObject instanceof String) {
                
                Credentials credentials = this.credentialsMap.get(credentialsObject);
                
                if(credentials == null) {
                    
                    // TODO: Exception
                    
                }
                
                store.setCredentials(credentials);                
            
            } else {
            
                // TODO: Parsing de l'objet credentials...
                
            }
            
        }

        try {

            store.setId(storeJsonObject.getString("id"));

        } catch (JSONException jsonException) {

            throw new ParseException("The 'id' attribute is 'null' or not provided !", jsonException);

        }

        try {

            store.setType(storeJsonObject.getString("type"));

        } catch (JSONException jsonException) {

            throw new ParseException("The 'type' attribute is 'null' or not provided !", jsonException);

        }

        if (storeJsonObject.has("additionalConfiguration")) {

            if (store.getType().equals("glacier")) {

                IAdditionalConfigurationParser additionalConfigurationParser = new GlacierAdditionalConfigurationParser();
                IAdditionalConfiguration additionalConfiguration = additionalConfigurationParser.parse(storeJsonObject
                        .getJSONObject("additionalConfiguration"));
                store.setAdditionalConfiguration(additionalConfiguration);

            }

        }

        return store;

    }

    private List<Store> parseStores(JSONObject jsonConfiguration) throws ParseException {

        List<Store> stores = new ArrayList<Store>();

        // If an 'stores' declaration is available
        if (jsonConfiguration.has("stores")) {

            JSONArray storesJsonArray = jsonConfiguration.getJSONArray("stores");

            for (int i = 0; i < storesJsonArray.length(); ++i) {

                JSONObject storeJsonObject = storesJsonArray.getJSONObject(i);

                Store store = null;

                try {

                    store = this.parseStore(storeJsonObject);

                } catch (ParseException parseException) {

                    throw new ParseException(
                            "Error encountered in store declaration at position \'" + (i + 1) + "\' !", parseException);

                }

                stores.add(store);

            }

        }

        return stores;

    }

}
