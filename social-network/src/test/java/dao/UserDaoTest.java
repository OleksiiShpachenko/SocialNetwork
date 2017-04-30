package dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import com.shpach.sn.persistence.entities.User;
import com.shpach.sn.persistence.entities.UserRole;
import com.shpach.sn.persistence.jdbc.dao.factory.IDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.user.IUserDao;
import com.shpach.sn.persistence.jdbc.dao.user.MySqlUserDao;

public class UserDaoTest extends DaoTest {
	private User user_1, user_2;
	private User userNew = null;
	private IUserDao userDao;
	private MySqlUserDao spyUserDao;

	@Before
	public void init() throws SQLException {
		super.init();
		IDaoFactory daoFactory = new MySqlDaoFactory();
		userDao = daoFactory.getUserDao();
		spyUserDao = (MySqlUserDao) spy(userDao);

		Date date=new Date();
		
		user_1 = new User();
		user_1.setUserId(1);
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
		user_1.setUserCreateDatetime(date);

		user_2 = new User();
		user_2.setUserId(2);
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
		user_2.setUserCreateDatetime(date);

		userNew = new User();
		userNew.setUserName("Lesha");
		userNew.setUserEmail("q@gmail.com");
		userNew.setUserPassword("password");
		userNew.setAvatarUrl("/avatars/123.jpg");
		userNew.setUserActive((byte) 1);
		userNew.setUserLogin("leshaLogin");
		userNew.setUserPostPermition((byte) 1);
		userNew.setUserCommentPermition((byte) 1);
		userNew.setUserInvitePermition((byte) 1);
		userNew.setUserCreateCommunityPermition((byte) 1);
		userNew.setUserCreateDatetime(date);

	}

	@Test
	public void populateDtoTest() throws SQLException {
		when(mockResultSet.getInt(anyInt())).thenReturn(user_1.getUserId());
		when(mockResultSet.getByte(anyInt())).thenReturn(user_1.getUserActive(), user_1.getUserPostPermition(),
				user_1.getUserInvitePermition(), user_1.getUserCommentPermition(),
				user_1.getUserCreateCommunityPermition());
		when(mockResultSet.getString(anyInt())).thenReturn(user_1.getUserLogin(), user_1.getUserPassword(),
				user_1.getUserName(), user_1.getUserEmail(), user_1.getAvatarUrl());
		when(mockResultSet.getTimestamp(anyInt())).thenReturn(new java.sql.Timestamp(user_1.getUserCreateDatetime().getTime()));

		User actual = spyUserDao.populateDto(mockResultSet);

		verify(mockResultSet, times(1)).getInt(anyInt());
		verify(mockResultSet, times(5)).getString(anyInt());
		verify(mockResultSet, times(5)).getByte(anyInt());
		verify(mockResultSet, times(1)).getTimestamp(anyInt());

		assertEquals(user_1, actual);
	}

	@SuppressWarnings("unused")
	@Test(expected = SQLException.class)
	public void populateDtoTestException() throws SQLException {

		when(mockResultSet.getInt(anyInt())).thenThrow(new SQLException());

		User actual = spyUserDao.populateDto(mockResultSet);

		verify(mockResultSet, times(1)).getInt(anyInt());

	}

	@Test
	public void findUserByEmailTestExistEmail() throws SQLException, NamingException {
		doReturn(new ArrayList<User>(Arrays.asList(user_1))).when(spyUserDao).findByDynamicSelect(anyString(),
				anyString(), anyObject());

		User actual = spyUserDao.findUserByEmail(user_1.getUserEmail());

		verify(spyUserDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(user_1, actual);
	}

	@Test
	public void findUserByEmailTestNullEmail() throws SQLException, NamingException {
		User actual = spyUserDao.findUserByEmail(null);

		verify(spyUserDao, times(0)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void findUserByEmailTestNotExistEmail() throws SQLException, NamingException {
		doReturn(new ArrayList<User>()).when(spyUserDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		User actual = spyUserDao.findUserByEmail("notExistEmail");

		verify(spyUserDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void findUserByNameTestExistEmail() throws SQLException, NamingException {
		doReturn(new ArrayList<User>(Arrays.asList(user_1))).when(spyUserDao).findByDynamicSelect(anyString(),
				anyString(), anyObject());

		User actual = spyUserDao.findUserByName(user_1.getUserName());

		verify(spyUserDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(user_1, actual);
	}

	@Test
	public void findUserByNameTestNullEmail() throws SQLException, NamingException {
		User actual = spyUserDao.findUserByName(null);

		verify(spyUserDao, times(0)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void findUserByNameTestNotExistEmail() throws SQLException, NamingException {
		doReturn(new ArrayList<User>()).when(spyUserDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		User actual = spyUserDao.findUserByName("notExistName");

		verify(spyUserDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void findUserByIdTestExistId() throws SQLException, NamingException {
		doReturn(new ArrayList<User>(Arrays.asList(user_1))).when(spyUserDao).findByDynamicSelect(anyString(),
				anyString(), anyObject());

		User actual = spyUserDao.findUserById(user_1.getUserId());

		verify(spyUserDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(user_1, actual);
	}

	@Test
	public void findUserByIdTestNotExistId() throws SQLException, NamingException {
		doReturn(new ArrayList<User>()).when(spyUserDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		User actual = spyUserDao.findUserById(0);

		verify(spyUserDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateTestNewUser() throws SQLException, NamingException {

		doReturn(mockConnection).when(spyUserDao).getConnection();
		doReturn(user_1.getUserId()).when(spyUserDao).dynamicAdd(anyString(),anyObject(), anyObject());

		User actual = spyUserDao.addOrUpdate(userNew);

		verify(spyUserDao, times(1)).getConnection();
		verify(mockConnection, times(1)).setAutoCommit(false);
		verify(mockConnection, times(1)).setAutoCommit(true);
		verify(mockConnection, times(0)).rollback();
		verify(mockConnection, times(1)).close();
		
		verify(spyUserDao, times(2)).dynamicAdd(anyString(),anyObject(), anyObject());

		assertEquals(user_1, actual);
	}
	
	@Test
	public void addOrApdateTestNewUserFailAddUser() throws SQLException, NamingException {

		doReturn(mockConnection).when(spyUserDao).getConnection();
		doReturn(0).when(spyUserDao).dynamicAdd(anyString(),anyObject(), anyObject());

		User actual = spyUserDao.addOrUpdate(userNew);

		verify(spyUserDao, times(1)).getConnection();
		verify(mockConnection, times(1)).setAutoCommit(false);
		verify(mockConnection, times(1)).setAutoCommit(true);
		verify(mockConnection, times(1)).rollback();
		verify(mockConnection, times(1)).close();
		
		verify(spyUserDao, times(1)).dynamicAdd(anyString(),anyObject(), anyObject());

		assertNull(actual);
	}
	@Test
	public void addOrApdateTestNewUserFailAddUserRoleRelations() throws SQLException, NamingException {

		doReturn(mockConnection).when(spyUserDao).getConnection();
		doReturn(user_1.getUserId()).doReturn(-1).when(spyUserDao).dynamicAdd(anyString(),anyObject(), anyObject());
		
		User actual = spyUserDao.addOrUpdate(userNew);

		verify(spyUserDao, times(1)).getConnection();
		verify(mockConnection, times(1)).setAutoCommit(false);
		verify(mockConnection, times(1)).setAutoCommit(true);
		verify(mockConnection, times(1)).rollback();
		verify(mockConnection, times(1)).close();
		
		verify(spyUserDao, times(2)).dynamicAdd(anyString(),anyObject(), anyObject());

		assertNull(actual);
	}
	
	@Test
	public void addOrApdateTestNewUserFailException() throws SQLException, NamingException {

		doThrow(new SQLException()).when(spyUserDao).getConnection();
		
		User actual = spyUserDao.addOrUpdate(userNew);

		verify(spyUserDao, times(1)).getConnection();
		verify(mockConnection, times(0)).setAutoCommit(false);
		verify(mockConnection, times(0)).setAutoCommit(true);
		verify(mockConnection, times(0)).rollback();
		verify(mockConnection, times(0)).close();
		
    	verify(spyUserDao, times(0)).dynamicAdd(anyString(),anyObject(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateTestUpdateUser() throws SQLException, NamingException {
		doReturn(true).when(spyUserDao).dynamicUpdate(anyString(), anyObject());

		User actual = spyUserDao.addOrUpdate(user_1);

		verify(spyUserDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertEquals(user_1, actual);
	}

	@Test
	public void addOrApdateTestUpdateUserFail() throws SQLException, NamingException {
		doReturn(false).when(spyUserDao).dynamicUpdate(anyString(), anyObject());

		User actual = spyUserDao.addOrUpdate(user_1);

		verify(spyUserDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertNull(actual);
	}

	// @Test
	// public void findUsersByCommunityIdTest() throws SQLException,
	// NamingException {
	// List<User> expecteds = new ArrayList<User>(Arrays.asList(user_1,
	// user_2));
	//
	// doReturn(expecteds).when(spyUserDao).findByDynamicSelect(anyString(),
	// anyObject());
	//
	// List<User> actuals = spyUserDao.findUsersByCommunityId(1);
	//
	// verify(spyUserDao, times(1)).findByDynamicSelect(anyString(),
	// anyObject());
	//
	// assertArrayEquals(expecteds.toArray(), actuals.toArray());
	// }
	//
	// @Test
	// public void findUsersByCommunityIdTestEmpty() throws SQLException,
	// NamingException {
	// doReturn(new
	// ArrayList<User>()).when(spyUserDao).findByDynamicSelect(anyString(),
	// anyObject());
	//
	// List<User> actuals = spyUserDao.findUsersByCommunityId(1);
	//
	// verify(spyUserDao, times(1)).findByDynamicSelect(anyString(),
	// anyObject());
	//
	// assertEquals(0, actuals.size());
	// }

	@Test
	public void findAllTest() throws SQLException, NamingException {
		List<User> expecteds = new ArrayList<>(Arrays.asList(new User(), new User()));

		doReturn(expecteds).when(spyUserDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<User> actuals = spyUserDao.findAll();

		verify(spyUserDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}
	
	
	@Test
	public void findUsersWhichLikesPostByPostIdOk() {
		List<User> expecteds = new ArrayList<>();

		expecteds.add(user_1);

		doReturn(expecteds).when(spyUserDao).findByDynamicSelect(anyString(), anyObject());

		List<User> actuals = spyUserDao.findUsersWhichLikesPostByPostId(1);

		verify(spyUserDao, times(1)).findByDynamicSelect(anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findUsersWhichLikesPostByPostIdEmpty() {
		List<User> expecteds = new ArrayList<>();

		doReturn(expecteds).when(spyUserDao).findByDynamicSelect(anyString(), anyObject());

		List<User> actuals = spyUserDao.findUsersWhichLikesPostByPostId(1);

		verify(spyUserDao, times(1)).findByDynamicSelect(anyString(), anyObject());

		assertEquals(0, actuals.size());
	}

}
