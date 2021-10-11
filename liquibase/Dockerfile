FROM liquibase/liquibase
ADD changelog.xml /liquibase/changelog
CMD ["sh", "-c", "docker-entrypoint.sh -url=${URL} --username=${DB_USERNAME} --password=${DB_PASSWORD} --classpath=/liquibase/changelog --changeLogFile=changelog.xml update"]