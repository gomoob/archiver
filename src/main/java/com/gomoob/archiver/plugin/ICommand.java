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
package com.gomoob.archiver.plugin;

import com.gomoob.archiver.plugin.impl.CommandException;


/**
 * Interface which defines a command to be executed.
 * <p>
 * A command receives command line arguments.
 * </p>
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public interface ICommand {

    /**
     * Executes the command with provided command line arguments.
     * 
     * @param args the provided command line arguments.
     */
    void execute(String[] args) throws CommandException;
    
    /**
     * Gets the name of the command.
     * 
     * @return the name of the command.
     */
    String getName();
    
    /**
     * Gets the plugin this command is linked to.
     * 
     * @return the plugin this command is linked to.
     */
    IPlugin getPlugin();
    
}
