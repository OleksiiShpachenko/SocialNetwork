package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.junit.*;

import com.shpach.sn.persistence.entities.Friend;
import com.shpach.sn.persistence.jdbc.dao.factory.IDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.friend.IFriendDao;
import com.shpach.sn.persistence.jdbc.dao.friend.MySqlFriendDao;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class FriendDaoTest extends DaoTest {
	private IFriendDao friendDao;
	private MySqlFriendDao spyFriendDao;

	@Before
	public void init() throws SQLException {
		super.init();
		IDaoFactory daoFactory = new MySqlDaoFactory();
		friendDao = daoFactory.getFriendDao();
		spyFriendDao = (MySqlFriendDao) spy(friendDao);
	}

	@Test
	public void findAllTest() throws Exception {
		List<Friend> expecteds = new ArrayList<>(Arrays.asList(new Friend(), new Friend()));

		doReturn(expecteds).when(spyFriendDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Friend> actuals = spyFriendDao.findAll();

		verify(spyFriendDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	
	@Test
	public void findFriendsByUserIdOk() {
		List<Friend> expecteds = new ArrayList<>();

		expecteds.add(initFriendEntity());

		doReturn(expecteds).when(spyFriendDao).findByDynamicSelect(anyString(), anyObject());

		List<Friend> actuals = spyFriendDao.findFriendByUserId(1);

		verify(spyFriendDao, times(1)).findByDynamicSelect(anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findFriendsByUserIdEmpty() {
		List<Friend> expecteds = new ArrayList<>();

		doReturn(expecteds).when(spyFriendDao).findByDynamicSelect(anyString(), anyObject());

		List<Friend> actuals = spyFriendDao.findFriendByUserId(1);

		verify(spyFriendDao, times(1)).findByDynamicSelect(anyString(),  anyObject());

		assertEquals(0, actuals.size());
	}

	
	@Test
	public void addTestOk() throws SQLException, NamingException {

		Friend expected = initFriendEntity();

		doReturn(0).when(spyFriendDao).dynamicAdd(anyString(), anyObject());

		Friend actual = spyFriendDao.add(expected);

		verify(spyFriendDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addTestFail() throws SQLException, NamingException {

		Friend expected = initFriendEntity();

		doReturn(-1).when(spyFriendDao).dynamicAdd(anyString(), anyObject());

		Friend actual = spyFriendDao.add(expected);

		verify(spyFriendDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertNull(actual);
	}
	
	@Test
	public void addTestNull() throws SQLException, NamingException {

		Friend actual = spyFriendDao.add(null);

		verify(spyFriendDao, times(0)).dynamicAdd(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void UpdateTestOk() throws SQLException, NamingException {
		Friend expected = initFriendEntity();

		doReturn(true).when(spyFriendDao).dynamicUpdate(anyString(), anyObject());

		Friend actual = spyFriendDao.update(expected);

		verify(spyFriendDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void UpdateTestFail() throws SQLException, NamingException {
		Friend expected = initFriendEntity();

		doReturn(false).when(spyFriendDao).dynamicUpdate(anyString(), anyObject());

		Friend actual = spyFriendDao.update(expected);

		verify(spyFriendDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void UpdateTestNull() throws SQLException, NamingException {

		Friend actual = spyFriendDao.update(null);

		verify(spyFriendDao, times(0)).dynamicUpdate(anyString(), anyObject());

		assertNull(actual);
	}

	@Ignore
	@Test
	public void deleteFriendByIdOk() throws SQLException, NamingException {

		boolean res = spyFriendDao.deleteFriendByUsersId(1, 2);

		assertTrue(res);
	}

	@Test
	public void populateDtoTest() throws SQLException {
		Friend expected = initFriendEntity();

		when(mockResultSet.getInt(anyInt())).thenReturn(expected.getHostUserId(), expected.getSlaveUserId(),
				expected.getFriendStatus());
		when(mockResultSet.getTimestamp(anyInt()))
				.thenReturn(new java.sql.Timestamp(expected.getFriendStatusDatetime().getTime()));

		Friend actual = spyFriendDao.populateDto(mockResultSet);

		verify(mockResultSet, times(3)).getInt(anyInt());
		verify(mockResultSet, times(1)).getTimestamp(anyInt());

		assertEquals(expected, actual);
	}

	@SuppressWarnings("unused")
	@Test(expected = SQLException.class)
	public void populateDtoTestException() throws SQLException {

		when(mockResultSet.getInt(anyInt())).thenThrow(new SQLException());

		Friend actual = spyFriendDao.populateDto(mockResultSet);

		verify(mockResultSet, times(1)).getInt(anyInt());

	}

	private Friend initFriendEntity() {
		Friend friend = new Friend();
		friend.setHostUserId(2);
		friend.setSlaveUserId(3);
		friend.setFriendStatus(1);
		friend.setFriendStatusDatetime(new Date());
		return friend;
	}

}
