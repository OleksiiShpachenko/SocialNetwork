package com.shpach.sn.persistence.jdbc.dao.userrole;

import java.util.List;

import com.shpach.sn.persistence.entities.UserRole;



public interface IUserRoleDao {
	public List<UserRole> findAll();

	public UserRole addOrUpdate(UserRole usersRole);

	public UserRole findUserRoleById(int id);
	
	public List<UserRole> findUserRoleByUserId(int userId);
}
