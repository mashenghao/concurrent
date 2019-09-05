package dpart4;

import chapter8.ThreadPool3;

/**
 * 模拟并发访问一个资源，通过对资源的加锁，达到单个线程访问的目的
 *
 * @author: mahao
 * @date: 2019/8/26
 */
public class MainClass {

    public static void main(String[] args) {
        Gate resource = new Gate();

        ThreadPool3 pool = new ThreadPool3();
        pool.submit(() -> {
            while (true) {
                resource.pass("zs", "zs");
            }

        });
        pool.submit(() -> {
            while (true) {
                resource.pass("ls", "ls");
            }

        });
        pool.submit(() -> {
            while (true) {
                resource.pass("ww", "ww");
            }
        });


    }
}
