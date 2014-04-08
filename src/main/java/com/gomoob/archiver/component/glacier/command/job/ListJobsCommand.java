package com.gomoob.archiver.component.glacier.command.job;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.json.JSONArray;
import org.json.JSONObject;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.GlacierJobDescription;
import com.amazonaws.services.glacier.model.ListJobsRequest;
import com.amazonaws.services.glacier.model.ListJobsResult;
import com.gomoob.archiver.component.glacier.command.AbstractGlacierCommand;
import com.gomoob.archiver.component.glacier.configuration.store.GlacierAdditionalConfiguration;
import com.gomoob.archiver.configuration.store.Store;

public class ListJobsCommand extends AbstractGlacierCommand {

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
                    "archiver --glacier --list-jobs", 
                    "\n " + 
                    "List jobs for a vault including jobs that are in-progress and jobs that have recently finished." + 
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
                AWSCredentials awsCredentials = this.createAWSCredentials(store.getCredentials());

                ListJobsRequest listJobsRequest = new ListJobsRequest(gac.getVaultName());
                listJobsRequest.setRequestCredentials(awsCredentials);

                ListJobsResult listJobsResult = amazonGlacierClient.listJobs(listJobsRequest);
                
                JSONObject responseJsonObject = new JSONObject();
                JSONArray jobListJsonArray = new JSONArray();
                
                for (GlacierJobDescription gjd : listJobsResult.getJobList()) {

                    JSONObject jdjo = new JSONObject();
                    jdjo.put("Action", gjd.getAction() == null ? JSONObject.NULL : gjd.getAction());
                    jdjo.put("ArchiveId", gjd.getArchiveId() == null ? JSONObject.NULL : gjd.getArchiveId());
                    jdjo.put("ArchiveSizeInBytes", gjd.getArchiveSizeInBytes() == null ? JSONObject.NULL : gjd.getArchiveSizeInBytes());
                    jdjo.put("ArchiveSHA256TreeHash", gjd.getArchiveSHA256TreeHash() == null ? JSONObject.NULL : gjd.getArchiveSHA256TreeHash());
                    jdjo.put("Completed", gjd.getCompleted());
                    jdjo.put("CompletionDate", gjd.getCompletionDate() == null ? JSONObject.NULL : gjd.getCompletionDate());
                    jdjo.put("CreationDate", gjd.getCreationDate());
                    jdjo.put("InventorySizeInBytes", gjd.getInventorySizeInBytes() == null ? JSONObject.NULL : gjd.getInventorySizeInBytes());
                    jdjo.put("JobDescription", gjd.getJobDescription() == null ? JSONObject.NULL : gjd.getJobDescription());
                    jdjo.put("JobId", gjd.getJobId());
                    jdjo.put("RetrievalByteRange", gjd.getRetrievalByteRange() == null ? JSONObject.NULL : gjd.getRetrievalByteRange());
                    jdjo.put("SHA256TreeHash", gjd.getSHA256TreeHash() == null ? JSONObject.NULL : gjd.getSHA256TreeHash());
                    jdjo.put("SNSTopic", gjd.getSNSTopic() == null ? JSONObject.NULL : gjd.getSNSTopic());
                    jdjo.put("StatusCode", gjd.getStatusCode());
                    jdjo.put("StatusMessage", gjd.getStatusMessage());
                    jdjo.put("VaultARN", gjd.getVaultARN());
                    
                    jobListJsonArray.put(jdjo);
                    
                }
                
                responseJsonObject.put("JobList", jobListJsonArray);
                responseJsonObject.put("Marker", listJobsResult.getMarker() == null ? JSONObject.NULL : listJobsResult.getMarker());
                
                System.out.println(responseJsonObject.toString(2));

            }

        } catch (ParseException parseException) {

            parseException.printStackTrace();

        }

    }

}
