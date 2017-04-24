package com.shpach.sn.persistence.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionPool {
	Connection getConnection() throws SQLException;
	void closeConnection(Connection connection);
}
