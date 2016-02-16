package com.sict.service;

import java.io.File;
import java.util.Calendar;
import java.util.TimerTask;

import javax.servlet.ServletContext;

public class MonitorTask extends TimerTask {
	private ServletContext context;
	private static boolean isRunning = false; // 任务正在执行中的标志
	private static boolean newTaskFlag = true; // 可以开始新任务的标志

	private static final int C_SCHEDULE_MINUTE = 5; // 时间间隔
	private static long tickCount = 0; // 心跳次数，检测系统是否正常运行

	public MonitorTask(ServletContext context) {
		this.context = context;
	}

	@Override
	public void run() {
		Calendar cal = Calendar.getInstance();

		if (!isRunning) {
			if (cal.get(Calendar.MINUTE) % C_SCHEDULE_MINUTE == 0
					&& newTaskFlag) {

				// 需要执行的代码
				isRunning = true;
				context.log("开始执行指定任务");
				tickCount++; // 循环检测计数

				String content = "";
				//content += checkPageChange("http://www.sohu.com/", "sohu",
				//		"GBK");
				content += checkPageChange(
						"http://www.koehres-kaktus.de/shop/Succulent-seeds/Echeveria---2_629.html?language=en",
						"p2_629", "iso-8859-15");
				content += checkPageChange(
						"http://www.koehres-kaktus.de/shop/Succulent-seeds/Graptopetalum---2_643.html",
						"p2_643", "iso-8859-15");
				content += checkPageChange(
						"http://www.koehres-kaktus.de/shop/Succulent-seeds/Pachyphytum---2_685.html",
						"p2_685", "iso-8859-15");
				if (content.length() > 0) {
					System.out.println("page changed:\n" + content);
					sendMail("变化网页地址:\n" + content); // 发送邮件通知
				}

				isRunning = false;
				newTaskFlag = false;
				context.log("指定任务执行结束");
			}
		} else {
			context.log("上一次任务执行还未结束");
		}

		if (cal.get(Calendar.MINUTE) % C_SCHEDULE_MINUTE != 0) {
			newTaskFlag = true;
		}
	}

	// 检测 url 指定的网页内容是否变化，fname是网页对应文件名,encodeType 网页编码类型
	private String checkPageChange(String url, String fname, String encodeType) {
		String result = "";
		try {
			String s = FileService.getHtmlFile(url, encodeType);

			String ctxPath = context.getRealPath("/") + "\\" + "WEB-INF\\";
			File fHtml = new File(ctxPath, fname + ".html");
			if (fHtml.exists()) { // 文件存在，读取文件，比较文件内容
				String sLast = FileService.readFile(fHtml);

				String newSid = getMODsid(s);
				String oldSid = getMODsid(sLast);
				if (newSid.length() > 0 && oldSid.length() > 0) {
					sLast = sLast.replaceAll(oldSid, newSid);
				}
				System.out.println(oldSid + "-->" + newSid);
				File fLast = new File(ctxPath, fname + "_last.html");
				FileService.saveFile(fLast, sLast);

				// 保存再读取，使字符编码一致
				// 将网页保存到文件时，由于编码原因，有些字符的码值发生变化，如？原来是231，241，结果保存后变成63，所以，应当都保存后再比较
				File fNow = new File(ctxPath, fname + "_now.html");
				FileService.saveFile(fNow, s);
				s = FileService.readFile(fNow);

				if (!sLast.equals(s)) { // 文件内容不同
					System.out.println(sLast.length() + "-->" + s.length());
					if (sLast.length() == s.length()) {
						for (int i = 0; i < sLast.length(); i++) {
							char c1, c2;
							c1 = sLast.charAt(i);
							c2 = s.charAt(i);
							if (c1 != c2) {
								System.out.println(i + ":" + c1 + ((int) c1)
										+ "-->" + c2 + ((int) c2));
								System.out.println(i + ":"
										+ sLast.substring(i - 10, i + 10)
										+ "-->" + s.substring(i - 10, i + 10));
							}
						}
					}
					result = url + "\n"; // 返回变化页面的 url
				}

				// 更新文件内容
				fLast.delete();
				fHtml.delete();
				fNow.renameTo(fHtml);
			} else { // 文件不存在，保存文件
				FileService.saveFile(fHtml, s);
			}
		} catch (Exception ex) {
			System.err.println("checkPageChange url = : " + url
					+ " exception = " + ex);
		}
		return result;
	}

	// 网页每次获取时，MODsid的值都变化，检测网页内容变化时，需将其忽略
	private String getMODsid(String htmlPage) {
		String sid = "";
		int s = htmlPage.indexOf("MODsid=");
		int e = htmlPage.indexOf("\"", s);
		if (s >= 0 && s + 7 <= e) {
			sid = htmlPage.substring(s + 7, e);
		}
		return sid;
	}

	private void sendMail(String content) {
		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.163.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("anguang1973@163.com");
		mailInfo.setPassword("Sictjsj_2013");// 您的邮箱密码
		mailInfo.setFromAddress("anguang1973@163.com");
		mailInfo.setToAddress("896995530@qq.com");
		mailInfo.setSubject("网页内容变化通知");

		mailInfo.setContent(content);
		// 这个类主要来发送邮件
		SimpleMailSender.sendTextMail(mailInfo);// 发送文体格式
		// SimpleMailSender.sendHtmlMail(mailInfo);// 发送html格式
	}

	public static long getTickCount() {
		return tickCount;
	}
}