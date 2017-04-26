package com.shpach.sn.persistent.jdbc.dao.comment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.sn.persistence.entities.Comment;
import com.shpach.sn.persistence.jdbc.dao.abstractdao.AbstractDao;

public class MySqlCommentDao extends AbstractDao<Comment> implements ICommentDao {
	private static final Logger logger = Logger.getLogger(MySqlCommentDao.class);

	protected enum Columns {

		comment_id(1), post_id(2), user_id(3), comment_text(4), comment_create_datetime(5);
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

	protected static final String TABLE_NAME = "comment";
	protected final String SQL_SELECT = "SELECT " + Columns.comment_id.name() + ", " + Columns.post_id.name() + ", "
			+ Columns.user_id.name() + ", " + Columns.comment_text.name() + ", "
			+ Columns.comment_create_datetime.name() + " FROM " + TABLE_NAME + "";

	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.post_id.name() + ", "
			+ Columns.user_id.name() + ", " + Columns.comment_text.name() + ", "
			+ Columns.comment_create_datetime.name() + ") VALUES (?, ?, ?, ?)";

	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.post_id.name() + "=?, "
			+ Columns.user_id.name() + "=?, " + Columns.comment_text.name() + "=?, "
			+ Columns.comment_create_datetime.name() + "=? WHERE " + Columns.comment_id.name() + "=?";

	private static MySqlCommentDao instance = null;

	private MySqlCommentDao() {

	}

	public static synchronized MySqlCommentDao getInstance() {
		if (instance == null)
			return instance = new MySqlCommentDao();
		else
			return instance;

	}

	@Override
	public List<Comment> findAll() {
		return findByDynamicSelect(SQL_SELECT, null, null);
	}

	@Override
	public Comment addOrUpdate(Comment comment) {
		if (comment == null)
			return null;
		boolean res = false;
		if (comment.getCommentId() == 0) {
			res = add(comment);
		} else {
			res = update(comment);
		}
		if (res == false)
			return null;
		return comment;
	}

	private boolean update(Comment comment) {

		Object[] sqlParams = new Object[] { comment.getPostId(), comment.getUserId(), comment.getCommentText(),
				comment.getCommentCreateDatetime(), comment.getCommentId() };
		return dynamicUpdate(SQL_UPDATE, sqlParams);
	}

	private boolean add(Comment comment) {
		Object[] sqlParams = new Object[] { comment.getPostId(), comment.getUserId(), comment.getCommentText(),
				comment.getCommentCreateDatetime() };
		int id = dynamicAdd(SQL_INSERT, sqlParams);
		if (id > 0) {
			comment.setCommentId(id);
			return true;
		}
		return false;
	}

	@Override
	public Comment findCommentById(int id) {
		List<Comment> res = null;
		res = findByDynamicSelect(SQL_SELECT, Columns.comment_id.name(), id);
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	@Override
	public List<Comment> findCommentByUserId(int userId) {
		List<Comment> res = null;
		res = findByDynamicSelect(SQL_SELECT, Columns.user_id.name(), userId);
		if (res != null && res.size() > 0)
			return res;
		return new ArrayList<Comment>();
	}

	@Override
	public List<Comment> findCommentByPostId(int postId) {
		List<Comment> res = null;
		res = findByDynamicSelect(SQL_SELECT, Columns.post_id.name(), postId);
		if (res != null && res.size() > 0)
			return res;
		return new ArrayList<Comment>();
	}

	@Override
	public boolean deleteCommentById(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Comment populateDto(ResultSet rs) throws SQLException {
		Comment dto = new Comment();
		dto.setCommentId(rs.getInt(Columns.comment_id.getId()));
		dto.setPostId(rs.getInt(Columns.post_id.getId()));
		dto.setUserId(rs.getInt(Columns.user_id.getId()));
		dto.setCommentText(rs.getString(Columns.comment_text.getId()));
		dto.setCommentCreateDatetime(new Date(rs.getTimestamp(Columns.comment_create_datetime.getId()).getTime()));
		return dto;
	}

}
