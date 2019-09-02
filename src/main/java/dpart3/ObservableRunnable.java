package dpart3;

/**
 * 可以被监控的类，事件源
 *
 * @author: mahao
 * @date: 2019/8/26
 */
public abstract class ObservableRunnable implements Runnable {

    private Listener listener;

    public ObservableRunnable(Listener listener) {
        this.listener = listener;
    }

    public void notifyChange(RunnableEvent event) {
        listener.onEvent(event);
    }

    public static class RunnableEvent {
        private RunnableStatue statue;
        private Thread thread;
        private Throwable throwable;

        public RunnableEvent(RunnableStatue statue, Thread thread, Throwable throwable) {
            this.statue = statue;
            this.thread = thread;
            this.throwable = throwable;
        }

        public RunnableStatue getStatue() {
            return statue;
        }

        public void setStatue(RunnableStatue statue) {
            this.statue = statue;
        }

        public Thread getThread() {
            return thread;
        }

        public void setThread(Thread thread) {
            this.thread = thread;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public void setThrowable(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public String toString() {
            return "RunnableEvent{" +
                    "statue=" + statue +
                    ", thread=" + thread +
                    ", throwable=" + throwable +
                    '}';
        }
    }

    public enum RunnableStatue {
        CREATE, RUNNING, DONE, ERROR;
    }
}
