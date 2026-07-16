# springtainer-mysql

[![Maven Central](https://img.shields.io/maven-central/v/com.avides.springboot.springtainer/springtainer-mysql.svg?label=maven-central)](https://search.maven.org/artifact/com.avides.springboot.springtainer/springtainer-mysql)
[![Release](https://github.com/springtainer/springtainer-mysql/actions/workflows/release.yml/badge.svg)](https://github.com/springtainer/springtainer-mysql/actions/workflows/release.yml)
[![Nightly build](https://github.com/springtainer/springtainer-mysql/actions/workflows/nightly.yml/badge.svg)](https://github.com/springtainer/springtainer-mysql/actions/workflows/nightly.yml)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=springtainer_springtainer-mysql&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=springtainer_springtainer-mysql)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=springtainer_springtainer-mysql&metric=coverage)](https://sonarcloud.io/summary/new_code?id=springtainer_springtainer-mysql)

### Dependency

```xml

<dependency>
  <groupId>com.avides.springboot.springtainer</groupId>
  <artifactId>springtainer-mysql</artifactId>
  <version>2.0.0-RC1</version>
  <scope>test</scope>
</dependency>
```

### Configuration

Properties consumed (in `bootstrap.properties`):

- `embedded.container.mysql.enabled` (default is `true`)
- `embedded.container.mysql.startup-timeout` (default is `30`)
- `embedded.container.mysql.docker-image` (default is `mysql:8.0.32`)
- `embedded.container.mysql.port` (default is `3306`)
- `embedded.container.mysql.root-password` (default is `root`)
- `embedded.container.mysql.database-name` (default is `embeddedMySqlDb`)
- `embedded.container.mysql.database-charset` (default is `utf8mb4`)

Properties provided (in `application-it.properties`):

- `embedded.container.mysql.url` (an alias for `embedded.container.mysql.jdbc-url`)
- `embedded.container.mysql.jdbc-url`
- `embedded.container.mysql.r2dbc-url`
- `embedded.container.mysql.host`
- `embedded.container.mysql.port`

Example for minimal configuration in `application-it.properties`:

```
spring.datasource.url=${embedded.container.mysql.url}
spring.datasource.username=root
spring.datasource.password=${embedded.container.mysql.root-password}
```

## Logging

To reduce logging insert this into the logback-configuration:

```xml
<!-- Springtainer -->
<logger name="com.github.dockerjava.jaxrs" level="WARN" />
<logger name="com.github.dockerjava.core.command" level="WARN" />
<logger name="org.apache.http" level="WARN" />
```

## Labels

The container exports multiple labels to analyze running springtainers:

- `SPRINGTAINER_SERVICE=mysql`
- `SPRINGTAINER_IMAGE=${embedded.container.mysql.docker-image}`
- `SPRINGTAINER_STARTED=$currentTimestamp`
