package com.avides.springboot.testcontainer.mysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MysqlPropertiesTest
{
    @Test
    public void testDefaults()
    {
        MysqlProperties properties = new MysqlProperties();
        assertTrue(properties.isEnabled());
        assertEquals(30, properties.getStartupTimeout());
        assertEquals("mysql:5.7.24", properties.getDockerImage());

        assertEquals(3306, properties.getPort());
        assertEquals("root", properties.getRootPassword());
        assertEquals("embeddedMySqlDb", properties.getDatabaseName());
        assertEquals("utf8mb4", properties.getDatabaseCharset());
    }
}
