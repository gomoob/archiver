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
package com.gomoob.archiver.configuration.archive;

/**
 * Enumeration for archive types.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public enum Type {

    /**
     * Archive type used to take the 'src' of an archive as the destination file to archive (i.e no compression is
     * performed using this type).
     * <p>
     * When this type is used inside an archive declaration the 'dst' MUST ALWAYS be <code>null</code> and the 'src'
     * parameter MUST ALWAYS be a uniq file which is not a directory.
     * </p>
     */
    RAW,

    /**
     * Archive type used to compress a file or directory using the TAR.GZIP algorithm.
     */
    TAR_GZ,

    /**
     * Archive type used to compress a file or directory using the ZIP algorithm.
     */
    ZIP
}
