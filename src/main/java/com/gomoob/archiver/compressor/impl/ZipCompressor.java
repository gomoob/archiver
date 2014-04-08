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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.gomoob.archiver.ArchiveFile;
import com.gomoob.archiver.configuration.archive.Archive;

/**
 * Compressor used to create ZIP files.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public class ZipCompressor extends AbstractCompressor {

    /**
     * {@inheritDoc}
     */
    @Override
    protected ArchiveFile doCompress(Archive archive, String[] filePaths) throws IOException {

        // Test if the base directory exists
        File cwdDir = new File(archive.getCwd());

        // The 'cwd' directory must exist
        if (!cwdDir.exists()) {

            throw new IOException("The 'cwd' directory '" + cwdDir.getAbsolutePath() + "' does not exist !");

        }

        // Creates a temporary file for the ZIP file to produce
        File tempDirectory = FileUtils.getTempDirectory();
        String archiveFilePath = FileUtils.getFile(tempDirectory, UUID.randomUUID().toString()).getAbsolutePath();
        
        // Transforms the archive file into an ArchiveFile instance and sets it to be deleted after processed
        ArchiveFile archiveFile = new ArchiveFile(archiveFilePath);
        archiveFile.setArchive(archive);
        archiveFile.setToBeDeletedAfterProcessed(true);
        
        // TODO: Créer un fichier temporaire
        ArchiveOutputStream aos = new ZipArchiveOutputStream(archiveFile);

        System.out.println("Number of files to process : '" + filePaths.length + "'.");

        double initialeSize = 0;
        
        // Computes the total size of the files to archive
        for (int i = 0; i < filePaths.length; ++i) {
            
            File file = new File(FilenameUtils.concat(cwdDir.getAbsolutePath(), filePaths[i]));
            initialeSize += (double) file.length() / 1073741824.0;
            
        }
        
        System.out.println("Size of data to compress : " + initialeSize + " Go");
        
        double goWritten = 0;
        double percentage = 0.0;
        int roundedPercentage = 0;

        // For each file to inject into the archive
        for (int i = 0; i < filePaths.length; i++) {

            File file = new File(FilenameUtils.concat(cwdDir.getAbsolutePath(), filePaths[i]));
            InputStream is = new FileInputStream(file);

            ZipArchiveEntry zae = new ZipArchiveEntry(filePaths[i]);
            zae.setSize(file.length());
            aos.putArchiveEntry(zae);

            byte buffer[] = new byte[1024];
            int nbRead = 0;

            while ((nbRead = is.read(buffer)) > 0) {

                aos.write(buffer, 0, nbRead);

                // Updates the number of Gigabytes written
                goWritten += (1024.0 / 1073741824.0);
                
                // Updates the percentage
                percentage = goWritten / initialeSize * 100;

                // If the rounded percentage is greater than the previous rounded percentage we display the updated 
                // percentage
                if((int) percentage > roundedPercentage) {

                    roundedPercentage = (int) percentage;
                    System.out.println(new Date().toString() + " - " + roundedPercentage + "%");
                    
                }

            }

            aos.closeArchiveEntry();

            is.close();
            
        }

        aos.close();

        return archiveFile;

    }

}
