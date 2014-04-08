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
package com.gomoob.archiver.component.glacier;

import java.util.Map;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import com.gomoob.archiver.component.AbstractPlugin;
import com.gomoob.archiver.component.ICommand;
import com.gomoob.archiver.component.glacier.command.archive.DownloadArchiveCommand;
import com.gomoob.archiver.component.glacier.command.archive.PostArchiveCommand;
import com.gomoob.archiver.component.glacier.command.job.GetJobOutputCommand;
import com.gomoob.archiver.component.glacier.command.job.InitiateVaultInventoryJobCommand;
import com.gomoob.archiver.component.glacier.command.job.ListJobsCommand;
import com.gomoob.archiver.component.glacier.command.multipartupload.MultipartUploadCommand;
import com.gomoob.archiver.component.glacier.command.vault.DescribeVaultCommand;
import com.gomoob.archiver.component.glacier.command.vault.ListVaultsCommand;

public class GlacierPlugin extends AbstractPlugin {

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("static-access")
    @Override
    protected void doConfigureOptions(Options options) {

        //@formatter:off
        Option describeVaultOption = OptionBuilder
                .withLongOpt("describe-vault")
                .withDescription("Print informations about a vault.")
                .create();

        Option downloadOption = OptionBuilder
                .withLongOpt("download")
                .withDescription("Download an archive.")
                .create();

        Option listJobsOption = OptionBuilder
                .withLongOpt("list-jobs")
                .withDescription("List jobs for a vault including jobs that are in-progress and jobs that have recently finished.")
                .create();
        
        Option listVaultsOption = OptionBuilder
                .withLongOpt("list-vaults")
                .withDescription("List vaults associated to an AWS account.")
                .create();

        Option getJobOutputOption = OptionBuilder
                .withLongOpt("get-job-output")
                .withDescription("Gets the output of a job.")
                .create();
        
        Option initiateVaultInventoryJobOption = OptionBuilder
                .withLongOpt("initiate-vault-inventory-job")
                .withDescription("Initiate a vault inventory job.")
                .create();

        Option initiateArchiveRetrievalJobOption = OptionBuilder
                .withLongOpt("initiate-archive-retrieval-job")
                .withDescription("Initiate an archive retrieval job.")
                .create();
        
        Option multipartUploadOption = OptionBuilder
                .withLongOpt("multipart-upload")
                .withDescription("Uploads an archive using a multipart upload.")
                .create();
        
        Option uploadArchiveOption = OptionBuilder
                .withLongOpt("upload-archive")
                .withDescription("Upload an archive into a vault.")
                .create();
        
        //@formatter:on

        options.addOption(describeVaultOption);
        options.addOption(downloadOption);
        options.addOption(getJobOutputOption);
        options.addOption(initiateVaultInventoryJobOption);
        options.addOption(initiateArchiveRetrievalJobOption);
        options.addOption(listJobsOption);
        options.addOption(listVaultsOption);
        options.addOption(multipartUploadOption);
        options.addOption(uploadArchiveOption);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doConfigureCommands(Map<String, Class<? extends ICommand>> commands) {

        commands.put("describe-vault", DescribeVaultCommand.class);
        commands.put("download", DownloadArchiveCommand.class);
        commands.put("get-job-output", GetJobOutputCommand.class);
        commands.put("initiate-vault-inventory-job", InitiateVaultInventoryJobCommand.class);
        commands.put("list-jobs", ListJobsCommand.class);
        commands.put("list-vaults", ListVaultsCommand.class);
        commands.put("multipart-upload", MultipartUploadCommand.class);
        commands.put("upload-archive", PostArchiveCommand.class);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String doGetName() {

        return "glacier";

    }

}
