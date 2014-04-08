package com.gomoob.archiver.component.glacier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.GlacierJobDescription;
import com.amazonaws.services.glacier.model.ListJobsRequest;
import com.amazonaws.services.glacier.model.ListJobsResult;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.glacier.transfer.UploadResult;
import com.gomoob.archiver.component.AbstractCommand;
import com.gomoob.archiver.component.glacier.command.archive.DownloadArchiveCommand;
import com.gomoob.archiver.component.glacier.command.archive.UploadArchiveCommand;
import com.gomoob.archiver.component.glacier.command.job.GetJobOutputCommand;
import com.gomoob.archiver.component.glacier.command.job.InitiateVaultInventoryJobCommand;
import com.gomoob.archiver.component.glacier.command.job.ListJobsCommand;
import com.gomoob.archiver.component.glacier.command.vault.DescribeVaultCommand;
import com.gomoob.archiver.component.glacier.command.vault.ListVaultsCommand;

public class GlacierCommands extends AbstractCommand {

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("static-access")
    @Override
    public void processCommand(String[] args) {

        Options options = new Options();

        //@formatter:off
        Option describeVaultOption = OptionBuilder
                .withLongOpt("describe-vault")
                .withDescription("Print informations about a vault.")
                .create();

        Option downloadOption = OptionBuilder
                .withLongOpt("download")
                .withDescription("Download an archive.")
                .create();
        
        Option helpOption = OptionBuilder
                .withLongOpt("help")
                .withDescription("Print this message.")
                .create();

        Option listJobsOption = OptionBuilder
                .withLongOpt("list-jobs")
                .withDescription("List jobs for a vault including jobs that are in-progress and jobs that have recently finished.")
                .create();
        
        Option listVaultsOption = OptionBuilder
                .withLongOpt("list-vaults")
                .withDescription("List vaults associated to an AWS account.")
                .create();
        
        Option backupOption = OptionBuilder
                .withLongOpt("backup")
                .withDescription("")
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
        
        Option uploadArchiveOption = OptionBuilder
                .withLongOpt("upload-archive")
                .withDescription("Upload an archive into a vault.")
                .create();
        
        //@formatter:on

        options.addOption(backupOption);
        options.addOption(describeVaultOption);
        options.addOption(helpOption);
        options.addOption(downloadOption);
        options.addOption(getJobOutputOption);
        options.addOption(initiateVaultInventoryJobOption);
        options.addOption(initiateArchiveRetrievalJobOption);
        options.addOption(listJobsOption);
        options.addOption(listVaultsOption);
        options.addOption(uploadArchiveOption);

        CommandLineParser commandLineParser = new PosixParser();

        try {

            String[] firstArgs = new String[] {};

            if (args.length > 0) {

                firstArgs = Arrays.copyOfRange(args, 0, 1);

            }

            CommandLine commandLine = commandLineParser.parse(options, firstArgs);

            if (commandLine.getOptions().length == 0
                    || (commandLine.getOptions().length == 1 && commandLine.hasOption("help"))) {

                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp(120, "archiver --glacier", "", options, "");

            } else if (commandLine.hasOption("describe-vault")) {

                DescribeVaultCommand retrieveVaultMetadataCommand = new DescribeVaultCommand();
                retrieveVaultMetadataCommand.setConfiguration(this.configuration);
                retrieveVaultMetadataCommand.processCommand(Arrays.copyOfRange(args, 1, args.length));

            } else if (commandLine.hasOption("download")) {

                DownloadArchiveCommand downloadCommand = new DownloadArchiveCommand();
                downloadCommand.setConfiguration(this.configuration);
                downloadCommand.processCommand(Arrays.copyOfRange(args, 1, args.length));

            }
            
            else if (commandLine.hasOption("get-job-output")) {
                
                GetJobOutputCommand getJobOutputCommand = new GetJobOutputCommand();
                getJobOutputCommand.setConfiguration(this.configuration);
                getJobOutputCommand.processCommand(Arrays.copyOfRange(args, 1, args.length));
                
            }

            else if (commandLine.hasOption("initiate-vault-inventory-job")) {

                InitiateVaultInventoryJobCommand initiateVaultInventoryJobCommand = new InitiateVaultInventoryJobCommand();
                initiateVaultInventoryJobCommand.setConfiguration(this.configuration);
                initiateVaultInventoryJobCommand.processCommand(Arrays.copyOfRange(args, 1, args.length));

            }

            else if (commandLine.hasOption("initiate-vault-inventory-job")) {

            }

            else if (commandLine.hasOption("list-jobs")) {

                ListJobsCommand listJobsCommand = new ListJobsCommand();
                listJobsCommand.setConfiguration(this.configuration);
                listJobsCommand.processCommand(Arrays.copyOfRange(args, 1, args.length));

            } else if (commandLine.hasOption("list-vaults")) {

                ListVaultsCommand listVaultsCommand = new ListVaultsCommand();
                listVaultsCommand.setConfiguration(this.configuration);
                listVaultsCommand.processCommand(Arrays.copyOfRange(args, 1, args.length));

            } else if (commandLine.hasOption("upload-archive")) {

                UploadArchiveCommand uploadArchiveCommand = new UploadArchiveCommand();
                uploadArchiveCommand.setConfiguration(this.configuration);
                uploadArchiveCommand.processCommand(Arrays.copyOfRange(args, 1, args.length));

            }

        } catch (ParseException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        }

    }

    /**
     * @param amazonGlacierClient
     * @param awsCredentials
     * @param configuration
     */
    private static void backup(AmazonGlacierClient amazonGlacierClient, AWSCredentials awsCredentials,
            Properties configuration) {

        ArchiveTransferManager archiveTransferManager = new ArchiveTransferManager(amazonGlacierClient, awsCredentials);

        File backupDirectory = new File(configuration.getProperty("backupDirectory"));

        for (File file : backupDirectory.listFiles()) {

            // Glacier fails to save empty files to we prefer to warn the user about this
            if (file.length() == 0) {

                throw new IllegalStateException("The file '" + file.getAbsolutePath() + "' is empty !");

            }

            System.out.println(file.getAbsolutePath());

            try {

                UploadResult uploadResult = archiveTransferManager.upload(configuration.getProperty("vaultName"),
                        file.getName(), file);
                System.out.println("Archive ID : " + uploadResult.getArchiveId());

            } catch (AmazonServiceException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (AmazonClientException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

}
