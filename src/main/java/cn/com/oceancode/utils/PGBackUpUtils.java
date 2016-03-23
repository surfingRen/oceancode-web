package cn.com.oceancode.utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PGBackUpUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PGBackUpUtils.class);

	String pathname = "c:\\back";

	public List<String> backUpPg() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("backUpPg() - start");
		}

		BufferedReader reader = null;
		List<String> list = new ArrayList<String>();

		try {
			String string = null;
			if (!System.getProperty("os.name").toLowerCase().startsWith("win")) {
				string = this.getClass().getClassLoader().getResource("backup.sh").toString().replace("file:///", "");
			} else {
				string = this.getClass().getClassLoader().getResource("backup.bat").toString().replace("file:/", "") + " " + pathname
						+ "\\back_" + LogUtils.getNowTimeForFile() + ".bak";
			}
			Process process = Runtime.getRuntime().exec(string);
			InputStream in = process.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			process.waitFor();
			process.destroy();// 抛出InterruptedExeption异常

		} catch (Exception e) {
			logger.error("backUpPg()", e);
			throw e;
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					logger.error("backUpPg()", e);

					e.printStackTrace();
				}// 关流
		}

		if (logger.isDebugEnabled()) {
			logger.debug("backUpPg() - end");
		}
		return list;
	}

	public static void main(String[] args) {
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - start");
		}

		try {
			new PGBackUpUtils().backUpPg();
		} catch (Exception e) {
			logger.error("main(String[])", e);

			e.printStackTrace();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - end");
		}
	}

	public List<String> fileList() {
		if (logger.isDebugEnabled()) {
			logger.debug("fileList() - start");
		}

		List<String> list = new ArrayList<String>();
		try {
			File f = new File(pathname);
			if (!f.exists()) {
				f.mkdir();
			}
			if (f.isDirectory()) {
				File[] t = f.listFiles();
				for (int i = 0; i < t.length; i++) {
					if (t[i].getName().endsWith(".bak")) {
						list.add(t[i].getName());
					}
				}
			}
		} catch (Exception e) {
			logger.error("fileList()", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("fileList() - end");
		}
		return list;
	}

}
