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
package com.gomoob.archiver.component.glacier.command.job;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.InitiateJobRequest;
import com.amazonaws.services.glacier.model.InitiateJobResult;
import com.amazonaws.services.glacier.model.JobParameters;
import com.gomoob.archiver.component.glacier.command.AbstractGlacierCommand;
import com.gomoob.archiver.component.glacier.configuration.store.GlacierAdditionalConfiguration;
import com.gomoob.archiver.configuration.store.Store;

/**
 * Command used to initiate a vault inventory job.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public class InitiateArchiveRetrievalJobCommand extends AbstractGlacierCommand {

    /**
     * {@inheritDoc}
     */
    @Override
    public void processCommand(String[] args) {
        
        Options options = new Options();
        options.addOption(this.createAStoreIdOption());
        options.addOption(this.createHelpOption());

        CommandLineParser commandLineParser = new PosixParser();

        try {

            CommandLine commandLine = commandLineParser.parse(options, args);

            if (commandLine.getOptions().length == 0
                    || (commandLine.getOptions().length == 1 && commandLine.hasOption("help"))) {

                //@formatter:off
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp(
                    120, 
                    "archiver --glacier --initiate-vault-inventory-job", 
                    "\n " + 
                    "Initiate a vault inventory job." + 
                    "\t\n", 
                    options, 
                    ""
                );
                //@formatter:on

            } else if (commandLine.hasOption("a-store-id")) {

                Store store = this.configuration.findStoreById(commandLine.getOptionValue("a-store-id"));
                GlacierAdditionalConfiguration gac = (GlacierAdditionalConfiguration) store
                        .getAdditionalConfiguration();

                AmazonGlacierClient amazonGlacierClient = this.createAmazonGlacierClient(store);

                JobParameters jobParameters = new JobParameters();
                jobParameters.setType("inventory-retrieval");
                // jobParameters.setSNSTopic("arn:aws:sns:eu-west-1:967297338056:verygoodmoment-glacier-notification");
                
                InitiateJobRequest initiateJobRequest = new InitiateJobRequest();
                initiateJobRequest.setVaultName(gac.getVaultName());
                initiateJobRequest.setJobParameters(jobParameters);
                
                InitiateJobResult initiateJobResult = amazonGlacierClient.initiateJob(initiateJobRequest);
                
                System.out.println("Job id : " + initiateJobResult.getJobId());
                System.out.println("Location : " + initiateJobResult.getLocation());

            }

        } catch (ParseException parseException) {

            parseException.printStackTrace();

        }

    }

}
