springboot-testcontainer-mysql
==============================

[![Maven Central](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/com/avides/springboot/testcontainer/springboot-testcontainer-mysql/maven-metadata.xml.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.avides.springboot.testcontainer%22%20AND%20a%3A%22springboot-testcontainer-mysql%22)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/3ef2b23118074ae7bbe52a3bd53defad)](https://www.codacy.com/app/avides-builds/springboot-testcontainer-mysql?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=springboot-testcontainer/springboot-testcontainer-mysql&amp;utm_campaign=Badge_Grade)
[![Coverage Status](https://coveralls.io/repos/springboot-testcontainer/springboot-testcontainer-mysql/badge.svg)](https://coveralls.io/r/springboot-testcontainer/springboot-testcontainer-mysql)
[![Build Status](https://travis-ci.org/springboot-testcontainer/springboot-testcontainer-mysql.svg?branch=master)](https://travis-ci.org/springboot-testcontainer/springboot-testcontainer-mysql)

### Dependency
```xml
<dependency>
	<groupId>com.avides.springboot.testcontainer</groupId>
	<artifactId>springboot-testcontainer-mysql</artifactId>
	<version>0.1.0-RC5</version>
	<scope>test</scope>
</dependency>
```

### Configuration
Properties consumed (in `bootstrap.properties`):
- `embedded.container.mysql.enabled` (default is `true`)
- `embedded.container.mysql.startup-timeout` (default is `30`)
- `embedded.container.mysql.docker-image` (default is `mysql:5.7.24`)
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
<!-- Testcontainers -->
<logger name="com.github.dockerjava.jaxrs" level="WARN" />
<logger name="com.github.dockerjava.core.command" level="WARN" />
<logger name="org.apache.http" level="WARN" />
```

## Labels
The container exports multiple labels to analyze running testcontainers:
- `TESTCONTAINER_SERVICE=mysql`
- `TESTCONTAINER_IMAGE=${embedded.container.mysql.docker-image}`
- `TESTCONTAINER_STARTED=$currentTimestamp`
