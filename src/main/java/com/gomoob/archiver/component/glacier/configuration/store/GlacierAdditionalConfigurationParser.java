package com.gomoob.archiver.component.glacier.configuration.store;

import org.json.JSONObject;

import com.gomoob.archiver.configuration.store.IAdditionalConfiguration;
import com.gomoob.archiver.configuration.store.IAdditionalConfigurationParser;

public class GlacierAdditionalConfigurationParser implements IAdditionalConfigurationParser {

    /**
     * {@inheritDoc}
     */
    @Override
    public IAdditionalConfiguration parse(JSONObject jsonObject) {

        GlacierAdditionalConfiguration additionalConfiguration = new GlacierAdditionalConfiguration();

        additionalConfiguration.setEndpoint(jsonObject.getString("endpoint"));
        additionalConfiguration.setVaultName(jsonObject.getString("vaultName"));

        return additionalConfiguration;

    }

}
