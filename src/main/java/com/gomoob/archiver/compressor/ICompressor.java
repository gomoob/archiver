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

import java.io.IOException;

import com.gomoob.archiver.ArchiveFile;
import com.gomoob.archiver.configuration.archive.Archive;

/**
 * Interface which defines a component used to compress files and directories.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public interface ICompressor {

    /**
     * Function used to compress an archive.
     * <p>
     * WARNING: When this function succeeds it returns a file created in the temporary directory of the system. After
     * use you'll have to delete the returned file to place the file system in the same state has it was before the
     * call. This is very important to not fill your file system and even crash your server !
     * </p>
     * 
     * @param archive the configuration of the archive to create.
     * @return an absolute path to the created archive, the path is a reference to a temporary file inside the temporary
     *         directory of the file system.
     * @throws IOException If an I/O error occurs while compressing the archive.
     */
    ArchiveFile compress(Archive archive) throws IOException;

}
