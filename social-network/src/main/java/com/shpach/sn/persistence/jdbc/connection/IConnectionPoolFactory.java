package com.shpach.sn.persistence.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionPoolFactory {

	Connection getConnection() throws SQLException;

}