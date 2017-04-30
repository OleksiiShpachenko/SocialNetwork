package com.shpach.sn.persistence.jdbc.dao.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.sn.persistence.entities.User;
import com.shpach.sn.persistence.jdbc.connection.ConnectionPoolTomCatFactory;
import com.shpach.sn.persistence.jdbc.connection.IConnectionPoolFactory;
import com.shpach.sn.persistence.jdbc.dao.abstractdao.AbstractDao;

public class MySqlUserDao extends AbstractDao<User> implements IUserDao {
	private static final Logger logger = Logger.getLogger(MySqlUserDao.class);

	protected enum Columns {

		user_id(1), user_login(2), user_password(3), user_name(4), user_email(5), avatar_url(6), user_active(
				7), user_post_permition(8), user_invite_permition(
						9), user_comment_permition(10), user_create_community_permition(11), user_create_datetime(12);
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

	protected static final String TABLE_NAME = "user";
	protected static final String TABLE_RELATIONSHIP_LIKES = "relationship_likes";
	protected static final String TABLE_USER_TO_USER_ROLE = "relationship_user_to_user_role";
	private static final int USER_ROLE_USER = 2;
	protected final String SQL_SELECT = "SELECT " + Columns.user_id.name() + ", " + Columns.user_login.name() + ", "
			+ Columns.user_password.name() + ", " + Columns.user_name.name() + ", " + Columns.user_email.name() + ", "
			+ Columns.avatar_url.name() + ", " + Columns.user_active.name() + ", " + Columns.user_post_permition.name()
			+ ", " + Columns.user_invite_permition.name() + ", " + Columns.user_comment_permition.name() + ", "
			+ Columns.user_create_community_permition.name() + ", " + Columns.user_create_datetime.name() + " FROM "
			+ TABLE_NAME + "";

	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.user_login.name() + ", "
			+ Columns.user_password.name() + ", " + Columns.user_name.name() + ", " + Columns.user_email.name() + ", "
			+ Columns.avatar_url.name() + ", " + Columns.user_active.name() + ", " + Columns.user_post_permition.name()
			+ ", " + Columns.user_invite_permition.name() + ", " + Columns.user_comment_permition.name() + ", "
			+ Columns.user_create_community_permition.name() + ", " + Columns.user_create_datetime.name()
			+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	protected final String SQL_INSERT_USER_ROLE_RELATOINSHIP = "INSERT INTO " + TABLE_USER_TO_USER_ROLE + " (" + Columns.user_id.name() + ", "
			+  " user_role_id " + ") VALUES (?, ?)";

	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.user_login.name() + "=?, "
			+ Columns.user_password.name() + "=?, " + Columns.user_name.name() + "=?, " + Columns.user_email.name()
			+ "=?, " + Columns.avatar_url.name() + "=?, " + Columns.user_active.name() + "=?, "
			+ Columns.user_post_permition.name() + "=?, " + Columns.user_invite_permition.name() + "=?, "
			+ Columns.user_comment_permition.name() + "=?, " + Columns.user_create_community_permition.name() + "=?, "
			+ Columns.user_create_datetime.name() + "=? WHERE " + Columns.user_id.name() + "=?";

	protected final String SQL_SELECT_LIKES_BY_POST_ID = SQL_SELECT + " t RIGHT JOIN " + TABLE_RELATIONSHIP_LIKES
			+ " j ON t." + Columns.user_id.name() + "=j." + Columns.user_id.name() + " WHERE j.post_id=?";
	// protected final String SQL_SELECT_BY_COMMUNITY_ID = "SELECT " +
	// TABLE_NAME + "." + Columns.user_id.name() + ", "
	// + TABLE_NAME + "." + Columns.role_id.name() + ", " + TABLE_NAME + "." +
	// Columns.user_email.name() + ", "
	// + TABLE_NAME + "." + Columns.user_password.name() + ", " + TABLE_NAME +
	// "." + Columns.user_name.name()
	// + " FROM " + TABLE_NAME + ", " + TABLE_USER_TO_COMMUNITY_RELATIONSHIP + "
	// WHERE "
	// + TABLE_USER_TO_COMMUNITY_RELATIONSHIP + ".community_id=? AND " +
	// TABLE_USER_TO_COMMUNITY_RELATIONSHIP + "."
	// + Columns.user_id.name() + "=" + TABLE_NAME + "." +
	// Columns.user_id.name();

	private static MySqlUserDao instance = null;

	private MySqlUserDao() {

	}

	public static synchronized MySqlUserDao getInstance() {
		if (instance == null)
			return instance = new MySqlUserDao();
		else
			return instance;

	}

	public List<User> findAll() {
		return findByDynamicSelect(SQL_SELECT, null, null);
	}

	public User addOrUpdate(User user) {
		boolean res = false;
		if (user.getUserId() == 0) {
			res = add(user);
		} else {
			res = update(user);
		}
		if (res == false)
			return null;
		return user;
	}

	private boolean update(User user) {

		Object[] sqlParams = new Object[] { user.getUserLogin(), user.getUserPassword(), user.getUserName(),
				user.getUserEmail(), user.getAvatarUrl(), user.getUserActive(), user.getUserPostPermition(),
				user.getUserInvitePermition(), user.getUserCommentPermition(), user.getUserCreateCommunityPermition(),
				user.getUserCreateDatetime(), user.getUserId() };
		return dynamicUpdate(SQL_UPDATE, sqlParams);
	}

	private boolean add(User user) {
		Object[] sqlParams = new Object[] { user.getUserLogin(), user.getUserPassword(), user.getUserName(),
				user.getUserEmail(), user.getAvatarUrl(), user.getUserActive(), user.getUserPostPermition(),
				user.getUserInvitePermition(), user.getUserCommentPermition(), user.getUserCreateCommunityPermition(),
				user.getUserCreateDatetime() };

		Connection cn = null;
		try {
			cn = getConnection();
			cn.setAutoCommit(false);
			int id = dynamicAdd(SQL_INSERT, cn, sqlParams);
			if (id > 0) {
				user.setUserId(id);

			} else {
				cn.rollback();
				return false;
			}
			id=dynamicAdd(SQL_INSERT_USER_ROLE_RELATOINSHIP, cn, new Object[] {user.getUserId(), USER_ROLE_USER });
			if (id<0){
				cn.rollback();
				return false;
			}
			return true;
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				cn.setAutoCommit(true);
				cn.close();
			} catch (SQLException | NullPointerException e ) {
				e.printStackTrace();
			}

		}
	}

	public User findUserById(int id) {
		List<User> res = null;
		res = findByDynamicSelect(SQL_SELECT, Columns.user_id.name(), id);
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	public User findUserByEmail(String login) {
		if (login == null)
			return null;
		List<User> res = null;
		res = findByDynamicSelect(SQL_SELECT, Columns.user_email.name(), login);
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	@Override
	public User findUserByName(String name) {
		if (name == null)
			return null;
		List<User> res = null;
		res = findByDynamicSelect(SQL_SELECT, Columns.user_name.name(), name);
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	@Override
	public User populateDto(ResultSet rs) throws SQLException {
		User dto = new User();
		dto.setUserId(rs.getInt(Columns.user_id.getId()));
		dto.setUserLogin(rs.getString(Columns.user_login.getId()));
		dto.setUserPassword(rs.getString(Columns.user_password.getId()));
		dto.setUserName(rs.getString(Columns.user_name.getId()));
		dto.setUserEmail(rs.getString(Columns.user_email.getId()));
		dto.setAvatarUrl(rs.getString(Columns.avatar_url.getId()));
		dto.setUserActive(rs.getByte(Columns.user_active.getId()));
		dto.setUserPostPermition(rs.getByte(Columns.user_post_permition.getId()));
		dto.setUserInvitePermition(rs.getByte(Columns.user_invite_permition.getId()));
		dto.setUserCommentPermition(rs.getByte(Columns.user_comment_permition.getId()));
		dto.setUserCreateCommunityPermition(rs.getByte(Columns.user_create_community_permition.getId()));
		dto.setUserCreateDatetime(new Date(rs.getTimestamp(Columns.user_create_datetime.getId()).getTime()));
		return dto;
	}

	@Override
	public List<User> findUsersWhichLikesPostByPostId(int postId) {
		List<User> res = null;
		res = findByDynamicSelect(SQL_SELECT_LIKES_BY_POST_ID, new Integer[] { postId });
		if (res != null && res.size() > 0)
			return res;
		return new ArrayList<User>();
	}

	// @Override
	// public List<User> findUsersByCommunityId(int id) {
	// List<User> res = null;
	// res = findByDynamicSelect(SQL_SELECT_BY_COMMUNITY_ID, new Integer[] { id
	// });
	// if (res != null && res.size() > 0)
	// return res;
	// return new ArrayList<User>();
	// }

}
