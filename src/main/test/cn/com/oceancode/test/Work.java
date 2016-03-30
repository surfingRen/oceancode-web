package cn.com.oceancode.test;
public class Work implements Runnable{

		public void run() {
			DBInit.initUser();
		}
		
	}