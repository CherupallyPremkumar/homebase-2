package com.homebase.ecom.payment.dto;

/**
 * Payload for completing 3DS/OTP authentication.
 */
public class CompleteAuthenticationPayload {
    private String comment;
    private String authenticationToken;

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getAuthenticationToken() { return authenticationToken; }
    public void setAuthenticationToken(String authenticationToken) { this.authenticationToken = authenticationToken; }
}
