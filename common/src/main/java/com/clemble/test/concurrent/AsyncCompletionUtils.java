package com.clemble.test.concurrent;

public class AsyncCompletionUtils {

    public static <T> T get(Get<T> get, long timeout) {
        long maxTimeout = System.currentTimeMillis() + timeout;
        do {
            try {
                return get.get();
            } catch (Throwable throwable) {
                if (maxTimeout < System.currentTimeMillis())
                    throw new AssertionError("Failed to get value");
                else
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new AssertionError("Failed to get value");
                    }
            }
        } while (maxTimeout > System.currentTimeMillis());
        throw new AssertionError("Failed to get value");

    }

    public static void check(Check check, long timeout) {
        long maxTimeout = System.currentTimeMillis() + timeout;
        do {
            try {
                if(check.check())
                    return;
            } catch (Throwable throwable) {
                if (maxTimeout < System.currentTimeMillis())
                    throw new AssertionError("Check failed");
                else
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new AssertionError("Check failed");
                    }
            }
        } while (maxTimeout > System.currentTimeMillis());
        throw new AssertionError("Check failed");
    }

    public static <T> void equals(Get<T> a, Get<T> b){
        equals(a, b, 5000);
    }

    public static <T> void equals(Get<T> a, Get<T> b, long timeout) {
        long maxTimeout = System.currentTimeMillis() + timeout;
        do {
            try {
                if(a.get().equals(b.get()))
                    return;
            } catch (Throwable throwable) {
                if (maxTimeout < System.currentTimeMillis()) {
                    throw new AssertionError("Expected " + a.get() + " Actual " + b.get());
                } else {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new AssertionError("Expected " + a.get() + " Actual " + b.get());
                    }
                }
            }
        } while (maxTimeout > System.currentTimeMillis());
        throw new AssertionError("Expected " + a.get() + " Actual " + b.get());
    }

}
