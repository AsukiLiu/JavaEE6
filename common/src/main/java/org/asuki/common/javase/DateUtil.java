package org.asuki.common.javase;

public final class DateUtil {

    private DateUtil() {
        // Reflectionを防ぐ
        throw new Error("インスタンス化できない");
    }

    public static long createNow() {
        return System.currentTimeMillis() / 1000L * 1000L;
    }

}
