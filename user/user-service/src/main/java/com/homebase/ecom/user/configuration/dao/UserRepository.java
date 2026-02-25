package com.homebase.ecom.user.configuration.dao;

import com.homebase.ecom.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface UserRepository extends JpaRepository<User,String> {}
