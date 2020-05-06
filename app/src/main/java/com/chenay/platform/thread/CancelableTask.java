package com.chenay.platform.thread;

import java.util.Collection;

public interface CancelableTask {

    public void addListener(Collection<CancelableTask> cancelableTaskCollection);

    public boolean cancel(boolean mayInterruptIfRunning);

    public void remove();
}
