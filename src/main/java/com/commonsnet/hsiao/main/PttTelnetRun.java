package com.commonsnet.hsiao.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public  class PttTelnetRun {
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext(new String[]{"beans-config.xml"});
		System.out.println("�Ұ� Quartz Task..PTT CodeJob����Ƽ����Ƶ{�Ұ� �i�C��06:30�o�ʡj!!!");
	}
}


