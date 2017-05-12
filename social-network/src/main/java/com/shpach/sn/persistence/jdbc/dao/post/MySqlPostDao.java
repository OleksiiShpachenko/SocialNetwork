package com.shpach.sn.persistence.jdbc.dao.post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.sn.persistence.entities.Post;
import com.shpach.sn.persistence.entities.User;
import com.shpach.sn.persistence.jdbc.dao.abstractdao.AbstractDao;

public class MySqlPostDao extends AbstractDao<Post> implements IPostDao {
	private static final Logger logger = Logger.getLogger(MySqlPostDao.class);

	protected enum Columns {

		post_id(1), user_id(2), community_id(3), post_text(4), post_create_datetime(5);
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

	protected static final String TABLE_NAME = "post";
	protected final String LIMIT = " LIMIT ?, ?";
	protected final String SQL_SELECT = "SELECT " + Columns.post_id.name() + ", " + Columns.user_id.name() + ", "
			+ Columns.community_id.name() + ", " + Columns.post_text.name() + ", " + Columns.post_create_datetime.name()
			+ " FROM " + TABLE_NAME + "";
	protected final String WHERE_USER_ID_IN = " WHERE " + Columns.user_id.name() + " IN ";
	protected final String ORDER_BY_CREATION_TIME = " ORDER BY " + Columns.post_create_datetime.name() + " DESC";
	protected final String SQL_SELECT_BY_USER_ORDER = "SELECT " + Columns.post_id.name() + ", " + Columns.user_id.name()
			+ ", " + Columns.community_id.name() + ", " + Columns.post_text.name() + ", "
			+ Columns.post_create_datetime.name() + " FROM " + TABLE_NAME + " WHERE " + Columns.user_id.name()
			+ "=? ORDER BY " + Columns.post_create_datetime.name() + " DESC";

	protected final String SQL_SELECT_BY_USER_ORDER_LIMIT = "SELECT " + Columns.post_id.name() + ", "
			+ Columns.user_id.name() + ", " + Columns.community_id.name() + ", " + Columns.post_text.name() + ", "
			+ Columns.post_create_datetime.name() + " FROM " + TABLE_NAME + " WHERE " + Columns.user_id.name()
			+ "=? ORDER BY " + Columns.post_create_datetime.name() + " DESC LIMIT ?, ?";

	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.user_id.name() + ", "
			+ Columns.community_id.name() + ", " + Columns.post_text.name() + ", " + Columns.post_create_datetime.name()
			+ ") VALUES (?, ?, ?, ?)";

	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.user_id.name() + "=?, "
			+ Columns.community_id.name() + "=?, " + Columns.post_text.name() + "=?, "
			+ Columns.post_create_datetime.name() + "=? WHERE " + Columns.post_id.name() + "=?";

	protected final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + Columns.post_id.name() + "=?";

	private static MySqlPostDao instance = null;

	private MySqlPostDao() {

	}

	public static synchronized MySqlPostDao getInstance() {
		if (instance == null)
			return instance = new MySqlPostDao();
		else
			return instance;

	}

	public List<Post> findAll() {
		return findByDynamicSelect(SQL_SELECT, null, null);
	}

	public Post addOrUpdate(Post post) {
		if (post == null)
			return null;
		boolean res = false;
		if (post.getPostId() == 0) {
			res = add(post);
		} else {
			res = update(post);
		}
		if (res == false)
			return null;
		return post;
	}

	private boolean update(Post post) {

		Object[] sqlParams = new Object[] { post.getUserId(), post.getCommunityId(), post.getPostText(),
				post.getPostCreateDatetime(), post.getPostId() };
		return dynamicUpdate(SQL_UPDATE, sqlParams);
	}

	private boolean add(Post post) {
		Object[] sqlParams = new Object[] { post.getUserId(), post.getCommunityId() > 0 ? post.getCommunityId() : null,
				post.getPostText(), post.getPostCreateDatetime() };
		int id = dynamicAdd(SQL_INSERT, sqlParams);
		if (id > 0) {
			post.setPostId(id);
			return true;
		}
		return false;
	}

	public Post findPostById(int id) {
		List<Post> res = null;
		res = findByDynamicSelect(SQL_SELECT, Columns.post_id.name(), id);
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	public List<Post> findPostByUserId(int userId, int startFrom, int itemsOnPage) {
		List<Post> res = null;
		Object[] sqlParams = new Object[] { userId, startFrom, itemsOnPage };
		res = findByDynamicSelect(SQL_SELECT_BY_USER_ORDER_LIMIT, sqlParams);// findByDynamicSelect(SQL_SELECT,
																				// Columns.user_id.name(),
																				// userId);
		if (res != null && res.size() > 0)
			return res;
		return new ArrayList<Post>();
	}

	public List<Post> findPostByUserId(int userId) {
		List<Post> res = null;
		Object[] sqlParams = new Object[] { userId };
		res = findByDynamicSelect(SQL_SELECT_BY_USER_ORDER, sqlParams);// findByDynamicSelect(SQL_SELECT,
																		// Columns.user_id.name(),
																		// userId);
		if (res != null && res.size() > 0)
			return res;
		return new ArrayList<Post>();
	}

	public List<Post> findPostByCommunityId(int communityId) {
		List<Post> res = null;
		res = findByDynamicSelect(SQL_SELECT, Columns.community_id.name(), communityId);
		if (res != null && res.size() > 0)
			return res;
		return new ArrayList<Post>();
	}

	@Override
	public Post populateDto(ResultSet rs) throws SQLException {
		Post dto = new Post();
		dto.setPostId(rs.getInt(Columns.post_id.getId()));
		dto.setUserId(rs.getInt(Columns.user_id.getId()));
		dto.setCommunityId(
				rs.getObject(Columns.community_id.getId()) != null ? rs.getInt(Columns.community_id.getId()) : 0);
		dto.setPostText(rs.getString(Columns.post_text.getId()));
		dto.setPostCreateDatetime(new Date(rs.getTimestamp(Columns.post_create_datetime.getId()).getTime()));
		return dto;
	}

	@Override
	public boolean deletePostById(int id, Connection con) {
		return dynamicUpdate(SQL_DELETE, con, new Object[] { id });
	}

	@Override
	public List<Post> findPostByUserId(List<Integer> usersId,int startFrom, int itemsOnPage) {
		List<Post> posts = new ArrayList<>();
		if (usersId == null)
			return posts;
		Object[] sqlParams = new Object[usersId.size()+2];
		sqlParams = usersId.toArray(sqlParams);
		sqlParams[sqlParams.length-2]=startFrom;
		sqlParams[sqlParams.length-1]=itemsOnPage;
		String sql = SQL_SELECT + WHERE_USER_ID_IN + " (";
		for (int i = 0; i < usersId.size(); i++) {
			sql += "?";
			if (i < (usersId.size() - 1))
				sql += ", ";
			else
				sql += ") " + ORDER_BY_CREATION_TIME+LIMIT;
		}
		posts = findByDynamicSelect(sql, sqlParams);
		if (posts != null && posts.size() > 0)
			return posts;
		return new ArrayList<Post>();
	}

	@Override
	public int countUsersPostsById(Object[] array) {
		String usersIs="";
		for (int i = 0; i < array.length; i++) {
			usersIs += "?";
			if (i < (array.length - 1))
				usersIs += ", ";
		}
		String sql ="SELECT SUM(countf) FROM (SELECT "+Columns.user_id.name()+", COUNT(*) as countf FROM "+TABLE_NAME+" WHERE "+Columns.user_id.name()+" IN("+usersIs+") GROUP BY "+Columns.user_id.name()+") AS tb";
		//String sql = "SELECT count(*),"+Columns.user_id.name()+" FROM post WHERE " + Columns.user_id.name() + " IN (";
		
		int res = -1;
		try {
			Connection cn = getConnection();
			PreparedStatement st = null;
			try {
				st = cn.prepareStatement(sql);
				for (int i = 0; array != null && i < array.length; i++) {
					st.setObject(i + 1, array[i]);
				}
				ResultSet rs = null;
				try {
					rs = st.executeQuery();
					while (rs.next()) {
						res = rs.getInt(1);
					}
				} finally {
					if (rs != null) {
						rs.close();
					}
				}
				return res;
			} finally {
				if (st != null) {
					st.close();
				}
				if (cn != null) {
					try {
						cn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return res;
		}
	}

}
