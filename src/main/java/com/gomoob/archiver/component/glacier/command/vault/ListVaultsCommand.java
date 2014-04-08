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
package com.gomoob.archiver.component.glacier.command.vault;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.json.JSONArray;
import org.json.JSONObject;

import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.DescribeVaultOutput;
import com.amazonaws.services.glacier.model.ListVaultsRequest;
import com.amazonaws.services.glacier.model.ListVaultsResult;
import com.gomoob.archiver.component.glacier.command.AbstractGlacierCommand;
import com.gomoob.archiver.configuration.credentials.Credentials;

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
    @SuppressWarnings("static-access")
    @Override
    public void processCommand(String[] args) {

        // Create the command line options
        Options options = new Options();
        
        //@formatter:off
        Option awsAccessKeyOption = OptionBuilder
                .withLongOpt("aws-access-key")
                .withDescription("The Amazon Web Services account access key.")
                .hasArg()
                .withArgName("AWS_ACCESS_KEY")
                .create();
        
        Option awsSecretKeOption = OptionBuilder
                .withLongOpt("aws-secret-key")
                .withDescription("The Amazon Web Services account secret key.")
                .hasArg()
                .withArgName("AWS_SECRET_KEY")
                .create();
        //@formatter:on
        
        options.addOption(this.createACredentialsIdOption());
        options.addOption(awsAccessKeyOption);
        options.addOption(awsSecretKeOption);
        options.addOption(this.createHelpOption());

        CommandLineParser commandLineParser = new PosixParser();

        try {

            CommandLine commandLine = commandLineParser.parse(options, args);

            if (commandLine.getOptions().length == 0
                    || (commandLine.getOptions().length == 1 && commandLine.hasOption("help"))) {

                //@formatter:off
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp(
                    120, 
                    "archiver --glacier --list-vaults", 
                    "\n " + 
                    "List all vaults in a specified region." + 
                    "\t\n", 
                    options, 
                    ""
                );
                //@formatter:on

            }

            // If an archiver credentials is provided then AWS specific options cannot be provided
            if (commandLine.hasOption("a-credentials-id")
                    && (commandLine.hasOption("aws-access-key") || commandLine.hasOption("aws-secret-key"))) {

                // TODO: Erreur

            }

            // If the command line uses AWS SDK informations
            else if (!commandLine.hasOption("a-credentials-id")
                    && (commandLine.hasOption("aws-access-key") || commandLine.hasOption("aws-secret-key"))) {

                // TODO:

            }

            // If an archiver credentials is provided then we use it
            else if (commandLine.hasOption("a-credentials-id")) {

                this.listVaultsUsingArchiverParameters(commandLine.getOptionValue("a-credentials-id"));

            }

        } catch (ParseException parseException) {

            // TODO Auto-generated catch block
            parseException.printStackTrace();

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

        Credentials credentials = this.configuration.findCredentialsById(credentialsId);

        AmazonGlacierClient amazonGlacierClient = this.createAmazonGlacierClient(credentials);

        ListVaultsRequest listVaultsRequest = new ListVaultsRequest();

        ListVaultsResult listVaultsResult = amazonGlacierClient.listVaults(listVaultsRequest);

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
