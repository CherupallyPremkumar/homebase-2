<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Sign In | HomeBase</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${url.resourcesPath}/css/styles.css">
</head>
<body>
    <div class="page-layout">
        <!-- Left: Login Form -->
        <div class="form-side">
            <div class="form-container">
                <h1 class="brand">HomeBase</h1>
                <h2 class="form-title">Welcome back</h2>
                <p class="form-subtitle">Sign in to your HomeBase account</p>

                <#if message?has_content>
                    <div class="alert alert-${message.type}">
                        ${kcSanitize(message.summary)?no_esc}
                    </div>
                </#if>

                <#if realm.password>
                    <form action="${url.loginAction}" method="post">
                        <div class="form-group">
                            <label for="username">Email address</label>
                            <#if usernameEditDisabled??>
                                <input type="text" id="username" name="username" value="${(login.username!'')}"
                                       disabled autocomplete="username" placeholder="you@example.com">
                            <#else>
                                <input type="text" id="username" name="username" value="${(login.username!'')}"
                                       autocomplete="username" autofocus placeholder="you@example.com">
                            </#if>
                        </div>

                        <div class="form-group">
                            <label for="password">
                                Password
                                <#if realm.resetPasswordAllowed>
                                    <a href="${url.loginResetCredentialsUrl}" class="forgot-link">Forgot password?</a>
                                </#if>
                            </label>
                            <input type="password" id="password" name="password"
                                   autocomplete="current-password" placeholder="Enter your password">
                        </div>

                        <#if realm.rememberMe && !usernameEditDisabled??>
                            <div class="form-group checkbox-group">
                                <input type="checkbox" id="rememberMe" name="rememberMe"
                                       <#if login.rememberMe??>checked</#if>>
                                <label for="rememberMe">Remember me for 30 days</label>
                            </div>
                        </#if>

                        <input type="hidden" id="id-hidden-input" name="credentialId" />
                        <button type="submit" class="btn-primary">Sign in</button>
                    </form>
                </#if>

                <#if realm.password && realm.registrationAllowed && !registrationDisabled??>
                    <div class="register-divider">
                        <span>New to HomeBase?</span>
                    </div>
                    <a href="${url.registrationUrl}" class="btn-outline">Create an account</a>
                </#if>
            </div>
        </div>

        <!-- Right: Branding Panel -->
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
