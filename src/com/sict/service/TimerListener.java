package com.sict.service;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TimerListener implements ServletContextListener {
	private Timer timer = null;
	private MonitorTask sampleTask;

	public void contextDestroyed(ServletContextEvent event) {
		timer.cancel();
		event.getServletContext().log("定时器销毁");
	}

	public void contextInitialized(ServletContextEvent event) {
		timer = new Timer(true);
		sampleTask = new MonitorTask(event.getServletContext());
		event.getServletContext().log("定时器已启动");
		timer.schedule(sampleTask, 0, 1000);
		event.getServletContext().log("已经添加任务调度表");
	}
}
