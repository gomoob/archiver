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
package com.gomoob.archiver.compressor.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.DirectoryScanner;

import com.gomoob.archiver.compressor.ICompressor;
import com.gomoob.archiver.configuration.archive.Archive;
import com.gomoob.archiver.configuration.archive.Src;

/**
 * Abstract class common to all compressor components.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public abstract class AbstractCompressor implements ICompressor {

    protected abstract File doCompress(Archive archive, String[] filePaths) throws IOException;

    /**
     * {@inheritDoc}
     */
    @Override
    public File compress(Archive archive) throws IOException {

        // Gets the path to the files to inject into the archive
        String[] filePaths = this.processGlobbingPatterns(archive);

        // Gets the temporary archive which have been produced
        return this.doCompress(archive, filePaths);

    }

    /**
     * Utility function used to process the globbing patterns specified into an archive configuration and returns the
     * path of the files to be compressed into a zipped archive.
     * 
     * @param archive the archive which contains the configuration of the globbing patterns to process.
     * @return an array of strings which contains the relative path to the files to compress. WARNING: All the returned
     *         path are relative to the 'cwd' parameter path of the provided archive.
     */
    private String[] processGlobbingPatterns(Archive archive) {

        // Test if the base directory exists
        File cwdDir = new File(archive.getCwd());

        if (!cwdDir.exists()) {

            // TODO: Erreur...

        }

        // Creates an Apache Ant directory scanner to scan the files associated to the globbing patterns
        DirectoryScanner directoryScanner = new DirectoryScanner();
        directoryScanner.setBasedir(cwdDir.getAbsolutePath());

        // Process the globbing patterns to create inclusion and exclusion patterns
        Src src = archive.getSrc();

        List<String> includes = new ArrayList<String>();
        List<String> excludes = new ArrayList<String>();

        if (src.getGlobbingPatten() != null) {

            if (src.getGlobbingPatten().startsWith("!")) {

                // TODO: Erreur

            }

            includes.add(src.getGlobbingPatten());

        } else if (src.getGlobbingPatterns() != null) {

            if (src.getGlobbingPatterns().size() == 0) {

                // TODO: Erreur

            }

            for (String globbingPattern : src.getGlobbingPatterns()) {

                if (globbingPattern.startsWith("!")) {

                    excludes.add(globbingPattern.substring(1));

                } else {

                    includes.add(globbingPattern);

                }

            }

        } else {

            // TODO: Erreur

        }

        directoryScanner.setIncludes(includes.toArray(new String[] {}));
        directoryScanner.setExcludes(excludes.toArray(new String[] {}));
        directoryScanner.setCaseSensitive(true);
        directoryScanner.scan();

        return directoryScanner.getIncludedFiles();

    }

}
