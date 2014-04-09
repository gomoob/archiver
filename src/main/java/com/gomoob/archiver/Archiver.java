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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.gomoob.archiver.configuration.Configuration;
import com.gomoob.archiver.configuration.ConfigurationParser;
import com.gomoob.archiver.glacier.GlacierPlugin;
import com.gomoob.archiver.plugin.impl.CommandException;

/**
 * Main entry of the GOMOOB AWS Glacier tool.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public class Archiver {

    /**
     * Main entry of the GOMOOB AWS Glacier backup tool.
     * 
     * @param args arguments passed to the command line.
     * @throws CommandException
     */
    @SuppressWarnings("static-access")
    public static void main(String[] args) throws CommandException {

        Options options = new Options();

        //@formatter:off
        Option aConfigurationFile = OptionBuilder
                .withLongOpt("a-configuration-file")
                .withDescription("Path to a custom archiver configuration file.")
                .hasArg()
                .withArgName("FILE_PATH")
                .create();
        
        Option glacierOption = OptionBuilder
                .withLongOpt("glacier")
                .withDescription("Amazon Glacier archiving commands.")
                .create();
        
        Option helpOption = OptionBuilder
                .withLongOpt("help")
                .withDescription("Print this message.")
                .create();
        //@formatter:on

        options.addOption(aConfigurationFile);
        options.addOption(glacierOption);
        options.addOption(helpOption);

        CommandLineParser commandLineParser = new PosixParser();

        try {

            String[] firstArgs = new String[] {};

            if (args.length == 1) {

                firstArgs = Arrays.copyOfRange(args, 0, 1);

            } else if (args.length > 1 && args[1].trim().toLowerCase().equals("a-configuration-file")) {

                firstArgs = Arrays.copyOfRange(args, 0, 2);

            } else if (args.length > 1) {

                firstArgs = Arrays.copyOfRange(args, 0, 1);

            }

            CommandLine commandLine = commandLineParser.parse(options, firstArgs);

            if (commandLine.getOptions().length == 0
                    || (commandLine.getOptions().length == 1 && commandLine.hasOption("help"))) {

                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp(120, "archiver", "", options, "");

            }

            else {

                ConfigurationParser configurationParser = new ConfigurationParser();
                InputStream configurationStream = null;

                // Loads a custom archiver configuration file
                if (commandLine.hasOption("a-configuration-file")) {

                    configurationStream = new FileInputStream(new File(
                            commandLine.getOptionValue("a-configuration-file")));

                }

                // Loads the default configuration file
                else {

                    try {

                        // Returns a string like
                        // 'jar:file:/C:/install_dir/lib/archiver-x.y.z.jar!/com/gomoob/archiver/Archiver.class
                        String archiverClassUri = Archiver.class.getResource("/com/gomoob/archiver/Archiver.class")
                                .toURI().toString();

                        // If the the 'Archiver.class' is declared inside an archiver JAR file
                        if (archiverClassUri.startsWith("jar:file:")) {

                            // Gets the file path to the archiver JAR file on the file system, for example
                            // '/C:/install_dir/archiver-x.y.z.jar'
                            String jarFilePath = archiverClassUri.substring(9, archiverClassUri.indexOf("!") + 1);

                            // Gets the parent dir of the JAR file, this should be the 'dir' directory of the archiver
                            // install
                            File jarFileParentDir = new File(jarFilePath).getParentFile();

                            // If we are inside a 'lib' directory
                            if (jarFileParentDir.getName().equals("lib")) {

                                // Gets the archiver 'conf' directory
                                File confDir = new File(jarFileParentDir.getParentFile(), "conf");
                                File configurationFile = null;

                                if (confDir.exists() && confDir.isDirectory()) {

                                    configurationFile = new File(confDir, "archiver.json");

                                    // Cannot find the configuration file
                                    if (!configurationFile.exists()) {

                                        throw new IllegalStateException("Cannot find the 'archiver.json' file !");

                                    }

                                    configurationStream = new FileInputStream(configurationFile);

                                } else {

                                    throw new IllegalStateException("Can't find the Archiver 'conf' directory !");

                                }

                            }

                            // The Archiver JAR file is not declared in the right directory
                            else {

                                throw new IllegalStateException(
                                        "The archiver JAR is not declared inside a 'lib' directory !");

                            }

                        }

                    } catch (URISyntaxException use) {
                        // TODO Auto-generated catch block
                        use.printStackTrace();
                    }

                }

                Configuration configuration = configurationParser.parse(configurationStream);

                if (commandLine.hasOption("glacier")) {

                    GlacierPlugin glacierComponent = new GlacierPlugin();
                    glacierComponent.setConfiguration(configuration);

                    if (commandLine.hasOption("a-configuration-file")) {

                        glacierComponent.execute(Arrays.copyOfRange(args, 2, args.length));

                    } else {

                        glacierComponent.execute(Arrays.copyOfRange(args, 1, args.length));

                    }

                } else {

                    HelpFormatter helpFormatter = new HelpFormatter();
                    helpFormatter.printHelp(120, "archiver", "", options, "");

                }

            }

        } catch (ParseException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (com.gomoob.archiver.configuration.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
