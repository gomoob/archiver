package com.gomoob.archiver.component.glacier.command.job;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.json.JSONObject;

import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.GetJobOutputRequest;
import com.amazonaws.services.glacier.model.GetJobOutputResult;
import com.gomoob.archiver.component.glacier.command.AbstractGlacierCommand;
import com.gomoob.archiver.component.glacier.configuration.store.GlacierAdditionalConfiguration;
import com.gomoob.archiver.configuration.store.Store;

public class GetJobOutputCommand extends AbstractGlacierCommand {

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("static-access")
    @Override
    public void processCommand(String[] args) {

        Options options = new Options();

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
        options.addOption(this.createHelpOption());
        options.addOption(jobIdOption);
        options.addOption(rangeOption);

        CommandLineParser commandLineParser = new PosixParser();

        try {

            CommandLine commandLine = commandLineParser.parse(options, args);

            if (commandLine.getOptions().length == 0
                    || (commandLine.getOptions().length == 1 && commandLine.hasOption("help"))) {

                //@formatter:off
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp(
                    120, 
                    "archiver --glacier --get-job-output", 
                    "\n " + 
                    "Gets the output of a job." + 
                    "\t\n", 
                    options, 
                    ""
                );
                //@formatter:on

            } else if (commandLine.hasOption("a-store-id")) {

                // Check if the required 'job-id' option is provided
                if (!commandLine.hasOption("job-id")) {

                    throw new IllegalArgumentException(
                            "You must provide an Amazon Glacier JobId with the 'job-id' option !");

                }

                // Try to get a configured store using the provided 'a-store-id'
                Store store = this.configuration.findStoreById(commandLine.getOptionValue("a-store-id"));

                if (store == null) {

                    throw new IllegalArgumentException("No store having an identifier equal to '"
                            + commandLine.getOptionValue("a-store-id") + "' has been found !");

                }

                GlacierAdditionalConfiguration gac = (GlacierAdditionalConfiguration) store.getAdditionalConfiguration();

                AmazonGlacierClient amazonGlacierClient = this.createAmazonGlacierClient(store);

                GetJobOutputRequest jobOutputRequest = new GetJobOutputRequest();
                jobOutputRequest.setVaultName(gac.getVaultName());
                jobOutputRequest.setJobId(commandLine.getOptionValue("job-id"));

                if(commandLine.hasOption("range")) {
                    
                    jobOutputRequest.setRange(commandLine.getOptionValue("range"));
                    
                }

                // TODO: On devrait vérifier que si le job est un inventaire ou une archive et interdire la sortie 
                //       console dans le cas d'une archive
                GetJobOutputResult jobOutputResult = amazonGlacierClient.getJobOutput(jobOutputRequest);
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

        } catch (ParseException parseException) {

            parseException.printStackTrace();

        }

    }

}
