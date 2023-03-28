package com.avides.springboot.springtainer.mysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MysqlPropertiesTest
{
    @Test
    public void testDefaults()
    {
        var properties = new MysqlProperties();
        assertTrue(properties.isEnabled());
        assertEquals(30, properties.getStartupTimeout());
        assertEquals("mysql:8.0.32", properties.getDockerImage());

        assertEquals(3306, properties.getPort());
        assertEquals("root", properties.getRootPassword());
        assertEquals("embeddedMySqlDb", properties.getDatabaseName());
        assertEquals("utf8mb4", properties.getDatabaseCharset());
    }
}
