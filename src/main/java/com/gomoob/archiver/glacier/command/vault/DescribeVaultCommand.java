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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.json.JSONObject;

import com.amazonaws.services.glacier.model.DescribeVaultRequest;
import com.amazonaws.services.glacier.model.DescribeVaultResult;
import com.gomoob.archiver.glacier.command.AbstractGlacierCommand;

/**
 * Command class used to retrieve informations about an Amazon Glacier Vault.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 * @see http://docs.aws.amazon.com/amazonglacier/latest/dev/api-vault-get.html
 */
public class DescribeVaultCommand extends AbstractGlacierCommand {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doConfigureOptions(Options options) {

        options.addOption(this.createHelpOption());
        options.addOption(this.createAStoreIdOption());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doExecute(CommandLine commandLine) {

        if (commandLine.getOptions().length == 0
                || (commandLine.getOptions().length == 1 && commandLine.hasOption("help"))) {

            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("archiver --glacier --describe-vault", this.getOptions());

        }

        if (!commandLine.hasOption("a-store-id") && !commandLine.hasOption("vault-name")) {

        } else if (commandLine.hasOption("a-store-id")) {

            this.describeVault(commandLine.getOptionValue("a-store-id"));

        }

    }

    /**
     * Function used to describe a vault and outputs its result in the console.
     * <p>
     * NOTE: The elements printed by this function are documented here :
     * http://docs.aws.amazon.com/amazonglacier/latest/dev/api-vault-get.html.
     * </p>
     * 
     * @param storeId the identifier of the archive store used to identify the vault to describe.
     */
    private void describeVault(String storeId) {

        DescribeVaultRequest describeVaultRequest = new DescribeVaultRequest().withVaultName(this.getVaultName());
        DescribeVaultResult describeVaultResult = this.getAmazonGlacierClient().describeVault(describeVaultRequest);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("CreationDate", describeVaultResult.getCreationDate());
        jsonObject.put("LastInventoryDate", describeVaultResult.getLastInventoryDate());
        jsonObject.put("NumberOfArchives", describeVaultResult.getNumberOfArchives());
        jsonObject.put("SizeInBytes", describeVaultResult.getSizeInBytes());
        jsonObject.put("VaultARN", describeVaultResult.getVaultARN());
        jsonObject.put("VaultName", describeVaultResult.getVaultName());

        System.out.println(jsonObject.toString(2));

    }

}
