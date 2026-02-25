package com.ecommerce.web.controller;

import com.ecommerce.shared.domain.Customer;
import com.ecommerce.customer.repository.CustomerRepository;
import com.ecommerce.web.dto.RegisterDto;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class AuthController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        if (!model.containsAttribute("registerDto")) {
            model.addAttribute("registerDto", new RegisterDto());
        }
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(
            @Valid @ModelAttribute("registerDto") RegisterDto registerDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.registerDto", "Passwords do not match");
            return "register";
        }

        if (customerRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "error.registerDto",
                    "An account is already registered with your email address");
            return "register";
        }

        Customer customer = new Customer();
        customer.setId(UUID.randomUUID().toString());
        customer.setFirstName(registerDto.getFirstName());
        customer.setLastName(registerDto.getLastName());
        customer.setEmail(registerDto.getEmail());
        customer.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        // Assign default role. Based on security configuration expectations.
        customer.setRole("ROLE_USER");

        customerRepository.save(customer);

        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! You can now log in.");
        return "redirect:/login";
    }
}
