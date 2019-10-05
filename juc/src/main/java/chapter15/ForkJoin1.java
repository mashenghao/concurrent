package chapter15;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * forkJoin计算1-100之间sum求和
 *
 * @author: mahao
 * @date: 2019/10/4
 */
public class ForkJoin1 extends RecursiveTask<Integer> {

    private int l;
    private int r;

    public ForkJoin1(int l, int r) {
        this.l = l;
        this.r = r;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        if (r - l <= 2) {
            for (int i = l; i <= r; i++) {
                sum += i;
            }
           // ThreadUtil.sleep(1000);
            return sum;
        } else {
            int mid = (l + r) / 2;
            ForkJoin1 lj = new ForkJoin1(l, mid);
            ForkJoin1 rj = new ForkJoin1(mid + 1, r);
            ForkJoinTask<Integer> fork1 = lj.fork();
            ForkJoinTask<Integer> fork2 = rj.fork();
            Integer l1 = fork1.join();
            Integer r1 = fork2.join();
            return l1 + r1;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool(100);
        ForkJoinTask<Integer> submit = pool.submit(new ForkJoin1(1, 100000));
        int result = submit.get();
        System.out.println(result);
    }
}
