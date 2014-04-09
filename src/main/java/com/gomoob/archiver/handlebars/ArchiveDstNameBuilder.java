package com.gomoob.archiver.handlebars;

import java.io.IOException;
import java.util.Date;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.helper.StringHelpers;
import com.gomoob.archiver.ArchiveFile;
import com.gomoob.archiver.configuration.archive.Archive;

public class ArchiveDstNameBuilder {

    private ArchiveFile archiveFile;

    private String templateString;

    public void setArchiveFile(ArchiveFile archiveFile) {

        this.archiveFile = archiveFile;

    }

    public void setTemplateString(String templateString) {

        this.templateString = templateString;

    }

    public String build() throws IOException {

        // An archive file must have been defined
        if (this.archiveFile == null) {

            throw new IllegalStateException("No archive file has been defined !");
            
        }
        
        Archive archive = this.archiveFile.getArchive();

        Handlebars handlebars = new Handlebars();
        handlebars.registerHelper("dateFormat", StringHelpers.dateFormat);
        
        Context context = Context.newContext(null);

        context.data("archiveFile.canExecute", this.archiveFile.canExecute());
        context.data("archiveFile.canRead", this.archiveFile.canRead());
        context.data("archiveFile.canWrite", this.archiveFile.canWrite());
        context.data("archiveFile.hashCode", this.archiveFile.hashCode());
        context.data("archiveFile.isHidden", this.archiveFile.isHidden());
        context.data("archiveFile.lastModified", this.archiveFile.lastModified());
        context.data("archiveFile.lastModificationDate", new Date(this.archiveFile.lastModified()));
        context.data("archiveFile.length", this.archiveFile.length());
        context.data("archiveFile.created", this.archiveFile.getCreationDate().getTime());
        context.data("archiveFile.creationDate", this.archiveFile.getCreationDate());

        String dstName = this.archiveFile.getName();

        // Try to generate an archive destination name using the archive 'dst'
        if (archive != null && archive.getDst() != null) {

            if (this.templateString != null) {

                //@formatter:off
                throw new IllegalStateException(
                    "Cannot determine the 'dst' string to use because the archive file is liked to an archive with " + 
                    "a 'dst' and a template file is provided !"
                );
                //@formatter:on

            }

            Template compiledTemplate = handlebars.compileInline(archive.getDst());
            dstName = compiledTemplate.apply(context);

        }

        // Try to generate an archive destination name using a template string
        else if (templateString != null) {

            // TODO
            
        }

        return dstName;

    }

}
