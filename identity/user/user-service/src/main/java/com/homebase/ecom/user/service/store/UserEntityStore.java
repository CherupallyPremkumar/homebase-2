package com.homebase.ecom.user.service.store;

import com.homebase.ecom.user.domain.model.User;
import com.homebase.ecom.user.domain.port.UserRepository;
import org.chenile.base.exception.NotFoundException;
import org.chenile.utils.entity.service.EntityStore;

/**
 * Bridges Chenile STM's EntityStore with the domain UserRepository port.
 *
 * Follows the same pattern as ChenilePolicyEntityStore in policy-infrastructure.
 * No @Autowired — injected via constructor in UserConfiguration.
 */
public class UserEntityStore implements EntityStore<User> {

    private final UserRepository userRepository;

    public UserEntityStore(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void store(User user) {
        userRepository.save(user);
    }

    @Override
    public User retrieve(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(1500, "User not found: " + id));
    }
}
