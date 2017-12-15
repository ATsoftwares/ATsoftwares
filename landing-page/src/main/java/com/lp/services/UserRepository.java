package com.lp.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.lp.models.RegistrationModel;

@Transactional
@Repository("userRepository")
public class UserRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<RegistrationModel> getUsers() {
		return entityManager.createQuery("select u from RegistrationModel u").getResultList();
	}

	@SuppressWarnings("unchecked")
	public RegistrationModel getUserByName(String email) {
		return (RegistrationModel) entityManager.createQuery("select u from RegistrationModel u where email = :email")
				.setParameter("email", email).getResultList().get(0);
	}

	public void saveOrUpdate(RegistrationModel model) {
		entityManager.merge(model);
	}
}
