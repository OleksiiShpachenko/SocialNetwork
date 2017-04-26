package com.shpach.sn.persistence.jdbc.dao.friend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.sn.persistence.entities.Friend;
import com.shpach.sn.persistence.jdbc.dao.abstractdao.AbstractDao;

public class MySqlFriendDao extends AbstractDao<Friend> implements IFriendDao {
	private static final Logger logger = Logger.getLogger(MySqlFriendDao.class);

	protected enum Columns {

		u1(1), u2(2), host_user_id(3), slave_user_id(4), friend_status(5), friend_status_datetime(6);
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

	protected static final String TABLE_NAME = "friend";

	protected final String SQL_SELECT = "SELECT " + Columns.host_user_id.name() + ", " + Columns.slave_user_id.name()
			+ ", " + Columns.friend_status.name() + ", " + Columns.friend_status_datetime.name() + " FROM " + TABLE_NAME
			+ " ";

	protected final String SQL_SELECT_WHERE = "SELECT " + Columns.host_user_id.name() + ", "
			+ Columns.slave_user_id.name() + ", " + Columns.friend_status.name() + ", "
			+ Columns.friend_status_datetime.name() + " FROM " + TABLE_NAME + " WHERE " + Columns.host_user_id.name()
			+ "=? OR " + Columns.slave_user_id.name() + "=?";

	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.host_user_id.name() + ", "
			+ Columns.slave_user_id.name() + ", " + Columns.friend_status.name() + ", "
			+ Columns.friend_status_datetime.name() + ") VALUES (?, ?, ?, ?)";

	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.host_user_id.name() + "=?, "
			+ Columns.slave_user_id.name() + "=?, " + Columns.friend_status.name() + "=?, "
			+ Columns.friend_status_datetime.name() + "=? WHERE " + Columns.u1.name() + "=LEAST(?, ?) AND "
			+ Columns.u2.name() + "=GREATEST(?, ?)";

	private static MySqlFriendDao instance = null;

	private MySqlFriendDao() {

	}

	public static synchronized MySqlFriendDao getInstance() {
		if (instance == null)
			return instance = new MySqlFriendDao();
		else
			return instance;

	}

	@Override
	public List<Friend> findAll() {
		return findByDynamicSelect(SQL_SELECT, null, null);
	}

	@Override
	public Friend add(Friend friend) {
		if (friend == null)
			return null;
		if (addBool(friend))
			return friend;
		return null;
	}

	@Override
	public Friend update(Friend friend) {
		if (friend == null)
			return null;
		if (updateBool(friend))
			return friend;
		return null;
	}

	private boolean updateBool(Friend friend) {
		Object[] sqlParams = new Object[] { friend.getHostUserId(), friend.getSlaveUserId(), friend.getFriendStatus(),
				friend.getFriendStatusDatetime(), friend.getHostUserId(), friend.getSlaveUserId(),
				friend.getHostUserId(), friend.getSlaveUserId() };
		return dynamicUpdate(SQL_UPDATE, sqlParams);
	}

	private boolean addBool(Friend friend) {
		Object[] sqlParams = new Object[] { friend.getHostUserId(), friend.getSlaveUserId(), friend.getFriendStatus(),
				friend.getFriendStatusDatetime() };
		int id = dynamicAdd(SQL_INSERT, sqlParams);
		if (id >= 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<Friend> findFriendByUserId(int userId) {
		List<Friend> res = null;
		res = findByDynamicSelect(SQL_SELECT_WHERE, new Object[] { userId });
		if (res != null && res.size() > 0)
			return res;
		return new ArrayList<Friend>();
	}

	@Override
	public boolean deleteFriendByUsersId(int userId_1, int userId_2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Friend populateDto(ResultSet rs) throws SQLException {
		Friend dto = new Friend();
		dto.setHostUserId(rs.getInt(Columns.host_user_id.getId()));
		dto.setSlaveUserId(rs.getInt(Columns.slave_user_id.getId()));
		dto.setFriendStatus(rs.getInt(Columns.friend_status.getId()));
		dto.setFriendStatusDatetime(new Date(rs.getTimestamp(Columns.friend_status_datetime.getId()).getTime()));
		return dto;
	}

}
