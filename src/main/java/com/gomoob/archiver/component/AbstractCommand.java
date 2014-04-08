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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.gomoob.archiver.ArchiveLocator;
import com.gomoob.archiver.compressor.ICompressor;
import com.gomoob.archiver.compressor.impl.ZipCompressor;
import com.gomoob.archiver.configuration.Configuration;
import com.gomoob.archiver.configuration.archive.Type;

/**
 * Abstract class common to all the archiver commands.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public abstract class AbstractCommand implements ICommand {

    /**
     * The component used to locate and build archive files to managed.
     */
    protected ArchiveLocator archiveLocator;

    /**
     * The configuration which is currently in use.
     */
    protected Configuration configuration;

    /**
     * Create a new instance of the {@link AbstractCommand} class.
     */
    protected AbstractCommand() {

        this.archiveLocator = new ArchiveLocator();

    }

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

    @SuppressWarnings("static-access")
    protected Option createHelpOption() {

        //@formatter:off
        Option helpOption = OptionBuilder
                .withLongOpt("help")
                .withDescription("Print this message")
                .create();
        //@formatter:on

        return helpOption;

    }

    protected CommandLine commandLine;

    protected CommandLine parseCommandLine(Options options, String[] args) throws ParseException {

        CommandLineParser commandLineParser = new PosixParser();

        this.commandLine = commandLineParser.parse(options, args);

        return this.commandLine;

    }

    public ICompressor getCompressor(Type type) {

        ICompressor compressor = null;

        if (type == Type.ZIP) {

            compressor = new ZipCompressor();

        }

        return compressor;

    }

    /**
     * Gets the archiver configuration which is currently in use.
     * 
     * @return the archiver configuration which is currently in use.
     */
    public Configuration getConfiguration() {

        return this.configuration;

    }

    /**
     * Sets the archiver configuration to use.
     * 
     * @param configuration the archiver configuration to use.
     */
    public void setConfiguration(Configuration configuration) {

        this.configuration = configuration;

    }

}
