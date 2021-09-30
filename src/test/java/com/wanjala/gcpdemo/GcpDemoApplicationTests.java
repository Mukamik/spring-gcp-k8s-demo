package com.wanjala.gcpdemo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@Testcontainers
class GcpDemoApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(GcpDemoApplicationTests.class);

    @ClassRule
    public static OracleContainer oracleContainer = new OracleContainer("phx.ocir.io/toddrsharp/oracle-db/oracle/database:18.4.0-xe")
            .withLogConsumer(new Slf4jLogConsumer(logger));

    @BeforeAll
    public static void startup() {
        oracleContainer.start();
    }

    @Test
    void testSimple() throws SQLException {
        try {
            oracleContainer.start();


            ResultSet resultSet = performQuery("SELECT 1");
            int resultSetInt = resultSet.getInt(1);

            assertEquals("Basic select query works", 1, resultSetInt);
        } catch (SQLException e) {
            fail("Unexpected error" + e.getStackTrace());
        }
    }

    @TestConfiguration
    static class OracleTestConfiguration {

        @Bean
        DataSource dataSource() {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(oracleContainer.getJdbcUrl());
            hikariConfig.setUsername(oracleContainer.getUsername());
            hikariConfig.setPassword(oracleContainer.getPassword());
            return new HikariDataSource(hikariConfig);
        }
    }

    protected ResultSet performQuery(String sql) throws SQLException {
        DataSource ds = getDataSource();
        Statement statement = ds.getConnection().createStatement();
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();

        resultSet.next();
        return resultSet;
    }

    protected DataSource getDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(oracleContainer.getJdbcUrl());
        hikariConfig.setUsername(oracleContainer.getUsername());
        hikariConfig.setPassword(oracleContainer.getPassword());
        hikariConfig.setDriverClassName(oracleContainer.getDriverClassName());
        return new HikariDataSource(hikariConfig);
    }

}
