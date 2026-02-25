package com.homebase.ecom.user.service.store;

import org.chenile.utils.entity.service.EntityStore;
import com.homebase.ecom.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.chenile.base.exception.NotFoundException;
import com.homebase.ecom.user.configuration.dao.UserRepository;
import java.util.Optional;

public class UserEntityStore implements EntityStore<User>{
    @Autowired private UserRepository userRepository;

	@Override
	public void store(User entity) {
        userRepository.save(entity);
	}

	@Override
	public User retrieve(String id) {
        Optional<User> entity = userRepository.findById(id);
        if (entity.isPresent()) return entity.get();
        throw new NotFoundException(1500,"Unable to find User with ID " + id);
	}

}
