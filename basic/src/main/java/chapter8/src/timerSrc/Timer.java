package chapter8.src.timerSrc;


import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 运行定时任务的类，里面包括定时规则和执行线程
 */
public class Timer {

    private final TaskQueue queue = new TaskQueue();

    final TimerThread thread = new TimerThread(queue);

    private final Object threadReaper = new Object() {
        protected void finalize() throws Throwable {
            synchronized (queue) {
                thread.newTasksMayBeScheduled = false;
                queue.notify(); // In case queue is empty.
            }
        }
    };


    private final static AtomicInteger nextSerialNumber = new AtomicInteger(0);

    private static int serialNumber() {
        return nextSerialNumber.getAndIncrement();
    }

    public Timer() {
        this("Timer-" + serialNumber());
    }

    public Timer(boolean isDaemon) {
        this("Timer-" + serialNumber(), isDaemon);
    }

    //创建timer，并启动工作线程
    public Timer(String name) {
        thread.setName(name);
        thread.start();
    }

    public Timer(String name, boolean isDaemon) {
        thread.setName(name);
        thread.setDaemon(isDaemon);
        thread.start();
    }


    public void schedule(TimerTask task, long delay) {
        if (delay < 0)
            throw new IllegalArgumentException("Negative delay.");
        sched(task, System.currentTimeMillis() + delay, 0);
    }

    /**
     * Schedules the specified task for execution at the specified time.  If
     * the time is in the past, the task is scheduled for immediate execution.
     *
     * @param task task to be scheduled.
     * @param time time at which task is to be executed.
     * @throws IllegalArgumentException if <tt>time.getTime()</tt> is negative.
     * @throws IllegalStateException    if task was already scheduled or
     *                                  cancelled, timer was cancelled, or timer thread terminated.
     * @throws NullPointerException     if {@code task} or {@code time} is null
     */
    public void schedule(TimerTask task, Date time) {
        sched(task, time.getTime(), 0);
    }

    /**
     * Schedules the specified task for repeated <i>fixed-delay execution</i>,
     * beginning after the specified delay.  Subsequent executions take place
     * at approximately regular intervals separated by the specified period.
     *
     * <p>In fixed-delay execution, each execution is scheduled relative to
     * the actual execution time of the previous execution.  If an execution
     * is delayed for any reason (such as garbage collection or other
     * background activity), subsequent executions will be delayed as well.
     * In the long run, the frequency of execution will generally be slightly
     * lower than the reciprocal of the specified period (assuming the system
     * clock underlying <tt>Object.wait(long)</tt> is accurate).
     *
     * <p>Fixed-delay execution is appropriate for recurring activities
     * that require "smoothness."  In other words, it is appropriate for
     * activities where it is more important to keep the frequency accurate
     * in the short run than in the long run.  This includes most animation
     * tasks, such as blinking a cursor at regular intervals.  It also includes
     * tasks wherein regular activity is performed in response to human
     * input, such as automatically repeating a character as long as a key
     * is held down.
     *
     * @param task   task to be scheduled.
     * @param delay  delay in milliseconds before task is to be executed.
     * @param period time in milliseconds between successive task executions.
     * @throws IllegalArgumentException if {@code delay < 0}, or
     *                                  {@code delay + System.currentTimeMillis() < 0}, or
     *                                  {@code period <= 0}
     * @throws IllegalStateException    if task was already scheduled or
     *                                  cancelled, timer was cancelled, or timer thread terminated.
     * @throws NullPointerException     if {@code task} is null
     */
    public void schedule(TimerTask task, long delay, long period) {
        if (delay < 0)
            throw new IllegalArgumentException("Negative delay.");
        if (period <= 0)
            throw new IllegalArgumentException("Non-positive period.");
        sched(task, System.currentTimeMillis() + delay, -period);
    }

    /**
     * Schedules the specified task for repeated <i>fixed-delay execution</i>,
     * beginning at the specified time. Subsequent executions take place at
     * approximately regular intervals, separated by the specified period.
     *
     * <p>In fixed-delay execution, each execution is scheduled relative to
     * the actual execution time of the previous execution.  If an execution
     * is delayed for any reason (such as garbage collection or other
     * background activity), subsequent executions will be delayed as well.
     * In the long run, the frequency of execution will generally be slightly
     * lower than the reciprocal of the specified period (assuming the system
     * clock underlying <tt>Object.wait(long)</tt> is accurate).  As a
     * consequence of the above, if the scheduled first time is in the past,
     * it is scheduled for immediate execution.
     *
     * <p>Fixed-delay execution is appropriate for recurring activities
     * that require "smoothness."  In other words, it is appropriate for
     * activities where it is more important to keep the frequency accurate
     * in the short run than in the long run.  This includes most animation
     * tasks, such as blinking a cursor at regular intervals.  It also includes
     * tasks wherein regular activity is performed in response to human
     * input, such as automatically repeating a character as long as a key
     * is held down.
     *
     * @param task      task to be scheduled.
     * @param firstTime First time at which task is to be executed.
     * @param period    time in milliseconds between successive task executions.
     * @throws IllegalArgumentException if {@code firstTime.getTime() < 0}, or
     *                                  {@code period <= 0}
     * @throws IllegalStateException    if task was already scheduled or
     *                                  cancelled, timer was cancelled, or timer thread terminated.
     * @throws NullPointerException     if {@code task} or {@code firstTime} is null
     */
    public void schedule(TimerTask task, Date firstTime, long period) {
        if (period <= 0)
            throw new IllegalArgumentException("Non-positive period.");
        sched(task, firstTime.getTime(), -period);
    }

    /**
     * Schedules the specified task for repeated <i>fixed-rate execution</i>,
     * beginning after the specified delay.  Subsequent executions take place
     * at approximately regular intervals, separated by the specified period.
     *
     * <p>In fixed-rate execution, each execution is scheduled relative to the
     * scheduled execution time of the initial execution.  If an execution is
     * delayed for any reason (such as garbage collection or other background
     * activity), two or more executions will occur in rapid succession to
     * "catch up."  In the long run, the frequency of execution will be
     * exactly the reciprocal of the specified period (assuming the system
     * clock underlying <tt>Object.wait(long)</tt> is accurate).
     *
     * <p>Fixed-rate execution is appropriate for recurring activities that
     * are sensitive to <i>absolute</i> time, such as ringing a chime every
     * hour on the hour, or running scheduled maintenance every day at a
     * particular time.  It is also appropriate for recurring activities
     * where the total time to perform a fixed number of executions is
     * important, such as a countdown timer that ticks once every second for
     * ten seconds.  Finally, fixed-rate execution is appropriate for
     * scheduling multiple repeating timer tasks that must remain synchronized
     * with respect to one another.
     *
     * @param task   task to be scheduled.
     * @param delay  delay in milliseconds before task is to be executed.
     * @param period time in milliseconds between successive task executions.
     * @throws IllegalArgumentException if {@code delay < 0}, or
     *                                  {@code delay + System.currentTimeMillis() < 0}, or
     *                                  {@code period <= 0}
     * @throws IllegalStateException    if task was already scheduled or
     *                                  cancelled, timer was cancelled, or timer thread terminated.
     * @throws NullPointerException     if {@code task} is null
     */
    public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
        if (delay < 0)
            throw new IllegalArgumentException("Negative delay.");
        if (period <= 0)
            throw new IllegalArgumentException("Non-positive period.");
        sched(task, System.currentTimeMillis() + delay, period);
    }

    /**
     * Schedules the specified task for repeated <i>fixed-rate execution</i>,
     * beginning at the specified time. Subsequent executions take place at
     * approximately regular intervals, separated by the specified period.
     *
     * <p>In fixed-rate execution, each execution is scheduled relative to the
     * scheduled execution time of the initial execution.  If an execution is
     * delayed for any reason (such as garbage collection or other background
     * activity), two or more executions will occur in rapid succession to
     * "catch up."  In the long run, the frequency of execution will be
     * exactly the reciprocal of the specified period (assuming the system
     * clock underlying <tt>Object.wait(long)</tt> is accurate).  As a
     * consequence of the above, if the scheduled first time is in the past,
     * then any "missed" executions will be scheduled for immediate "catch up"
     * execution.
     *
     * <p>Fixed-rate execution is appropriate for recurring activities that
     * are sensitive to <i>absolute</i> time, such as ringing a chime every
     * hour on the hour, or running scheduled maintenance every day at a
     * particular time.  It is also appropriate for recurring activities
     * where the total time to perform a fixed number of executions is
     * important, such as a countdown timer that ticks once every second for
     * ten seconds.  Finally, fixed-rate execution is appropriate for
     * scheduling multiple repeating timer tasks that must remain synchronized
     * with respect to one another.
     *
     * @param task      task to be scheduled.
     * @param firstTime First time at which task is to be executed.
     * @param period    time in milliseconds between successive task executions.
     * @throws IllegalArgumentException if {@code firstTime.getTime() < 0} or
     *                                  {@code period <= 0}
     * @throws IllegalStateException    if task was already scheduled or
     *                                  cancelled, timer was cancelled, or timer thread terminated.
     * @throws NullPointerException     if {@code task} or {@code firstTime} is null
     */
    public void scheduleAtFixedRate(TimerTask task, Date firstTime,
                                    long period) {
        if (period <= 0)
            throw new IllegalArgumentException("Non-positive period.");
        sched(task, firstTime.getTime(), period);
    }


    /**
     * 在指定的延迟之后开始 ，重新执行固定延迟执行的指定任务。 后续执行按照规定的时间间隔间隔进行。
     * <p>
     * 在固定延迟执行中，每个执行都相对于上一次执行的实际执行时间进行调度。 如果由于任何原因
     * （如垃圾收集或其他背景活动）延迟执行，则后续执行也将被延迟。 从长远来看，执行频率通常比指定的周期
     * （假定系统时钟基本Object.wait(long)是准确）的倒数略低。
     * <p>
     * 固定延迟执行适用于需要“平滑”的重复活动。 换句话说，适合在短期内保持频率准确的活动比从长远来看更为重要的活动。
     * 这包括大多数动画任务，例如定期闪烁光标。 它还包括其中响应于人类输入执行常规活动的任务，例如只要按住键被自动重复一个字符。
     */
    private void sched(TimerTask task, long time, long period) {
        if (time < 0)
            throw new IllegalArgumentException("Illegal execution time.");

        // Constrain value of period sufficiently to prevent numeric
        // overflow while still being effectively infinitely large.
        if (Math.abs(period) > (Long.MAX_VALUE >> 1))
            period >>= 1;

        synchronized (queue) {
            if (!thread.newTasksMayBeScheduled)
                throw new IllegalStateException("Timer already cancelled.");

            synchronized (task.lock) {
                if (task.state != TimerTask.VIRGIN)
                    throw new IllegalStateException(
                            "Task already scheduled or cancelled");
                //将任务的执行周期规则，传到task中。
                task.nextExecutionTime = time;
                task.period = period;
                task.state = TimerTask.SCHEDULED;
            }

            queue.add(task);//任务队列
            if (queue.getMin() == task)
                queue.notify();
        }
    }


    public void cancel() {
        synchronized (queue) {
            thread.newTasksMayBeScheduled = false;
            queue.clear();
            queue.notify();  // In case queue was already empty.
        }
    }

    /**
     * Removes all cancelled tasks from this timer's task queue.  <i>Calling
     * this method has no effect on the behavior of the timer</i>, but
     * eliminates the references to the cancelled tasks from the queue.
     * If there are no external references to these tasks, they become
     * eligible for garbage collection.
     *
     * <p>Most programs will have no need to call this method.
     * It is designed for use by the rare application that cancels a large
     * number of tasks.  Calling this method trades time for space: the
     * runtime of the method may be proportional to n + c log n, where n
     * is the number of tasks in the queue and c is the number of cancelled
     * tasks.
     *
     * <p>Note that it is permissible to call this method from within a
     * a task scheduled on this timer.
     *
     * @return the number of tasks removed from the queue.
     * @since 1.5
     */
    public int purge() {
        int result = 0;

        synchronized (queue) {
            for (int i = queue.size(); i > 0; i--) {
                if (queue.get(i).state == TimerTask.CANCELLED) {
                    queue.quickRemove(i);
                    result++;
                }
            }

            if (result != 0)
                queue.heapify();
        }

        return result;
    }
}


//执行线程类
class TimerThread extends Thread {
    /**
     * This flag is set to false by the reaper to inform us that there
     * are no more live references to our Timer object.  Once this flag
     * is true and there are no more tasks in our queue, there is no
     * work left for us to do, so we terminate gracefully.  Note that
     * this field is protected by queue's monitor!
     */
    boolean newTasksMayBeScheduled = true;

    /**
     * Our Timer's queue.  We store this reference in preference to
     * a reference to the Timer so the reference graph remains acyclic.
     * Otherwise, the Timer would never be garbage-collected and this
     * thread would never go away.
     */
    private TaskQueue queue;

    TimerThread(TaskQueue queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            mainLoop();
        } finally {
            // Someone killed this Thread, behave as if Timer cancelled
            synchronized (queue) {
                newTasksMayBeScheduled = false;
                queue.clear();  // Eliminate obsolete references
            }
        }
    }

    //主要执行。
    private void mainLoop() {
        while (true) {
            try {
                TimerTask task;
                boolean taskFired;
                synchronized (queue) {

                    // Wait for queue to become non-empty
                    while (queue.isEmpty() && newTasksMayBeScheduled)
                        queue.wait();
                    if (queue.isEmpty())//取消任务后，会将任务队列置为null，并且唤醒阻塞线程，结束条件。
                        break; // Queue is empty and will forever remain; die

                    // Queue nonempty; look at first evt and do the right thing
                    long currentTime, executionTime;
                    task = queue.getMin();//队列头任务
                    synchronized (task.lock) {
                        if (task.state == TimerTask.CANCELLED) {
                            queue.removeMin();
                            continue;  //如果任务取消，继续循环。
                        }
                        currentTime = System.currentTimeMillis();
                        executionTime = task.nextExecutionTime;
                        if (taskFired = (executionTime <= currentTime)) {
                            if (task.period == 0) {  //period == 0 任务是一次性的任务，执行完毕后就删除了。
                                queue.removeMin();
                                task.state = TimerTask.EXECUTED;
                            } else { // 否则是重复任务，
                                queue.rescheduleMin( //为下次执行时间，进行赋值，等于当前时间+周期时间。
                                        task.period < 0 ? currentTime - task.period
                                                : executionTime + task.period);
                            }
                        }
                    }
                    if (!taskFired) // 时间不到，等待
                        queue.wait(executionTime - currentTime);
                }
                if (taskFired)  // 执行第一个任务
                    task.run();
            } catch (InterruptedException e) {
            }
        }
    }
}


/**
 * 任务队列
 */
class TaskQueue {

    private TimerTask[] queue = new TimerTask[128];

    private int size = 0;

    int size() {
        return size;
    }

    void add(TimerTask task) {
        if (size + 1 == queue.length)
            queue = Arrays.copyOf(queue, 2 * queue.length);

        queue[++size] = task;
        fixUp(size);
    }

    TimerTask getMin() {
        return queue[1];
    }

    TimerTask get(int i) {
        return queue[i];
    }

    void removeMin() {
        queue[1] = queue[size];
        queue[size--] = null;  // Drop extra reference to prevent memory leak
        fixDown(1);
    }

    void quickRemove(int i) {
        assert i <= size;

        queue[i] = queue[size];
        queue[size--] = null;  // Drop extra ref to prevent memory leak
    }

    void rescheduleMin(long newTime) {
        queue[1].nextExecutionTime = newTime;
        fixDown(1);
    }

    boolean isEmpty() {
        return size == 0;
    }

    void clear() {
        for (int i = 1; i <= size; i++)
            queue[i] = null;
        size = 0;
    }

    private void fixUp(int k) {
        while (k > 1) {
            int j = k >> 1;
            if (queue[j].nextExecutionTime <= queue[k].nextExecutionTime)
                break;
            TimerTask tmp = queue[j];
            queue[j] = queue[k];
            queue[k] = tmp;
            k = j;
        }
    }

    private void fixDown(int k) {
        int j;
        while ((j = k << 1) <= size && j > 0) {
            if (j < size &&
                    queue[j].nextExecutionTime > queue[j + 1].nextExecutionTime)
                j++; // j indexes smallest kid
            if (queue[k].nextExecutionTime <= queue[j].nextExecutionTime)
                break;
            TimerTask tmp = queue[j];
            queue[j] = queue[k];
            queue[k] = tmp;
            k = j;
        }
    }

    void heapify() {
        for (int i = size / 2; i >= 1; i--)
            fixDown(i);
    }
}
