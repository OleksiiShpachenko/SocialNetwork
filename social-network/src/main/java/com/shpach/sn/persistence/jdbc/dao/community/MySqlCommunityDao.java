package com.shpach.sn.persistence.jdbc.dao.community;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.sn.persistence.entities.Community;
import com.shpach.sn.persistence.jdbc.dao.abstractdao.AbstractDao;


public class MySqlCommunityDao extends AbstractDao<Community> implements ICommunityDao {
	private static final Logger logger = Logger.getLogger(MySqlCommunityDao.class);

	protected enum Columns {

		community_id(1), community_name(2);
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

	protected static final String TABLE_NAME = "community";
	protected final String SQL_SELECT = "SELECT " + Columns.community_id.name() + ", " + Columns.community_name.name()  + " FROM " + TABLE_NAME + "";

	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.community_name.name() + ") VALUES (?)";

	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.community_name.name()  + "=? WHERE " + Columns.community_id.name() + "=?";

	private static MySqlCommunityDao instance = null;

	private MySqlCommunityDao() {

	}

	public static synchronized MySqlCommunityDao getInstance() {
		if (instance == null)
			return instance = new MySqlCommunityDao();
		else
			return instance;

	}

	@Override
	public List<Community> findAll() {
		return findByDynamicSelect(SQL_SELECT, null, null);
	}

	@Override
	public Community addOrUpdate(Community community) {
		if (community == null)
			return null;
		boolean res = false;
		if (community.getCommunityId() == 0) {
			res = add(community);
		} else {
			res = update(community);
		}
		if (res == false)
			return null;
		return community;
	}

	private boolean update(Community community) {

		Object[] sqlParams = new Object[] { community.getCommunityName(), community.getCommunityId() };
		return dynamicUpdate(SQL_UPDATE, sqlParams);
	}

	private boolean add(Community community) {
		Object[] sqlParams = new Object[] { community.getCommunityName() };
		int id = dynamicAdd(SQL_INSERT, sqlParams);
		if (id > 0) {
			community.setCommunityId(id);
			return true;
		}
		return false;
	}

	@Override
	public Community findCommunityById(int id) {
		List<Community> res = null;
		res = findByDynamicSelect(SQL_SELECT, Columns.community_id.name(), id);
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	@Override
	public boolean deleteCommunityById(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Community populateDto(ResultSet rs) throws SQLException {
		Community dto = new Community();
		dto.setCommunityId(rs.getInt(Columns.community_id.getId()));
		dto.setCommunityName(rs.getString(Columns.community_name.getId()));
		return dto;
	}

}
