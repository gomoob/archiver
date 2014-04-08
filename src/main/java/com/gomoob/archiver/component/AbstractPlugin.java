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
package com.gomoob.archiver.component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

/**
 * Abstract class common to all the archiver commands.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public abstract class AbstractPlugin extends AbstractPluginOrCommand implements IPlugin {

    /**
     * A map which maps command line option names to command classes.
     */
    private Map<String, Class<? extends ICommand>> commands = new HashMap<String, Class<? extends ICommand>>();

    /**
     * The name of the plugin.
     */
    private String name;

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
    public void execute(String[] args) throws CommandException {

        // Backup the command line arguments passed to the plugin
        this.args = args;

        // Creates the default options
        this.options = new Options();
        this.options.addOption(this.createHelpOption());

        // Configure the available command line options
        this.doConfigureOptions(this.options);

        // Configure the commands to call
        this.doConfigureCommands(this.commands);

        // The name of the command to call
        String commandName = null;

        // The command arguments
        String[] commandArgs = new String[] {};

        if (args.length > 0) {

            // TODO: ici on supprime le '--' mais on devrait vérifier la taille de l'argument avant
            commandName = args[0].substring(2);

        }

        if (args.length > 1) {

            commandArgs = Arrays.copyOfRange(args, 1, args.length);

        }

        // If no command is provided or the '--help' option is provided we display the help of the plugin
        if (commandName == null || commandName.trim().toLowerCase().equals("help")) {

            HelpFormatter helpFormatter = new HelpFormatter();

            //@formatter:off
            helpFormatter.printHelp(
                120, 
                "archiver --" + this.doGetName(), 
                this.doGetHelpHeader(), 
                this.getOptions(), 
                this.doGetHelpFooter()
            );
            //@formatter:on

            return;

        }

        // Executes the command
        this.executeCommand(commandName, commandArgs);

    }

    /**
     * Creates a new instance of the {@link AbstractPlugin} class.
     */
    protected AbstractPlugin() {

        this.name = this.doGetName();

    }

    /**
     * Function used to configure the mapping between command line options and the commands to call in the plugin.
     * 
     * @param commands a map to be filled with command line option name to command class mappings.
     */
    protected abstract void doConfigureCommands(Map<String, Class<? extends ICommand>> commands);

    /**
     * Function used to execute a command of the plugin.
     * 
     * @param commandName the name of the command to execute.
     * @param args the command line arguments to pass to the command.
     */
    protected void executeCommand(String commandName, String args[]) {

        Class<? extends ICommand> commandClazz = this.commands.get(commandName);

        ICommand command = null;

        try {
            
            // Instanciate the command
            command = commandClazz.newInstance();

            // Configure the command
            ((AbstractCommand) command).setConfiguration(this.configuration);
            ((AbstractCommand) command).setName(commandName);
            ((AbstractCommand) command).setPlugin(this);

        } catch (InstantiationException ie) {

            // TODO Auto-generated catch block
            ie.printStackTrace();

        } catch (IllegalAccessException iae) {

            // TODO Auto-generated catch block
            iae.printStackTrace();

        }

        try {

            command.execute(args);

        } catch (CommandException ce) {

            // TODO Auto-generated catch block
            ce.printStackTrace();

        }

    }

    /**
     * Gets the name of the plugin.
     * 
     * @return the name of the plugin.
     */
    protected abstract String doGetName();

}
