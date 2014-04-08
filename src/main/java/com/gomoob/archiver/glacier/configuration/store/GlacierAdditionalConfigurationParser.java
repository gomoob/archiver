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
