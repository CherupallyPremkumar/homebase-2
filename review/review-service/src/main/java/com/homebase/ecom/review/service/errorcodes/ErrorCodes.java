package com.homebase.ecom.review.service.errorcodes;

public enum ErrorCodes {
	RATING_OUT_OF_RANGE(80001),
	REVIEW_TOO_SHORT(80002),
	REVIEW_TOO_LONG(80003),
	TOO_MANY_IMAGES(80004),
	PROFANITY_DETECTED(80005),
	REJECTION_REASON_REQUIRED(80006),
	EDIT_INSTRUCTIONS_REQUIRED(80007),
	REVIEW_NOT_FOUND(80008),
	INVALID_STATE_TRANSITION(80009);

	private final int subError;
	private ErrorCodes(int subError) {
		this.subError = subError;
	}

	public int getSubError() {
		return this.subError;
	}
}
