package com.gomoob.archiver.component.glacier.command.vault;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.json.JSONObject;

import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.DescribeVaultRequest;
import com.amazonaws.services.glacier.model.DescribeVaultResult;
import com.gomoob.archiver.component.glacier.command.AbstractGlacierCommand;
import com.gomoob.archiver.component.glacier.configuration.store.GlacierAdditionalConfiguration;
import com.gomoob.archiver.configuration.store.Store;

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
    public void processCommand(String[] args) {

        Options options = new Options();
        options.addOption(this.createHelpOption());
        options.addOption(this.createAStoreIdOption());

        CommandLineParser commandLineParser = new PosixParser();

        try {

            CommandLine commandLine = commandLineParser.parse(options, args);

            if (commandLine.getOptions().length == 0
                    || (commandLine.getOptions().length == 1 && commandLine.hasOption("help"))) {

                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("archiver --glacier --describe-vault", options);

            }

            if (!commandLine.hasOption("a-store-id") && !commandLine.hasOption("vault-name")) {

            } else if (commandLine.hasOption("a-store-id")) {

                this.describeVault(commandLine.getOptionValue("a-store-id"));

            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

        Store store = this.configuration.findStoreById(storeId);

        AmazonGlacierClient amazonGlacierClient = this.createAmazonGlacierClient(store);

        DescribeVaultRequest describeVaultRequest = new DescribeVaultRequest()
                .withVaultName(((GlacierAdditionalConfiguration) store.getAdditionalConfiguration()).getVaultName());

        DescribeVaultResult describeVaultResult = amazonGlacierClient.describeVault(describeVaultRequest);

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
