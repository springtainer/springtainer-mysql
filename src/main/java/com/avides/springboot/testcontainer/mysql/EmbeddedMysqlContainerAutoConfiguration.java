package com.avides.springboot.testcontainer.mysql;

import static com.avides.springboot.testcontainer.mysql.MysqlProperties.BEAN_NAME;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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

import com.avides.springboot.testcontainer.common.container.AbstractBuildingEmbeddedContainer;
import com.avides.springboot.testcontainer.common.container.EmbeddedContainer;

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
            List<String> envs = new ArrayList<>();
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
            Map<String, Object> provided = new HashMap<>();
            provided.put("embedded.container.mysql.url", generateSqlConnectionUrl(properties) + properties
                    .getDatabaseName() + "?verifyServerCertificate=false&useSSL=false");
            provided.put("embedded.container.mysql.port", Integer.valueOf(getContainerPort(properties.getPort())));
            provided.put("embedded.container.mysql.root-password", properties.getRootPassword());
            provided.put("embedded.container.mysql.database-name", properties.getDatabaseName());
            provided.put("embedded.container.mysql.database-charset", properties.getDatabaseCharset());
            return provided;
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
        protected boolean isContainerReady(MysqlProperties properties)
        {
            try (Connection connection = createSqlConnection(properties))
            {
                initDatabase(connection);
                return true;
            }
        }

        @SneakyThrows
        private void initDatabase(Connection connection)
        {
            try (Statement statement = connection.createStatement())
            {
                statement.execute("CREATE SCHEMA `" + properties.getDatabaseName() + "` DEFAULT CHARACTER SET " + properties.getDatabaseCharset() + " ;");
            }
        }

        private String generateSqlConnectionUrl(MysqlProperties properties)
        {
            return "jdbc:mysql://" + getContainerHost() + ":" + getContainerPort(properties.getPort()) + "/";
        }

        private Connection createSqlConnection(MysqlProperties properties)
                throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String connectionCommand = generateSqlConnectionUrl(properties) + "?verifyServerCertificate=false&useSSL=false&user=root&password=" + properties
                    .getRootPassword();
            return DriverManager.getConnection(connectionCommand);
        }
    }
}
