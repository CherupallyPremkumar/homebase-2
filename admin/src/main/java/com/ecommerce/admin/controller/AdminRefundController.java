package com.ecommerce.admin.controller;

import com.ecommerce.admin.dto.AdminRefundDto;
import com.ecommerce.admin.service.AdminRefundService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/admin/refunds")
public class AdminRefundController {

    private final AdminRefundService refundService;

    public AdminRefundController(AdminRefundService refundService) {
        this.refundService = refundService;
    }

    @GetMapping
    public String listRefunds(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model) {
        Page<AdminRefundDto> refunds = refundService.getAllRefunds(PageRequest.of(page, size));
        model.addAttribute("refunds", refunds);
        return "admin/refunds/list";
    }

    @PostMapping("/{id}/approve")
    public String approveRefund(@PathVariable String id, Principal principal) {
        refundService.approveRefund(id, principal.getName());
        return "redirect:/admin/refunds?success=approved";
    }

    @PostMapping("/{id}/process")
    public String processRefund(@PathVariable String id, Principal principal) {
        refundService.processRefund(id, principal.getName());
        return "redirect:/admin/refunds?success=processed";
    }
}
