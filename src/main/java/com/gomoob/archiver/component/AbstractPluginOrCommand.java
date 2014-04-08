package com.gomoob.archiver.component;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import com.gomoob.archiver.ArchiveLocator;
import com.gomoob.archiver.configuration.Configuration;

public abstract class AbstractPluginOrCommand {

    /**
     * The component used to locate and build archive files to manage.
     */
    ArchiveLocator archiveLocator;

    /**
     * The command line arguments passed to the plugin or the command.
     */
    String[] args;

    /**
     * The archiver configuration which is currently in use.
     */
    Configuration configuration;

    /**
     * The command line options configured in the plugin or the command.
     * <p>
     * WARNING: The concrete plugin or command implementations MUST NOT add the help option to the options. The help
     * option is ALWAYS automatically added to the options.
     * </p>
     */
    Options options;

    /**
     * Sets the archiver configuration to use.
     * 
     * @param configuration the archiver configuration to use.
     */
    public void setConfiguration(Configuration configuration) {

        this.configuration = configuration;

        // Sets the configuration attached to the archive locator
        this.archiveLocator.setConfiguration(configuration);

    }

    /**
     * Creates a new {@link AbstractPluginOrCommand} instance.
     */
    protected AbstractPluginOrCommand() {

        this.archiveLocator = new ArchiveLocator();

    }

    /**
     * Creates the help option of the plugin or the command.
     * 
     * @return the help option of the plugin or the command.
     */
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

    /**
     * Function used to configure the command line options of the plugin or the command.
     * 
     * @param options the options to be configured in the plugin or the command.
     */
    protected abstract void doConfigureOptions(Options options);

    /**
     * Gets the help footer of the plugin or the command.
     * 
     * @return the help footer of the plugin or the command.
     */
    protected String doGetHelpFooter() {

        return "";

    }

    /**
     * Gets the help header of the plugin or the command.
     * 
     * @return the help footer of the plugin or the command.
     */
    protected String doGetHelpHeader() {

        return "";

    }
    
    /**
     * Gets the component used to locate and build archive files to manage.
     * 
     * @return the component used to locate and build archive files to manage.
     */
    protected ArchiveLocator getArchiveLocator() {

        return this.archiveLocator;

    }

    /**
     * Gets the command line arguments passed to the plugin or the command.
     * 
     * @return the command line arguments passed to the plugin or the command.
     */
    protected String[] getArgs() {

        return this.args;

    }

    /**
     * Gets the command line options configured in the plugin or the command.
     * <p>
     * WARNING: The concrete plugin or command implementations MUST NOT add the help option to the options. The help
     * option is ALWAYS automatically added to the options.
     * </p>
     * 
     * @return the command line options configured in the plugin or the command.
     */
    protected Options getOptions() {

        return this.options;

    }

}
