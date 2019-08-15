package chapter4;

import chapter3.ThreadUtil;

/**
 * synchronized的两种用法：1.类锁<br>
 * 
 * 当存在多个实例并发访问同一个数据时，加对象锁只能保证 同一个实例的方法，对数据的获取是同步的，而无法保证对象之间
 * 的数据也是同步的，因此需要类锁，让各个实例之间用同一把锁， 实例并发之间数据同步。
 * 
 * 方式： 1. 静态方法上加synchronized关键字 2. 同步代码块加Class对象锁
 *
 * @author mahao
 * @date 2019年4月27日 下午3:12:19
 */
public class ClassLock implements Runnable {
	/*
	 * 案列：多个实例中add方法，同时对静态变量num递增
	 */
	private static int num = 0;

	public void run() {
		for (int i = 0; i < 5; i++) {
			synchronized (ClassLock.class) {
				System.out.println(Thread.currentThread().getName() + "--start--" + num);
				num++;
				ThreadUtil.sleep(300);// 睡眠0.1s
				System.out.println(Thread.currentThread().getName() + "-- end --" + num);
			}
		}
	}

	public static void main(String[] args) {
		ClassLock instance1 = new ClassLock();
		ClassLock instance2 = new ClassLock();

		Thread t1 = new Thread(instance1);
		Thread t2 = new Thread(instance1);
		Thread t3 = new Thread(instance1);
		Thread r1 = new Thread(instance2);
		Thread r2 = new Thread(instance2);

		// 单实例多线程调用加了对象锁方法 正确
		System.out.println("单实例多线程调用加了对象锁方法  -- 正确");
		t1.start();
		t2.start();
		while (t1.isAlive() || t2.isAlive()) {//等待测试情况结束
		}
	
		System.out.println("多实例多线程调用加了对象锁方法  --  失败");
		//解决方案： 升级为类锁
		num = 0;
		t3.start();
		r1.start();
	}

}
