/**
 * Copyright 2014 SARL GOMOOB. All rights reserved.
 */
package com.gomoob.archiver.configuration.archive;

/**
 * A configuration which describe how to create an archive to be sent on Amazon Glacier.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public class Archive {

    /**
     * The path from which one the 'src' matches are relative to.
     */
    private String cwd = null;

    /**
     * The name of the destination archives.
     */
    private String dst = null;

    /**
     * The uniq identifier of the archive.
     */
    private String id = null;

    /**
     * The source files configuration.
     */
    private Src src = new Src();

    /**
     * The type of the archives to create.
     */
    private Type type = Type.ZIP;

    /**
     * Gets the path from which one the 'src' matches are relative to.
     * 
     * @return the path from which one the 'src' matches are relative to.
     */
    public String getCwd() {

        return this.cwd;

    }

    /**
     * Gets the name of the destination archives.
     * 
     * @return the name of the destination archives.
     */
    public String getDst() {

        return this.dst;

    }

    /**
     * Gets the uniq identifier of the archive.
     * 
     * @return the uniq identifier of the archive.
     */
    public String getId() {

        return this.id;

    }

    /**
     * Gets the source files configuration.
     * 
     * @return the source files configuration.
     */
    public Src getSrc() {

        return this.src;

    }

    /**
     * Gets the type of the archives to create.
     * 
     * @return the type of the archives to create.
     */
    public Type getType() {

        return this.type;

    }

    /**
     * Sets the path from which one the 'src' matches are relative to.
     * 
     * @param cwd the path from which one the 'src' matches are relative to.
     */
    public void setCwd(String cwd) {

        this.cwd = cwd;

    }

    /**
     * Sets the name of the destination archives.
     * 
     * @param dst the name of the destination archives.
     */
    public void setDst(String dst) {

        this.dst = dst;

    }

    /**
     * Sets the uniq identifier of the archive.
     * 
     * @param id the uniq identifier of the archive.
     */
    public void setId(String id) {

        this.id = id;

    }

    /**
     * Sets the source files configuration.
     * 
     * @param src the source files configuration.
     */
    public void setSrc(Src src) {

        this.src = src;

    }

    /**
     * Sets the type of the archives to create.
     * 
     * @param type the type of the archives to created.
     */
    public void setType(Type type) {

        this.type = type;

    }

}
