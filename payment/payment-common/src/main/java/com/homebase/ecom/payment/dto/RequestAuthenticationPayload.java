package com.homebase.ecom.payment.dto;

/**
 * Payload for requesting 3DS/OTP authentication from gateway.
 */
public class RequestAuthenticationPayload {
    private String comment;
    private String authenticationUrl;
    private String challengeType;

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getAuthenticationUrl() { return authenticationUrl; }
    public void setAuthenticationUrl(String authenticationUrl) { this.authenticationUrl = authenticationUrl; }
    public String getChallengeType() { return challengeType; }
    public void setChallengeType(String challengeType) { this.challengeType = challengeType; }
}
