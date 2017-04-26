package com.shpach.sn.persistence.jdbc.dao.factory;

import com.shpach.sn.persistence.jdbc.dao.comment.ICommentDao;
import com.shpach.sn.persistence.jdbc.dao.community.ICommunityDao;
import com.shpach.sn.persistence.jdbc.dao.communitymember.ICommunityMemberDao;
import com.shpach.sn.persistence.jdbc.dao.friend.IFriendDao;
import com.shpach.sn.persistence.jdbc.dao.image.IImageDao;
import com.shpach.sn.persistence.jdbc.dao.post.IPostDao;
import com.shpach.sn.persistence.jdbc.dao.user.IUserDao;
import com.shpach.sn.persistence.jdbc.dao.userrole.IUserRoleDao;

public interface IDaoFactory {
	IUserDao getUserDao();

	IUserRoleDao getUserRoleDao();
	
	IPostDao getPostDao();
	
	IImageDao getImageDao();
	
	ICommentDao getCommentDao();
	
	ICommunityDao getCommunityDao();
	
	ICommunityMemberDao gerCommunityMemberDao();
	
	IFriendDao getFriendDao();

//	ICommunityDao getCommunityDao();
//
//	ITestDao getTestDao();
//
//	ICategoryDao getCategoryDao();
//
//	IQuestionDao getQuestionDao();
//
//	ITestQuestionsBankDao getTestQuestionsBankDao();
//
//	IAnswerDao getAnswerDao();
//
//	ITaskDao getTaskDao();
//
//	IQuestionLogDao getQuestionLogDao();
//
//	IAnswerLogDao getAnswersLogDao();
}
