package com.wappcode.java.graphql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wappcode.java.graphql.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
