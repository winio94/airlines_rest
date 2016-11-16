package com.repository;

import com.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Michał on 2016-11-14.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByEmail(@Param("email") String email);
}