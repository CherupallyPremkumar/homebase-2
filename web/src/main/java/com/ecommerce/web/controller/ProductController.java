package com.ecommerce.web.controller;

import com.ecommerce.cart.service.CartService;
import com.ecommerce.product.domain.Offer;
import com.ecommerce.product.domain.Product;
import com.ecommerce.product.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;
    private final CartService cartService;

    public ProductController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    /**
     * Resolves cart key: user email if logged in, session ID for guests.
     */
    private String resolveCartKey(Authentication auth, HttpSession session) {
        if (auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return "session:" + session.getId();
    }

    @GetMapping
    public String listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Authentication auth,
            HttpSession session,
            Model model) {

        Page<Product> products = productService.getAllProducts(PageRequest.of(page, size));

        Map<String, Offer> offers = products.getContent().stream()
                .map(p -> productService.getMainOffer(p.getId()))
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .collect(Collectors.toMap(Offer::getProductId, o -> o));

        model.addAttribute("products", products.getContent());
        model.addAttribute("offers", offers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalItems", products.getTotalElements());

        // Cart count is always available (guest or logged-in)
        String cartKey = resolveCartKey(auth, session);
        model.addAttribute("cartCount", cartService.getCart(cartKey).getItemCount());

        return "products/list";
    }

    @GetMapping("/{id}")
    public String productDetail(@PathVariable String id, Authentication auth,
            HttpSession session, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));

        Offer offer = productService.getMainOffer(id)
                .orElseThrow(() -> new RuntimeException("No active offer for product: " + id));

        model.addAttribute("product", product);
        model.addAttribute("offer", offer);

        String cartKey = resolveCartKey(auth, session);
        model.addAttribute("cartCount", cartService.getCart(cartKey).getItemCount());

        return "products/detail";
    }

    @PostMapping("/{id}/add-to-cart")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(
            @PathVariable String id,
            @RequestParam(defaultValue = "1") Integer quantity,
            Authentication auth,
            HttpSession session) {

        // No login required — use session-based cart for guests
        String cartKey = resolveCartKey(auth, session);
        cartService.addItem(cartKey, id, quantity);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Product added to cart");
        response.put("cartCount", cartService.getCart(cartKey).getItemCount());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String query) {
        List<Product> products = productService.searchProducts(query);
        return ResponseEntity.ok(products);
    }
}
