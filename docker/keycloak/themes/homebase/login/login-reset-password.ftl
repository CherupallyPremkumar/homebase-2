<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Reset Password | HomeBase</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${url.resourcesPath}/css/styles.css">
</head>
<body>
    <div class="page-layout">
        <div class="form-side">
            <div class="form-container">
                <h1 class="brand">HomeBase</h1>
                <h2 class="form-title">Reset your password</h2>
                <p class="form-subtitle">Enter your email and we'll send you a reset link</p>

                <#if message?has_content>
                    <div class="alert alert-${message.type}">
                        ${kcSanitize(message.summary)?no_esc}
                    </div>
                </#if>

                <form action="${url.loginAction}" method="post">
                    <div class="form-group">
                        <label for="username">Email address</label>
                        <input type="text" id="username" name="username"
                               value="${(auth.attemptedUsername!'')}"
                               autocomplete="username" autofocus
                               placeholder="you@example.com">
                    </div>

                    <button type="submit" class="btn-primary">Send reset link</button>
                </form>

                <p class="register-link">
                    Remember your password?
                    <a href="${url.loginUrl}">Sign in</a>
                </p>
            </div>
        </div>

        <div class="brand-side">
            <div class="brand-content">
                <h2 class="brand-title">Shop smarter with HomeBase</h2>
                <p class="brand-subtitle">India's trusted marketplace for everything you need. Fast delivery, easy returns, secure payments.</p>
                <div class="features-grid">
                    <div class="feature">
                        <span class="feature-icon">&#x2713;</span>
                        <div>
                            <div class="feature-title">Secure</div>
                            <div class="feature-desc">256-bit encryption</div>
                        </div>
                    </div>
                    <div class="feature">
                        <span class="feature-icon">&#x1F69A;</span>
                        <div>
                            <div class="feature-title">Fast Delivery</div>
                            <div class="feature-desc">2-5 business days</div>
                        </div>
                    </div>
                    <div class="feature">
                        <span class="feature-icon">&#x1F4B3;</span>
                        <div>
                            <div class="feature-title">Easy Payments</div>
                            <div class="feature-desc">UPI, Cards, COD</div>
                        </div>
                    </div>
                    <div class="feature">
                        <span class="feature-icon">&#x1F4AC;</span>
                        <div>
                            <div class="feature-title">24/7 Support</div>
                            <div class="feature-desc">Always here to help</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
