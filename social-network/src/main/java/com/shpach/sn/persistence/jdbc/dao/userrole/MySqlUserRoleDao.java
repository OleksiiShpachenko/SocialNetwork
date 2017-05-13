package com.shpach.sn.persistence.jdbc.dao.userrole;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.sn.persistence.entities.UserRole;
import com.shpach.sn.persistence.jdbc.dao.abstractdao.AbstractDao;

public class MySqlUserRoleDao extends AbstractDao<UserRole> implements IUserRoleDao {
	private static final Logger logger = Logger.getLogger(MySqlUserRoleDao.class);

	protected enum Columns {

		user_role_id(1), user_role_name(2);
		Columns(int id) {
			this.id = id;
		}

		private int id;

		public int getId() {
			return id;
		}

		public static String getClassName() {
			return Columns.class.getName();
		}

	}

	protected static final String TABLE_NAME = "user_role";
	protected static final String TABLE_USER_TO_USER_ROLE = "relationship_user_to_user_role";
	protected final String SQL_SELECT = "SELECT " + Columns.user_role_id.name() + ", " + Columns.user_role_name.name()
			+ " FROM " + TABLE_NAME + "";
	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.user_role_name.name()
			+ ") VALUES (?)";
	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.user_role_name.name() + "=? WHERE "
			+ Columns.user_role_id.name() + "=?";
	protected final String SQL_SELECT_BY_USER_ID = "SELECT t." + Columns.user_role_id.name() + ", t."
			+ Columns.user_role_name.name() +  " FROM " + TABLE_NAME + " t RIGHT JOIN " + TABLE_USER_TO_USER_ROLE
			+ " j ON t." + Columns.user_role_id.name() + "=j." + Columns.user_role_id.name() + " WHERE j.user_id=?";

	private static MySqlUserRoleDao instance = null;

	private MySqlUserRoleDao() {

	}

	public static synchronized MySqlUserRoleDao getInstance() {
		if (instance == null)
			return instance = new MySqlUserRoleDao();
		else
			return instance;

	}

	public List<UserRole> findAll() {
		return findByDynamicSelect(SQL_SELECT, null, null);
	}

	public UserRole addOrUpdate(UserRole usersRole) {
		if (usersRole == null)
			return null;
		boolean res = false;
		if (usersRole.getUserRoleId() == 0) {
			res = add(usersRole);
		} else {
			res = update(usersRole);
		}
		if (res == false)
			return null;
		return usersRole;
	}

	private boolean update(UserRole usersRole) {
		Object[] sqlParams = new Object[] { usersRole.getUserRoleName(),
				usersRole.getUserRoleId() };
		return dynamicUpdate(SQL_UPDATE, sqlParams);
	}

	private boolean add(UserRole usersRole) {
		Object[] sqlParams = new Object[] { usersRole.getUserRoleName() };
		int id = dynamicAdd(SQL_INSERT, sqlParams);
		if (id > 0) {
			usersRole.setUserRoleId(id);
			return true;
		}
		return false;
	}

	public UserRole findUserRoleById(int id) {
		List<UserRole> res = null;
		res = findByDynamicSelect(SQL_SELECT, Columns.user_role_id.name(), id);
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	public UserRole populateDto(ResultSet rs) throws SQLException {
		UserRole dto = new UserRole();
		dto.setUserRoleId(rs.getInt(Columns.user_role_id.getId()));
		dto.setUserRoleName(rs.getString(Columns.user_role_name.getId()));
		return dto;
	}

	
	@Override
	public List<UserRole> findUserRoleByUserId(int userId) {
		List<UserRole> res = null;
		res = findByDynamicSelect(SQL_SELECT_BY_USER_ID, new Integer[] { userId });
		if (res != null && res.size() > 0)
			return res;
		return new ArrayList<UserRole>();
	}

}
