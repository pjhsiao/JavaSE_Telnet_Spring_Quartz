package com.commonsnet.hsiao.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public  class PttTelnetRun {
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext(new String[]{"beans-config.xml"});
		System.out.println("啟動 Quartz Task..PTT CodeJob版資料撈取排程啟動 【每天06:30發動】!!!");
	}
}


