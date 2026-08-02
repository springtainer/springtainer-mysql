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
  <version>3.0.0-RC1</version>
  <scope>test</scope>
</dependency>
```

### Configuration

Properties consumed (in `bootstrap.properties`):

- `embedded.container.mysql.enabled` (default is `true`)
- `embedded.container.mysql.startup-timeout` (default is `30`)
- `embedded.container.mysql.docker-image` (default is `mysql:8.4.11`)
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

## Spring's test-context cache is bounded automatically

`spring.test.context.cache.maxSize=1` ships as a classpath `spring.properties`
resource inside springtainer-common itself, so it's picked up automatically for every consumer - no configuration
needed on your side. This bounds Spring's test-context cache so a no-longer-current context (and, via its
`ContextClosedEvent` listener, its embedded container) gets evicted and cleanly closed as soon as a differently-configured
context needs the slot, instead of piling up unclosed until the whole JVM exits.

This works the same way whether tests are launched via Maven Surefire/Failsafe or directly from an IDE's own test
runner (e.g. Eclipse), since Spring resolves it from the classpath (`org.springframework.core.SpringProperties`) rather
than from a JVM system property.

## Logging

To reduce logging insert this into the logback-configuration:

```xml
<!-- Springtainer -->
<logger name="com.github.dockerjava" level="WARN" />
```

## Labels

The container exports multiple labels to analyze running springtainers:

- `SPRINGTAINER_SERVICE=mysql`
- `SPRINGTAINER_IMAGE=${embedded.container.mysql.docker-image}`
- `SPRINGTAINER_STARTED=$currentTimestamp`
