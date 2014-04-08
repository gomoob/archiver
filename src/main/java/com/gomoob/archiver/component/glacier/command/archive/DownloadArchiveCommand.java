package com.gomoob.archiver.component.glacier.command.archive;

import java.io.File;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.gomoob.archiver.component.glacier.command.AbstractGlacierCommand;

/**
 * Command used to download an Amazon Glacier archive.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public class DownloadArchiveCommand extends AbstractGlacierCommand {

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("static-access")
    @Override
    public void processCommand(String[] args) {

        Options options = new Options();

        //@formatter:off
        Option glacierarchiveidOption = OptionBuilder
                .withLongOpt("glacier-archive-id")
                .withDescription("The identifier of the Amazon Glacier archive to download.")
                .hasArg()
                .withArgName("CLOUD_FRONT_ARCHIVE_ID")
                .create();

        Option outputOption = OptionBuilder
                .withLongOpt("output")
                .withDescription("The path to the output file to create.")
                .hasArg()
                .withArgName("FILE_PATH")
                .create();
        //@formatter:on

        
        options.addOption(this.createAStoreIdOption());
        options.addOption(this.createHelpOption());
        options.addOption(glacierarchiveidOption);
        options.addOption(outputOption);

        CommandLineParser commandLineParser = new PosixParser();

        try {

            CommandLine commandLine = commandLineParser.parse(options, args);

            if (commandLine.getOptions().length == 1 && commandLine.hasOption("help")) {

                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("garchive -glacier -download", options);

            }

            if (!commandLine.hasOption("store")) {

            }

            if (!commandLine.hasOption("glacierarchiveid")) {

            }

            if (!commandLine.hasOption("output")) {

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
     * @param archiveId
     */
    private void download(AmazonGlacierClient amazonGlacierClient, AWSCredentials awsCredentials,
            Properties configuration, String archiveId) {

        ArchiveTransferManager archiveTransferManager = new ArchiveTransferManager(amazonGlacierClient, awsCredentials);

        archiveTransferManager.download(configuration.getProperty("vaultName"), archiveId, new File("downloaded.txt"));

    }

}
