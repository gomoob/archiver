package com.gomoob.archiver.component;

import com.gomoob.archiver.configuration.Configuration;


public interface ICommand {

    void processCommand(String[] args);
    
    void setConfiguration(Configuration configuration);

}
