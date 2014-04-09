package com.gomoob.archiver.handlebars;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.text.DateFormat;

import javax.swing.text.DateFormatter;

import org.junit.Test;

import com.gomoob.archiver.ArchiveFile;
import com.gomoob.archiver.configuration.archive.Archive;

public class ArchiveDstNameBuilderTest {

    @Test
    public void testBuild() throws Exception {
        //quipo-database-{{dateFormat archiveFile.creationDate "d/m/yyyy"}}.zip
        
        ArchiveFile archiveFile = new ArchiveFile("target/test-classes/archive.zip");
        
        ArchiveDstNameBuilder archiveDstNameBuilder = new ArchiveDstNameBuilder();
        archiveDstNameBuilder.setArchiveFile(archiveFile);
        
        // If the archive file is not linked to any Archive configuration then the destination name is the name of the 
        // file
        assertThat(archiveDstNameBuilder.build()).isEqualTo("archive.zip");
        
        // Test with an empty Archive linked
        Archive archive = new Archive();
        archiveFile.setArchive(archive);
        
        // If the archive file is linked to an Archive configuration without any 'cwd' or 'dst' property then the 
        // destination name is the name of the file
        archiveDstNameBuilder.setArchiveFile(archiveFile);
        assertThat(archiveDstNameBuilder.build()).isEqualTo("archive.zip");
        
        // Test with an Archive having a 'dest' property
        archive.setDst("quipo-database-{{dateFormat archiveFile.creationDate \"yyyy-mm-dd_hh'h'MM\"}}.zip");
        
        System.out.println(archiveDstNameBuilder.build());
        // fail("Not yet implemented");
    }

}
