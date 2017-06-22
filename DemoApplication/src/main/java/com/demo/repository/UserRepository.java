package com.demo.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.demo.models.UserInfo;

@Transactional
@Repository("userRepository")
public class UserRepository /* implements GenericRepository */ {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<UserInfo> getUsers() {
		return entityManager.createQuery("select u from UserInfo u").getResultList();
	}

	@SuppressWarnings("unchecked")
	public UserInfo getUserByName(String userName) {
		return (UserInfo) entityManager.createQuery("select u from UserInfo u where userName = :userName")
				.setParameter("userName", userName).getResultList().get(0);
	}

	@SuppressWarnings("unchecked")
	public UserInfo getUserByCredentials(String userName, String password) {
		List<UserInfo> user = entityManager
				.createQuery("select u from UserInfo u where userName = :userName and password = :password")
				.setParameter("userName", userName).setParameter("password", password).getResultList();
		return user.size() > 0 ? user.get(0) : null;
	}

	public void saveOrUpdate(UserInfo entity) {
		entityManager.merge(entity);
	}

	public boolean exists(Integer id) {
		return false;
	}

	public void delete(Integer id) {
		// TODO Auto-generated method stub
	}

	public void delete(UserInfo entity) {
		// TODO Auto-generated method stub
	}

	public void deleteAll() {

	}

}
