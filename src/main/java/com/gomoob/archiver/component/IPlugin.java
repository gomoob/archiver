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

/**
 * Interface which defines an archiving plugin to be executed.
 * <p>
 * An archiving plugin receives the following arguments: <verbatim>command_name [command_options...] </verbatim>
 * </p>
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public interface IPlugin {

    /**
     * Executes the command with provided command line arguments.
     * 
     * @param args the provided command line arguments.
     */
    void execute(String[] args) throws CommandException;

    /**
     * Gets the name of the plugin.
     * 
     * @return the name of the plugin.
     */
    String getName();

}
