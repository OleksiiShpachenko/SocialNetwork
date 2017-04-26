package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;

import org.junit.*;

import com.shpach.sn.persistence.entities.Community;
import com.shpach.sn.persistence.jdbc.dao.community.ICommunityDao;
import com.shpach.sn.persistence.jdbc.dao.community.MySqlCommunityDao;
import com.shpach.sn.persistence.jdbc.dao.factory.IDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.factory.MySqlDaoFactory;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class CommunityDaoTest extends DaoTest {
	private ICommunityDao communityDao;
	private MySqlCommunityDao spyCommunityDao;

	@Before
	public void init() throws SQLException {
		super.init();
		IDaoFactory daoFactory = new MySqlDaoFactory();
		communityDao = daoFactory.getCommunityDao();
		spyCommunityDao = (MySqlCommunityDao) spy(communityDao);
	}

	@Test
	public void findAllTest() throws Exception {
		List<Community> expecteds = new ArrayList<>(Arrays.asList(new Community(), new Community()));

		doReturn(expecteds).when(spyCommunityDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Community> actuals = spyCommunityDao.findAll();

		verify(spyCommunityDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findCommunityByIdTestExistingId() throws SQLException, NamingException {
		List<Community> expecteds = new ArrayList<>();

		expecteds.add(initCommunityEntity());

		doReturn(expecteds).when(spyCommunityDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		Community actual = spyCommunityDao.findCommunityById(expecteds.get(0).getCommunityId());

		verify(spyCommunityDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(expecteds.get(0), actual);
	}

	@Test
	public void findCommunityByIdTestNoExistingId() throws SQLException, NamingException {
		doReturn(new ArrayList<Community>()).when(spyCommunityDao).findByDynamicSelect(anyString(), anyString(),
				anyObject());

		Community actual = spyCommunityDao.findCommunityById(1);

		verify(spyCommunityDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateCommunitytNewCommunity() throws SQLException, NamingException {

		Community inserted = initCommunityEntity();
		inserted.setCommunityId(0);

		Community expected = initCommunityEntity();

		doReturn(expected.getCommunityId()).when(spyCommunityDao).dynamicAdd(anyString(), anyObject());

		Community actual = spyCommunityDao.addOrUpdate(inserted);

		verify(spyCommunityDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateCommunityNewCommunityFail() throws SQLException, NamingException {

		Community inserted = initCommunityEntity();
		inserted.setCommunityId(0);

		doReturn(0).when(spyCommunityDao).dynamicAdd(anyString(), anyObject());

		Community actual = spyCommunityDao.addOrUpdate(inserted);

		verify(spyCommunityDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateCommunityUpdateCommunity() throws SQLException, NamingException {
		Community expected = initCommunityEntity();

		doReturn(true).when(spyCommunityDao).dynamicUpdate(anyString(), anyObject());

		Community actual = spyCommunityDao.addOrUpdate(expected);

		verify(spyCommunityDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateCommunityUpdateCommunityFail() throws SQLException, NamingException {
		Community expected = initCommunityEntity();

		doReturn(false).when(spyCommunityDao).dynamicUpdate(anyString(), anyObject());

		Community actual = spyCommunityDao.addOrUpdate(expected);

		verify(spyCommunityDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateCommunityNull() throws SQLException, NamingException {

		Community actual = spyCommunityDao.addOrUpdate(null);

		verify(spyCommunityDao, times(0)).dynamicUpdate(anyString(), anyObject());

		assertNull(actual);
	}

	@Ignore
	@Test
	public void deletePostByIdOk() throws SQLException, NamingException {

		boolean res = spyCommunityDao.deleteCommunityById(1);

		assertTrue(res);
	}

	@Test
	public void populateDtoTest() throws SQLException {
		Community expected = initCommunityEntity();

		when(mockResultSet.getInt(anyInt())).thenReturn(expected.getCommunityId());
		when(mockResultSet.getString(anyInt())).thenReturn(expected.getCommunityName());

		Community actual = spyCommunityDao.populateDto(mockResultSet);

		verify(mockResultSet, times(1)).getInt(anyInt());
		verify(mockResultSet, times(1)).getString(anyInt());

		assertEquals(expected, actual);
	}

	@SuppressWarnings("unused")
	@Test(expected = SQLException.class)
	public void populateDtoTestException() throws SQLException {

		when(mockResultSet.getInt(anyInt())).thenThrow(new SQLException());

		Community actual = spyCommunityDao.populateDto(mockResultSet);

		verify(mockResultSet, times(1)).getInt(anyInt());

	}

	private Community initCommunityEntity() {
		Community comment = new Community();
		comment.setCommunityId(8);
		comment.setCommunityName("community name");
		return comment;
	}

}
