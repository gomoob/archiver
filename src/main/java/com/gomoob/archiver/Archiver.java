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
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.gomoob.archiver.component.CommandException;
import com.gomoob.archiver.component.glacier.GlacierPlugin;
import com.gomoob.archiver.configuration.Configuration;
import com.gomoob.archiver.configuration.ConfigurationParser;

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
            
            if(args.length == 1) {
                
                firstArgs = Arrays.copyOfRange(args, 0, 1);
                
            } else if(args.length > 1) {
                
                firstArgs = Arrays.copyOfRange(args, 0, 2);
                
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
                if(commandLine.hasOption("a-configuration-file")) {
                    
                    configurationStream = new FileInputStream(new File(commandLine.getOptionValue("a-configuration-file")));
                    
                } 
                
                // Loads the default configuration file
                else {
                
                    configurationStream = Archiver.class.getResourceAsStream("/configuration.json");
                    
                }
                
                Configuration configuration = configurationParser.parse(configurationStream);
                
                if (commandLine.hasOption("glacier")) {

                    GlacierPlugin glacierComponent = new GlacierPlugin();
                    glacierComponent.setConfiguration(configuration);
                    
                    if(commandLine.hasOption("a-configuration-file")) {
                    
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
