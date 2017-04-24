package com.shpach.sn.persistence.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolTomCatFactory implements IConnectionPoolFactory {
	/* (non-Javadoc)
	 * @see com.shpach.sn.persistance.jdbc.connection.IConnectionPoolFactory#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return ConnectionPoolTomCat.getInstance().getConnection();
	}
}
