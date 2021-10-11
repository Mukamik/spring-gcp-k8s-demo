FROM liquibase/liquibase
COPY changelog.xml /
CMD ["sh", "-c", "docker-entrypoint.sh -url=${SPRING_DATASOURCE_URL} --username=${SPRING_DATASOURCE_USERNAME} --password=${SPRING_DATASOURCE_PASSWORD} --classpath=/ --changeLogFile=changelog.xml update"]