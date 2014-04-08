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
package com.gomoob.archiver.plugin.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.gomoob.archiver.configuration.Configuration;
import com.gomoob.archiver.configuration.credentials.Credentials;
import com.gomoob.archiver.configuration.store.Store;
import com.gomoob.archiver.plugin.ICommand;
import com.gomoob.archiver.plugin.IPlugin;

/**
 * Abstract class common to all the archiver commands.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public abstract class AbstractCommand extends AbstractPluginOrCommand implements ICommand {

    /**
     * The archive file to upload.
     */
    private File archiveFile;

    /**
     * The Command Line which have been created.
     */
    protected CommandLine commandLine;

    /**
     * The configured credentials.
     */
    private Credentials credentials;

    /**
     * The name of the command.
     */
    private String name;

    /**
     * The plugin this command is linked to.
     */
    private IPlugin plugin;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {

        return this.name;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPlugin getPlugin() {

        return this.plugin;

    }

    /**
     * Sets the name of the command.
     * 
     * @param name the name of the command.
     */
    void setName(String name) {

        this.name = name;

    }

    /**
     * Sets the plugin this command is linked to.
     * 
     * @param plugin the plugin this command is linked to.
     */
    void setPlugin(IPlugin plugin) {

        this.plugin = plugin;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(String[] args) throws CommandException {

        // Backup the command line arguments passed to the command
        this.args = args;

        // Creates the default options
        this.options = new Options();
        this.options.addOption(this.createHelpOption());

        // Configure the available command line options
        this.doConfigureOptions(this.options);

        // Creates the command line
        CommandLineParser commandLineParser = new PosixParser();

        try {

            this.commandLine = commandLineParser.parse(this.options, args);

        } catch (ParseException parseException) {

            throw new CommandException("", parseException);

        }

        // If no arguments are provided or the '--help' option is provided we display the help of the plugin
        if (args.length == 0 || this.commandLine.hasOption("help")) {

            HelpFormatter helpFormatter = new HelpFormatter();

            //@formatter:off
            helpFormatter.printHelp(
                120, 
                "archiver --" + this.getPlugin().getName() + " --" + this.getName(), 
                this.doGetHelpHeader(), 
                this.getOptions(), 
                this.doGetHelpFooter()
            );
            //@formatter:on

            return;

        }

        // Executes the command
        this.doExecute(commandLine);

    }

    protected abstract void doExecute(CommandLine commandLine);

    @SuppressWarnings("static-access")
    protected Option createAArchiveIdOption() {

        Option aArchiveIdOption = OptionBuilder.withLongOpt("a-archive-id")
                .withDescription("Identifier of an archiver archive.").hasArg().withArgName("ARCHIVER_ARCHIVE_ID")
                .create();

        return aArchiveIdOption;

    }

    @SuppressWarnings("static-access")
    protected Option createACredentialsIdOption() {

        Option aCredentialsIdOption = OptionBuilder.withLongOpt("a-credentials-id")
                .withDescription("Identifier of an archiver credentials.").hasArg()
                .withArgName("ARCHIVER_CREDENTIALS_ID").create();

        return aCredentialsIdOption;

    }

    @SuppressWarnings("static-access")
    protected Option createAStoreIdOption() {

        Option storeIdOption = OptionBuilder.withLongOpt("a-store-id")
                .withDescription("Identifier of an archiver store.").hasArg().withArgName("ARCHIVER_STORE_ID").create();

        return storeIdOption;

    }

    protected Store findStoreById(String storeId) {

        Store store = this.getConfiguration().findStoreById(storeId);

        // The store must have been found
        if (store == null) {

            throw new IllegalArgumentException("No store having an identifier equal to '" + storeId
                    + "' has been found !");

        }

        return store;

    }

    protected CommandLine getCommandLine() {

        if (this.commandLine == null) {

            throw new IllegalStateException("No command line has been created !");

        }

        return this.commandLine;

    }

    /**
     * Gets the archiver configuration which is currently in use.
     * 
     * @return the archiver configuration which is currently in use.
     */
    protected Configuration getConfiguration() {

        return this.configuration;

    }
    
    /**
     * Gets an archive file which have been configured with the Archiver configuration or the command line parameters.
     * <p>
     * This function scans the Archiver configuration file and the command line arguments to automatically locate or 
     * create the associated archive file.
     * </p>
     * 
     * @return the archive file.
     */
    protected File getArchiveFile() {

        // If the archive file has not already been located we locate it
        if (this.archiveFile == null) {

            try {

                this.archiveFile = this.getArchiveLocator().locate(this.commandLine);

            } catch (IOException e1) {

                // TODO Auto-generated catch block
                e1.printStackTrace();

            }

        }

        return this.archiveFile;

    }

    /**
     * Gets the Credentials which have been configured with the Archiver configuration file or the command line
     * parameters.
     * <p>
     * This function scans the Archiver configuration file and the command line arguments to automatically create the
     * Credentials which are configured.
     * </p>
     * 
     * @return the configured credentials.
     */
    protected Credentials getCredentials() {

        // If the credentials have not been created
        if (this.credentials == null) {

            this.credentials = new Credentials();

            //@formatter:off
            
            // The 'a-credentials-key' option cannot be provided without the 'a-credentials-secret' option
            if(this.getCommandLine().hasOption("a-credentials-key") && 
               !this.getCommandLine().hasOption("a-credentials-secret")) {
                
                throw new IllegalStateException(
                    "The 'a-credential-key' option cannot be provided without the 'a-credentials-secret' option !"
                );
                
            }
            
            // The 'a-credentials-secret' option cannot be provided without the 'a-credentials-secret' option
            if(!this.getCommandLine().hasOption("a-credentials-key") &&
               this.getCommandLine().hasOption("a-credentials-secret")) {
                
                throw new IllegalStateException(
                    "The 'a-credentials-secret' option cannot be provided without the 'a-credentials-key' option !"
                );
                
            }
            
            // If the 'a-credentials-key' and 'a-credentials-secret' options are provided they take precendence on all the 
            // other credentials options provided
            if(this.getCommandLine().hasOption("a-credentials-key") && 
               this.getCommandLine().hasOption("a-credentials-secret")) {
               
                // If the 'a-credentials-id' option is provided with the 'a-credentials-key' and 'a-credentials-key' options 
                // then the 'a-credentials-id' option will be ignored.
                if(this.getCommandLine().hasOption("a-credentials-id")) {
                    
                    //@formatter:off
                    System.out.println(
                        "NOTE: The provided 'a-credentials-id' option will be ignored because the provided " + 
                        "'a-credentials-key' and 'a-credentials-secret' options takes precedence !"
                    );
                    //@formatter:on

                }

                // Checks if a store id is provided and if credentials are attached to this store
                if (this.getCommandLine().hasOption("a-store-id")) {

                    Store store = this.getConfiguration().findStoreById(
                            this.getCommandLine().getOptionValue("a-store-id"));

                    // Credentials have been attached to the store, those credentials will be ignored because the
                    // 'a-credentials-key' and 'a-credentials-id' options takes precedence
                    if (store != null && store.getCredentials() != null) {

                        //@formatter:off
                        System.out.println(
                            "NOTE: The credentials attached to the store you provide will be ignored because the " + 
                            "provided 'a-credentials-key' and 'a-credentials-secret' options takes precedence !"
                        );
                        //@formatter:on

                    }

                }

                this.credentials.setKey(this.getCommandLine().getOptionValue("a-credentials-key"));
                this.credentials.setSecret(this.getCommandLine().getOptionValue("a-credentials-value"));

            }

            // If the 'a-credentials-id' option is provided
            else if (this.getCommandLine().hasOption("a-credentials-id")) {

                // Checks if a store id is provided and if credentials are attached to this store
                if (this.getCommandLine().hasOption("a-store-id")) {

                    Store store = this.getConfiguration().findStoreById(
                            this.getCommandLine().getOptionValue("a-store-id"));

                    // Credentials have been attached to the store, those credentials will be ignored because the
                    // 'a-credentials-key' and 'a-credentials-id' options takes precedence
                    if (store != null && store.getCredentials() != null) {

                        //@formatter:off
                        System.out.println(
                            "NOTE: The credentials attached to the store you provide will be ignored because the " + 
                            "provided 'a-credentials-key' and 'a-credentials-secret' options takes precedence !"
                        );
                        //@formatter:on

                    }

                }

                this.credentials = this.getConfiguration().findCredentialsById(
                        this.getCommandLine().getOptionValue("a-credentials-id"));

            }

            // Try to get credentials from the configured store
            else if (this.getCommandLine().hasOption("a-store-id")) {

                Store store = this.getConfiguration().findStoreById(this.getCommandLine().getOptionValue("a-store-id"));

                // No credentials have been attached to the store
                if (store != null && store.getCredentials() == null) {

                    //@formatter:off
                    throw new IllegalStateException(
                        "Impossible to get any credentials from a configuration file or a command line option !"
                    );
                    //@formatter:on

                }

                this.credentials = store.getCredentials();

            }

            // Impossible the get credentials
            else {

                //@formatter:off
                throw new IllegalStateException(
                    "Impossible to get any credentials from a configuration file or a command line option !"
                );
                //@formatter:on

            }

            // @formatter:on

        }

        return this.credentials;

    }

}
