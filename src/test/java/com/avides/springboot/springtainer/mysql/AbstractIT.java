package com.avides.springboot.springtainer.mysql;

import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.dockerjava.api.DockerClient;
import com.avides.springboot.springtainer.common.util.DockerClients;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = { "spring.datasource.url=${embedded.container.mysql.url}", "spring.datasource.username=root", "spring.datasource.password=${embedded.container.mysql.root-password}" })
public abstract class AbstractIT
{
    protected DockerClient dockerClient = DockerClients.build();

    @Autowired
    protected ConfigurableEnvironment environment;

    @Autowired
    private DataSource dataSource;

    protected JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init()
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
