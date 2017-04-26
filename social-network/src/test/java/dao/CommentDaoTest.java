package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.junit.*;

import com.shpach.sn.persistence.entities.Comment;
import com.shpach.sn.persistence.jdbc.dao.comment.ICommentDao;
import com.shpach.sn.persistence.jdbc.dao.comment.MySqlCommentDao;
import com.shpach.sn.persistence.jdbc.dao.factory.IDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.factory.MySqlDaoFactory;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class CommentDaoTest extends DaoTest {
	private ICommentDao commentDao;
	private MySqlCommentDao spyCommentDao;

	@Before
	public void init() throws SQLException {
		super.init();
		IDaoFactory daoFactory = new MySqlDaoFactory();
		commentDao = daoFactory.getCommentDao();
		spyCommentDao = (MySqlCommentDao) spy(commentDao);
	}

	@Test
	public void findAllTest() throws Exception {
		List<Comment> expecteds = new ArrayList<>(Arrays.asList(new Comment(), new Comment()));

		doReturn(expecteds).when(spyCommentDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Comment> actuals = spyCommentDao.findAll();

		verify(spyCommentDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findCommentByIdTestExistingId() throws SQLException, NamingException {
		List<Comment> expecteds = new ArrayList<>();

		expecteds.add(initCommentEntity());

		doReturn(expecteds).when(spyCommentDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		Comment actual = spyCommentDao.findCommentById(expecteds.get(0).getCommentId());

		verify(spyCommentDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(expecteds.get(0), actual);
	}

	@Test
	public void findCommentByIdTestNoExistingId() throws SQLException, NamingException {
		doReturn(new ArrayList<Comment>()).when(spyCommentDao).findByDynamicSelect(anyString(), anyString(),
				anyObject());

		Comment actual = spyCommentDao.findCommentById(1);

		verify(spyCommentDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void findCommentByUserIdOk() {
		List<Comment> expecteds = new ArrayList<>();

		expecteds.add(initCommentEntity());

		doReturn(expecteds).when(spyCommentDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Comment> actuals = spyCommentDao.findCommentByUserId(1);

		verify(spyCommentDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findCommandByUserIdEmpty() {
		List<Comment> expecteds = new ArrayList<>();

		doReturn(expecteds).when(spyCommentDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Comment> actuals = spyCommentDao.findCommentByUserId(1);

		verify(spyCommentDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(0, actuals.size());
	}

	@Test
	public void findCommentByPostIdOk() {
		List<Comment> expecteds = new ArrayList<>();

		expecteds.add(initCommentEntity());

		doReturn(expecteds).when(spyCommentDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Comment> actuals = spyCommentDao.findCommentByPostId(1);

		verify(spyCommentDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findCommentByPostIdEmpty() {
		List<Comment> expecteds = new ArrayList<>();

		doReturn(expecteds).when(spyCommentDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Comment> actuals = spyCommentDao.findCommentByPostId(1);

		verify(spyCommentDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(0, actuals.size());
	}

	@Test
	public void addOrApdateCommentNewComment() throws SQLException, NamingException {

		Comment inserted = initCommentEntity();
		inserted.setCommentId(0);

		Comment expected = initCommentEntity();

		doReturn(expected.getCommentId()).when(spyCommentDao).dynamicAdd(anyString(), anyObject());

		Comment actual = spyCommentDao.addOrUpdate(inserted);

		verify(spyCommentDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateCommentNewCommentFail() throws SQLException, NamingException {

		Comment inserted = initCommentEntity();
		inserted.setCommentId(0);

		doReturn(0).when(spyCommentDao).dynamicAdd(anyString(), anyObject());

		Comment actual = spyCommentDao.addOrUpdate(inserted);

		verify(spyCommentDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateCommentUpdateComment() throws SQLException, NamingException {
		Comment expected = initCommentEntity();

		doReturn(true).when(spyCommentDao).dynamicUpdate(anyString(), anyObject());

		Comment actual = spyCommentDao.addOrUpdate(expected);

		verify(spyCommentDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateCommentUpdateCommentFail() throws SQLException, NamingException {
		Comment expected = initCommentEntity();

		doReturn(false).when(spyCommentDao).dynamicUpdate(anyString(), anyObject());

		Comment actual = spyCommentDao.addOrUpdate(expected);

		verify(spyCommentDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateCommentNull() throws SQLException, NamingException {

		Comment actual = spyCommentDao.addOrUpdate(null);

		verify(spyCommentDao, times(0)).dynamicUpdate(anyString(), anyObject());

		assertNull(actual);
	}

	@Ignore
	@Test
	public void deletePostByIdOk() throws SQLException, NamingException {

		boolean res = spyCommentDao.deleteCommentById(1);

		assertTrue(res);
	}

	@Test
	public void populateDtoTest() throws SQLException {
		Comment expected = initCommentEntity();

		when(mockResultSet.getInt(anyInt())).thenReturn(expected.getCommentId(), expected.getPostId(),
				expected.getUserId());
		when(mockResultSet.getString(anyInt())).thenReturn(expected.getCommentText());
		when(mockResultSet.getTimestamp(anyInt()))
				.thenReturn(new java.sql.Timestamp(expected.getCommentCreateDatetime().getTime()));

		Comment actual = spyCommentDao.populateDto(mockResultSet);

		verify(mockResultSet, times(3)).getInt(anyInt());
		verify(mockResultSet, times(1)).getString(anyInt());
		verify(mockResultSet, times(1)).getTimestamp(anyInt());

		assertEquals(expected, actual);
	}

	@SuppressWarnings("unused")
	@Test(expected = SQLException.class)
	public void populateDtoTestException() throws SQLException {

		when(mockResultSet.getInt(anyInt())).thenThrow(new SQLException());

		Comment actual = spyCommentDao.populateDto(mockResultSet);

		verify(mockResultSet, times(1)).getInt(anyInt());

	}

	private Comment initCommentEntity() {
		Comment comment = new Comment();
		comment.setCommentId(8);
		comment.setPostId(10);
		comment.setUserId(5);
		comment.setCommentText("postText");
		comment.setCommentCreateDatetime(new Date());
		return comment;
	}

}
