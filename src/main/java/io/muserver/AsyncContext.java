package io.muserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @deprecated This interface is no longer used. Instead call {@link MuRequest#handleAsync()} from a standard Mu Handler.
 */
@Deprecated
public class AsyncContext {
    private static final Logger log = LoggerFactory.getLogger(AsyncContext.class);
    public final MuRequest request;
    public final MuResponse response;
    private final Runnable completedCallback;
    public Object state;
    GrowableByteBufferInputStream requestBody;
    private AtomicBoolean completed = new AtomicBoolean(false);

    AsyncContext(MuRequest request, MuResponse response, Runnable completedCallback) {
        this.request = request;
        this.response = response;
        this.completedCallback = completedCallback;
    }

    public Future<Void> complete(boolean forceDisconnect) {
        boolean wasCompleted = this.completed.getAndSet(true);
        if (wasCompleted) {
            log.debug("AsyncContext.complete called twice for " + request);
            return null;
        } else {
            Future<Void> complete = ((NettyResponseAdaptor) response)
                .complete(forceDisconnect);
            completedCallback.run();
            return complete;
        }
    }

    boolean isComplete() {
        return completed.get();
    }

    void onCancelled(boolean forceDisconnect) {
        boolean wasCompleted = isComplete();
        ((NettyRequestAdapter) request).onCancelled(wasCompleted);
        ((NettyResponseAdaptor) response).onCancelled();
        if (!wasCompleted) {
            complete(forceDisconnect);
        }
    }
}
