package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.junit.*;

import com.shpach.sn.persistence.entities.Comment;
import com.shpach.sn.persistence.entities.CommunityMember;
import com.shpach.sn.persistence.jdbc.dao.communitymember.ICommunityMemberDao;
import com.shpach.sn.persistence.jdbc.dao.communitymember.MySqlCommunityMemberDao;
import com.shpach.sn.persistence.jdbc.dao.factory.IDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.sn.persistent.jdbc.dao.comment.ICommentDao;
import com.shpach.sn.persistent.jdbc.dao.comment.MySqlCommentDao;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class CommunityMemberDaoTest extends DaoTest {
	private ICommunityMemberDao communityMemberDao;
	private MySqlCommunityMemberDao spyCommunityMemberDao;

	@Before
	public void init() throws SQLException {
		super.init();
		IDaoFactory daoFactory = new MySqlDaoFactory();
		communityMemberDao = daoFactory.gerCommunityMemberDao();
		spyCommunityMemberDao = (MySqlCommunityMemberDao) spy(communityMemberDao);
	}

	@Test
	public void findAllTest() throws Exception {
		List<CommunityMember> expecteds = new ArrayList<>(Arrays.asList(new CommunityMember(), new CommunityMember()));

		doReturn(expecteds).when(spyCommunityMemberDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<CommunityMember> actuals = spyCommunityMemberDao.findAll();

		verify(spyCommunityMemberDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findCommunityMemberByIdTestExistingId() throws SQLException, NamingException {
		List<CommunityMember> expecteds = new ArrayList<>();

		expecteds.add(initCommunityMemberEntity());

		doReturn(expecteds).when(spyCommunityMemberDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		CommunityMember actual = spyCommunityMemberDao.findCommunityMemberById(expecteds.get(0).getCommunityMemberId());

		verify(spyCommunityMemberDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(expecteds.get(0), actual);
	}

	@Test
	public void findCommunityMemberByIdTestNoExistingId() throws SQLException, NamingException {
		doReturn(new ArrayList<CommunityMember>()).when(spyCommunityMemberDao).findByDynamicSelect(anyString(), anyString(),
				anyObject());

		CommunityMember actual = spyCommunityMemberDao.findCommunityMemberById(1);

		verify(spyCommunityMemberDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void findfindCommunityMemberByUserIdOk() {
		List<CommunityMember> expecteds = new ArrayList<>();

		expecteds.add(initCommunityMemberEntity());

		doReturn(expecteds).when(spyCommunityMemberDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<CommunityMember> actuals = spyCommunityMemberDao.findCommunityMemberByUserId(1);

		verify(spyCommunityMemberDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findfindCommunityMemberByUserIdEmpty() {
		List<CommunityMember> expecteds = new ArrayList<>();

		doReturn(expecteds).when(spyCommunityMemberDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<CommunityMember> actuals = spyCommunityMemberDao.findCommunityMemberByUserId(1);

		verify(spyCommunityMemberDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(0, actuals.size());
	}

	@Test
	public void findCommunityMemberByCommunityIdOk() {
		List<CommunityMember> expecteds = new ArrayList<>();

		expecteds.add(initCommunityMemberEntity());

		doReturn(expecteds).when(spyCommunityMemberDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<CommunityMember> actuals = spyCommunityMemberDao.findCommunityMemberByCommunityId(1);

		verify(spyCommunityMemberDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findCommunityMemberByCommunityIdEmpty() {
		List<CommunityMember> expecteds = new ArrayList<>();

		doReturn(expecteds).when(spyCommunityMemberDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<CommunityMember> actuals = spyCommunityMemberDao.findCommunityMemberByCommunityId(1);

		verify(spyCommunityMemberDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(0, actuals.size());
	}

	@Test
	public void addOrApdateCommentNewComment() throws SQLException, NamingException {

		CommunityMember inserted = initCommunityMemberEntity();
		inserted.setCommunityMemberId(0);

		CommunityMember expected = initCommunityMemberEntity();

		doReturn(expected.getCommunityMemberId()).when(spyCommunityMemberDao).dynamicAdd(anyString(), anyObject());

		CommunityMember actual = spyCommunityMemberDao.addOrUpdate(inserted);

		verify(spyCommunityMemberDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateCommentNewCommentFail() throws SQLException, NamingException {

		CommunityMember inserted = initCommunityMemberEntity();
		inserted.setCommunityMemberId(0);

		doReturn(0).when(spyCommunityMemberDao).dynamicAdd(anyString(), anyObject());

		CommunityMember actual = spyCommunityMemberDao.addOrUpdate(inserted);

		verify(spyCommunityMemberDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateCommentUpdateComment() throws SQLException, NamingException {
		CommunityMember expected = initCommunityMemberEntity();

		doReturn(true).when(spyCommunityMemberDao).dynamicUpdate(anyString(), anyObject());

		CommunityMember actual = spyCommunityMemberDao.addOrUpdate(expected);

		verify(spyCommunityMemberDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateCommentUpdateCommentFail() throws SQLException, NamingException {
		CommunityMember expected = initCommunityMemberEntity();

		doReturn(false).when(spyCommunityMemberDao).dynamicUpdate(anyString(), anyObject());

		CommunityMember actual = spyCommunityMemberDao.addOrUpdate(expected);

		verify(spyCommunityMemberDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateCommentNull() throws SQLException, NamingException {

		CommunityMember actual = spyCommunityMemberDao.addOrUpdate(null);

		verify(spyCommunityMemberDao, times(0)).dynamicUpdate(anyString(), anyObject());

		assertNull(actual);
	}

	@Ignore
	@Test
	public void deletePostByIdOk() throws SQLException, NamingException {

		boolean res = spyCommunityMemberDao.deleteCommunityMemberById(1);

		assertTrue(res);
	}

	@Test
	public void populateDtoTest() throws SQLException {
		CommunityMember expected = initCommunityMemberEntity();

		when(mockResultSet.getInt(anyInt())).thenReturn(expected.getCommunityMemberId(), expected.getCommunityId(),
				expected.getUserId(), expected.getCommunityMemberStatus());
		when(mockResultSet.getTimestamp(anyInt()))
				.thenReturn(new java.sql.Timestamp(expected.getCommunityMemberDatetime().getTime()));

		CommunityMember actual = spyCommunityMemberDao.populateDto(mockResultSet);

		verify(mockResultSet, times(4)).getInt(anyInt());
		verify(mockResultSet, times(1)).getTimestamp(anyInt());

		assertEquals(expected, actual);
	}

	@SuppressWarnings("unused")
	@Test(expected = SQLException.class)
	public void populateDtoTestException() throws SQLException {

		when(mockResultSet.getInt(anyInt())).thenThrow(new SQLException());

		CommunityMember actual = spyCommunityMemberDao.populateDto(mockResultSet);

		verify(mockResultSet, times(1)).getInt(anyInt());

	}

	private CommunityMember initCommunityMemberEntity() {
		CommunityMember communityMember = new CommunityMember();
		communityMember.setCommunityMemberId(8);
		communityMember.setCommunityId(10);
		communityMember.setUserId(5);
		communityMember.setCommunityMemberStatus(1);
		communityMember.setCommunityMemberDatetime(new Date());
		return communityMember;
	}

}
