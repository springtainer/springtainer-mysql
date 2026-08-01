package com.avides.springboot.springtainer.mysql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * Runs against the image configured by default, unlike the tests that pin an older one. Without it a default nobody can start goes unnoticed here and only
 * surfaces in the consuming projects.
 */
public class EmbeddedMysqlContainerAutoConfigurationWithDefaultImageIT extends AbstractIT
{
    @Test
    public void testGeneratedProperties()
    {
        assertThat(environment.getProperty("embedded.container.mysql.url")).startsWith("jdbc:");
        assertThat(environment.getProperty("embedded.container.mysql.jdbc-url")).startsWith("jdbc:");
        assertThat(environment.getProperty("embedded.container.mysql.r2dbc-url")).startsWith("r2dbc:");
        assertThat(environment.getProperty("embedded.container.mysql.host")).isNotEmpty();
        assertThat(environment.getProperty("embedded.container.mysql.port")).isNotEmpty();
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
        assertThat(jdbcTemplate.queryForObject("SELECT @@version", String.class)).startsWith(new MysqlProperties().getDockerImage().replace("mysql:", ""));
    }

    @Configuration
    @EnableAutoConfiguration
    static class TestConfiguration
    {
        // nothing
    }
}
