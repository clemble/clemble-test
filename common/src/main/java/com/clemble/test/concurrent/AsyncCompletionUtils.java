package com.clemble.test.concurrent;

public class AsyncCompletionUtils {

    public static <T> T get(Get<T> get) {
        return get(get, 5000);
    }

    public static <T> T get(Get<T> get, long timeout) {
        return get(get, timeout, 300);
    }

    public static <T> T get(Get<T> get, long timeout, long getDelay) {
        long maxTimeout = System.currentTimeMillis() + timeout;
        do {
            try {
                return get.get();
            } catch (Throwable throwable) {
                if (maxTimeout < System.currentTimeMillis())
                    throw new AssertionError("Failed to get value");
                else
                    try {
                        Thread.sleep(getDelay);
                    } catch (InterruptedException e) {
                        throw new AssertionError("Failed to get value");
                    }
            }
        } while (maxTimeout > System.currentTimeMillis());
        throw new AssertionError("Failed to get value");

    }

    public static void check(Check check) {
        check(check, 500);
    }

    public static void check(Check check, long timeout) {
        check(check, timeout, 300);
    }

    public static void check(Check check, long timeout, long checkDelay) {
        long maxTimeout = System.currentTimeMillis() + timeout;
        do {
            try {
                if(check.check())
                    return;
                else
                    try {
                        Thread.sleep(checkDelay);
                    } catch (InterruptedException e) {
                        throw new AssertionError("Check failed");
                    }
            } catch (Throwable throwable) {
                if (maxTimeout < System.currentTimeMillis())
                    throw new AssertionError("Check failed");
                else
                    try {
                        Thread.sleep(checkDelay);
                    } catch (InterruptedException e) {
                        throw new AssertionError("Check failed");
                    }
            }
        } while (maxTimeout > System.currentTimeMillis());
        throw new AssertionError("Check failed");
    }

}
