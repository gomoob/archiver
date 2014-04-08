/**
 * Copyright 2014 SARL GOMOOB. All rights reserved.
 */
package com.gomoob.archiver.configuration.archive;

import java.util.List;

/**
 * Class used to defines sets of file sources using globbing patterns.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 * @see http://docs.oracle.com/javase/tutorial/essential/io/fileOps.html#glob
 */
public class Src {

    /**
     * A globbing pattern which is defined when only one globbing pattern is provided. This attribute is null where
     * multiple globbing patterns are defined.
     */
    private String globbingPattern = null;

    /**
     * An array of globbing patterns. This attribute is null when only one globbing pattern is defines without an array
     * declaration.
     */
    private List<String> globbingPatterns = null;

    /**
     * Gets the globbing pattern which is defined when only one globbing pattern is provided. This attribute is null
     * where multiple globbing patterns are defined.
     * 
     * @return the globbing pattern which is defined when only one globbing pattern is provided. This attribute is null
     *         where multiple globbing patterns are defined.
     */
    public String getGlobbingPatten() {

        return this.globbingPattern;
    }

    /**
     * Gets the array of globbing patterns. This attribute is null when only one globbing pattern is defines without an
     * array declaration.
     * 
     * @return the array of globbing patterns. This attribute is null when only one globbing pattern is defines without
     *         an array declaration.
     */
    public List<String> getGlobbingPatterns() {

        return this.globbingPatterns;

    }

    /**
     * Sets the globbing pattern which is defined when only one globbing pattern is provided. This attribute is null
     * where multiple globbing patterns are defined.
     * 
     * @param globbingPattern the globbing pattern which is defined when only one globbing pattern is provided. This
     *            attribute is null where multiple globbing patterns are defined.
     */
    public void setGlobbingPattern(String globbingPattern) {

        this.globbingPattern = globbingPattern;

    }

    /**
     * Sets the array of globbing patterns. This attribute is null when only one globbing pattern is defines without an
     * array declaration.
     * 
     * @param globbingPatterns the array of globbing patterns. This attribute is null when only one globbing pattern is
     *            defines without an array declaration.
     */
    public void setGlobbingPatterns(List<String> globbingPatterns) {

        this.globbingPatterns = globbingPatterns;

    }

}
