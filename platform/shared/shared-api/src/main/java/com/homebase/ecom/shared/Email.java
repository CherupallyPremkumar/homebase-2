package com.homebase.ecom.shared;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class Email implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Column(name = "email", nullable = false)
    private String value;

    protected Email() {} // JPA

    public Email(String value) {
        if (value == null || !VALID_EMAIL_ADDRESS_REGEX.matcher(value).find()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.value = value;
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
