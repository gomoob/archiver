/**
 * Copyright 2014 SARL GOMOOB. All rights reserved.
 */
package com.gomoob.archiver;

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

import com.gomoob.archiver.component.glacier.GlacierCommands;
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
     */
    @SuppressWarnings("static-access")
    public static void main(String[] args) {

        Options options = new Options();

        //@formatter:off
        Option glacierOption = OptionBuilder
                .withLongOpt("glacier")
                .withDescription("glacier archiving components.")
                .create();
        
        Option helpOption = OptionBuilder
                .withLongOpt("help")
                .withDescription("print this message.")
                .create();
        //@formatter:on

        options.addOption(glacierOption);
        options.addOption(helpOption);
        
        CommandLineParser commandLineParser = new PosixParser();

        try {
            
            String[] firstArgs = new String[] {};
            
            if(args.length > 0) {
                
                firstArgs = Arrays.copyOfRange(args, 0, 1);
                
            }

            CommandLine commandLine = commandLineParser.parse(options, firstArgs);

            if (commandLine.getOptions().length == 0
                    || (commandLine.getOptions().length == 1 && commandLine.hasOption("help"))) {

                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("archiver", options);

            }

            else {

                ConfigurationParser configurationParser = new ConfigurationParser();
                InputStream configurationStream = Archiver.class.getResourceAsStream("/configuration.json");
                Configuration configuration = configurationParser.parse(configurationStream);

                if (commandLine.hasOption("glacier")) {

                    GlacierCommands glacierComponent = new GlacierCommands();
                    glacierComponent.setConfiguration(configuration);
                    glacierComponent.processCommand(Arrays.copyOfRange(args, 1, args.length));

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

        /* else if (args[0].equals("jobOutput")) {
         *  else if (args[0].equals("archiveRetrieval")) {
         * JobParameters jobParameters = new JobParameters(); jobParameters.setType("archive-retrieval");
         * jobParameters.setSNSTopic("arn:aws:sns:eu-west-1:967297338056:verygoodmoment-glacier-notification");
         * jobParameters.setArchiveId(args[1]); InitiateJobRequest initiateJobRequest = new InitiateJobRequest();
         * initiateJobRequest.setVaultName(configuration.getProperty("vaultName"));
         * initiateJobRequest.setJobParameters(jobParameters); InitiateJobResult initiateJobResult =
         * amazonGlacierClient.initiateJob(initiateJobRequest); String jobId = initiateJobResult.getJobId();
         * System.out.println("Job id : " + jobId); } else { printHelp(); }
         */

    }

    /**
	 * 
	 */
    private static void printHelp() {

        System.out.println("Available options are : ");
        System.out.println(" - backup : Perform a backup of files located into the configured backup directory.");
        System.out.println(" - listJobs : Lists the Glacier jobs currently in execution.");
        System.out.println(" - vaultInventory : Perform a Vault Inventory and be notified using Amazon SNS.");

    }

}
