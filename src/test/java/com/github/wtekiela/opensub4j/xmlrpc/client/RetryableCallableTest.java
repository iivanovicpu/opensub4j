package com.github.wtekiela.opensub4j.xmlrpc.client;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RetryableCallableTest {

    public static final int TEST_INTERVAL_MILLIS = 2;

    @Test
    void testCallOKIfExceptionsLessThenMaxAttempts() throws Exception {
        // given
        int attempts = 3;
        Callable<Object> testTask = new TestCallable(attempts-1);
        RetryableCallable objectUnderTest = new RetryableCallable(attempts, TEST_INTERVAL_MILLIS, testTask);

        // when
        Object result = objectUnderTest.call();

        // then
        assertEquals(TestCallable.SUCCESS, result);
    }

    @Test
    void testCallFailsIfExceptionsEqualMaxAttempts() {
        // given
        int attempts = 3;
        Callable<Object> testTask = new TestCallable(attempts);
        RetryableCallable objectUnderTest = new RetryableCallable(attempts, TEST_INTERVAL_MILLIS, testTask);

        // when
        assertThrows(Exception.class, () -> objectUnderTest.call());

        // then
        // exception should be thrown
    }

    private static class TestCallable implements Callable<Object> {

        public static final Object SUCCESS = new Object();

        private int exceptionsToThrow;

        public TestCallable(int exceptionsToThrow) {
            this.exceptionsToThrow = exceptionsToThrow;
        }

        @Override
        public Object call() throws Exception {
            if (exceptionsToThrow > 0) {
                exceptionsToThrow--;
                throw new Exception();
            }
            return SUCCESS;
        }
    }

}