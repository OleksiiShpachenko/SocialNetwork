package com.shpach.sn.persistence.jdbc.dao.factory;

import com.shpach.sn.persistence.jdbc.dao.community.ICommunityDao;
import com.shpach.sn.persistence.jdbc.dao.community.MySqlCommunityDao;
import com.shpach.sn.persistence.jdbc.dao.communitymember.ICommunityMemberDao;
import com.shpach.sn.persistence.jdbc.dao.communitymember.MySqlCommunityMemberDao;
import com.shpach.sn.persistence.jdbc.dao.friend.IFriendDao;
import com.shpach.sn.persistence.jdbc.dao.friend.MySqlFriendDao;
import com.shpach.sn.persistence.jdbc.dao.image.IImageDao;
import com.shpach.sn.persistence.jdbc.dao.image.MySqlImageDao;
import com.shpach.sn.persistence.jdbc.dao.post.IPostDao;
import com.shpach.sn.persistence.jdbc.dao.post.MySqlPostDao;
import com.shpach.sn.persistence.jdbc.dao.user.IUserDao;
import com.shpach.sn.persistence.jdbc.dao.user.MySqlUserDao;
import com.shpach.sn.persistence.jdbc.dao.userrole.IUserRoleDao;
import com.shpach.sn.persistence.jdbc.dao.userrole.MySqlUserRoleDao;
import com.shpach.sn.persistent.jdbc.dao.comment.ICommentDao;
import com.shpach.sn.persistent.jdbc.dao.comment.MySqlCommentDao;

/**Implementation of {@link IDaoFactory} for MySql database 
 * @author Shpachenko_A_K
 *
 */
public class MySqlDaoFactory implements IDaoFactory {

	public IUserDao getUserDao() {
		return MySqlUserDao.getInstance();
	}

	public IUserRoleDao getUserRoleDao() {
		return MySqlUserRoleDao.getInstance();
	}
//
//	public ICommunityDao getCommunityDao() {
//		return MySqlCommunityDao.getInstance();
//	}
//
//	public ITestDao getTestDao() {
//		return MySqlTestDao.getInstance();
//	}
//
//	@Override
//	public ICategoryDao getCategoryDao() {
//		return MySqlCategoryDao.getInstance();
//	}
//
//	@Override
//	public IQuestionDao getQuestionDao() {
//		return MySqlQuestionDao.getInstance();
//	}
//
//	@Override
//	public ITestQuestionsBankDao getTestQuestionsBankDao() {
//		return MySqlTestQuestionsBankDao.getInstance();
//	}
//
//	@Override
//	public IAnswerDao getAnswerDao() {
//		return MySqlAnswerDao.getInstance();
//	}
//
//	@Override
//	public ITaskDao getTaskDao() {
//		return MySqlTaskDao.getInstance();
//	}
//
//	@Override
//	public IQuestionLogDao getQuestionLogDao() {
//		return MySqlQuestionLogDao.getInstance();
//	}
//
//	@Override
//	public IAnswerLogDao getAnswersLogDao() {
//		return MySqlAnswerLogDao.getInstance();
//	}

	@Override
	public IPostDao getPostDao() {
		return MySqlPostDao.getInstance();
	}

	@Override
	public IImageDao getImageDao() {
		return MySqlImageDao.getInstance();
	}

	@Override
	public ICommentDao getCommentDao() {
		return MySqlCommentDao.getInstance();
	}

	@Override
	public ICommunityDao getCommunityDao() {
		return MySqlCommunityDao.getInstance();
	}

	@Override
	public ICommunityMemberDao gerCommunityMemberDao() {
		return MySqlCommunityMemberDao.getInstance();
	}

	@Override
	public IFriendDao getFriendDao() {
		return MySqlFriendDao.getInstance();
	}

}
