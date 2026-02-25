package com.ecommerce.config;

import com.ecommerce.shared.domain.Customer;
import com.ecommerce.customer.repository.CustomerRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password."));

        return new User(
                customer.getEmail(),
                customer.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(
                        customer.getRole() != null ? customer.getRole() : "ROLE_USER")));
    }
}
