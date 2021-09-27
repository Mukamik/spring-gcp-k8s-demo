package com.wanjala.gcpdemo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

@Testcontainers
class GcpDemoApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(GcpDemoApplicationTests.class);

	public static final DockerImageName MYSQL_57_IMAGE = DockerImageName.parse("mysql:5.7.34");

	@Test
	void testSimple() throws SQLException {
		try(MySQLContainer<?> mysql = new MySQLContainer<>(MYSQL_57_IMAGE)
				.withDatabaseName("test")
				.withLogConsumer(new Slf4jLogConsumer(logger))
		) {
			mysql.start();

			ResultSet resultSet = performQuery(mysql, "SELECT 1");
			int resultSetInt = resultSet.getInt(1);

			assertEquals("Basic select query works", 1, resultSetInt);
		}
	}

	protected ResultSet performQuery(JdbcDatabaseContainer<?> container, String sql) throws SQLException {
		DataSource ds = getDataSource(container);
		Statement statement = ds.getConnection().createStatement();
		statement.execute(sql);
		ResultSet resultSet = statement.getResultSet();

		resultSet.next();
		return resultSet;
	}

	protected DataSource getDataSource(JdbcDatabaseContainer<?> container) {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setJdbcUrl(container.getJdbcUrl());
		hikariConfig.setUsername(container.getUsername());
		hikariConfig.setPassword(container.getPassword());
		hikariConfig.setDriverClassName(container.getDriverClassName());
		return new HikariDataSource(hikariConfig);
	}

}
