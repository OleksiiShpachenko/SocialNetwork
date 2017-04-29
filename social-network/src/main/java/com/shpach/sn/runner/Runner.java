package com.shpach.sn.runner;

import java.util.Date;

import com.shpach.sn.persistence.entities.User;
import com.shpach.sn.persistence.jdbc.dao.factory.IDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.friend.IFriendDao;
import com.shpach.sn.persistence.jdbc.dao.user.IUserDao;

public class Runner {

	private static User user_1;
	private static User user_2;

	public static void main(String[] args) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		IUserDao userDao=daoFactory.getUserDao();
		IFriendDao friendDao=daoFactory.getFriendDao();
		
		initUsers();
		userDao.addOrUpdate(user_1);
		userDao.addOrUpdate(user_2);

	}

	private static void initUsers() {
		user_1 = new User();
		//user_1.setUserId(1);
		user_1.setUserName("Lesha");
		user_1.setUserEmail("q@gmail.com");
		user_1.setUserPassword("password");
		user_1.setAvatarUrl("/avatars/123.jpg");
		user_1.setUserActive((byte) 1);
		user_1.setUserLogin("leshaLogin");
		user_1.setUserPostPermition((byte) 1);
		user_1.setUserCommentPermition((byte) 1);
		user_1.setUserInvitePermition((byte) 1);
		user_1.setUserCreateCommunityPermition((byte) 1);
		user_1.setUserCreateDatetime(new Date());

		user_2 = new User();
		//user_2.setUserId(2);
		user_2.setUserName("Sasha");
		user_2.setUserEmail("q2@gmail.com");
		user_2.setUserPassword("password");
		user_2.setAvatarUrl("/avatars/12345.jpg");
		user_2.setUserActive((byte) 1);
		user_2.setUserLogin("SashaLogin");
		user_2.setUserPostPermition((byte) 1);
		user_2.setUserCommentPermition((byte) 1);
		user_2.setUserInvitePermition((byte) 1);
		user_2.setUserCreateCommunityPermition((byte) 1);
		user_2.setUserCreateDatetime(new Date());
	}

}
