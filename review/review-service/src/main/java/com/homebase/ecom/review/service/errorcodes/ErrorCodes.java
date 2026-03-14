package com.homebase.ecom.review.service.errorcodes;

public enum ErrorCodes {
	SOME_ERROR(80000);

	private final int subError;
	private ErrorCodes(int subError) {
		this.subError = subError;
	}

	public int getSubError() {
		return this.subError;
	}
}
