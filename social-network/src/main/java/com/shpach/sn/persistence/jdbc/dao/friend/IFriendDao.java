package com.shpach.sn.persistence.jdbc.dao.friend;

import java.util.List;

import com.shpach.sn.persistence.entities.Friend;

public interface IFriendDao {
	public List<Friend> findAll();

	//public Friend addOrUpdate(Friend friend);
	
	public Friend add(Friend friend);
	
	public Friend update(Friend friend);

	public List<Friend> findFriendByUserId(int userId);

	public boolean deleteFriendByUsersId(int userId_1, int userId_2);

}
