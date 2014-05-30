package org.asuki.common.javase;

import static org.asuki.common.exception.CommonError.CANNOT_BE_INSTANCED;

import org.asuki.common.exception.CommonError;

public final class DateUtil {

    private DateUtil() {
        // Reflectionを防ぐ
        throw new CommonError(CANNOT_BE_INSTANCED);
    }

    public static long createNow() {
        return System.currentTimeMillis() / 1000L * 1000L;
    }

}
