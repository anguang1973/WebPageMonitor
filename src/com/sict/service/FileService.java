package com.sict.service;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class FileService {
	// 保存文件
	public static void saveFile(File file, String content) {
		try {
			FileWriter out = new FileWriter(file, true);
			out.write(content);
			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 保存文件
	public static void saveFile(String fName, String content) {
		try {
			File file = new File(fName);
			FileWriter out = new FileWriter(file, true);
			out.write(content);
			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 读取文件
	public static String readFile(File f) {
		StringBuffer s = new StringBuffer("");
		try {
			if (f.exists()) {
				FileReader is = new FileReader(f);
				String line; // 用来保存每行读取的内容
				BufferedReader reader = new BufferedReader(is);
				line = reader.readLine(); // 读取第一行
				while (line != null) { // 如果 line 为空说明读完了
					s.append(line); // 将读到的内容添加到 buffer 中
					s.append("\n"); // 添加换行符
					line = reader.readLine(); // 读取下一行
				}
				reader.close();
				is.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return s.toString();
	}

	// 读取文件
	public static String readFile(String fName) {
		StringBuffer s = new StringBuffer("");
		try {
			File f = new File(fName);
			if (f.exists()) {
				FileReader is = new FileReader(f);
				String line; // 用来保存每行读取的内容
				BufferedReader reader = new BufferedReader(is);
				line = reader.readLine(); // 读取第一行
				while (line != null) { // 如果 line 为空说明读完了
					s.append(line); // 将读到的内容添加到 buffer 中
					s.append("\n"); // 添加换行符
					line = reader.readLine(); // 读取下一行
				}
				reader.close();
				is.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return s.toString();
	}

	// 获取网页内容
	public static String getHtmlFile(String url, String encoding) throws Exception{
		String htmls = "";
		if (url != null && url.length() > 0) {
			htmls = getPageSource(url, encoding);
		}

		return htmls;
	}

	public static String getPageSource(String pageUrl, String encoding) throws Exception {
		StringBuffer sb = new StringBuffer();

		// 构建一URL对象
		URL url = new URL(pageUrl);
		// 使用openStream得到一输入流并由此构造一个BufferedReader对象
		BufferedReader in = new BufferedReader(new InputStreamReader(
				url.openStream(), encoding));
		String line;
		// 读取www资源
		while ((line = in.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		in.close();

		return sb.toString();
	}
}
