package com.ecommerce;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for all HTTP endpoints.
 * Tests are organized by endpoint category and verify:
 * - Public pages are accessible without auth
 * - Protected pages redirect to login when unauthenticated
 * - Admin pages are accessible with ROLE_ADMIN
 * - Templates render without errors (no 500s)
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EndpointIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // ──────────────────────────────────────────────
    // PUBLIC PAGES (No Authentication Required)
    // ──────────────────────────────────────────────

    @Nested
    @DisplayName("Public Pages")
    class PublicPages {

        @Test
        @DisplayName("GET / → redirects to /products")
        void homepageRedirects() throws Exception {
            mockMvc.perform(get("/"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/products"));
        }

        @Test
        @DisplayName("GET /products → 200 OK")
        void productListPage() throws Exception {
            mockMvc.perform(get("/products"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("products/list"));
        }

        @Test
        @DisplayName("GET /products/search → 200 OK")
        void productSearchPage() throws Exception {
            mockMvc.perform(get("/products/search").param("query", "art"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /login → 200 OK")
        void loginPage() throws Exception {
            mockMvc.perform(get("/login"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /register → 200 OK")
        void registerPage() throws Exception {
            mockMvc.perform(get("/register"))
                    .andExpect(status().isOk());
        }
    }

    // ──────────────────────────────────────────────
    // PROTECTED USER PAGES (Auth Required)
    // ──────────────────────────────────────────────

    @Nested
    @DisplayName("Protected User Pages — Unauthenticated")
    class ProtectedPagesUnauthenticated {

        @Test
        @DisplayName("GET /cart → 200 OK (guest cart)")
        void cartAccessibleWithoutAuth() throws Exception {
            mockMvc.perform(get("/cart"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("cart/view"));
        }

        @Test
        @DisplayName("GET /checkout → redirects to login")
        void checkoutRequiresAuth() throws Exception {
            mockMvc.perform(get("/checkout"))
                    .andExpect(status().is3xxRedirection());
        }

        @Test
        @DisplayName("GET /orders → redirects to login")
        void ordersRequiresAuth() throws Exception {
            mockMvc.perform(get("/orders"))
                    .andExpect(status().is3xxRedirection());
        }
    }

    @Nested
    @DisplayName("Protected User Pages — Authenticated")
    class ProtectedPagesAuthenticated {

        @Test
        @WithMockUser(username = "user@test.com", roles = "USER")
        @DisplayName("GET /cart → 200 OK")
        void cartPageAccessible() throws Exception {
            mockMvc.perform(get("/cart"))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "user@test.com", roles = "USER")
        @DisplayName("GET /orders → 200 OK")
        void ordersPageAccessible() throws Exception {
            mockMvc.perform(get("/orders"))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "user@test.com", roles = "USER")
        @DisplayName("GET /checkout → redirects to /cart when cart is empty")
        void checkoutRedirectsWhenCartEmpty() throws Exception {
            mockMvc.perform(get("/checkout"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/cart"));
        }

        @Test
        @WithMockUser(username = "user@test.com", roles = "USER")
        @DisplayName("GET /checkout/cancel → 200 OK")
        void checkoutCancelAccessible() throws Exception {
            mockMvc.perform(get("/checkout/cancel"))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "user@test.com", roles = "USER")
        @DisplayName("GET /api/v1/orders → 200 OK")
        void restOrdersAccessible() throws Exception {
            mockMvc.perform(get("/api/v1/orders"))
                    .andExpect(status().isOk());
        }
    }

    // ──────────────────────────────────────────────
    // ADMIN PAGES
    // ──────────────────────────────────────────────

    @Nested
    @DisplayName("Admin Pages — Unauthenticated")
    class AdminPagesUnauthenticated {

        @Test
        @DisplayName("GET /admin/login → 200 OK")
        void adminLoginPage() throws Exception {
            mockMvc.perform(get("/admin/login"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /admin/dashboard → redirects to login")
        void adminDashboardRequiresAuth() throws Exception {
            mockMvc.perform(get("/admin/dashboard"))
                    .andExpect(status().is3xxRedirection());
        }
    }

    @Nested
    @DisplayName("Admin Dashboard")
    class AdminDashboard {

        @Test
        @WithMockUser(username = "admin@homemade.com", roles = "ADMIN")
        @DisplayName("GET /admin/dashboard → 200 OK, renders with stats")
        void dashboardRendersWithStats() throws Exception {
            mockMvc.perform(get("/admin/dashboard"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("admin/dashboard/index"))
                    .andExpect(model().attributeExists("stats"))
                    .andExpect(model().attributeExists("recentOrders"))
                    .andExpect(model().attributeExists("pendingOrders"))
                    .andExpect(model().attributeExists("lowStockCount"));
        }

        @Test
        @WithMockUser(username = "admin@homemade.com", roles = "ADMIN")
        @DisplayName("GET /admin/dashboard/stats → 200 OK (AJAX fragment)")
        void dashboardStatsAjax() throws Exception {
            mockMvc.perform(get("/admin/dashboard/stats"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("stats"));
        }
    }

    @Nested
    @DisplayName("Admin Orders")
    class AdminOrders {

        @Test
        @WithMockUser(username = "admin@homemade.com", roles = "ADMIN")
        @DisplayName("GET /admin/orders → 200 OK")
        void orderListPage() throws Exception {
            mockMvc.perform(get("/admin/orders"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("orders"));
        }
    }

    @Nested
    @DisplayName("Admin Products")
    class AdminProducts {

        @Test
        @WithMockUser(username = "admin@homemade.com", roles = "ADMIN")
        @DisplayName("GET /admin/products → 200 OK")
        void productListPage() throws Exception {
            mockMvc.perform(get("/admin/products"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("products"));
        }

        @Test
        @WithMockUser(username = "admin@homemade.com", roles = "ADMIN")
        @DisplayName("GET /admin/products/create → 200 OK")
        void productCreateForm() throws Exception {
            mockMvc.perform(get("/admin/products/create"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("categories"));
        }
    }

    @Nested
    @DisplayName("Admin Inventory")
    class AdminInventory {

        @Test
        @WithMockUser(username = "admin@homemade.com", roles = "ADMIN")
        @DisplayName("GET /admin/inventory → 200 OK")
        void inventoryListPage() throws Exception {
            mockMvc.perform(get("/admin/inventory"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("inventory"));
        }

        @Test
        @WithMockUser(username = "admin@homemade.com", roles = "ADMIN")
        @DisplayName("GET /admin/inventory/alerts/low-stock → 200 OK")
        void lowStockAlertsPage() throws Exception {
            mockMvc.perform(get("/admin/inventory/alerts/low-stock"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("items"));
        }
    }

    @Nested
    @DisplayName("Admin Customers")
    class AdminCustomers {

        @Test
        @WithMockUser(username = "admin@homemade.com", roles = "ADMIN")
        @DisplayName("GET /admin/customers → 200 OK")
        void customerListPage() throws Exception {
            mockMvc.perform(get("/admin/customers"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("customers"));
        }
    }

    @Nested
    @DisplayName("Admin Analytics")
    class AdminAnalytics {

        @Test
        @WithMockUser(username = "admin@homemade.com", roles = "ADMIN")
        @DisplayName("GET /admin/analytics → 302 redirect to /sales")
        void analyticsBaseRedirects() throws Exception {
            mockMvc.perform(get("/admin/analytics"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/admin/analytics/sales"));
        }

        @Test
        @WithMockUser(username = "admin@homemade.com", roles = "ADMIN")
        @DisplayName("GET /admin/analytics/sales → 200 OK")
        void salesReportPage() throws Exception {
            mockMvc.perform(get("/admin/analytics/sales"))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "admin@homemade.com", roles = "ADMIN")
        @DisplayName("GET /admin/analytics/revenue → 200 OK")
        void revenueAnalyticsPage() throws Exception {
            mockMvc.perform(get("/admin/analytics/revenue"))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "admin@homemade.com", roles = "ADMIN")
        @DisplayName("GET /admin/analytics/customers → 200 OK")
        void customerAnalyticsPage() throws Exception {
            mockMvc.perform(get("/admin/analytics/customers"))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "admin@homemade.com", roles = "ADMIN")
        @DisplayName("GET /admin/analytics/products → 200 OK")
        void productPerformancePage() throws Exception {
            mockMvc.perform(get("/admin/analytics/products"))
                    .andExpect(status().isOk());
        }
    }
}
