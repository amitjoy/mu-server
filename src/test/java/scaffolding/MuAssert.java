package scaffolding;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MuAssert {


    public static void assertNotTimedOut(String message, CountDownLatch latch) {
        assertNotTimedOut(message, latch, 30, TimeUnit.SECONDS);
    }

    public static void assertNotTimedOut(String message, CountDownLatch latch, int timeout, TimeUnit unit) {
        try {
            boolean completed = latch.await(timeout, unit);
            assertThat("Timed out: " + message, completed, is(true));
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted");
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted", e);
        }
    }

}
