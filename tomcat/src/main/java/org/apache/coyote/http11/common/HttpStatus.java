package org.apache.coyote.http11.common;

public enum HttpStatus {

	OK(200, "OK"),
	CREATED(201, "Created"),
	FOUND(302, "Found"),
	NOT_FOUND(404, "Not Found"),
	UNAUTHORIZED(401, "Unauthorized"),
	INTERNAL_SERVER_ERROR(500, "Internal Server Erro"),
	BAD_REQUEST(400, "Bad Request");

	private final int code;
	private final String status;

	HttpStatus(int code, String status) {
		this.code = code;
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public String getStatus() {
		return status;
	}
}
