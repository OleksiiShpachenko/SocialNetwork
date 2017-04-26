package com.shpach.sn.persistence.jdbc.dao.communitymember;

import java.util.List;

import com.shpach.sn.persistence.entities.CommunityMember;

public interface ICommunityMemberDao {
	public List<CommunityMember> findAll();

	public CommunityMember addOrUpdate(CommunityMember communityMember);

	public CommunityMember findCommunityMemberById(int id);

	public List<CommunityMember> findCommunityMemberByUserId(int userId);

	public List<CommunityMember> findCommunityMemberByCommunityId(int communityId);

	public boolean deleteCommunityMemberById(int id);
}
