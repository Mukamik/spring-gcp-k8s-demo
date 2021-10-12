FROM liquibase/liquibase
COPY changelog.xml /
CMD ["sh", "-c", "docker-entrypoint.sh --url=${SPRING_DATASOURCE_URL} --username=${SPRING_DATASOURCE_USERNAME} --password=${SPRING_DATASOURCE_PASSWORD} -v ~/.m2/repository/mysql/mysql-connector-java/8.0.26:/liquibase/classpath --classpath=/liquibase/classpath --changeLogFile=changelog.xml update"]