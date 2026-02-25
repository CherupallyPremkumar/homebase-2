/**
 * Checkout form handling.
 *
 * Supports:
 * - STRIPE: hosted checkout redirect (checkoutUrl is a real URL)
 * - RAZORPAY: Razorpay Checkout modal (checkoutUrl is "razorpay_order_id:<id>")
 */
(function () {
    'use strict';

    const checkoutForm = document.getElementById('checkout-form');
    if (!checkoutForm) return;

    const errorBox = document.getElementById('error-message');
    const submitBtn = document.getElementById('submit-btn');

    function showError(message) {
        if (!errorBox) {
            alert(message);
            return;
        }
        errorBox.textContent = message;
        errorBox.style.display = 'block';
    }

    function setSubmitting(labelHtml) {
        if (!submitBtn) return;
        submitBtn.disabled = true;
        submitBtn.innerHTML = labelHtml;
    }

    function resetSubmitting(defaultLabelHtml) {
        if (!submitBtn) return;
        submitBtn.disabled = false;
        if (defaultLabelHtml) submitBtn.innerHTML = defaultLabelHtml;
    }

    function getConfig() {
        const cfg = (window.__HM_CHECKOUT__ || {});
        const dataset = checkoutForm.dataset || {};

        return {
            checkoutUrl: cfg.checkoutUrl ?? dataset.checkoutUrl ?? '',
            gatewayType: (cfg.gatewayType ?? dataset.gatewayType ?? '').toString(),
            razorpayKeyId: cfg.razorpayKeyId ?? dataset.razorpayKeyId ?? '',
            razorpayOrderId: cfg.razorpayOrderId ?? dataset.razorpayOrderId ?? '',
            orderId: cfg.orderId ?? dataset.orderId ?? '',
            amountPaise: Number(cfg.amountPaise ?? dataset.amountPaise ?? 0),
            currency: (cfg.currency ?? dataset.currency ?? 'INR').toString(),
            customerEmail: cfg.customerEmail ?? dataset.customerEmail ?? '',
            cancelUrl: cfg.cancelUrl ?? dataset.cancelUrl ?? '/checkout/cancel',
            successBaseUrl: cfg.successBaseUrl ?? dataset.successBaseUrl ?? '/checkout/success'
        };
    }

    function parseRazorpayOrderId(checkoutUrl) {
        const prefix = 'razorpay_order_id:';
        if (typeof checkoutUrl !== 'string') return '';
        if (checkoutUrl.startsWith(prefix)) return checkoutUrl.substring(prefix.length);
        return '';
    }

    function startStripeHostedRedirect(cfg) {
        if (!cfg.checkoutUrl) {
            showError('Checkout session could not be initialized. Please try again.');
            return;
        }

        setSubmitting('<i class="fas fa-spinner fa-spin me-2"></i>Redirecting to secure checkout…');
        window.location.href = cfg.checkoutUrl;
    }

    function startRazorpayCheckout(cfg) {
        const razorpayOrderId = cfg.razorpayOrderId || parseRazorpayOrderId(cfg.checkoutUrl);

        if (!cfg.razorpayKeyId) {
            showError('Razorpay is active but no Razorpay key id is configured.');
            return;
        }

        if (!razorpayOrderId) {
            showError('Razorpay checkout could not be initialized (missing Razorpay order id).');
            return;
        }

        if (typeof window.Razorpay !== 'function') {
            showError('Razorpay checkout script failed to load. Please refresh and try again.');
            return;
        }

        // Razorpay expects amount in the smallest unit (paise).
        const options = {
            key: cfg.razorpayKeyId,
            amount: cfg.amountPaise,
            currency: cfg.currency || 'INR',
            name: 'HomeMade',
            description: cfg.orderId ? ('Order ' + cfg.orderId) : 'HomeMade Order',
            order_id: razorpayOrderId,
            prefill: {
                email: cfg.customerEmail || undefined
            },
            notes: {
                app_order_id: cfg.orderId || undefined
            },
            theme: {
                color: '#4a90d9'
            },
            modal: {
                ondismiss: function () {
                    window.location.href = cfg.cancelUrl || '/checkout/cancel';
                }
            },
            handler: function (response) {
                // IMPORTANT: client-side success does not replace webhook verification.
                const params = new URLSearchParams();
                params.set('session_id', razorpayOrderId);
                if (response && response.razorpay_payment_id) {
                    params.set('gateway_transaction_id', response.razorpay_payment_id);
                }
                window.location.href = (cfg.successBaseUrl || '/checkout/success') + '?' + params.toString();
            }
        };

        const rzp = new window.Razorpay(options);
        rzp.on('payment.failed', function (resp) {
            const msg = resp?.error?.description || resp?.error?.reason || 'Payment failed';
            const params = new URLSearchParams();
            params.set('error_message', msg);
            window.location.href = '/checkout/failure?' + params.toString();
        });

        setSubmitting('<i class="fas fa-wallet me-2"></i>Opening Razorpay…');
        rzp.open();

        // If the modal is closed, ondismiss will handle redirect.
        // If the payment fails, we redirect to failure.
        // If the payment succeeds, handler redirects to success.
    }

    checkoutForm.addEventListener('submit', function (event) {
        event.preventDefault();
        if (errorBox) errorBox.style.display = 'none';

        const cfg = getConfig();
        const gateway = (cfg.gatewayType || '').toUpperCase();

        // Preserve existing label for reset when needed.
        const defaultLabel = submitBtn ? submitBtn.innerHTML : null;

        try {
            if (gateway === 'RAZORPAY') {
                startRazorpayCheckout(cfg);
            } else {
                // Default to hosted redirect behavior (Stripe + any future hosted gateways).
                startStripeHostedRedirect(cfg);
            }
        } catch (e) {
            console.error(e);
            showError('Payment could not be started. Please try again.');
            resetSubmitting(defaultLabel);
        }
    });
})();
