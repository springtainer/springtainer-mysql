package com.avides.springboot.springtainer.mysql;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.avides.springboot.springtainer.common.container.AbstractEmbeddedContainerProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ConfigurationProperties("embedded.container.mysql")
@Getter
@Setter
@ToString(callSuper = true)
public class MysqlProperties extends AbstractEmbeddedContainerProperties
{
    public static final String BEAN_NAME = "embeddedMysqlContainer";

    private int port = 3306;

    private String rootPassword = "root"; // NOSONAR

    private String databaseName = "embeddedMySqlDb";

    private String databaseCharset = "utf8mb4";

    public MysqlProperties()
    {
        setDockerImage("mysql:8.0.28");
    }
}
