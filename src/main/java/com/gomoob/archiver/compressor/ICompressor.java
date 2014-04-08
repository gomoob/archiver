package com.gomoob.archiver.compressor;

import java.io.File;
import java.io.IOException;

import com.gomoob.archiver.configuration.archive.Archive;

/**
 * Interface which defines a component used to compress files and directories.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public interface ICompressor {

    /**
     * Function used to compress an archive.
     * 
     * @param archive the configuration of the archive to create.
     * @return an absolute path to the created archive, the path is a reference to a temporary file inside the temporary
     *         directory of the system.
     * @throws IOException TODO: A documenter
     */
    File compress(Archive archive) throws IOException;

}
