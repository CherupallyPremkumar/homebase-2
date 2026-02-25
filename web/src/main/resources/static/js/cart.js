/**
 * Cart AJAX operations
 */
(function () {
    'use strict';

    window.CartManager = {
        addItem: async function (productId, quantity) {
            try {
                const response = await fetch('/cart/add', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: `productId=${productId}&quantity=${quantity || 1}`
                });

                if (response.ok) {
                    const data = await response.json();
                    this.updateCartBadge(data.cartCount);
                    this.showNotification(data.message || 'Item added to cart!');
                    return data;
                }
            } catch (error) {
                console.error('Add to cart error:', error);
                this.showNotification('Failed to add item', 'error');
            }
        },

        removeItem: async function (productId) {
            try {
                const response = await fetch(`/cart/remove/${productId}`, { method: 'DELETE' });
                if (response.ok) {
                    location.reload();
                }
            } catch (error) {
                console.error('Remove from cart error:', error);
            }
        },

        updateCartBadge: function (count) {
            const badge = document.querySelector('.cart-badge');
            if (badge) {
                badge.textContent = count;
            }
        },

        showNotification: function (message, type) {
            const toast = document.getElementById('cartToast');
            if (toast) {
                document.getElementById('toastMessage').textContent = message;
                const bsToast = new bootstrap.Toast(toast);
                bsToast.show();
            } else {
                console.log('Notification:', message);
            }
        }
    };
})();
