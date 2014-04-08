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
import org.apache.commons.cli.Options;

import com.amazonaws.services.glacier.model.InitiateJobRequest;
import com.amazonaws.services.glacier.model.InitiateJobResult;
import com.amazonaws.services.glacier.model.JobParameters;
import com.gomoob.archiver.component.glacier.command.AbstractGlacierCommand;

/**
 * Command used to initiate a vault inventory job.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public class InitiateVaultInventoryJobCommand extends AbstractGlacierCommand {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doConfigureOptions(Options options) {

        options.addOption(this.createAStoreIdOption());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String doGetHelpHeader() {

        return "Initiate a vault inventory job.";

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doExecute(CommandLine commandLine) {

        if (commandLine.hasOption("a-store-id")) {

            JobParameters jobParameters = new JobParameters();
            jobParameters.setType("inventory-retrieval");
            // jobParameters.setSNSTopic("arn:aws:sns:eu-west-1:967297338056:verygoodmoment-glacier-notification");

            InitiateJobRequest initiateJobRequest = new InitiateJobRequest();
            initiateJobRequest.setVaultName(this.getVaultName());
            initiateJobRequest.setJobParameters(jobParameters);

            InitiateJobResult initiateJobResult = this.getAmazonGlacierClient().initiateJob(initiateJobRequest);

            System.out.println("Job id : " + initiateJobResult.getJobId());
            System.out.println("Location : " + initiateJobResult.getLocation());

        }

    }

}
