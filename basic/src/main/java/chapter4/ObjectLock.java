package chapter4;


import chapter3.ThreadUtil;

/**
 * synchronized的两种用法：1.对象锁<br>
 * 
 * 在出现并发问题的方法上或者局部代码块上添加<br>
 * 对象锁（通常用当当前对象this当做锁），当并发<br>
 * 调用这个同步代码块中的代码，必须获得先获得对象<br>
 * 锁，才能执行。这个对象锁，同步代码的作用单位范围是<br>
 * 同一个对象，多个线程并发调用个一个实例中的方法，对象锁<br>
 * 可以作用。但是对于多个实例，并发调用这些实例中的方法，是无法同步的<br>
 * 这是类锁的作用。<br>
 * 
 * @author mahao
 * @date 2019年4月27日 下午2:07:57
 */
public class ObjectLock implements Runnable {
	/*
	 * 案列测试： count并发递增++，会有重重复数据出现
	 * 
	 * 解决方案 ： 同步代码块，或者同步方法
	 */
	private int count = 0;

	public void run() {
		/*
		 * run方法对count进行10次++操作，
		 */
		for (int i = 0; i < 10; i++) {
			//方案一： 同步代码块，在需要同步的地方，进行加锁操作
			synchronized (this) {
				System.out.println(Thread.currentThread().getName() + "--start--" + count);
				count++;
				ThreadUtil.sleep(300);// 睡眠0.1s
				System.out.println(Thread.currentThread().getName() + "-- end --" + count);
			}
			
			//方案二：同步方法，同步方法是对整个方法加对象锁，默认是this
			add();
		}

	}

	public synchronized void add() {
		System.out.println(Thread.currentThread().getName() + "--start--" + count);
		count++;
		ThreadUtil.sleep(300);// 睡眠0.1s
		System.out.println(Thread.currentThread().getName() + "-- end --" + count);
	}

	public static void main(String[] args) {

		ObjectLock instance = new ObjectLock();

		Thread t1 = new Thread(instance);
		Thread t2 = new Thread(instance);
		Thread t3 = new Thread(instance);
		Thread t4= new Thread(instance);
		// 线程t1和t2共用一个实例，开启了两个线程
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
	}

}
