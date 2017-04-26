package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.junit.*;

import com.shpach.sn.persistence.entities.Post;
import com.shpach.sn.persistence.entities.UserRole;
import com.shpach.sn.persistence.jdbc.dao.factory.IDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.post.IPostDao;
import com.shpach.sn.persistence.jdbc.dao.post.MySqlPostDao;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class PostDaoTest extends DaoTest {
	private IPostDao postDao;
	private MySqlPostDao spyPostDao;

	@Before
	public void init() throws SQLException {
		super.init();
		IDaoFactory daoFactory = new MySqlDaoFactory();
		postDao = daoFactory.getPostDao();
		spyPostDao = (MySqlPostDao) spy(postDao);
	}

	@Test
	public void findAllTest() throws Exception {
		List<Post> expecteds = new ArrayList<>(Arrays.asList(new Post(), new Post()));

		doReturn(expecteds).when(spyPostDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Post> actuals = spyPostDao.findAll();

		verify(spyPostDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findPostByIdTestExistingId() throws SQLException, NamingException {
		List<Post> expecteds = new ArrayList<>();

		expecteds.add(initPostEntity());

		doReturn(expecteds).when(spyPostDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		Post actual = spyPostDao.findPostById(expecteds.get(0).getPostId());

		verify(spyPostDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(expecteds.get(0), actual);
	}

	@Test
	public void findPostByIdTestNoExistingId() throws SQLException, NamingException {
		doReturn(new ArrayList<Post>()).when(spyPostDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		Post actual = spyPostDao.findPostById(1);

		verify(spyPostDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void findPostByUserIdOk() {
		List<Post> expecteds = new ArrayList<>();

		expecteds.add(initPostEntity());

		doReturn(expecteds).when(spyPostDao).findByDynamicSelect(anyString(),anyString(), anyObject());

		List<Post> actuals = spyPostDao.findPostByUserId(1);

		verify(spyPostDao, times(1)).findByDynamicSelect(anyString(),anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findPostByUserIdEmpty() {
		List<Post> expecteds = new ArrayList<>();

		doReturn(expecteds).when(spyPostDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Post> actuals = spyPostDao.findPostByUserId(1);

		verify(spyPostDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(0, actuals.size());
	}
	
	@Test
	public void findPostByCommunityOk() {
		List<Post> expecteds = new ArrayList<>();

		expecteds.add(initPostEntity());

		doReturn(expecteds).when(spyPostDao).findByDynamicSelect(anyString(),anyString(), anyObject());

		List<Post> actuals = spyPostDao.findPostByCommunityId(1);

		verify(spyPostDao, times(1)).findByDynamicSelect(anyString(),anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findPostByCommunityEmpty() {
		List<Post> expecteds = new ArrayList<>();

		doReturn(expecteds).when(spyPostDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Post> actuals = spyPostDao.findPostByCommunityId(1);

		verify(spyPostDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(0, actuals.size());
	}

	@Test
	public void addOrApdateTestNewPost() throws SQLException, NamingException {

		Post inserted = initPostEntity();
		inserted.setPostId(0);

		Post expected = initPostEntity();

		doReturn(expected.getPostId()).when(spyPostDao).dynamicAdd(anyString(), anyObject());

		Post actual = spyPostDao.addOrUpdate(inserted);

		verify(spyPostDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateTestNewPostFail() throws SQLException, NamingException {

		Post inserted = initPostEntity();
		inserted.setPostId(0);

		doReturn(0).when(spyPostDao).dynamicAdd(anyString(), anyObject());

		Post actual = spyPostDao.addOrUpdate(inserted);

		verify(spyPostDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateTestUpdatePost() throws SQLException, NamingException {
		Post expected = initPostEntity();

		doReturn(true).when(spyPostDao).dynamicUpdate(anyString(), anyObject());

		Post actual = spyPostDao.addOrUpdate(expected);

		verify(spyPostDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateTestUpdatePostFail() throws SQLException, NamingException {
		Post expected = initPostEntity();

		doReturn(false).when(spyPostDao).dynamicUpdate(anyString(), anyObject());

		Post actual = spyPostDao.addOrUpdate(expected);

		verify(spyPostDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateTestNull() throws SQLException, NamingException {

		Post actual = spyPostDao.addOrUpdate(null);

		verify(spyPostDao, times(0)).dynamicUpdate(anyString(), anyObject());

		assertNull(actual);
	}
	
	
	@Ignore
	@Test
	public void deletePostByIdOk() throws SQLException, NamingException {

		boolean res = spyPostDao.deletePostById(1);

		assertTrue(res);
	}
	

	@Test
	public void populateDtoTest() throws SQLException {
		Post expected = initPostEntity();

		when(mockResultSet.getInt(anyInt())).thenReturn(expected.getPostId(), expected.getUserId(),
				expected.getCommunityId());
		when(mockResultSet.getString(anyInt())).thenReturn(expected.getPostText());
		when(mockResultSet.getTimestamp(anyInt())).thenReturn(new java.sql.Timestamp(expected.getPostCreateDatetime().getTime()));
		
		Post actual = spyPostDao.populateDto(mockResultSet);

		verify(mockResultSet, times(3)).getInt(anyInt());
		verify(mockResultSet, times(1)).getString(anyInt());
		verify(mockResultSet, times(1)).getTimestamp(anyInt());
		
		assertEquals(expected, actual);
	}

	@SuppressWarnings("unused")
	@Test(expected = SQLException.class)
	public void populateDtoTestException() throws SQLException {

		when(mockResultSet.getInt(anyInt())).thenThrow(new SQLException());

		Post actual = spyPostDao.populateDto(mockResultSet);

		verify(mockResultSet, times(1)).getInt(anyInt());

	}
	

	private Post initPostEntity() {
		Post post = new Post();
		post.setPostId(10);
		post.setUserId(5);
		post.setCommunityId(7);
		post.setPostText("postText");
		post.setPostCreateDatetime(new Date());
		return post;
	}

}
