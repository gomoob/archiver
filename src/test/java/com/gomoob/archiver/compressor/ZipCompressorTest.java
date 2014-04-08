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
package com.gomoob.archiver.compressor;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Test;

import com.gomoob.archiver.compressor.impl.ZipCompressor;
import com.gomoob.archiver.configuration.archive.Archive;
import com.gomoob.archiver.configuration.archive.Src;
import com.gomoob.archiver.configuration.archive.Type;

/**
 * Test case for the {@link ZipCompressor} class.
 */
public class ZipCompressorTest {

    /**
     * Test method for the {@link ZipCompressor#compress(Archive)} function.
     */
    @Test
    public void testCompress() throws Exception {

        ICompressor zipCompressor = new ZipCompressor();
        
        // Test with an archive wich define a globbing pattern for all the files inside a directory
        Archive archive = new Archive();
        Src src = new Src();
        src.setGlobbingPattern("**/*");
        archive.setCwd("target/test-classes/files/file_set_2");
        archive.setDst("target/file_set_2.zip");
        archive.setSrc(src);
        archive.setType(Type.ZIP);

        File tempZipFile = zipCompressor.compress(archive);
        ZipFile zipFile = new ZipFile(tempZipFile);
        
        assertThat(tempZipFile).exists();
        assertThat(zipFile.getEntry("dir0/dir0_file.txt")).isNotNull();
        assertThat(zipFile.getEntry("dir1/dir1_file.txt")).isNotNull();
        assertThat(zipFile.getEntry("dir2/dir2_file.txt")).isNotNull();
        assertThat(zipFile.getEntry("file.txt")).isNotNull();
        
        zipFile.close();
        tempZipFile.delete();
        
        // Test with an archive which define globbing pattern with inclusions and exclusions
        archive = new Archive();
        src = new Src();
        List<String> globbingPatterns = new ArrayList<String>();
        globbingPatterns.add("**/*");
        globbingPatterns.add("!dir0/**/*");
        src.setGlobbingPatterns(globbingPatterns);
        archive.setCwd("target/test-classes/files/file_set_2");
        archive.setDst("target/file_set_2.zip");
        archive.setSrc(src);
        archive.setType(Type.ZIP);

        tempZipFile = zipCompressor.compress(archive);
        zipFile = new ZipFile(tempZipFile);
        
        assertThat(tempZipFile).exists();
        assertThat(zipFile.getEntry("dir0/dir0_file.txt")).isNull();
        assertThat(zipFile.getEntry("dir1/dir1_file.txt")).isNotNull();
        assertThat(zipFile.getEntry("dir2/dir2_file.txt")).isNotNull();
        assertThat(zipFile.getEntry("file.txt")).isNotNull();
        
        zipFile.close();
        tempZipFile.delete();
        
    }

}
