package com.clemble.test.util;

public class PossibleAssert {

    final private static long DEFAULT_TIMEOUT = 60000;
    final private static long DEFAULT_PERIOD = 1000;

    public static void assertHappens(Runnable check){
        assertHappens(check, DEFAULT_TIMEOUT);
    }

    public static void assertHappens(Runnable check, long timeout) {
        assertHappens(check, DEFAULT_TIMEOUT, DEFAULT_PERIOD);
    }

    public static void assertHappens(Runnable check, long timeout, long period) {
        Throwable exc = null;
        long endTime = System.currentTimeMillis() + timeout;
        do {
            try {
                if (exc != null)
                    Thread.sleep(period);
                exc = null;
                check.run();
            } catch (Throwable error) {
                exc = error;
            }
        } while(endTime > System.currentTimeMillis() && exc != null);
    }

}
