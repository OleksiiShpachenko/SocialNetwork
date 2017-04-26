package com.shpach.sn.persistence.jdbc.dao.image;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.sn.persistence.entities.Image;
import com.shpach.sn.persistence.jdbc.dao.abstractdao.AbstractDao;

public class MySqlImageDao extends AbstractDao<Image> implements IImageDao {
	private static final Logger logger = Logger.getLogger(MySqlImageDao.class);

	protected enum Columns {

		image_id(1), post_id(2), image_url(3);
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

	protected static final String TABLE_NAME = "image";
	protected final String SQL_SELECT = "SELECT " + Columns.image_id.name() + ", " + Columns.post_id.name() + ", "
			+ Columns.image_url.name() + " FROM " + TABLE_NAME + "";
	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.post_id.name() + ", "
			+ Columns.image_url.name() + ") VALUES (?, ?)";
	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.post_id.name() + "=?, "
			+ Columns.image_url.name() + "=? WHERE " + Columns.image_id.name() + "=?";

	private static MySqlImageDao instance = null;

	private MySqlImageDao() {

	}

	public static synchronized MySqlImageDao getInstance() {
		if (instance == null)
			return instance = new MySqlImageDao();
		else
			return instance;

	}

	@Override
	public List<Image> findAll() {
		return findByDynamicSelect(SQL_SELECT, null, null);
	}

	@Override
	public Image addOrUpdate(Image image) {
		if (image == null)
			return null;
		boolean res = false;
		if (image.getImageId() == 0) {
			res = add(image);
		} else {
			res = update(image);
		}
		if (res == false)
			return null;
		return image;
	}

	private boolean update(Image image) {

		Object[] sqlParams = new Object[] { image.getPostId(),image.getImageUrl(), image.getImageId()};
		return dynamicUpdate(SQL_UPDATE, sqlParams);
	}

	private boolean add(Image image) {
		Object[] sqlParams = new Object[] { image.getPostId(),image.getImageUrl() };
		int id = dynamicAdd(SQL_INSERT, sqlParams);
		if (id > 0) {
			image.setImageId(id);
			return true;
		}
		return false;
	}


	@Override
	public Image findImageById(int id) {
		List<Image> res = null;
		res = findByDynamicSelect(SQL_SELECT, Columns.image_id.name(), id);
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	@Override
	public List<Image> findImageByPostId(int postId) {
		List<Image> res = null;
		res = findByDynamicSelect(SQL_SELECT, Columns.post_id.name(), postId);
		if (res != null && res.size() > 0)
			return res;
		return new ArrayList<Image>();
	}

	@Override
	public boolean deleteImageById(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Image populateDto(ResultSet rs) throws SQLException {
		Image dto = new Image();
		dto.setImageId(rs.getInt(Columns.image_id.getId()));
		dto.setPostId(rs.getInt(Columns.post_id.getId()));
		dto.setImageUrl(rs.getString(Columns.image_url.getId()));
		return dto;
	}

}
