package com.shpach.sn.persistence.jdbc.dao.communitymember;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.sn.persistence.entities.CommunityMember;
import com.shpach.sn.persistence.jdbc.dao.abstractdao.AbstractDao;

public class MySqlCommunityMemberDao extends AbstractDao<CommunityMember> implements ICommunityMemberDao {
	private static final Logger logger = Logger.getLogger(MySqlCommunityMemberDao.class);

	protected enum Columns {

		community_member_id(1), community_id(2), user_id(3), community_member_datetime(4), community_member_status(5);
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
	protected final String SQL_SELECT = "SELECT " + Columns.community_member_id.name() + ", "
			+ Columns.community_id.name() + ", " + Columns.user_id.name() + ", "
			+ Columns.community_member_datetime.name() + ", " + Columns.community_member_status.name() + " FROM "
			+ TABLE_NAME + "";

	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.community_id.name() + ", "
			+ Columns.user_id.name() + ", " + Columns.community_member_datetime.name() + ", "
			+ Columns.community_member_status.name() + ") VALUES (?, ?, ?, ?)";

	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.community_id.name() + "=?, "
			+ Columns.user_id.name() + "=?, " + Columns.community_member_datetime.name() + "=?, "
			+ Columns.community_member_status.name() + "=? WHERE " + Columns.community_member_id.name() + "=?";

	private static MySqlCommunityMemberDao instance = null;

	private MySqlCommunityMemberDao() {

	}

	public static synchronized MySqlCommunityMemberDao getInstance() {
		if (instance == null)
			return instance = new MySqlCommunityMemberDao();
		else
			return instance;

	}

	@Override
	public List<CommunityMember> findAll() {
		return findByDynamicSelect(SQL_SELECT, null, null);
	}

	@Override
	public CommunityMember addOrUpdate(CommunityMember communityMember) {
		if (communityMember == null)
			return null;
		boolean res = false;
		if (communityMember.getCommunityMemberId() == 0) {
			res = add(communityMember);
		} else {
			res = update(communityMember);
		}
		if (res == false)
			return null;
		return communityMember;
	}

	private boolean update(CommunityMember communityMember) {

		Object[] sqlParams = new Object[] { communityMember.getCommunityId(), communityMember.getUserId(),
				communityMember.getCommunityMemberDatetime(), communityMember.getCommunityMemberStatus(),
				communityMember.getCommunityMemberId() };
		return dynamicUpdate(SQL_UPDATE, sqlParams);
	}

	private boolean add(CommunityMember communityMember) {
		Object[] sqlParams = new Object[] { communityMember.getCommunityId(), communityMember.getUserId(),
				communityMember.getCommunityMemberDatetime(), communityMember.getCommunityMemberStatus() };
		int id = dynamicAdd(SQL_INSERT, sqlParams);
		if (id > 0) {
			communityMember.setCommunityMemberId(id);
			return true;
		}
		return false;
	}

	@Override
	public CommunityMember findCommunityMemberById(int id) {
		List<CommunityMember> res = null;
		res = findByDynamicSelect(SQL_SELECT, Columns.community_member_id.name(), id);
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	@Override
	public List<CommunityMember> findCommunityMemberByUserId(int userId) {
		List<CommunityMember> res = null;
		res = findByDynamicSelect(SQL_SELECT, Columns.user_id.name(), userId);
		if (res != null && res.size() > 0)
			return res;
		return new ArrayList<CommunityMember>();
	}

	@Override
	public List<CommunityMember> findCommunityMemberByCommunityId(int communityId) {
		List<CommunityMember> res = null;
		res = findByDynamicSelect(SQL_SELECT, Columns.community_id.name(), communityId);
		if (res != null && res.size() > 0)
			return res;
		return new ArrayList<CommunityMember>();
	}

	@Override
	public boolean deleteCommunityMemberById(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CommunityMember populateDto(ResultSet rs) throws SQLException {
		CommunityMember dto = new CommunityMember();
		dto.setCommunityMemberId(rs.getInt(Columns.community_member_id.getId()));
		dto.setCommunityId(rs.getInt(Columns.community_id.getId()));
		dto.setUserId(rs.getInt(Columns.user_id.getId()));
		dto.setCommunityMemberDatetime(new Date(rs.getTimestamp(Columns.community_member_datetime.getId()).getTime()));
		dto.setCommunityMemberStatus(rs.getInt(Columns.community_member_status.getId()));
		return dto;
	}

}
