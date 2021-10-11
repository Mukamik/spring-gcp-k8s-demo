FROM liquibase/liquibase
COPY changelog.xml ./
CMD ["sh", "-c", "docker-entrypoint.sh -url=${URL} --username=${DB_USERNAME} --password=${DB_PASSWORD} --classpath=/ --changeLogFile=changelog.xml update"]