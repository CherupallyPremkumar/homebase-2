package com.ecommerce.admin.service;

import com.ecommerce.admin.dto.AdminCustomerDto;
import com.ecommerce.shared.domain.Customer;
import com.ecommerce.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCustomerService {
    private final CustomerRepository customerRepository;

    public Page<AdminCustomerDto> getCustomers(int page, int size) {
        return customerRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")))
                .map(this::mapToDto);
    }

    private AdminCustomerDto mapToDto(Customer customer) {
        AdminCustomerDto dto = new AdminCustomerDto();
        dto.setId(customer.getId());
        dto.setEmail(customer.getEmail());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setRole(customer.getRole());
        dto.setCreatedAt(customer.getCreatedAt());
        return dto;
    }
}
