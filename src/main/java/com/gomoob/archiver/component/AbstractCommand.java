package com.gomoob.archiver.component;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import com.gomoob.archiver.compressor.ICompressor;
import com.gomoob.archiver.compressor.ZipCompressor;
import com.gomoob.archiver.configuration.Configuration;
import com.gomoob.archiver.configuration.archive.Type;

public abstract class AbstractCommand implements ICommand {

    protected Configuration configuration;

    @SuppressWarnings("static-access")
    protected Option createAArchiveIdOption() {
        
        Option aArchiveIdOption = OptionBuilder
                .withLongOpt("a-archive-id")
                .withDescription("Identifier of an archiver archive.")
                .hasArg()
                .withArgName("ARCHIVER_ARCHIVE_ID")
                .create();
        
        return aArchiveIdOption;
        
    }
    
    @SuppressWarnings("static-access")
    protected Option createACredentialsIdOption() {
        
        Option aCredentialsIdOption = OptionBuilder
                .withLongOpt("a-credentials-id")
                .withDescription("Identifier of an archiver credentials.")
                .hasArg()
                .withArgName("ARCHIVER_CREDENTIALS_ID").create();
        
        return aCredentialsIdOption;
        
    }
    
    @SuppressWarnings("static-access")
    protected Option createAStoreIdOption() {
        
        Option storeIdOption = OptionBuilder
                .withLongOpt("a-store-id")
                .withDescription("Identifier of an archiver store.")
                .hasArg()
                .withArgName("ARCHIVER_STORE_ID").create();
        
        return storeIdOption;
        
    }
    
    @SuppressWarnings("static-access")
    protected Option createHelpOption() {
        
        //@formatter:off
        Option helpOption = OptionBuilder
                .withLongOpt("help")
                .withDescription("Print this message")
                .create();
        //@formatter:on
        
        return helpOption;
        
    }
    
    public ICompressor getCompressor(Type type) {

        ICompressor compressor = null;

        if (type == Type.ZIP) {

            compressor = new ZipCompressor();

        }

        return compressor;

    }

    public Configuration getConfiguration() {

        return this.configuration;

    }

    public void setConfiguration(Configuration configuration) {

        this.configuration = configuration;

    }

}
