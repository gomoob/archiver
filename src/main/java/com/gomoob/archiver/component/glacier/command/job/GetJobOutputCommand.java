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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.json.JSONObject;

import com.amazonaws.services.glacier.model.GetJobOutputRequest;
import com.amazonaws.services.glacier.model.GetJobOutputResult;
import com.gomoob.archiver.component.glacier.command.AbstractGlacierCommand;

public class GetJobOutputCommand extends AbstractGlacierCommand {

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("static-access")
    @Override
    protected void doConfigureOptions(Options options) {

        //@formatter:off
        Option jobIdOption = OptionBuilder
                .withLongOpt("job-id")
                .withDescription("The identifier of the Amazon Glacier job.")
                .hasArg()
                .create();
        Option rangeOption = OptionBuilder
                .withLongOpt("range")
                .withDescription("The range of bytes to retrieve from the output.")
                .hasArg()
                .create();
        //@formatter:on

        options.addOption(this.createAStoreIdOption());
        options.addOption(jobIdOption);
        options.addOption(rangeOption);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String doGetHelpHeader() {

        return "Gets the output of a job.";

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doExecute(CommandLine commandLine) {

        if (commandLine.hasOption("a-store-id")) {

            // Check if the required 'job-id' option is provided
            if (!commandLine.hasOption("job-id")) {

                throw new IllegalArgumentException(
                        "You must provide an Amazon Glacier JobId with the 'job-id' option !");

            }

            GetJobOutputRequest jobOutputRequest = new GetJobOutputRequest();
            jobOutputRequest.setVaultName(this.getVaultName());
            jobOutputRequest.setJobId(commandLine.getOptionValue("job-id"));

            if (commandLine.hasOption("range")) {

                jobOutputRequest.setRange(commandLine.getOptionValue("range"));

            }

            // TODO: On devrait vérifier que si le job est un inventaire ou une archive et interdire la sortie
            // console dans le cas d'une archive
            GetJobOutputResult jobOutputResult = this.getAmazonGlacierClient().getJobOutput(jobOutputRequest);
            OutputStream baos = new ByteArrayOutputStream();
            InputStream jis = null;

            try {

                // fos = new FileOutputStream(new File("jobOutput.txt"));

                jis = jobOutputResult.getBody();

                byte[] buffer = new byte[1024];
                int len;

                while ((len = jis.read(buffer)) != -1) {

                    baos.write(buffer, 0, len);
                }

            } catch (FileNotFoundException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();

            } catch (IOException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();

            } finally {

                if (baos != null) {

                    try {

                        baos.close();

                    } catch (IOException e) {

                        // TODO Auto-generated catch block
                        e.printStackTrace();

                    }

                }

                if (jis != null) {

                    try {

                        jis.close();

                    } catch (IOException e) {

                        // TODO Auto-generated catch block
                        e.printStackTrace();

                    }

                }

            }

            JSONObject jsonObject = new JSONObject(baos.toString());
            System.out.println(jsonObject.toString(2));

        }

    }

}
