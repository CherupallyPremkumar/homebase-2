package com.homebase.ecom.cart.configuration;

public class CartConfig {

    private Policies policies = new Policies();

    public Policies getPolicies() {
        return policies;
    }

    public void setPolicies(Policies policies) {
        this.policies = policies;
    }

    public static class Policies {
        private Limits limits = new Limits();
        private Currency currency = new Currency();
        private Expiration expiration = new Expiration();
        private Inventory inventory = new Inventory();
        private MultiSeller multi_seller = new MultiSeller();

        public Limits getLimits() {
            return limits;
        }

        public void setLimits(Limits limits) {
            this.limits = limits;
        }

        public Currency getCurrency() {
            return currency;
        }

        public void setCurrency(Currency currency) {
            this.currency = currency;
        }

        public Expiration getExpiration() {
            return expiration;
        }

        public void setExpiration(Expiration expiration) {
            this.expiration = expiration;
        }

        public Inventory getInventory() {
            return inventory;
        }

        public void setInventory(Inventory inventory) {
            this.inventory = inventory;
        }

        public MultiSeller getMulti_seller() {
            return multi_seller;
        }

        public void setMulti_seller(MultiSeller multi_seller) {
            this.multi_seller = multi_seller;
        }
    }

    public static class Limits {
        private int maxItemsPerCart = 50;
        private int maxQuantityPerItem = 10;

        public int getMaxItemsPerCart() {
            return maxItemsPerCart;
        }

        public void setMaxItemsPerCart(int maxItemsPerCart) {
            this.maxItemsPerCart = maxItemsPerCart;
        }

        public int getMaxQuantityPerItem() {
            return maxQuantityPerItem;
        }

        public void setMaxQuantityPerItem(int maxQuantityPerItem) {
            this.maxQuantityPerItem = maxQuantityPerItem;
        }
    }

    public static class Currency {
        private boolean enforceSingle = true;

        public boolean isEnforceSingle() {
            return enforceSingle;
        }

        public void setEnforceSingle(boolean enforceSingle) {
            this.enforceSingle = enforceSingle;
        }
    }

    public static class Expiration {
        private int idleDays = 10;
        private int stuckCheckoutMinutes = 15;
        private int stuckPaymentMinutes = 60;

        public int getIdleDays() {
            return idleDays;
        }

        public void setIdleDays(int idleDays) {
            this.idleDays = idleDays;
        }

        public int getStuckCheckoutMinutes() {
            return stuckCheckoutMinutes;
        }

        public void setStuckCheckoutMinutes(int stuckCheckoutMinutes) {
            this.stuckCheckoutMinutes = stuckCheckoutMinutes;
        }

        public int getStuckPaymentMinutes() {
            return stuckPaymentMinutes;
        }

        public void setStuckPaymentMinutes(int stuckPaymentMinutes) {
            this.stuckPaymentMinutes = stuckPaymentMinutes;
        }
    }

    public static class Inventory {
        private String reservationType = "soft";

        public String getReservationType() {
            return reservationType;
        }

        public void setReservationType(String reservationType) {
            this.reservationType = reservationType;
        }
    }

    public static class MultiSeller {
        private boolean allowed = true;

        public boolean isAllowed() {
            return allowed;
        }

        public void setAllowed(boolean allowed) {
            this.allowed = allowed;
        }
    }
}
