# springtainer-mysql

[![Maven Central](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/com/avides/springboot/springtainer/springtainer-mysql/maven-metadata.xml.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.avides.springboot.springtainer%22%20AND%20a%3A%22springtainer-mysql%22)
[![Build](https://github.com/springtainer/springtainer-mysql/workflows/release/badge.svg)](https://github.com/springtainer/springtainer-mysql/actions)
[![Nightly build](https://github.com/springtainer/springtainer-mysql/workflows/nightly/badge.svg)](https://github.com/springtainer/springtainer-mysql/actions)
[![Coverage report](https://sonarcloud.io/api/project_badges/measure?project=springtainer_springtainer-mysql&metric=coverage)](https://sonarcloud.io/dashboard?id=springtainer_springtainer-mysql)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=springtainer_springtainer-mysql&metric=alert_status)](https://sonarcloud.io/dashboard?id=springtainer_springtainer-mysql)
[![Technical dept](https://sonarcloud.io/api/project_badges/measure?project=springtainer_springtainer-mysql&metric=sqale_index)](https://sonarcloud.io/dashboard?id=springtainer_springtainer-mysql)

### Dependency
```xml
<dependency>
	<groupId>com.avides.springboot.springtainer</groupId>
	<artifactId>springtainer-mysql</artifactId>
	<version>1.0.1</version>
	<scope>test</scope>
</dependency>
```

### Configuration
Properties consumed (in `bootstrap.properties`):
- `embedded.container.mysql.enabled` (default is `true`)
- `embedded.container.mysql.startup-timeout` (default is `30`)
- `embedded.container.mysql.docker-image` (default is `mysql:8.0.18`)
- `embedded.container.mysql.port` (default is `3306`)
- `embedded.container.mysql.root-password` (default is `root`)
- `embedded.container.mysql.database-name` (default is `embeddedMySqlDb`)
- `embedded.container.mysql.database-charset` (default is `utf8mb4`)

Properties provided (in `application-it.properties`):
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
