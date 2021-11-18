package com.avides.springboot.springtainer.mysql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "embedded.container.mysql.docker-image=mysql:8.0.18")
public class EmbeddedMysqlContainerAutoConfigurationWith57IT extends AbstractIT
{
    @Test
    public void testGeneratedProperties()
    {
        assertThat(environment.getProperty("embedded.container.mysql.url")).startsWith("jdbc:");
        assertThat(environment.getProperty("embedded.container.mysql.jdbc-url")).startsWith("jdbc:");
        assertThat(environment.getProperty("embedded.container.mysql.r2dbc-url")).startsWith("r2dbc:");
        assertThat(environment.getProperty("embedded.container.mysql.host")).isNotEmpty();
        assertThat(environment.getProperty("embedded.container.mysql.port")).isNotEmpty();
        assertThat(environment.getProperty("embedded.container.mysql.root-password")).isNotEmpty();
        assertThat(environment.getProperty("embedded.container.mysql.database-name")).isNotEmpty();
        assertThat(environment.getProperty("embedded.container.mysql.database-charset")).isNotEmpty();

        System.out.println();
        System.out.println("Resolved properties:");
        System.out.println("URL:        " + environment.getProperty("embedded.container.mysql.url"));
        System.out.println("JDBC-URL:   " + environment.getProperty("embedded.container.mysql.jdbc-url"));
        System.out.println("R2DBC-URL:  " + environment.getProperty("embedded.container.mysql.r2dbc-url"));
        System.out.println("Host:       " + environment.getProperty("embedded.container.mysql.host"));
        System.out.println("Port:       " + environment.getProperty("embedded.container.mysql.port"));
        System.out.println("Password:   " + environment.getProperty("embedded.container.mysql.root-password"));
        System.out.println("Database:   " + environment.getProperty("embedded.container.mysql.database-name"));
        System.out.println("Charset:    " + environment.getProperty("embedded.container.mysql.database-charset"));
        System.out.println();
    }

    @Test
    public void testCreatedDatabase()
    {
        var createdDatabase = environment.getProperty("embedded.container.mysql.database-name");
        assertTrue(jdbcTemplate.queryForList("SHOW DATABASES", String.class).stream().anyMatch(database -> database.equals(createdDatabase)));

        var createdDatabaseCharset = environment.getProperty("embedded.container.mysql.database-charset");
        jdbcTemplate.update("USE " + createdDatabase);
        assertEquals(createdDatabaseCharset, jdbcTemplate.queryForObject("SELECT @@character_set_database;", String.class));
    }

    @Test
    public void testCheckVersion()
    {
        assertThat(jdbcTemplate.queryForObject("SELECT @@version", String.class)).startsWith("8.");
    }

    @Configuration
    @EnableAutoConfiguration
    static class TestConfiguration
    {
        // nothing
    }
}
