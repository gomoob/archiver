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
package com.gomoob.archiver;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;

import com.gomoob.archiver.compressor.ICompressor;
import com.gomoob.archiver.compressor.impl.ZipCompressor;
import com.gomoob.archiver.configuration.Configuration;
import com.gomoob.archiver.configuration.archive.Archive;
import com.gomoob.archiver.configuration.archive.Type;

/**
 * Class which is used to locate archives, this class has multiple responsibilities :
 * <ul>
 * <li>Determine how to locate are archive, using an 'archive' configuration or using a path to a file which is
 * provided.</li>
 * <li>If the archive has to be located using an 'archive' configuration then determine how to create / build the
 * archive.</li>
 * </ul>
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public class ArchiveLocator {

    /**
     * The configuration used to locate archives to build.
     */
    private Configuration configuration;

    /**
     * Gets the configuration used to locate the archives to build.
     * 
     * @return the configuration used to locate the archives to build.
     */
    public Configuration getConfiguration() {

        return this.configuration;

    }

    /**
     * Function used to build / locate an archive using parameters provided at command line.
     * <p>
     * This function expects a command line object which will inspect the following command line parameters :
     * </p>
     * <ul>
     * <li>'a-archive-file' : An absolute path to an archive.</li>
     * <li>'a-archive-id' : The identifier of an archive configured inside the configuration attached to this archive
     * builder.</li>
     * </ul>
     * 
     * @param commandLine TODO: A DOCUMENTER
     * @return TODO: A DOCUMENTER
     * @throws IOException TODO: A DOCUMENTER
     */
    public File locate(CommandLine commandLine) throws IOException {

        File archiveFile = null;

        // Providing both the 'a-archive-id' and 'a-archive-file' options is forbidden
        if (commandLine.hasOption("a-archive-id") && commandLine.hasOption("a-archive-file")) {

            throw new IllegalArgumentException(
                    "The options 'a-archive-id' and 'a-archive-file' cannot be provided together !");

        }

        // Build the archive path using the identifier of an archive described in an archiver configuration file
        else if (commandLine.hasOption("a-archive-id")) {

            // A configuration must have been associated to the builder
            if (this.configuration == null) {

                throw new IllegalStateException("No configuration is attached to the archive locator !");

            }

            String archiveId = commandLine.getOptionValue("a-archive-id");
            Archive archive = this.configuration.findArchiveById(archiveId);

            // No archive configuration has been found for the provided 'a-archive-id' parameter
            if (archive == null) {

                throw new IllegalArgumentException(
                        "No archive configuration has been found for the provided 'a-archive-id="
                                + commandLine.getOptionValue("a-archive-id") + "' parameter !");

            }

            // The archive is of type 'zip' so here we create the ZIP file
            if (archive.getType() == Type.ZIP) {

                ICompressor compressor = new ZipCompressor();
                archiveFile = compressor.compress(archive);

            }

            // The archive is of type 'raw' so we only create the path to the file
            else if (archive.getType() == Type.RAW) {

                // TODO:

            }

        }

        // Build the archive path using an archive file path provided at command line
        else if (commandLine.hasOption("a-archive-file")) {

            archiveFile = new File(commandLine.getOptionValue("a-archive-file"));

            // The archive file must exist
            if (!archiveFile.exists()) {

                throw new IOException("The file '" + archiveFile.getAbsolutePath() + "' does not exist !");

            }

        }

        // Nothing allows us to create an archive file path
        else {

            throw new IllegalArgumentException("You must provide a 'a-archive-id' or 'a-archive-file' option !");

        }

        return archiveFile;

    }

    /**
     * Sets the configuration used to locate the archives to build.
     * 
     * @param configuration the configuration used to locate the archives to build.
     */
    public void setConfiguration(Configuration configuration) {

        this.configuration = configuration;

    }

}
