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
import org.json.JSONArray;
import org.json.JSONObject;

import com.amazonaws.services.glacier.model.GlacierJobDescription;
import com.amazonaws.services.glacier.model.ListJobsRequest;
import com.amazonaws.services.glacier.model.ListJobsResult;
import com.gomoob.archiver.component.glacier.command.AbstractGlacierCommand;

public class ListJobsCommand extends AbstractGlacierCommand {

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

        return "List jobs for a vault including jobs that are in-progress and jobs that have recently finished.";

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doExecute(CommandLine commandLine) {

        if (commandLine.hasOption("a-store-id")) {

            ListJobsRequest listJobsRequest = new ListJobsRequest(this.getVaultName());
            listJobsRequest.setRequestCredentials(this.getAWSCredentials());

            ListJobsResult listJobsResult = this.getAmazonGlacierClient().listJobs(listJobsRequest);

            JSONObject responseJsonObject = new JSONObject();
            JSONArray jobListJsonArray = new JSONArray();

            for (GlacierJobDescription gjd : listJobsResult.getJobList()) {

                JSONObject jdjo = new JSONObject();
                jdjo.put("Action", gjd.getAction() == null ? JSONObject.NULL : gjd.getAction());
                jdjo.put("ArchiveId", gjd.getArchiveId() == null ? JSONObject.NULL : gjd.getArchiveId());
                jdjo.put("ArchiveSizeInBytes",
                        gjd.getArchiveSizeInBytes() == null ? JSONObject.NULL : gjd.getArchiveSizeInBytes());
                jdjo.put("ArchiveSHA256TreeHash",
                        gjd.getArchiveSHA256TreeHash() == null ? JSONObject.NULL : gjd.getArchiveSHA256TreeHash());
                jdjo.put("Completed", gjd.getCompleted());
                jdjo.put("CompletionDate", gjd.getCompletionDate() == null ? JSONObject.NULL : gjd.getCompletionDate());
                jdjo.put("CreationDate", gjd.getCreationDate());
                jdjo.put("InventorySizeInBytes",
                        gjd.getInventorySizeInBytes() == null ? JSONObject.NULL : gjd.getInventorySizeInBytes());
                jdjo.put("JobDescription", gjd.getJobDescription() == null ? JSONObject.NULL : gjd.getJobDescription());
                jdjo.put("JobId", gjd.getJobId());
                jdjo.put("RetrievalByteRange",
                        gjd.getRetrievalByteRange() == null ? JSONObject.NULL : gjd.getRetrievalByteRange());
                jdjo.put("SHA256TreeHash", gjd.getSHA256TreeHash() == null ? JSONObject.NULL : gjd.getSHA256TreeHash());
                jdjo.put("SNSTopic", gjd.getSNSTopic() == null ? JSONObject.NULL : gjd.getSNSTopic());
                jdjo.put("StatusCode", gjd.getStatusCode());
                jdjo.put("StatusMessage", gjd.getStatusMessage());
                jdjo.put("VaultARN", gjd.getVaultARN());

                jobListJsonArray.put(jdjo);

            }

            responseJsonObject.put("JobList", jobListJsonArray);
            responseJsonObject.put("Marker",
                    listJobsResult.getMarker() == null ? JSONObject.NULL : listJobsResult.getMarker());

            System.out.println(responseJsonObject.toString(2));

        }

    }

}
