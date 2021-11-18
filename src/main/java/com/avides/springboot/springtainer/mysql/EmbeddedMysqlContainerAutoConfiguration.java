package com.avides.springboot.springtainer.mysql;

import static com.avides.springboot.springtainer.mysql.MysqlProperties.BEAN_NAME;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import com.avides.springboot.springtainer.common.container.AbstractBuildingEmbeddedContainer;
import com.avides.springboot.springtainer.common.container.EmbeddedContainer;
import com.github.dockerjava.api.command.CreateContainerCmd;

import lombok.SneakyThrows;

@Configuration
@ConditionalOnProperty(name = "embedded.container.mysql.enabled", matchIfMissing = true)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(MysqlProperties.class)
public class EmbeddedMysqlContainerAutoConfiguration
{
    @ConditionalOnMissingBean(MysqlContainer.class)
    @Bean(BEAN_NAME)
    public EmbeddedContainer mysqlContainer(ConfigurableEnvironment environment, MysqlProperties properties)
    {
        return new MysqlContainer("mysql", environment, properties);
    }

    public class MysqlContainer extends AbstractBuildingEmbeddedContainer<MysqlProperties>
    {
        public MysqlContainer(String service, ConfigurableEnvironment environment, MysqlProperties properties)
        {
            super(service, environment, properties);
        }

        @Override
        protected List<String> getEnvs()
        {
            var envs = new ArrayList<String>();
            envs.add("MYSQL_ROOT_PASSWORD=" + properties.getRootPassword());
            return envs;
        }

        @Override
        protected List<String> getTmpDirectories()
        {
            return Collections.singletonList("/var/lib/mysql");
        }

        @Override
        protected Map<String, Object> providedProperties()
        {
            var provided = new HashMap<String, Object>();
            var jdbcUrl = generateSqlConnectionUrl(properties, "jdbc") + properties.getDatabaseName() + "?verifyServerCertificate=false&useSSL=false";
            provided.put("embedded.container.mysql.url", jdbcUrl);
            provided.put("embedded.container.mysql.jdbc-url", jdbcUrl);
            provided.put("embedded.container.mysql.r2dbc-url", generateSqlConnectionUrl(properties, "r2dbc") + properties
                    .getDatabaseName() + "?verifyServerCertificate=false&useSSL=false");
            provided.put("embedded.container.mysql.host", getContainerHost());
            provided.put("embedded.container.mysql.port", Integer.valueOf(getContainerPort(properties.getPort())));
            provided.put("embedded.container.mysql.root-password", properties.getRootPassword());
            provided.put("embedded.container.mysql.database-name", properties.getDatabaseName());
            provided.put("embedded.container.mysql.database-charset", properties.getDatabaseCharset());
            return provided;
        }

        @SuppressWarnings("resource")
        @Override
        protected void adjustCreateCommand(CreateContainerCmd createContainerCmd)
        {
            var commands = new ArrayList<String>();
            // performance tweaks
            commands.add("--skip-log-bin");
            commands.add("--sync_binlog=0");
            commands.add("--innodb_flush_log_at_trx_commit=2");
            commands.add("--innodb_flush_method=O_DIRECT");
            // legacy support for mysql 8.X
            commands.add("--default-authentication-plugin=mysql_native_password");
            createContainerCmd.withCmd(commands);
        }

        /**
         * Skip ssl-key creation which reduces io and startup-time
         * <p>
         * Better solution: https://github.com/docker-library/mysql/pull/428
         */
        // @SneakyThrows
        // @Override
        // protected void adjustCreateCommand(CreateContainerCmd createContainerCmd)
        // {
        // File emptyFile = File.createTempFile("abc", "xyz");
        // emptyFile.deleteOnExit();
        //
        // Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(emptyFile.toPath());
        // permissions.add(PosixFilePermission.OWNER_EXECUTE);
        // permissions.add(PosixFilePermission.GROUP_EXECUTE);
        // permissions.add(PosixFilePermission.OTHERS_EXECUTE);
        // Files.setPosixFilePermissions(emptyFile.toPath(), permissions);
        //
        // createContainerCmd.withBinds(Bind.parse(emptyFile.getAbsolutePath() + ":/usr/bin/mysql_ssl_rsa_setup:ro"));
        // }

        @SneakyThrows
        @Override
        protected boolean isContainerReady(MysqlProperties mysqlProperties)
        {
            try (var connection = createSqlConnection(mysqlProperties))
            {
                initDatabase(connection);
                return true;
            }
        }

        @SneakyThrows
        private void initDatabase(Connection connection)
        {
            try (var statement = connection.createStatement())
            {
                statement.execute("CREATE SCHEMA `" + properties.getDatabaseName() + "` DEFAULT CHARACTER SET " + properties.getDatabaseCharset() + " ;");
            }
        }

        private String generateSqlConnectionUrl(MysqlProperties mysqlProperties, String schema)
        {
            return schema + ":mysql://" + getContainerHost() + ":" + getContainerPort(mysqlProperties.getPort()) + "/";
        }

        private Connection createSqlConnection(MysqlProperties mysqlProperties) throws SQLException
        {
            return DriverManager
                    .getConnection(generateSqlConnectionUrl(mysqlProperties, "jdbc") + "?verifyServerCertificate=false&useSSL=false&user=root&password=" + mysqlProperties
                            .getRootPassword());
        }
    }
}
