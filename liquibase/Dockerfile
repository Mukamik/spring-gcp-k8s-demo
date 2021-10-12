FROM liquibase/liquibase
COPY changelog.xml /liquibase/changelog/
COPY mysql-connector-java-8.0.26.jar /liquibase/classpath/
CMD ["sh", "-c", "docker-entrypoint.sh --url=${SPRING_DATASOURCE_URL} --username=${SPRING_DATASOURCE_USERNAME} --password=${SPRING_DATASOURCE_PASSWORD} --classpath=/liquibase/classpath/mysql-connector-java-8.0.26.jar --changelog-file=changelog/changelog.xml --driver=com.mysql.jdbc.Driver update"]