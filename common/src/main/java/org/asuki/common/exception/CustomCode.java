package org.asuki.common.exception;

import lombok.Getter;

public enum CustomCode implements ErrorCode {

    // @formatter:off
	SERVICE_TIMEOUT(101),
	UNEXPECTED_ERROR(102),
    VALUE_REQUIRED(201),
    INVALID_FORMAT(202),
    VALUE_TOO_SHORT(203);
	// @formatter:on

    @Getter
    private int number;

    private CustomCode(int number) {
        this.number = number;
    }

}
