package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;

import org.junit.*;

import com.shpach.sn.persistence.entities.Image;
import com.shpach.sn.persistence.entities.Post;
import com.shpach.sn.persistence.entities.UserRole;
import com.shpach.sn.persistence.jdbc.dao.factory.IDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.image.IImageDao;
import com.shpach.sn.persistence.jdbc.dao.image.MySqlImageDao;
import com.shpach.sn.persistence.jdbc.dao.post.IPostDao;
import com.shpach.sn.persistence.jdbc.dao.post.MySqlPostDao;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class ImageDaoTest extends DaoTest {
	private IImageDao imageDao;
	private MySqlImageDao spyImageDao;

	@Before
	public void init() throws SQLException {
		super.init();
		IDaoFactory daoFactory = new MySqlDaoFactory();
		imageDao = daoFactory.getImageDao();
		spyImageDao = (MySqlImageDao) spy(imageDao);
	}

	@Test
	public void findAllTest() throws Exception {
		List<Image> expecteds = new ArrayList<>(Arrays.asList(new Image(), new Image()));

		doReturn(expecteds).when(spyImageDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Image> actuals = spyImageDao.findAll();

		verify(spyImageDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findImageByIdTestExistingId() throws SQLException, NamingException {
		List<Image> expecteds = new ArrayList<>();

		expecteds.add(initImageEntity());

		doReturn(expecteds).when(spyImageDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		Image actual = spyImageDao.findImageById(expecteds.get(0).getPostId());

		verify(spyImageDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(expecteds.get(0), actual);
	}

	@Test
	public void findImageByIdTestNoExistingId() throws SQLException, NamingException {
		doReturn(new ArrayList<Image>()).when(spyImageDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		Image actual =  spyImageDao.findImageById(1);

		verify(spyImageDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void findImageByPostIdOk() {
		List<Image> expecteds = new ArrayList<>();

		expecteds.add(initImageEntity());

		doReturn(expecteds).when(spyImageDao).findByDynamicSelect(anyString(),anyString(), anyObject());

		List<Image> actuals = spyImageDao.findImageByPostId(1);

		verify(spyImageDao, times(1)).findByDynamicSelect(anyString(),anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findPostByUserIdEmpty() {
		List<Image> expecteds = new ArrayList<>();

		doReturn(expecteds).when(spyImageDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Image> actuals = spyImageDao.findImageByPostId(1);

		verify(spyImageDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(0, actuals.size());
	}
	
	@Test
	public void addOrApdateImageNewImage() throws SQLException, NamingException {

		Image inserted = initImageEntity();
		inserted.setImageId(0);

		Image expected = initImageEntity();

		doReturn(expected.getImageId()).when(spyImageDao).dynamicAdd(anyString(), anyObject());

		Image actual = spyImageDao.addOrUpdate(inserted);

		verify(spyImageDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateImageNewImageFail() throws SQLException, NamingException {

		Image inserted = initImageEntity();
		inserted.setImageId(0);

		doReturn(0).when(spyImageDao).dynamicAdd(anyString(), anyObject());

		Image actual = spyImageDao.addOrUpdate(inserted);

		verify(spyImageDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateImageUpdateImage() throws SQLException, NamingException {
		Image expected = initImageEntity();

		doReturn(true).when(spyImageDao).dynamicUpdate(anyString(), anyObject());

		Image actual = spyImageDao.addOrUpdate(expected);

		verify(spyImageDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateImageUpdateImageFail() throws SQLException, NamingException {
		Image expected = initImageEntity();

		doReturn(false).when(spyImageDao).dynamicUpdate(anyString(), anyObject());

		Image actual = spyImageDao.addOrUpdate(expected);

		verify(spyImageDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateImageNull() throws SQLException, NamingException {

		Image actual = spyImageDao.addOrUpdate(null);

		verify(spyImageDao, times(0)).dynamicUpdate(anyString(), anyObject());

		assertNull(actual);
	}
	
	
	@Ignore
	@Test
	public void deletePostByIdOk() throws SQLException, NamingException {

		boolean res = spyImageDao.deleteImageById(1);

		assertTrue(res);
	}
	

	@Test
	public void populateDtoTest() throws SQLException {
		Image expected = initImageEntity();

		when(mockResultSet.getInt(anyInt())).thenReturn(expected.getImageId(), expected.getPostId());
		when(mockResultSet.getString(anyInt())).thenReturn(expected.getImageUrl());

		Image actual = spyImageDao.populateDto(mockResultSet);

		verify(mockResultSet, times(2)).getInt(anyInt());
		verify(mockResultSet, times(1)).getString(anyInt());

		assertEquals(expected, actual);
	}

	@SuppressWarnings("unused")
	@Test(expected = SQLException.class)
	public void populateDtoTestException() throws SQLException {

		when(mockResultSet.getInt(anyInt())).thenThrow(new SQLException());

		Image actual = spyImageDao.populateDto(mockResultSet);

		verify(mockResultSet, times(1)).getInt(anyInt());

	}
	

	private Image initImageEntity() {
		Image image = new Image();
		image.setImageId(10);
		image.setPostId(5);
		image.setImageUrl("/images/img.jpg");
		return image;
	}

}
