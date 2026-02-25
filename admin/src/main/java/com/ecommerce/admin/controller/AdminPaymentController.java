package com.ecommerce.admin.controller;

import com.ecommerce.admin.config.GatewayConfigurationManager;
import com.ecommerce.admin.dto.AdminPayoutDetailDto;
import com.ecommerce.admin.dto.AdminPayoutDto;
import com.ecommerce.admin.dto.AdminPaymentDetailDto;
import com.ecommerce.admin.dto.AdminPaymentTransactionDto;
import com.ecommerce.admin.service.AdminPaymentService;
import com.ecommerce.payment.domain.LedgerAccount;
import com.ecommerce.payment.repository.LedgerAccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.math.BigDecimal;
import java.security.Principal;

@Controller
@RequestMapping("/admin/payments")
public class AdminPaymentController {

    private final AdminPaymentService paymentService;
    private final GatewayConfigurationManager gatewayManager;
    private final LedgerAccountRepository ledgerAccountRepository;

    public AdminPaymentController(AdminPaymentService paymentService,
            GatewayConfigurationManager gatewayManager,
            LedgerAccountRepository ledgerAccountRepository) {
        this.paymentService = paymentService;
        this.gatewayManager = gatewayManager;
        this.ledgerAccountRepository = ledgerAccountRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        BigDecimal revenueInPaise = ledgerAccountRepository.findByName("Sales_Revenue")
                .map(LedgerAccount::getBalance)
                .map(BigDecimal::abs)
                .orElse(BigDecimal.ZERO);
        BigDecimal revenue = revenueInPaise.divide(BigDecimal.valueOf(100));

        BigDecimal feesInPaise = ledgerAccountRepository.findByName("Payment_Gateway_Fee_Exp")
                .map(LedgerAccount::getBalance)
                .orElse(BigDecimal.ZERO);
        BigDecimal fees = feesInPaise.divide(BigDecimal.valueOf(100));

        model.addAttribute("totalRevenue", revenue);
        model.addAttribute("totalFees", fees);
        model.addAttribute("totalTransactions", paymentService.getTotalTransactionCount());
        model.addAttribute("todayTransactions", paymentService.getTodayTransactionCount());
        model.addAttribute("failedPayments", paymentService.getFailedTransactionCount());
        model.addAttribute("gatewayStatuses", paymentService.getGatewayStatuses());
        model.addAttribute("activeGateway", gatewayManager.getActiveGateway());

        return "admin/payments/dashboard";
    }

    @GetMapping("/transactions")
    public String listTransactions(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model) {
        Page<AdminPaymentTransactionDto> transactions = paymentService.getAllTransactions(PageRequest.of(page, size));
        model.addAttribute("transactions", transactions);
        return "admin/payments/transactions";
    }

    @GetMapping("/transactions/{id}")
    public String viewTransaction(@PathVariable String id, Model model) {
        AdminPaymentDetailDto detail = paymentService.getPaymentDetail(id);
        model.addAttribute("payment", detail);
        return "admin/payments/payment-detail";
    }

    @GetMapping("/payouts")
    public String listPayouts(@RequestParam(required = false) String gateway, Model model) {
        String selectedGateway = (gateway == null || gateway.isBlank())
                ? gatewayManager.getActiveGateway()
                : gateway;

        List<AdminPayoutDto> payouts = paymentService.getPayouts(selectedGateway);

        model.addAttribute("activeGateway", selectedGateway);
        model.addAttribute("payouts", payouts);
        return "admin/payments/payouts";
    }

    @GetMapping("/payouts/{id}")
    public String viewPayout(@PathVariable String id, Model model) {
        AdminPayoutDetailDto payout = paymentService.getPayoutDetail(id);
        model.addAttribute("payout", payout);
        return "admin/payments/payout-detail";
    }

    @PostMapping("/payouts/{payoutId}/lines/{lineId}/link")
    public String linkPayoutLine(
            @PathVariable String payoutId,
            @PathVariable String lineId,
            @RequestParam(required = false) String paymentTransactionId,
            @RequestParam(required = false) String refundId,
            @RequestParam(required = false) String notes,
            Principal principal) {
        String performedBy = principal != null ? principal.getName() : "SYSTEM";

        try {
            paymentService.linkPayoutLine(payoutId, lineId, paymentTransactionId, refundId, performedBy, notes);
            return "redirect:/admin/payments/payouts/" + payoutId + "?success=linked";
        } catch (IllegalArgumentException ex) {
            return "redirect:/admin/payments/payouts/" + payoutId + "?error=" + ex.getMessage();
        }
    }

    @PostMapping("/payouts/{payoutId}/lines/{lineId}/unlink")
    public String unlinkPayoutLine(
            @PathVariable String payoutId,
            @PathVariable String lineId,
            @RequestParam(required = false) String notes,
            Principal principal) {
        String performedBy = principal != null ? principal.getName() : "SYSTEM";

        try {
            paymentService.unlinkPayoutLine(payoutId, lineId, performedBy, notes);
            return "redirect:/admin/payments/payouts/" + payoutId + "?success=unlinked";
        } catch (IllegalArgumentException ex) {
            return "redirect:/admin/payments/payouts/" + payoutId + "?error=" + ex.getMessage();
        }
    }

    @GetMapping("/gateways")
    public String manageGateways(Model model) {
        model.addAttribute("activeGateway", gatewayManager.getActiveGateway());
        model.addAttribute("statuses", paymentService.getGatewayStatuses());
        return "admin/payments/gateway-management";
    }

    @PostMapping("/gateways/switch")
    public String switchGateway(@RequestParam String gateway, Principal principal) {
        gatewayManager.setActiveGateway(gateway);
        return "redirect:/admin/payments/gateways?success=true";
    }
}
