package cn.com.oceancode.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class LogUtils {

	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	static SimpleDateFormat formatterForFile = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	public static String getNowTime() {
		return formatter.format(new Date());
	}

	public static String getNowTimeForFile() {
		return formatterForFile.format(new Date());
	}

	public static void main(String[] args) {
		System.out.println(getNowTime());
	}

	public static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			t.printStackTrace(pw);
			return sw.toString();
		} finally {
			pw.close();
		}
	}

	public static long getLogId() {
		Map<String, Object> args = new HashMap<String, Object>();
		JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringContextUtil.getBean("jdbcTemplate_mysql");
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		return template.queryForLong("select nextval('crm_log_lid_seq')", args);
	}

	public static int writeLog(long lid, String fwm, String ffm, String kssj, String jssj, String zt, String bz, String n1, String n2,
			String n3, String n4, String n5, String n6, String n7, String n8, String exception, String czr) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringContextUtil.getBean("jdbcTemplate_mysql");
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("lid", lid);
		args.put("fwm", fwm);
		args.put("ffm", ffm);
		args.put("kssj", kssj);
		args.put("jssj", jssj);
		args.put("zt", zt);
		args.put("exception", exception);
		args.put("bz", bz);
		args.put("n1", n1);
		args.put("n2", n2);
		args.put("n3", n3);
		args.put("n4", n4);
		args.put("n5", n5);
		args.put("n6", n6);
		args.put("n7", n7);
		args.put("n8", n8);
		args.put("czr", czr);
		return template
				.update("INSERT INTO crm_log(lid, fwm, ffm, kssj, jssj, zt, exception, bz, n1, n2, n3, n4, n5, n6, n7, n8,czr) VALUES (:lid, :fwm, :ffm, :kssj, :jssj, :zt, :exception, :bz, :n1, :n2, :n3, :n4, :n5, :n6, :n7, :n8, :czr)",
						args);
	}

	public static int writeLogDetail(long lid, String czlx, String czzlx, String yxdx, String yxxm, String yxsl, String sxsj, String n1,
			String n2, String n3, String n4, String n5, String n6, String n7, String n8) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringContextUtil.getBean("jdbcTemplate_mysql");
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("lid", lid);
		args.put("czlx", czlx);
		args.put("czzlx", czzlx);
		args.put("yxdx", yxdx);
		args.put("yxxm", yxxm);
		args.put("yxsl", yxsl);
		args.put("sxsj", sxsj);
		args.put("n1", n1);
		args.put("n2", n2);
		args.put("n3", n3);
		args.put("n4", n4);
		args.put("n5", n5);
		args.put("n6", n6);
		args.put("n7", n7);
		args.put("n8", n8);
		return template
				.update("INSERT INTO crm_log_mx(lid, czlx, czzlx, yxdx, yxxm, yxsl, sxsj, n1, n2, n3, n4, n5, n6, n7, n8) VALUES (:lid, :czlx, :czzlx, :yxdx, :yxxm, :yxsl, :sxsj, :n1, :n2, :n3, :n4, :n5, :n6, :n7, :n8)",
						args);
	}

	public static int writeLogDetail(long lid, String czlx, String czzlx, String yxdx, String yxxm, String yxsl, String sxsj) {
		return writeLogDetail(lid, czlx, czzlx, yxdx, yxxm, yxsl, sxsj, null, null, null, null, null, null, null, null);
	}

	public static int writeLogDetail(long lid, String czlx, String czzlx, String yxdx, String yxxm, String yxsl, String sxsj, String n1) {
		return writeLogDetail(lid, czlx, czzlx, yxdx, yxxm, yxsl, sxsj, n1, null, null, null, null, null, null, null);
	}

	public static int writeLogDetail(long lid, String czlx, String czzlx, String yxdx, String yxxm, String yxsl, String sxsj, String n1,
			String n2) {
		return writeLogDetail(lid, czlx, czzlx, yxdx, yxxm, yxsl, sxsj, n1, n2, null, null, null, null, null, null);
	}

	public static int writeLogDetail(long lid, String czlx, String czzlx, String yxdx, String yxxm, String yxsl, String sxsj, String n1,
			String n2, String n3) {
		return writeLogDetail(lid, czlx, czzlx, yxdx, yxxm, yxsl, sxsj, n1, n2, n3, null, null, null, null, null);
	}

	public static int writeLog(long lid, String fwm, String ffm, String kssj, String jssj, String zt, String bz, String exception,
			String czr) {
		return writeLog(lid, fwm, ffm, kssj, jssj, zt, bz.substring(0, (bz.length() > 4000 ? 4000 : bz.length())), null, null, null, null,
				null, null, null, null, exception.substring(0, (exception.length() > 4000 ? 4000 : exception.length())), czr);
	}

	public static int writeLogS(long lid, String fwm, String ffm, String kssj, String jssj, String bz, String czr) {
		return writeLog(lid, fwm, ffm, kssj, jssj, "1", bz.substring(0, (bz.length() > 4000 ? 4000 : bz.length())), null, null, null, null,
				null, null, null, null, null, czr);
	}

	public static int writeLogS(long lid, String fwm, String ffm, String kssj, String jssj, String bz, String czr, String n1, String n2,
			String n3) {
		return writeLog(lid, fwm, ffm, kssj, jssj, "1", bz.substring(0, (bz.length() > 4000 ? 4000 : bz.length())), n1, n2, n3, null, null,
				null, null, null, null, czr);
	}

	public static int writeLogE(long lid, String fwm, String ffm, String kssj, String jssj, String bz, String exception, String czr) {
		return writeLog(lid, fwm, ffm, kssj, jssj, "0", bz.substring(0, (bz.length() > 4000 ? 4000 : bz.length())), null, null, null, null,
				null, null, null, null, exception.substring(0, (exception.length() > 4000 ? 4000 : exception.length())), czr);
	}

	public static int writeLogE(long lid, String fwm, String ffm, String kssj, String jssj, String bz, String exception, String n1,
			String czr) {
		return writeLog(lid, fwm, ffm, kssj, jssj, "0", bz.substring(0, (bz.length() > 4000 ? 4000 : bz.length())), n1, null, null, null,
				null, null, null, null, exception.substring(0, (exception.length() > 4000 ? 4000 : exception.length())), czr);
	}

	public static int writeLogE(long lid, String fwm, String ffm, String kssj, String jssj, String bz, String exception, String n1,
			String n2, String czr) {
		return writeLog(lid, fwm, ffm, kssj, jssj, "0", bz.substring(0, (bz.length() > 4000 ? 4000 : bz.length())), n1, n2, null, null,
				null, null, null, null, exception.substring(0, (exception.length() > 4000 ? 4000 : exception.length())), czr);
	}

	public static int roolBack(Integer logId) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringContextUtil.getBean("jdbcTemplate_mysql");
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String, Object> args = new HashMap<String, Object>();
		return template.update("", args);
	}

	public static String getUserId(HttpServletRequest request) {
		return (String) request.getSession().getAttribute("userId");
	}

	public static String getUserId() {
		return "100000";
	}

}
