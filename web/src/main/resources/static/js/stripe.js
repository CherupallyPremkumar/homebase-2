/**
 * Stripe Elements integration
 */
(function() {
    'use strict';

    // Initialize Stripe only if publishable key is available
    const publishableKey = document.querySelector('meta[name="stripe-key"]')?.content;

    if (!publishableKey) {
        console.log('Stripe key not found, skipping initialization');
        return;
    }

    const stripe = Stripe(publishableKey);
    const elements = stripe.elements();

    // Create and mount card element
    const cardElement = elements.create('card', {
        style: {
            base: {
                fontSize: '16px',
                color: '#32325d',
                fontFamily: '"Segoe UI", Roboto, sans-serif',
                '::placeholder': { color: '#aab7c4' }
            },
            invalid: {
                color: '#fa755a',
                iconColor: '#fa755a'
            }
        }
    });

    const cardContainer = document.getElementById('card-element');
    if (cardContainer) {
        cardElement.mount('#card-element');
    }

    // Handle card validation errors
    cardElement.addEventListener('change', function(event) {
        const displayError = document.getElementById('card-errors');
        if (displayError) {
            if (event.error) {
                displayError.textContent = event.error.message;
                displayError.style.display = 'block';
            } else {
                displayError.style.display = 'none';
            }
        }
    });
})();
