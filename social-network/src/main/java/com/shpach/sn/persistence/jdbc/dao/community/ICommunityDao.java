package com.shpach.sn.persistence.jdbc.dao.community;

import java.util.List;

import com.shpach.sn.persistence.entities.Community;

public interface ICommunityDao {
	public List<Community> findAll();

	public Community addOrUpdate(Community community);

	public Community findCommunityById(int id);

	public boolean deleteCommunityById(int id);
}
