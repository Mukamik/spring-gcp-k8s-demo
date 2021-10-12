FROM liquibase/liquibase
COPY changelog.xml /
RUN curl -L -o /mysql-connector-java-8.0.26.jar https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.26/mysql-connector-java-8.0.26.jar
ENV CLASSPATH=/mysql-connector-java-8.0.26.jar:${CLASSPATH}
CMD ["sh", "-c", "docker-entrypoint.sh --url=${SPRING_DATASOURCE_URL} --username=${SPRING_DATASOURCE_USERNAME} --password=${SPRING_DATASOURCE_PASSWORD} --classpath=/ --changeLogFile=changelog.xml update"]