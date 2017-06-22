package com.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.demo.models.UserInfo;

public interface GenericRepository extends CrudRepository<UserInfo, Integer> {

}
