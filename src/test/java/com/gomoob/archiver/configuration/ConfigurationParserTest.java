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
package com.gomoob.archiver.configuration;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.gomoob.archiver.configuration.archive.Archive;
import com.gomoob.archiver.configuration.archive.Type;
import com.gomoob.archiver.configuration.store.Store;
import com.gomoob.archiver.glacier.configuration.store.GlacierAdditionalConfiguration;

/**
 * Test case for the {@link ConfigurationParser} class.
 * 
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public class ConfigurationParserTest {

    /**
     * Test methos used to test the {@link ConfigurationParser#parse(String)} function with a valid string.
     * 
     * @throws Exception if an unexpected error occurs in this function.
     */
    @Test
    public void testParseString() throws Exception {

        ConfigurationParser configurationParser = new ConfigurationParser();
        String configurationString = FileUtils
                .readFileToString(new File("target/test-classes/configuration/valid.json"));

        Configuration configuration = configurationParser.parse(configurationString);

        // Checks the credentials
        assertThat(configuration.getCredentialsList()).hasSize(3);
        assertThat(configuration.getCredentialsList().get(0).getId()).isEqualTo("credentials1");
        assertThat(configuration.getCredentialsList().get(0).getKey()).isEqualTo("AAAAAA");
        assertThat(configuration.getCredentialsList().get(0).getSecret()).isEqualTo("BBBBBB");
        assertThat(configuration.getCredentialsList().get(1).getId()).isEqualTo("credentials2");
        assertThat(configuration.getCredentialsList().get(1).getKey()).isEqualTo("CCCCCC");
        assertThat(configuration.getCredentialsList().get(1).getSecret()).isEqualTo("DDDDDD");
        assertThat(configuration.getCredentialsList().get(2).getId()).isEqualTo("credentials3");
        assertThat(configuration.getCredentialsList().get(2).getKey()).isEqualTo("EEEEEE");
        assertThat(configuration.getCredentialsList().get(2).getSecret()).isEqualTo("FFFFFF");

        // Checks the stores
        assertThat(configuration.getStores()).hasSize(2);

        Store store1 = configuration.getStores().get(0);
        assertThat(store1.getId()).isEqualTo("store1");
        assertThat(store1.getType()).isEqualTo("glacier");
        assertThat(store1.getCredentials()).isSameAs(configuration.getCredentialsList().get(0));
        assertThat(store1.getAdditionalConfiguration()).isNotNull();
        assertThat(store1.getAdditionalConfiguration()).isInstanceOf(GlacierAdditionalConfiguration.class);
        GlacierAdditionalConfiguration gac = (GlacierAdditionalConfiguration) store1.getAdditionalConfiguration();
        assertThat(gac.getEndpoint()).isEqualTo("https://glacier.eu-west-1.amazonaws.com");
        assertThat(gac.getVaultName()).isEqualTo("VAULT_NAME_1");

        Store store2 = configuration.getStores().get(1);
        assertThat(store2.getId()).isEqualTo("store2");
        assertThat(store2.getType()).isEqualTo("glacier");
        assertThat(store2.getCredentials()).isSameAs(configuration.getCredentialsList().get(1));
        assertThat(store2.getAdditionalConfiguration()).isNotNull();
        assertThat(store2.getAdditionalConfiguration()).isInstanceOf(GlacierAdditionalConfiguration.class);
        gac = (GlacierAdditionalConfiguration) store2.getAdditionalConfiguration();
        assertThat(gac.getEndpoint()).isEqualTo("https://glacier.eu-west-1.amazonaws.com");
        assertThat(gac.getVaultName()).isEqualTo("VAULT_NAME_2");

        // Checks the archives
        assertThat(configuration.getArchives()).hasSize(4);

        Archive archive1 = configuration.getArchives().get(0);
        assertThat(archive1.getCwd()).isEqualTo("a/b/c");
        assertThat(archive1.getDst()).isEqualTo("archive1.zip");
        assertThat(archive1.getSrc().getGlobbingPatten()).isEqualTo("x/directory1");
        assertThat(archive1.getSrc().getGlobbingPatterns()).isNull();
        assertThat(archive1.getType()).isEqualTo(Type.ZIP);

        Archive archive2 = configuration.getArchives().get(1);
        assertThat(archive2.getCwd()).isEqualTo("d/e/f");
        assertThat(archive2.getDst()).isEqualTo("archive2.zip");
        assertThat(archive2.getSrc().getGlobbingPatten()).isEqualTo("y/directory2");
        assertThat(archive2.getSrc().getGlobbingPatterns()).isNull();
        assertThat(archive2.getType()).isEqualTo(Type.ZIP);

        Archive archive3 = configuration.getArchives().get(2);
        assertThat(archive3.getCwd()).isEqualTo("g/h/i");
        assertThat(archive3.getDst()).isEqualTo("archive3.zip");
        assertThat(archive3.getSrc().getGlobbingPatten()).isEqualTo("z/**/*.txt");
        assertThat(archive3.getSrc().getGlobbingPatterns()).isNull();
        assertThat(archive3.getType()).isEqualTo(Type.ZIP);

        Archive archive4 = configuration.getArchives().get(3);
        assertThat(archive4.getCwd()).isEqualTo("k/l/m");
        assertThat(archive4.getDst()).isEqualTo("archive4.zip");
        assertThat(archive4.getSrc().getGlobbingPatten()).isNull();
        assertThat(archive4.getSrc().getGlobbingPatterns()).hasSize(2);
        assertThat(archive4.getSrc().getGlobbingPatterns().get(0)).isEqualTo("**/*.png");
        assertThat(archive4.getSrc().getGlobbingPatterns().get(1)).isEqualTo("**/*.gif");
        assertThat(archive4.getType()).isEqualTo(Type.ZIP);

    }

}
