package com.gomoob.archiver;

import java.io.File;
import java.net.URI;

import com.gomoob.archiver.configuration.archive.Archive;

/**
 * Extension of the {@link File} class used to add additional functionalities specific to the archiver.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public class ArchiveFile extends File {

    /**
     * The default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The archiver archive which have been used to create this archive file.
     */
    private Archive archive;

    /**
     * Boolean which indicate if the archive file has to be deleted after it is processed. Generally an archive file has
     * to be deleted when it has been created by a compressor or an other kind of component inside the temporary
     * directory of the file system.
     */
    private boolean toBeDeletedAfterProcess = false;

    /**
     * Creates a new <code>ArchiveFile</code> instance from a parent abstract pathname and a child pathname string.
     * <p>
     * If <code>parent</code> is <code>null</code> then the new <code>File</code> instance is created as if by invoking
     * the single-argument <code>File</code> constructor on the given <code>child</code> pathname string.
     * <p>
     * Otherwise the <code>parent</code> abstract pathname is taken to denote a directory, and the <code>child</code>
     * pathname string is taken to denote either a directory or a file. If the <code>child</code> pathname string is
     * absolute then it is converted into a relative pathname in a system-dependent way. If <code>parent</code> is the
     * empty abstract pathname then the new <code>File</code> instance is created by converting <code>child</code> into
     * an abstract pathname and resolving the result against a system-dependent default directory. Otherwise each
     * pathname string is converted into an abstract pathname and the child abstract pathname is resolved against the
     * parent.
     * 
     * @param parent The parent abstract pathname.
     * @param child The child pathname string.
     * @throws NullPointerException If <code>child</code> is <code>null</code>
     */
    public ArchiveFile(File parent, String child) {

        super(parent, child);

    }

    /**
     * Creates a new <code>ArchiveFile</code> instance by converting the given pathname string into an abstract
     * pathname. If the given string is the empty string, then the result is the empty abstract pathname.
     * 
     * @param pathname A pathname string.
     * @throws NullPointerException If the <code>pathname</code> argument is <code>null</code>
     */
    public ArchiveFile(String pathname) {

        super(pathname);

    }

    /*
     * Note: The two-argument File constructors do not interpret an empty parent abstract pathname as the current user
     * directory. An empty parent instead causes the child to be resolved against the system-dependent directory defined
     * by the FileSystem.getDefaultParent method. On Unix this default is "/", while on Microsoft Windows it is "\\".
     * This is required for compatibility with the original behavior of this class.
     */

    /**
     * Creates a new <code>ArchiveFile</code> instance from a parent pathname string and a child pathname string.
     * <p>
     * If <code>parent</code> is <code>null</code> then the new <code>File</code> instance is created as if by invoking
     * the single-argument <code>File</code> constructor on the given <code>child</code> pathname string.
     * <p>
     * Otherwise the <code>parent</code> pathname string is taken to denote a directory, and the <code>child</code>
     * pathname string is taken to denote either a directory or a file. If the <code>child</code> pathname string is
     * absolute then it is converted into a relative pathname in a system-dependent way. If <code>parent</code> is the
     * empty string then the new <code>File</code> instance is created by converting <code>child</code> into an abstract
     * pathname and resolving the result against a system-dependent default directory. Otherwise each pathname string is
     * converted into an abstract pathname and the child abstract pathname is resolved against the parent.
     * 
     * @param parent The parent pathname string.
     * @param child The child pathname string.
     * @throws NullPointerException If <code>child</code> is <code>null</code>
     */
    public ArchiveFile(String parent, String child) {

        super(parent, child);

    }

    /**
     * Creates a new <tt>ArchiveFile</tt> instance by converting the given <tt>file:</tt> URI into an abstract pathname.
     * <p>
     * The exact form of a <tt>file:</tt> URI is system-dependent, hence the transformation performed by this
     * constructor is also system-dependent.
     * <p>
     * For a given abstract pathname <i>f</i> it is guaranteed that <blockquote><tt>
     * new File(</tt><i>&nbsp;f</i><tt>.{@link #toURI() toURI}()).equals(</tt><i>&nbsp;f</i>
     * <tt>.{@link #getAbsoluteFile() getAbsoluteFile}())
     * </tt></blockquote> so long as the original abstract pathname, the URI, and the new abstract pathname are all
     * created in (possibly different invocations of) the same Java virtual machine. This relationship typically does
     * not hold, however, when a <tt>file:</tt> URI that is created in a virtual machine on one operating system is
     * converted into an abstract pathname in a virtual machine on a different operating system.
     * 
     * @param uri An absolute, hierarchical URI with a scheme equal to <tt>"file"</tt>, a non-empty path component, and
     *            undefined authority, query, and fragment components
     * @throws NullPointerException If <tt>uri</tt> is <tt>null</tt>
     * @throws IllegalArgumentException If the preconditions on the parameter do not hold
     * @see #toURI()
     * @see java.net.URI
     * @since 1.4
     */
    public ArchiveFile(URI uri) {

        super(uri);

    }

    /**
     * Gets the archiver archive which have been used to create this archive file.
     * 
     * @return the archiver archive which have been used to crate this archive file.
     */
    public Archive getArchive() {

        return this.archive;

    }

    /**
     * Indicates if the archive file has to be deleted after it has been processed.
     * 
     * @return <code>true</code> if the file has to be deleted after its processed, <code>false</code> otherwise.
     */
    public boolean isToBeDeletedAfterProcess() {

        return this.toBeDeletedAfterProcess;

    }

    /**
     * Sets the archiver archive which have been used to create this archive file.
     * 
     * @param archive the archiver archive which have been used to create this archive file.
     */
    public void setArchive(Archive archive) {

        this.archive = archive;

    }

    /**
     * Sets if the archive file has to be deleted after it has been processed.
     * 
     * @param toBeDeletedAfterProcess <code>true</code> if the file has to be deleted after its processed,
     *            <code>false</code> otherwise.
     */
    public void setToBeDeletedAfterProcessed(boolean toBeDeletedAfterProcess) {

        this.toBeDeletedAfterProcess = toBeDeletedAfterProcess;

    }

}
