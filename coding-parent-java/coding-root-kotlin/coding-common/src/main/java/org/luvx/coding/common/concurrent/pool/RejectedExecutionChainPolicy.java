package org.luvx.coding.common.concurrent.pool;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class RejectedExecutionChainPolicy implements RejectedExecutionHandler {
    private final RejectedExecutionHandler[] handlerChain;

    private RejectedExecutionChainPolicy(RejectedExecutionHandler[] handlerChain) {
        this.handlerChain = requireNonNull(handlerChain, "handlerChain must not be null");
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        for (RejectedExecutionHandler handler : handlerChain) {
            handler.rejectedExecution(r, executor);
        }
    }

    public static RejectedExecutionHandler build(List<RejectedExecutionHandler> chain) {
        requireNonNull(chain, "handlerChain must not be null");
        RejectedExecutionHandler[] handlerChain = chain.toArray(new RejectedExecutionHandler[0]);
        return new RejectedExecutionChainPolicy(handlerChain);
    }
}