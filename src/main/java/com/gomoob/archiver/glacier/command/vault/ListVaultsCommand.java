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
package com.gomoob.archiver.glacier.command.vault;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.json.JSONArray;
import org.json.JSONObject;

import com.amazonaws.services.glacier.model.DescribeVaultOutput;
import com.amazonaws.services.glacier.model.ListVaultsRequest;
import com.amazonaws.services.glacier.model.ListVaultsResult;
import com.gomoob.archiver.glacier.command.AbstractGlacierCommand;

/**
 * Command class used to list vaults owned by the calling user's account.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 * @see http://docs.aws.amazon.com/amazonglacier/latest/dev/api-vaults-get.html
 */
public class ListVaultsCommand extends AbstractGlacierCommand {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doConfigureOptions(Options options) {

        options.addOption(this.createACredentialsIdOption());

    }

    protected String doGetHelpHeader() {

        return "List all vaults in a specified region.";

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doExecute(CommandLine commandLine) {

        if (commandLine.hasOption("a-credentials-id")) {

            this.listVaultsUsingArchiverParameters(commandLine.getOptionValue("a-credentials-id"));

        }

    }

    /**
     * Function used to list the vaults owned by the calling user's account.
     * <p>
     * NOTE: The elements printed by this function are documented here :
     * http://docs.aws.amazon.com/amazonglacier/latest/dev/api-vaults-get.html
     * </p>
     * 
     * @param storeId the identifier of the archive store used to identify the vault to describe.
     */
    private void listVaultsUsingArchiverParameters(String credentialsId) {

        ListVaultsResult listVaultsResult = this.getAmazonGlacierClient().listVaults(new ListVaultsRequest());

        List<DescribeVaultOutput> vaultList = listVaultsResult.getVaultList();

        JSONArray vaultsJsonArray = new JSONArray();

        for (DescribeVaultOutput vault : vaultList) {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CreationDate", vault.getCreationDate());
            jsonObject.put("LastInventoryDate", vault.getLastInventoryDate());
            jsonObject.put("NumberOfArchives", vault.getNumberOfArchives());
            jsonObject.put("SizeInBytes", vault.getSizeInBytes());
            jsonObject.put("VaultARN", vault.getVaultARN());
            jsonObject.put("VaultName", vault.getVaultName());

            vaultsJsonArray.put(jsonObject);

        }

        System.out.println(vaultsJsonArray.toString(2));

    }

}
