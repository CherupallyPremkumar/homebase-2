package com.homebase.ecom.build.bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;
import org.chenile.cucumber.CukesContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * BDD step definitions for end-to-end shopping flow.
 *
 * Chenile provides built-in REST steps via RestCukesSteps:
 * - "I POST/GET/PATCH/PUT/DELETE a REST request to URL ... with payload"
 * - "the http status code is {int}"
 * - "the REST response key {string} is {string}"
 * - "store {string} from response to {string}"
 * - "the REST expression {string} size is {int}"
 *
 * Custom steps below handle domain-specific setup and assertions.
 */
@CucumberContextConfiguration
@SpringBootTest(
    classes = SpringTestConfig.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("unittest")
public class ShoppingFlowSteps {

    @Given("the system is running with tenant {string}")
    public void systemRunningWithTenant(String tenant) {
        // DefaultTenantInterceptor handles this automatically
    }

    @Given("a product {string} exists with price {double} and status {string}")
    public void productExistsWithPriceAndStatus(String name, double price, String status) {
        // TODO: Seed test product via repository
        // Store productId in CukesContext for later use
        // CukesContext.getObject("productId", productId);
    }

    @Given("inventory for {string} has quantity {int}")
    public void inventoryForProductHasQuantity(String productName, int quantity) {
        // TODO: Seed inventory record via repository
    }

    @Given("a cart exists with {string} quantity {int}")
    public void cartExistsWithProduct(String productName, int quantity) {
        // TODO: Create cart via REST API, add item, store cartId and itemId
    }

    @Given("a promo code {string} exists with {int} percent discount")
    public void promoCodeExistsWithDiscount(String code, int percentage) {
        // TODO: Seed promo code via repository
    }

    @Given("an order exists for {string} with status {string}")
    public void orderExistsWithStatus(String productName, String status) {
        // TODO: Create order in given state, store orderId
    }

    @Given("{int} orders exist for the current user")
    public void ordersExistForCurrentUser(int count) {
        // TODO: Seed multiple orders via repository
    }

    @Given("a cart with {string} quantity {int}")
    public void cartWithProductQuantity(String productName, int quantity) {
        cartExistsWithProduct(productName, quantity);
    }

    @Given("an order in CREATED state")
    public void orderInCreatedState() {
        // TODO: Create order in CREATED state
    }

    @Given("an order with reserved inventory")
    public void orderWithReservedInventory() {
        // TODO: Create order + inventory reservation
    }

    // --- Event chain verification steps ---

    @When("a product is published via event")
    public void productPublishedViaEvent() {
        // TODO: Trigger product publish STM event
    }

    @When("checkout is initiated")
    public void checkoutInitiated() {
        // TODO: Trigger cart checkout via PATCH
    }

    @When("payment succeeded event is received")
    public void paymentSucceededEventReceived() {
        // TODO: Publish PaymentSucceededEvent via KafkaTemplate
    }

    @When("order is cancelled")
    public void orderCancelled() {
        // TODO: Trigger cancelOrder STM event via PATCH
    }

    @Then("inventory record is created with quantity {int}")
    public void inventoryRecordCreatedWithQuantity(int quantity) {
        // TODO: Query inventory and assert quantity
    }

    @Then("inventory reserved quantity increases by {int}")
    public void inventoryReservedQuantityIncreasesBy(int amount) {
        // TODO: Query inventory and assert reserved quantity
    }

    @Then("order transitions to PAYMENT_CONFIRMED")
    public void orderTransitionsToPaymentConfirmed() {
        // TODO: Query order and assert status
    }

    @Then("a shipment record is created")
    public void shipmentRecordCreated() {
        // TODO: Query shipments by orderId
    }

    @Then("inventory reserved quantity decreases")
    public void inventoryReservedQuantityDecreases() {
        // TODO: Assert reserved quantity went down
    }

    @Then("inventory available quantity increases")
    public void inventoryAvailableQuantityIncreases() {
        // TODO: Assert available quantity went up
    }
}
