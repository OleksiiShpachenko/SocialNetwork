package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;

import org.junit.*;

import com.shpach.sn.persistence.entities.UserRole;
import com.shpach.sn.persistence.jdbc.dao.factory.IDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.userrole.IUserRoleDao;
import com.shpach.sn.persistence.jdbc.dao.userrole.MySqlUserRoleDao;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class UserRoleDaoTest extends DaoTest {
	private IUserRoleDao userRoleDao;
	private MySqlUserRoleDao spyUserRoleDao;

	@Before
	public void init() throws SQLException {
		super.init();
		IDaoFactory daoFactory = new MySqlDaoFactory();
		userRoleDao = daoFactory.getUserRoleDao();
		spyUserRoleDao = (MySqlUserRoleDao) spy(userRoleDao);
	}

	@Test
	public void findAllTest() throws Exception {
		List<UserRole> expecteds = new ArrayList<>(Arrays.asList(new UserRole(), new UserRole()));

		doReturn(expecteds).when(spyUserRoleDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<UserRole> actuals = spyUserRoleDao.findAll();

		verify(spyUserRoleDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findUserRoleByIdTestExistingId() throws SQLException, NamingException {
		List<UserRole> expecteds = new ArrayList<>();
		int roleId = 1;
		String roleName = "Tutor";

		UserRole userRoleExpected = new UserRole();
		userRoleExpected.setUserRoleId(roleId);
		userRoleExpected.setUserRoleName(roleName);
		expecteds.add(userRoleExpected);

		doReturn(expecteds).when(spyUserRoleDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		UserRole usersRoleActual = spyUserRoleDao.findUserRoleById(roleId);

		verify(spyUserRoleDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(userRoleExpected, usersRoleActual);
	}

	@Test
	public void findUserRoleByIdTestNoExistingId() throws SQLException, NamingException {
		doReturn(new ArrayList<UserRole>()).when(spyUserRoleDao).findByDynamicSelect(anyString(), anyString(),
				anyObject());

		UserRole usersRoleActual = spyUserRoleDao.findUserRoleById(1);

		verify(spyUserRoleDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(usersRoleActual);
	}

	@Test
	public void findUserRoleUserIdOk() {
		List<UserRole> expecteds = new ArrayList<>();
		int roleId = 1;
		String roleName = "Tutor";

		UserRole userRoleExpected = new UserRole();
		userRoleExpected.setUserRoleId(roleId);
		userRoleExpected.setUserRoleName(roleName);
		expecteds.add(userRoleExpected);

		doReturn(expecteds).when(spyUserRoleDao).findByDynamicSelect(anyString(), anyObject());

		List<UserRole> actuals = spyUserRoleDao.findUserRoleByUserId(1);

		verify(spyUserRoleDao, times(1)).findByDynamicSelect(anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findUserRoleByUserIdEmpty() {
		List<UserRole> expecteds = new ArrayList<>();

		doReturn(expecteds).when(spyUserRoleDao).findByDynamicSelect(anyString(), anyObject());

		List<UserRole> actuals = spyUserRoleDao.findUserRoleByUserId(1);

		verify(spyUserRoleDao, times(1)).findByDynamicSelect(anyString(), anyObject());

		assertEquals(0, actuals.size());
	}

	@Test
	public void addOrApdateTestNewUserRole() throws SQLException, NamingException {

		int roleId = 1;
		String roleName = "Tutor";

		UserRole usersRole = new UserRole();
		usersRole.setUserRoleName(roleName);

		UserRole userRoleExpected = new UserRole();
		userRoleExpected.setUserRoleId(roleId);
		userRoleExpected.setUserRoleName(roleName);

		doReturn(roleId).when(spyUserRoleDao).dynamicAdd(anyString(), anyObject());

		UserRole usersRoleActual = spyUserRoleDao.addOrUpdate(usersRole);

		verify(spyUserRoleDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertEquals(userRoleExpected, usersRoleActual);
	}

	@Test
	public void addOrApdateTestNewUserRoleFail() throws SQLException, NamingException {

		int roleId = 1;
		String roleName = "Tutor";

		UserRole usersRole = new UserRole();
		usersRole.setUserRoleName(roleName);

		UserRole userRoleExpected = new UserRole();
		userRoleExpected.setUserRoleId(roleId);
		userRoleExpected.setUserRoleName(roleName);

		doReturn(0).when(spyUserRoleDao).dynamicAdd(anyString(), anyObject());

		UserRole usersRoleActual = spyUserRoleDao.addOrUpdate(usersRole);

		verify(spyUserRoleDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertNull(usersRoleActual);
	}

	@Test
	public void addOrApdateTestUpdateUserRole() throws SQLException, NamingException {

		int roleId = 1;
		String roleName = "Tutor";

		UserRole userRoleExpected = new UserRole();
		userRoleExpected.setUserRoleId(roleId);
		userRoleExpected.setUserRoleName(roleName);

		doReturn(true).when(spyUserRoleDao).dynamicUpdate(anyString(), anyObject());

		UserRole usersRoleActual = spyUserRoleDao.addOrUpdate(userRoleExpected);

		verify(spyUserRoleDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertEquals(userRoleExpected, usersRoleActual);
	}

	@Test
	public void addOrApdateTestUpdateUserRoleFail() throws SQLException, NamingException {

		int roleId = 1;
		String roleName = "Tutor";

		UserRole userRoleExpected = new UserRole();
		userRoleExpected.setUserRoleId(roleId);
		userRoleExpected.setUserRoleName(roleName);

		doReturn(false).when(spyUserRoleDao).dynamicUpdate(anyString(), anyObject());

		UserRole usersRoleActual = spyUserRoleDao.addOrUpdate(userRoleExpected);

		verify(spyUserRoleDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertNull(usersRoleActual);
	}

	@Test
	public void addOrApdateTestNull() throws SQLException, NamingException {

		UserRole usersRoleActual = spyUserRoleDao.addOrUpdate(null);

		verify(spyUserRoleDao, times(0)).dynamicUpdate(anyString(), anyObject());

		assertNull(usersRoleActual);
	}

	@Test
	public void populateDtoTest() throws SQLException {
		int roleId = 1;
		String roleName = "Tutor";

		UserRole userRoleExpected = new UserRole();
		userRoleExpected.setUserRoleId(roleId);
		userRoleExpected.setUserRoleName(roleName);

		when(mockResultSet.getInt(anyInt())).thenReturn(roleId);
		when(mockResultSet.getString(anyInt())).thenReturn(roleName);

		UserRole actual = spyUserRoleDao.populateDto(mockResultSet);

		verify(mockResultSet, times(1)).getInt(anyInt());
		verify(mockResultSet, times(1)).getString(anyInt());

		assertEquals(userRoleExpected, actual);
	}

	@SuppressWarnings("unused")
	@Test(expected = SQLException.class)
	public void populateDtoTestException() throws SQLException {

		when(mockResultSet.getInt(anyInt())).thenThrow(new SQLException());

		UserRole actual = spyUserRoleDao.populateDto(mockResultSet);

		verify(mockResultSet, times(1)).getInt(anyInt());

	}
}
