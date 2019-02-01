package com.avides.springboot.testcontainer.mysql;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = { "spring.datasource.driver-class-name=com.mysql.jdbc.Driver", "spring.datasource.url=${embedded.container.mysql.url}", "spring.datasource.username=root", "spring.datasource.password=${embedded.container.mysql.root-password}" })
public abstract class AbstractIT
{
    protected DockerClient dockerClient = DockerClientBuilder.getInstance().build();

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
