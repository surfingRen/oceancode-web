package cn.com.oceancode.service;

import java.io.File;
import java.io.StringWriter;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.sun.jersey.spi.resource.Singleton;

import cn.com.oceancode.utils.CodeUtils;
import cn.com.oceancode.utils.DownloadUtils;
import cn.com.oceancode.utils.LogUtils;
import cn.com.oceancode.utils.PGBackUpUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

@Singleton
@Component
@Path("/crm")
public class CrmService {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CrmService.class);

	static final String SERVICE_NAME = "crm";

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	private JdbcTemplate jdbcTemplate;
	private PlatformTransactionManager transactionManager;

	public PlatformTransactionManager getTransactionManager() {
		if (logger.isDebugEnabled()) {
			logger.debug("getTransactionManager() - start");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getTransactionManager() - end");
		}
		return transactionManager;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		if (logger.isDebugEnabled()) {
			logger.debug("setTransactionManager(PlatformTransactionManager) - start");
		}

		this.transactionManager = transactionManager;

		if (logger.isDebugEnabled()) {
			logger.debug("setTransactionManager(PlatformTransactionManager) - end");
		}
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		if (logger.isDebugEnabled()) {
			logger.debug("setJdbcTemplate(JdbcTemplate) - start");
		}

		this.jdbcTemplate = jdbcTemplate;

		if (logger.isDebugEnabled()) {
			logger.debug("setJdbcTemplate(JdbcTemplate) - end");
		}
	}

	// @POST
	// @Path("reg")
	// public String reg(@FormParam("mymm2") final String mymm2,
	// @FormParam("name") final String zh, @FormParam("userlevel") final String
	// lx,
	// @FormParam("yhq") final String yhq2, @FormParam("password1") final String
	// mm1, @FormParam("password11") final String mm11,
	// @FormParam("password2") final String mm2, @FormParam("password22") final
	// String mm22, @FormParam("fwcenter") final String fwzx,
	// @FormParam("ztuser") final String tjr, @FormParam("azuser") final String
	// jdr, @FormParam("azsite") final String azwz,
	// @FormParam("realname") final String xm, @FormParam("sex") final String
	// xb, @FormParam("tel") final String lxdh,
	// @FormParam("email") final String dzyx, @FormParam("qq") final String qq,
	// @FormParam("BankName") final String khyh,
	// @FormParam("bankaddr") final String khhdz, @FormParam("bankcard") final
	// String yhkh,
	// @FormParam("cardusername") final String hz, @FormParam("dz1") final
	// String dz1, @Context final HttpServletRequest request) {
	// if (logger.isDebugEnabled()) {
	// logger.debug("reg(String, String, String, String, String, String, String,
	// String, String, String, String, String, String, String, String, String,
	// String, String, String, String, String, HttpServletRequest) - start");
	// }
	//
	// long logId = LogUtils.getLogId();
	// String startTime = LogUtils.getNowTime();
	// TransactionTemplate tt = new
	// TransactionTemplate(getTransactionManager());
	// TTReturnObj ttReturnObj = tt.execute(new
	// TransactionCallback<TTReturnObj>() {
	// public TTReturnObj doInTransaction(TransactionStatus ts) {
	// if (logger.isDebugEnabled()) {
	// logger.debug("doInTransaction(TransactionStatus) - start");
	// }
	//
	// String msg = "";
	// long logId = LogUtils.getLogId();
	// String startTime = LogUtils.getNowTime();
	//
	// try {
	// NamedParameterJdbcTemplate template = new
	// NamedParameterJdbcTemplate(jdbcTemplate);
	//
	// if (!checkMm2(mymm2, request)) {
	// return new
	// TTReturnObj("alert('二级密码不正确');$('#button').attr('disabled',false);$('#sb_box2').hide();");
	// }
	//
	// Double yhq = null;
	// try {
	// yhq = new Double(yhq2);
	// } catch (Exception e) {
	// yhq = 0.0;
	// }
	//
	// if (azwz == null || azwz.equals("")) {
	// LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime,
	// LogUtils.getNowTime(), "请选择安置位置", "",
	// LogUtils.getUserId(request));
	// if (logger.isDebugEnabled()) {
	// logger.debug("reg(String, String, String, String, String, String, String,
	// String, String, String, String, String, String, String, String, String,
	// String, String, String, String, String, HttpServletRequest) - end");
	// }
	// return new
	// TTReturnObj("alert('请选择安置位置');$('#button').attr('disabled',false);$('#sb_box2').hide();");
	// }
	//
	// // 验证报单币,优惠券使用情况
	// if ("1".equals(lx)) {
	// if (new Double((yhq)) > 1000) {
	// LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime,
	// LogUtils.getNowTime(), "金卡最多使用1000优惠券", "",
	// LogUtils.getUserId(request));
	// if (logger.isDebugEnabled()) {
	// logger.debug("reg(String, String, String, String, String, String, String,
	// String, String, String, String, String, String, String, String, String,
	// String, String, String, String, String, HttpServletRequest) - end");
	// }
	// return new
	// TTReturnObj("alert('金卡最多使用1000优惠券');$('#button').attr('disabled',false);$('#sb_box2').hide();");
	// }
	// } else if ("2".equals(lx)) {
	// if (new Double((yhq)) > 500) {
	// LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime,
	// LogUtils.getNowTime(), "普卡最多使用500优惠券", "",
	// LogUtils.getUserId(request));
	// if (logger.isDebugEnabled()) {
	// logger.debug("reg(String, String, String, String, String, String, String,
	// String, String, String, String, String, String, String, String, String,
	// String, String, String, String, String, HttpServletRequest) - end");
	// }
	// return new
	// TTReturnObj("alert('普卡最多使用500优惠券');$('#button').attr('disabled',false);$('#sb_box2').hide();");
	// }
	// } else {
	// LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime,
	// LogUtils.getNowTime(), "会员类型获取失败", "",
	// LogUtils.getUserId(request));
	// if (logger.isDebugEnabled()) {
	// logger.debug("reg(String, String, String, String, String, String, String,
	// String, String, String, String, String, String, String, String, String,
	// String, String, String, String, String, HttpServletRequest) - end");
	// }
	// return new
	// TTReturnObj("alert('会员类型获取失败');$('#button').attr('disabled',false);$('#sb_box2').hide();");
	// }
	// Map<String, Object> me = initMe(request);
	// if (new Double(yhq) > new Double(me.get("yhq").toString())) {
	// LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime,
	// LogUtils.getNowTime(), "优惠券余额不足", "",
	// LogUtils.getUserId(request));
	// if (logger.isDebugEnabled()) {
	// logger.debug("reg(String, String, String, String, String, String, String,
	// String, String, String, String, String, String, String, String, String,
	// String, String, String, String, String, HttpServletRequest) - end");
	// }
	// return new
	// TTReturnObj("alert('优惠券余额不足');$('#button').attr('disabled',false);$('#sb_box2').hide();");
	// }
	//
	// // 报单币校验
	// if ("1".equals(lx)) {
	// if (new Double(me.get("bdb").toString()) < (7000 - new Double(yhq))) {
	// String returnString = "alert('报单币不够,目前只有:" + me.get("bdb")
	// + "');$('#button').attr('disabled',false);$('#sb_box2').hide();";
	// LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime,
	// LogUtils.getNowTime(), "报单币不够,目前只有" + me.get("bdb"),
	// "", LogUtils.getUserId(request));
	// if (logger.isDebugEnabled()) {
	// logger.debug("reg(String, String, String, String, String, String, String,
	// String, String, String, String, String, String, String, String, String,
	// String, String, String, String, String, HttpServletRequest) - end");
	// }
	// return new TTReturnObj(returnString);
	// }
	// }
	// if ("2".equals(lx)) {
	// if (new Double(me.get("bdb").toString()) < (3500 - new Double(yhq))) {
	// String returnString = "alert('报单币不够,目前只有:" + me.get("bdb")
	// + "');$('#button').attr('disabled',false);$('#sb_box2').hide();";
	// LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime,
	// LogUtils.getNowTime(), "报单币不够,目前只有:" + me.get("bdb"),
	// "", LogUtils.getUserId(request));
	// if (logger.isDebugEnabled()) {
	// logger.debug("reg(String, String, String, String, String, String, String,
	// String, String, String, String, String, String, String, String, String,
	// String, String, String, String, String, HttpServletRequest) - end");
	// }
	// return new TTReturnObj(returnString);
	// }
	// }
	//
	// // 表单信息验证
	// if (!mm1.equals(mm11)) {
	// LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime,
	// LogUtils.getNowTime(), "登录密码不一致", "",
	// LogUtils.getUserId(request));
	// if (logger.isDebugEnabled()) {
	// logger.debug("reg(String, String, String, String, String, String, String,
	// String, String, String, String, String, String, String, String, String,
	// String, String, String, String, String, HttpServletRequest) - end");
	// }
	// return new
	// TTReturnObj("alert('登录密码不一致');$('#button').attr('disabled',false);$('#sb_box2').hide();");
	// }
	//
	// if (!mm2.equals(mm22)) {
	// LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime,
	// LogUtils.getNowTime(), "二级密码不一致", "",
	// LogUtils.getUserId(request));
	// if (logger.isDebugEnabled()) {
	// logger.debug("reg(String, String, String, String, String, String, String,
	// String, String, String, String, String, String, String, String, String,
	// String, String, String, String, String, HttpServletRequest) - end");
	// }
	// return new
	// TTReturnObj("alert('二级密码不一致');$('#button').attr('disabled',false);$('#sb_box2').hide();");
	// }
	//
	// Map<String, Object> args = new HashMap<String, Object>();
	// args.put("zh", zh);
	// if (template.queryForInt("select count(1) from crm_user where zh = :zh",
	// args) > 0) {
	// String returnString = "alert('账号:" + zh +
	// "重复');$('#button').attr('disabled',false);$('#sb_box2').hide();";
	// LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime,
	// LogUtils.getNowTime(), "账号:" + zh + "重复", "",
	// LogUtils.getUserId(request));
	// if (logger.isDebugEnabled()) {
	// logger.debug("reg(String, String, String, String, String, String, String,
	// String, String, String, String, String, String, String, String, String,
	// String, String, String, String, String, HttpServletRequest) - end");
	// }
	// return new TTReturnObj(returnString);
	// }
	//
	// args = new HashMap<String, Object>();
	// args.put("zh", tjr);
	// if (template.queryForInt("select count(1) from crm_user where zh = :zh",
	// args) == 0) {
	// String returnString = "alert('推荐人:" + tjr +
	// "不存在');$('#button').attr('disabled',false);$('#sb_box2').hide();";
	// LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime,
	// LogUtils.getNowTime(), "推荐人:" + tjr + "不存在", "",
	// LogUtils.getUserId(request));
	// if (logger.isDebugEnabled()) {
	// logger.debug("reg(String, String, String, String, String, String, String,
	// String, String, String, String, String, String, String, String, String,
	// String, String, String, String, String, HttpServletRequest) - end");
	// }
	// return new TTReturnObj(returnString);
	// }
	//
	// args = new HashMap<String, Object>();
	// args.put("zh", jdr);
	// if (template.queryForInt("select count(1) from crm_user where zh = :zh",
	// args) == 0) {
	// String returnString = "alert('接点人:" + jdr +
	// "不存在');$('#button').attr('disabled',false);$('#sb_box2').hide();";
	// LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime,
	// LogUtils.getNowTime(), "接点人:" + jdr + "不存在", "",
	// LogUtils.getUserId(request));
	// if (logger.isDebugEnabled()) {
	// logger.debug("reg(String, String, String, String, String, String, String,
	// String, String, String, String, String, String, String, String, String,
	// String, String, String, String, String, HttpServletRequest) - end");
	// }
	// return new TTReturnObj(returnString);
	// }
	// List<Map<String, Object>> queryForList =
	// template.queryForList("select azwz from crm_user where jdr = :zh", args);
	// String azwz2 = null;
	// if (queryForList.size() >= 2) {
	// Map<String, Object> jdrMap =
	// template.queryForMap("select * from crm_user where zh = :zh", args);
	// if (new Integer(jdrMap.get("jb").toString()) <= 4) {
	// LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime,
	// LogUtils.getNowTime(), "接点人:" + jdr
	// + "已经有2个会员,请选择其他接点人", "", LogUtils.getUserId(request));
	// String returnString2 = "alert('接点人:" + jdr
	// +
	// "已经有2个会员,请选择其他接点人');$('#button').attr('disabled',false);$('#sb_box2').hide();";
	// if (logger.isDebugEnabled()) {
	// logger.debug("doInTransaction(TransactionStatus) - end");
	// }
	// return new TTReturnObj(returnString2);
	// }
	//
	// List<Map<String, Object>> list =
	// template.queryForList("select * from crm_user where jdr = :zh and azwz <>
	// '0'",
	// args);
	// int i = list.size() + 1;
	// azwz2 = i + "";
	//
	// }
	// if (queryForList.size() == 1) {
	// if (queryForList.get(0).get("azwz").equals(azwz) && !isCompany(request))
	// {
	// String returnString = "alert('接点人:" + jdr + " " + azwz
	// + "区已经有人了');$('#button').attr('disabled',false);$('#sb_box2').hide();";
	// LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime,
	// LogUtils.getNowTime(), "接点人:" + jdr + " " + azwz
	// + "区已经有人了", "", LogUtils.getUserId(request));
	// if (logger.isDebugEnabled()) {
	// logger.debug("reg(String, String, String, String, String, String, String,
	// String, String, String, String, String, String, String, String, String,
	// String, String, String, String, String, HttpServletRequest) - end");
	// }
	// return new TTReturnObj(returnString);
	// }
	// }
	//
	// args = new HashMap<String, Object>();
	// args.put("zh", fwzx);
	// List<Map<String, Object>> fwzx_userList =
	// template.queryForList("select sffwzx from crm_user where zh = :zh",
	// args);
	// if (fwzx_userList.size() == 0) {
	// String returnString = "alert('服务中心:" + fwzx +
	// "不存在');$('#button').attr('disabled',false);$('#sb_box2').hide();";
	// LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime,
	// LogUtils.getNowTime(), "服务中心:" + fwzx + "不存在", "",
	// LogUtils.getUserId(request));
	// if (logger.isDebugEnabled()) {
	// logger.debug("reg(String, String, String, String, String, String, String,
	// String, String, String, String, String, String, String, String, String,
	// String, String, String, String, String, HttpServletRequest) - end");
	// }
	// return new TTReturnObj(returnString);
	// } else if (fwzx_userList.get(0).get("sffwzx").equals("0")) {
	// String returnString = "alert('" + fwzx +
	// " 不是服务中心');$('#button').attr('disabled',false);$('#sb_box2').hide();";
	// LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime,
	// LogUtils.getNowTime(), fwzx + " 不是服务中心", "",
	// LogUtils.getUserId(request));
	// if (logger.isDebugEnabled()) {
	// logger.debug("reg(String, String, String, String, String, String, String,
	// String, String, String, String, String, String, String, String, String,
	// String, String, String, String, String, HttpServletRequest) - end");
	// }
	// return new TTReturnObj(returnString);
	// }
	//
	// int lx_je = lx.equals("1") ? 7000 : 3500;
	//
	// args = new HashMap<String, Object>();
	// args.put("zh", zh);
	// args.put("xm", xm);
	// args.put("xb", xb);
	// args.put("lxdh", lxdh);
	// args.put("dzyx", dzyx);
	// args.put("qq", qq);
	// args.put("khyh", khyh);
	// args.put("khhdz", khhdz);
	// args.put("yhkh", yhkh);
	// args.put("hz", hz);
	// args.put("mm1", mm1);
	// args.put("mm2", mm2);
	// args.put("fwzx", fwzx);
	// args.put("tjr", tjr);
	// args.put("lx", lx);
	// args.put("jdr", jdr);
	// args.put("azwz", azwz2 == null ? azwz : azwz2);
	// args.put("dz1", dz1);
	// String userId = getUserId(request);
	// args.put("cjr", userId);
	// args.put("thb", lx_je);
	// args.put("yhq", 0);// equals ? "70" : "35");
	// int i = template
	// .update("INSERT INTO crm_user(zh, xm, xb, lxdh, dzyx, qq, khyh, khhdz,
	// yhkh, hz, mm1, mm2, fwzx, tjr, lx, jdr, azwz, dz1, cjr, thb, yhq)VALUES
	// (:zh, :xm, :xb, :lxdh, :dzyx, :qq, :khyh, :khhdz, :yhkh, :hz, :mm1, :mm2,
	// :fwzx, :tjr, :lx, :jdr, :azwz, :dz1, :cjr, :thb, :yhq)",
	// args);
	// LogUtils.writeLogDetail(logId, "会员注册", "插入会员信息", "crm_user", "row", i +
	// "", LogUtils.getNowTime(), args.toString());
	// LogUtils.writeLogDetail(logId, "会员注册", "发放提货币", zh, "thb", lx_je + "",
	// LogUtils.getNowTime(), args.toString());
	// // 扣除报单币和优惠券
	// args = new HashMap<String, Object>();
	// args.put("zh", userId);
	// Object yhq_new = new Double(me.get("yhq").toString()) - new
	// Double((yhq));
	// double d = lx_je - new Double(yhq);
	// Object bdb_new = new Double(me.get("bdb").toString()) - d;
	// args.put("yhq", yhq_new);
	// args.put("bdb", bdb_new);
	// template.update("update crm_user set bdb = :bdb , yhq = :yhq where zh =
	// :zh",
	// args);
	// LogUtils.writeLogDetail(logId, "会员注册", "扣除报单币", userId, "bdb", d + "",
	// LogUtils.getNowTime(), args.toString());
	// if (new Double(yhq.toString()) > 0) {
	// LogUtils.writeLogDetail(logId, "会员注册", "扣除优惠券", userId, "yhq", yhq + "",
	// LogUtils.getNowTime(), args.toString());
	// }
	//
	// // 修改接点人,及上级业绩
	// args = new HashMap<String, Object>();
	// args.put("zh", zh);
	// // 业绩修改List
	// List<Map<String, Object>> yjxgList = template.queryForList(
	// "WITH recursive cte AS( SELECT 1 AS DEPTH, a.* FROM crm_user a WHERE
	// zh=:zh"
	// + " UNION ALL"
	// +
	// " SELECT DEPTH + 1 AS DEPTH,b.* FROM crm_user b INNER JOIN cte c ON
	// b.zh=c.jdr"
	// + " ) "
	// +
	// "select c.*,(select azwz from cte d where d.jdr = c. zh ) as qw from cte
	// c where c.depth <> 1",
	// args);
	// for (Map<String, Object> map : yjxgList) {
	// args.put("zh", map.get("zh"));
	// args.put("sl", lx_je);
	// args.put("sl2", !map.get("jb").equals("1") ? 0 : lx_je);
	// args.put("qw", map.get("qw"));
	// template.update("UPDATE crm_user SET "
	// +
	// " zqyj = zqyj + (CASE :qw WHEN '1' THEN :sl ELSE 0 END) , "
	// +
	// " yqyj = yqyj + (CASE :qw WHEN '2' THEN :sl ELSE 0 END) , "
	// +
	// " zqdryj = zqdryj + (CASE :qw WHEN '1' THEN :sl ELSE 0 END) , "
	// +
	// " yqdryj = yqdryj + (CASE :qw WHEN '2' THEN :sl ELSE 0 END) , "
	// +
	// " zqsyyj = zqsyyj + (CASE :qw WHEN '1' THEN :sl2 ELSE 0 END) , "
	// +
	// " yqsyyj = yqsyyj + (CASE :qw WHEN '2' THEN :sl2 ELSE 0 END) where zh =
	// :zh ",
	// args);
	// LogUtils.writeLogDetail(logId, "会员注册", "业绩修改", map.get("zh").toString(),
	// map.get("qw").toString(), lx_je + "",
	// LogUtils.getNowTime(), args.toString());
	// }
	//
	// // 升级经理操作
	// args = new HashMap<String, Object>();
	// args.put("zh", jdr);
	// List<Map<String, Object>> list = template.queryForList(
	// "WITH recursive cte AS( SELECT 1 AS DEPTH, a.* FROM crm_user a WHERE
	// zh=:zh "
	// +
	// " UNION ALL "
	// +
	// " SELECT DEPTH + 1 AS DEPTH,b.* FROM crm_user b INNER JOIN cte c ON
	// b.zh=c.jdr "
	// +
	// " ) "
	// + " select * from cte where cast(jb as int) = 1 and (zqyj+yqyj)>200000 ",
	// args);
	// if (list.size() > 0) {
	//
	// // 如果触发了升级总裁和董事的动作，则需要后续判定是否可以继续升级
	// List<Map<String, Object>> sj_list = new ArrayList<Map<String, Object>>();
	//
	// // 升级_经理_账号
	// Map<String, Object> jl = list.get(0);
	// String zh_jl = (String) jl.get("zh");
	// args = new HashMap<String, Object>();
	// args.put("zh", zh_jl);
	// int ii =
	// template.update("update crm_user set jb = '2',zqsyyj = 0,yqsyyj = 0 where
	// zh = :zh",
	// args);
	// if (ii != 1) {
	// throw new Exception(zh_jl + " 升级经理操作失败");
	// }
	// LogUtils.writeLogDetail(logId, "会员注册", "升级经理", zh_jl, "jb", "1",
	// LogUtils.getNowTime(), args.toString());
	//
	// // 升级经理成功 继续升级推荐人
	// String zh_jl_jdr = (String) jl.get("jdr");
	// args = new HashMap<String, Object>();
	// args.put("zh", zh_jl_jdr);
	// Map<String, Object> jl_jdr =
	// template.queryForMap("select * from crm_user where zh = :zh", args);
	// if (jl_jdr.get("jb").equals("2")) { // 如果经理的上级是经理,则升级为总监
	// int i2 = template.update("update crm_user set jb = '3' where zh = :zh",
	// args);
	// if (i2 != 1) {
	// throw new Exception(zh_jl_jdr + " 升级总监操作失败");
	// }
	// LogUtils.writeLogDetail(logId, "会员注册", "升级总监", zh_jl_jdr, "jb", "1",
	// LogUtils.getNowTime(), args.toString());
	// // 升级总监成功 结束
	//
	// } else if (jl_jdr.get("jb").equals("3")) {// 如果经理的上级是总监,则升级为总裁
	// int i2 = template.update("update crm_user set jb = '4' where zh = :zh",
	// args);
	// if (i2 != 1) {
	// throw new Exception(zh_jl_jdr + " 升级总裁操作失败");
	// }
	// LogUtils.writeLogDetail(logId, "会员注册", "升级总裁", zh_jl_jdr, "jb", "1",
	// LogUtils.getNowTime(), args.toString());
	//
	// jl_jdr.put("jb", new Integer(jl_jdr.get("jb").toString()) + 1);
	// sj_list.add(jl_jdr); // 添加被升级人
	//
	// // 升级总裁后继续升级推荐人
	// String zh_zc_jdr = (String) jl_jdr.get("jdr");
	// args = new HashMap<String, Object>();
	// args.put("zh", zh_zc_jdr);
	// Map<String, Object> user_jdr2 =
	// template.queryForMap("select * from crm_user where zh = :zh", args);
	// if (user_jdr2.get("jb").equals("4")) { // 如果总裁的上级是总裁,则升级为董事
	// int i3 = template.update("update crm_user set jb = '5' where zh = :zh",
	// args);
	// if (i3 != 1) {
	// throw new Exception(zh_zc_jdr + " 升级董事操作失败");
	// }
	// LogUtils.writeLogDetail(logId, "会员注册", "升级董事", zh_zc_jdr, "jb", "1",
	// LogUtils.getNowTime(), args.toString());
	//
	// user_jdr2.put("jb", new Integer(user_jdr2.get("jb").toString()) + 1);
	// sj_list.add(user_jdr2); // 添加被升级人
	//
	// // 升级董事后继续升级推荐人
	// args = new HashMap<String, Object>();
	// args.put("zh", zh_zc_jdr);
	// Map<String, Object> ds_jdr =
	// template.queryForMap("select * from crm_user where zh = :zh", args);
	// String zh_ds_jdr = (String) ds_jdr.get("jdr");
	// args = new HashMap<String, Object>();
	// args.put("zh", zh_ds_jdr);
	// Map<String, Object> user_jdr3 =
	// template.queryForMap("select * from crm_user where zh = :zh", args);
	// if (new Integer(user_jdr3.get("jb").toString()) == 5) { // 董事级别以上有
	// // 新董事晋升,则升级
	// int i4 =
	// template.update("update crm_user set jb = '6', fhjf = 1 where zh = :zh",
	// args);
	// if (i4 != 1) {
	// throw new Exception(zh_ds_jdr + " 升级" + (new
	// Integer(user_jdr3.get("jb").toString()) - 4)
	// + "星董事操作失败");
	// }
	// LogUtils.writeLogDetail(logId, "会员注册",
	// "升级" + (new Integer(user_jdr3.get("jb").toString()) - 4) + "星董事",
	// zh_ds_jdr, "jb", "1",
	// LogUtils.getNowTime(), args.toString());
	//
	// } else if (new Integer(user_jdr3.get("jb").toString()) == 6) { // 董事级别以上有
	// // 新董事晋升,则升级
	// int i4 =
	// template.update("update crm_user set jb = '7', fhjf = 3 where zh = :zh",
	// args);
	// if (i4 != 1) {
	// throw new Exception(zh_ds_jdr + " 升级" + (new
	// Integer(user_jdr3.get("jb").toString()) - 4)
	// + "星董事操作失败");
	// }
	// LogUtils.writeLogDetail(logId, "会员注册",
	// "升级" + (new Integer(user_jdr3.get("jb").toString()) - 4) + "星董事",
	// zh_ds_jdr, "jb", "1",
	// LogUtils.getNowTime(), args.toString());
	//
	// } else if (new Integer(user_jdr3.get("jb").toString()) == 7) { // 董事级别以上有
	// // 新董事晋升,则升级
	// int i4 =
	// template.update("update crm_user set jb = '8', fhjf = 6 where zh = :zh",
	// args);
	// if (i4 != 1) {
	// throw new Exception(zh_ds_jdr + " 升级" + (new
	// Integer(user_jdr3.get("jb").toString()) - 4)
	// + "星董事操作失败");
	// }
	// LogUtils.writeLogDetail(logId, "会员注册",
	// "升级" + (new Integer(user_jdr3.get("jb").toString()) - 4) + "星董事",
	// zh_ds_jdr, "jb", "1",
	// LogUtils.getNowTime(), args.toString());
	//
	// } else if (new Integer(user_jdr3.get("jb").toString()) == 8) { // 董事级别以上有
	// // 新董事晋升,则升级
	// int i4 =
	// template.update("update crm_user set jb = '9', fhjf = 10 where zh = :zh",
	// args);
	// if (i4 != 1) {
	// throw new Exception(zh_ds_jdr + " 升级" + (new
	// Integer(user_jdr3.get("jb").toString()) - 4)
	// + "星董事操作失败");
	// }
	// LogUtils.writeLogDetail(logId, "会员注册",
	// "升级" + (new Integer(user_jdr3.get("jb").toString()) - 4) + "星董事",
	// zh_ds_jdr, "jb", "1",
	// LogUtils.getNowTime(), args.toString());
	//
	// } else if (new Integer(user_jdr3.get("jb").toString()) > 8) { // 董事级别以上有
	// // 新董事晋升,则升级
	// int i4 = template.update("update crm_user set jb = '"
	// + (new Integer(user_jdr3.get("jb").toString()) + 1) +
	// "' , fhjf = fhjf+5 where zh = :zh", args);
	// if (i4 != 1) {
	// throw new Exception(zh_ds_jdr + " 升级" + (new
	// Integer(user_jdr3.get("jb").toString()) - 4)
	// + "星董事操作失败");
	// }
	// LogUtils.writeLogDetail(logId, "会员注册",
	// "升级" + (new Integer(user_jdr3.get("jb").toString()) - 4) + "星董事",
	// zh_ds_jdr, "jb", "1",
	// LogUtils.getNowTime(), args.toString());
	// }
	// }
	// }
	//
	// Map<String, Object> user3 = null;
	// // 二次升级操作
	// for (Map<String, Object> user : sj_list) {
	// args = new HashMap<String, Object>();
	// args.put("zh", user.get("zh"));
	// list = template.queryForList("select * from crm_user where jdr = :zh",
	// args);
	// for (Map<String, Object> child : list) {
	// if (new Integer(child.get("jb").toString()) >= new
	// Integer(user.get("jb").toString())) {
	// if (user.get("jb").equals("4")) {
	// // 三次升级操作
	// user3 = user;
	// }
	// args = new HashMap<String, Object>();
	// args.put("zh", user.get("zh"));
	// int newJb = new Integer(user.get("jb").toString()) + 1;
	// args.put("jb", newJb);
	// args.put("fhjf", newJb == 6 ? 1 : 0);
	// template.update("update crm_user set jb = :jb,fhjf = :fhjf where zh =
	// :zh",
	// args);
	// LogUtils.writeLogDetail(logId, "会员注册", "升级2", user.get("zh").toString(),
	// "jb", newJb + "",
	// LogUtils.getNowTime(), args.toString());
	// }
	// }
	// }
	// // 三次升级
	// if (user3 != null) {
	// args = new HashMap<String, Object>();
	// args.put("zh", user3.get("zh"));
	// list = template.queryForList("select * from crm_user where jdr = :zh",
	// args);
	// for (Map<String, Object> child : list) {
	// if (new Integer(child.get("jb").toString()) >= new
	// Integer(user3.get("jb").toString())) {
	// args = new HashMap<String, Object>();
	// args.put("zh", user3.get("zh"));
	// int newJb = new Integer(user3.get("jb").toString()) + 1;
	// args.put("jb", newJb);
	// args.put("fhjf", newJb == 6 ? 1 : 0);
	// template.update("update crm_user set jb = :jb,fhjf = :fhjf where zh =
	// :zh",
	// args);
	// LogUtils.writeLogDetail(logId, "会员注册", "升级3", user3.get("zh").toString(),
	// "jb", newJb + "",
	// LogUtils.getNowTime(), args.toString());
	// }
	// }
	// }
	//
	// }
	//
	// // 修改推荐人数
	// args = new HashMap<String, Object>();
	// args.put("zh", tjr);
	// template.update("update crm_user set tjrs = tjrs+1 where zh = :zh",
	// args);
	// LogUtils.writeLogDetail(logId, "会员注册", "修改推荐人数", tjr.toLowerCase(),
	// "tjrs", "1", LogUtils.getNowTime(), args.toString());
	//
	// } catch (Exception e) {
	// logger.error("doInTransaction(TransactionStatus)", e);
	//
	// ts.setRollbackOnly();
	//
	// if (logger.isDebugEnabled()) {
	// logger.debug("doInTransaction(TransactionStatus) - end");
	// }
	//
	// return new
	// TTReturnObj("alert('注册失败!');$('#button').attr('disabled',false);$('#sb_box2').hide();",
	// e);
	// }
	//
	// LogUtils.writeLogS(logId, SERVICE_NAME, "reg", startTime,
	// LogUtils.getNowTime(), msg, LogUtils.getUserId(request));
	//
	// if (logger.isDebugEnabled()) {
	// logger.debug("doInTransaction(TransactionStatus) - end");
	// }
	// return new TTReturnObj(
	// "alert('注册成功!');$('#button').attr('disabled',false);$('#sb_box2').hide();$('#sb_box2').hide();//load('wzcdhy.html');");
	// }
	//
	// private boolean isCompany(HttpServletRequest request) {
	// if (logger.isDebugEnabled()) {
	// logger.debug("isCompany(HttpServletRequest) - start");
	// }
	//
	// if (getUserId(request).equals(CodeUtils.getValue("COMPANY", "1"))) {
	// if (logger.isDebugEnabled()) {
	// logger.debug("isCompany(HttpServletRequest) - end");
	// }
	// return true;
	// }
	//
	// if (logger.isDebugEnabled()) {
	// logger.debug("isCompany(HttpServletRequest) - end");
	// }
	// return false;
	// }
	// });
	//
	// if (ttReturnObj.hasException()) {
	// LogUtils.writeLogE(logId, SERVICE_NAME,
	// Thread.currentThread().getStackTrace()[1].getMethodName(), startTime,
	// LogUtils.getNowTime(), ttReturnObj.getE().getMessage(),
	// LogUtils.getStackTrace(ttReturnObj.getE()),
	// LogUtils.getUserId(request));
	//
	// }
	// if (logger.isDebugEnabled()) {
	// logger.debug("reg(String, String, String, String, String, String, String,
	// String, String, String, String, String, String, String, String, String,
	// String, String, String, String, String, HttpServletRequest) - end");
	// }
	//
	// return ttReturnObj.getMsg();
	// }

	@POST
	@Path("reg")
	public String reg(@FormParam("mymm2") final String mymm2, @FormParam("name") final String zh,
			@FormParam("userlevel") final String lx, @FormParam("yhq") final String yhq2,
			@FormParam("password1") final String mm1, @FormParam("password11") final String mm11,
			@FormParam("password2") final String mm2, @FormParam("password22") final String mm22,
			@FormParam("fwcenter") final String fwzx, @FormParam("ztuser") final String tjr,
			@FormParam("azuser") final String jdr, @FormParam("azsite") final String azwz,
			@FormParam("realname") final String xm, @FormParam("sex") final String xb,
			@FormParam("tel") final String lxdh, @FormParam("email") final String dzyx,
			@FormParam("qq") final String qq, @FormParam("BankName") final String khyh,
			@FormParam("bankaddr") final String khhdz, @FormParam("bankcard") final String yhkh,
			@FormParam("cardusername") final String hz, @FormParam("dz1") final String dz1,
			@Context final HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug(
					"reg(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, HttpServletRequest) - start");
		}

		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		TransactionTemplate tt = new TransactionTemplate(getTransactionManager());
		TTReturnObj ttReturnObj = tt.execute(new TransactionCallback<TTReturnObj>() {
			public TTReturnObj doInTransaction(TransactionStatus ts) {
				if (logger.isDebugEnabled()) {
					logger.debug("doInTransaction(TransactionStatus) - start");
				}

				String msg = "";
				long logId = LogUtils.getLogId();
				String startTime = LogUtils.getNowTime();

				try {
					NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);

					if (!checkMm2(mymm2, request)) {
						TTReturnObj returnTTReturnObj = new TTReturnObj(
								"alert('二级密码不正确');$('#button').attr('disabled',false);$('#sb_box2').hide();");
						if (logger.isDebugEnabled()) {
							logger.debug("doInTransaction(TransactionStatus) - end");
						}
						return returnTTReturnObj;
					}

					Map<String, Object> me = initMe(request);

					Double yhq = null;
					try {
						yhq = new Double(yhq2);
					} catch (Exception e) {
						yhq = 0.0;
					}

					if (azwz == null || azwz.equals("")) {
						LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime, LogUtils.getNowTime(), "请选择安置位置", "",
								LogUtils.getUserId(request));
						if (logger.isDebugEnabled()) {
							logger.debug(
									"reg(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, HttpServletRequest) - end");
						}
						return new TTReturnObj(
								"alert('请选择安置位置');$('#button').attr('disabled',false);$('#sb_box2').hide();");
					}

					// 表单信息验证
					if (!mm1.equals(mm11)) {
						LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime, LogUtils.getNowTime(), "登录密码不一致", "",
								LogUtils.getUserId(request));
						if (logger.isDebugEnabled()) {
							logger.debug(
									"reg(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, HttpServletRequest) - end");
						}
						return new TTReturnObj(
								"alert('登录密码不一致');$('#button').attr('disabled',false);$('#sb_box2').hide();");
					}

					if (!mm2.equals(mm22)) {
						LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime, LogUtils.getNowTime(), "二级密码不一致", "",
								LogUtils.getUserId(request));
						if (logger.isDebugEnabled()) {
							logger.debug(
									"reg(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, HttpServletRequest) - end");
						}
						return new TTReturnObj(
								"alert('二级密码不一致');$('#button').attr('disabled',false);$('#sb_box2').hide();");
					}

					Map<String, Object> args = new HashMap<String, Object>();
					args.put("zh", zh);
					if (template.queryForInt("select count(1) from crm_user where zh = :zh", args) > 0) {
						String returnString = "alert('账号:" + zh
								+ "重复');$('#button').attr('disabled',false);$('#sb_box2').hide();";
						LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime, LogUtils.getNowTime(),
								"账号:" + zh + "重复", "", LogUtils.getUserId(request));
						if (logger.isDebugEnabled()) {
							logger.debug(
									"reg(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, HttpServletRequest) - end");
						}
						return new TTReturnObj(returnString);
					}

					args = new HashMap<String, Object>();
					args.put("zh", tjr);
					if (template.queryForInt("select count(1) from crm_user where zh = :zh and yxzt = '1'",
							args) == 0) {
						String returnString = "alert('推荐人:" + tjr
								+ "不存在');$('#button').attr('disabled',false);$('#sb_box2').hide();";
						LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime, LogUtils.getNowTime(),
								"推荐人:" + tjr + "不存在", "", LogUtils.getUserId(request));
						if (logger.isDebugEnabled()) {
							logger.debug(
									"reg(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, HttpServletRequest) - end");
						}
						return new TTReturnObj(returnString);
					}

					args = new HashMap<String, Object>();
					args.put("zh", jdr);
					if (template.queryForInt("select count(1) from crm_user where zh = :zh and yxzt = '1'",
							args) == 0) {
						String returnString = "alert('接点人:" + jdr
								+ "不存在');$('#button').attr('disabled',false);$('#sb_box2').hide();";
						LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime, LogUtils.getNowTime(),
								"接点人:" + jdr + "不存在", "", LogUtils.getUserId(request));
						if (logger.isDebugEnabled()) {
							logger.debug(
									"reg(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, HttpServletRequest) - end");
						}
						return new TTReturnObj(returnString);
					}
					List<Map<String, Object>> queryForList = template
							.queryForList("select azwz from crm_user where jdr = :zh", args);
					String azwz2 = null;
					if (queryForList.size() >= 2) {
						Map<String, Object> jdrMap = template.queryForMap("select * from crm_user where zh = :zh",
								args);
						if (new Integer(jdrMap.get("jb").toString()) <= 4) {
							LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime, LogUtils.getNowTime(),
									"接点人:" + jdr + "已经有2个会员,请选择其他接点人", "", LogUtils.getUserId(request));
							String returnString2 = "alert('接点人:" + jdr
									+ "已经有2个会员,请选择其他接点人');$('#button').attr('disabled',false);$('#sb_box2').hide();";
							if (logger.isDebugEnabled()) {
								logger.debug("doInTransaction(TransactionStatus) - end");
							}
							return new TTReturnObj(returnString2);
						}

						List<Map<String, Object>> list = template
								.queryForList("select * from crm_user where jdr = :zh and azwz <> '0'", args);
						int i = list.size() + 1;
						azwz2 = i + "";

					}
					if (queryForList.size() == 1) {
						if (queryForList.get(0).get("azwz").equals(azwz) && !isCompany(request)) {
							String returnString = "alert('接点人:" + jdr + " " + azwz
									+ "区已经有人了');$('#button').attr('disabled',false);$('#sb_box2').hide();";
							LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime, LogUtils.getNowTime(),
									"接点人:" + jdr + " " + azwz + "区已经有人了", "", LogUtils.getUserId(request));
							if (logger.isDebugEnabled()) {
								logger.debug(
										"reg(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, HttpServletRequest) - end");
							}
							return new TTReturnObj(returnString);
						}
					}

					args = new HashMap<String, Object>();
					args.put("zh", fwzx);
					List<Map<String, Object>> fwzx_userList = template
							.queryForList("select sffwzx from crm_user where zh = :zh and yxzt = '1'", args);
					if (fwzx_userList.size() == 0) {
						String returnString = "alert('服务中心:" + fwzx
								+ "不存在');$('#button').attr('disabled',false);$('#sb_box2').hide();";
						LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime, LogUtils.getNowTime(),
								"服务中心:" + fwzx + "不存在", "", LogUtils.getUserId(request));
						if (logger.isDebugEnabled()) {
							logger.debug(
									"reg(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, HttpServletRequest) - end");
						}
						return new TTReturnObj(returnString);
					} else if (fwzx_userList.get(0).get("sffwzx").equals("0")) {
						String returnString = "alert('" + fwzx
								+ " 不是服务中心');$('#button').attr('disabled',false);$('#sb_box2').hide();";
						LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime, LogUtils.getNowTime(),
								fwzx + " 不是服务中心", "", LogUtils.getUserId(request));
						if (logger.isDebugEnabled()) {
							logger.debug(
									"reg(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, HttpServletRequest) - end");
						}
						return new TTReturnObj(returnString);
					}

					int lx_je = lx.equals("1") ? 7000 : 3500;

					args = new HashMap<String, Object>();
					args.put("zh", zh);
					args.put("xm", xm);
					args.put("xb", xb);
					args.put("lxdh", lxdh);
					args.put("dzyx", dzyx);
					args.put("qq", qq);
					args.put("khyh", khyh);
					args.put("khhdz", khhdz);
					args.put("yhkh", yhkh);
					args.put("hz", hz);
					args.put("mm1", mm1);
					args.put("mm2", mm2);
					args.put("fwzx", fwzx);
					args.put("tjr", tjr);
					args.put("lx", lx);
					args.put("jdr", jdr);
					args.put("azwz", azwz2 == null ? azwz : azwz2);
					args.put("dz1", dz1);
					String userId = getUserId(request);
					args.put("cjr", userId);
					args.put("thb", lx_je);
					args.put("yhq", 0);// equals ? "70" : "35");
					int i = template.update(
							"INSERT INTO crm_user(zh, xm, xb, lxdh, dzyx, qq, khyh, khhdz, yhkh, hz, mm1, mm2, fwzx, tjr, lx, jdr, azwz, dz1, cjr, thb, yhq)VALUES (:zh, :xm, :xb, :lxdh, :dzyx, :qq, :khyh, :khhdz, :yhkh, :hz, :mm1, :mm2, :fwzx, :tjr, :lx, :jdr, :azwz, :dz1, :cjr, :thb, :yhq)",
							args);
					LogUtils.writeLogDetail(logId, "会员注册", "插入会员信息", "crm_user", "row", i + "", LogUtils.getNowTime(),
							args.toString());
					LogUtils.writeLogDetail(logId, "会员注册", "发放提货币", zh, "thb", lx_je + "", LogUtils.getNowTime(),
							args.toString());

				} catch (Exception e) {
					logger.error("doInTransaction(TransactionStatus)", e);

					ts.setRollbackOnly();

					TTReturnObj returnTTReturnObj = new TTReturnObj(
							"alert('注册失败!');$('#button').attr('disabled',false);$('#sb_box2').hide();", e);
					if (logger.isDebugEnabled()) {
						logger.debug("doInTransaction(TransactionStatus) - end");
					}
					return returnTTReturnObj;
				}

				LogUtils.writeLogS(logId, SERVICE_NAME, "reg", startTime, LogUtils.getNowTime(), msg,
						LogUtils.getUserId(request));

				TTReturnObj returnTTReturnObj = new TTReturnObj(
						"alert('注册成功!');$('#button').attr('disabled',false);$('#sb_box2').hide();$('#sb_box2').hide();load('hysh.html');");
				if (logger.isDebugEnabled()) {
					logger.debug("doInTransaction(TransactionStatus) - end");
				}
				return returnTTReturnObj;
			}

		});

		if (ttReturnObj.hasException()) {
			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), ttReturnObj.getE().getMessage(),
					LogUtils.getStackTrace(ttReturnObj.getE()), LogUtils.getUserId(request));

		}
		return ttReturnObj.getMsg();
	}

	private boolean isCompany(HttpServletRequest request) {

		if (getUserId(request).equals(CodeUtils.getValue("COMPANY", "1"))) {
			return true;
		}

		return false;
	}

	private boolean checkMm2(String mymm2, HttpServletRequest request) {
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("zh", getUserId(request));
		args.put("mm2", mymm2);
		if (template.queryForInt("select count(1) from crm_user where zh = :zh and mm2 = :mm2 and yxzt = '1'",
				args) == 0) {
			return false;
		}
		return true;
	}

	@POST
	@Path("updateUserMm1")
	public String updateUserMm1(@FormParam("mymm2") final String mymm2, @FormParam("oldmm") String oldmm,
			@FormParam("mm1") String mm1, @FormParam("mm11") String mm11, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateUserMm1(String, String, String, HttpServletRequest) - start");
		}

		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		try {
			if (!checkMm2(mymm2, request)) {
				return "alert('二级密码不正确');$('#sb_box2').hide();";
			}
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			String userId = getUserId(request);
			args.put("zh", userId);
			args.put("mm1", oldmm);

			if (template.queryForInt("select count(1) from crm_user where zh = :zh and mm1 = :mm1 and yxzt = '1'",
					args) == 0) {
				LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), "原始密码不正确", "", LogUtils.getUserId(request));
				if (logger.isDebugEnabled()) {
					logger.debug("updateUserMm1(String, String, String, HttpServletRequest) - end");
				}
				return "alert('原始密码不正确');$('#sb_box2').hide();";
			}

			if (!mm1.equals(mm11)) {
				LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), "两次密码不一致", "", LogUtils.getUserId(request));
				if (logger.isDebugEnabled()) {
					logger.debug("updateUserMm1(String, String, String, HttpServletRequest) - end");
				}
				return "alert('两次密码不一致');$('#sb_box2').hide();";
			}
			int i = jdbcTemplate.update("update crm_user set mm1 = ? where zh = ?", new Object[] { mm1, userId },
					new int[] { Types.VARCHAR, Types.VARCHAR });
			if (i == 1) {
				LogUtils.writeLogDetail(logId, "修改登录密码", "", userId, "mm1", mm1, LogUtils.getNowTime(),
						args.toString());
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));
				if (logger.isDebugEnabled()) {
					logger.debug("updateUserMm1(String, String, String, HttpServletRequest) - end");
				}
				return "alert('登录密码修改成功');$('#sb_box2').hide();";
			}

			if (logger.isDebugEnabled()) {
				logger.debug("updateUserMm1(String, String, String, HttpServletRequest) - end");
			}
			return "alert('登录密码修改失败');$('#sb_box2').hide();";
		} catch (Exception e) {
			logger.error("updateUserMm1(String, String, String, HttpServletRequest)", e);

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("updateUserMm1(String, String, String, HttpServletRequest) - end");
			}
			return "alert('登录密码修改失败');$('#sb_box2').hide();";
		}
	}

	@POST
	@Path("updateUserMm2")
	public String updateUserMm2(@FormParam("oldmm") String oldmm, @FormParam("mm2") String mm2,
			@FormParam("mm22") String mm22, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateUserMm2(String, String, String, HttpServletRequest) - start");
		}

		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();

		try {
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			String userId = getUserId(request);
			args.put("zh", userId);
			args.put("mm2", oldmm);

			if (template.queryForInt("select count(1) from crm_user where zh = :zh and mm2 = :mm2 and yxzt = '1'",
					args) == 0) {
				LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), "原始密码不正确", "", LogUtils.getUserId(request));
				if (logger.isDebugEnabled()) {
					logger.debug("updateUserMm2(String, String, String, HttpServletRequest) - end");
				}
				return "alert('原始密码不正确');$('#sb_box2').hide();";
			}

			if (!mm2.equals(mm22)) {
				LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), "两次密码不一致", "", LogUtils.getUserId(request));
				if (logger.isDebugEnabled()) {
					logger.debug("updateUserMm2(String, String, String, HttpServletRequest) - end");
				}
				return "alert('两次密码不一致');$('#sb_box2').hide();";
			}
			int i = jdbcTemplate.update("update crm_user set mm2 = ? where zh = ?", new Object[] { mm2, userId },
					new int[] { Types.VARCHAR, Types.VARCHAR });
			if (i == 1) {
				LogUtils.writeLogDetail(logId, "修改二级密码", "", userId, "mm2", mm2, LogUtils.getNowTime(),
						args.toString());
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));
				if (logger.isDebugEnabled()) {
					logger.debug("updateUserMm2(String, String, String, HttpServletRequest) - end");
				}
				return "alert('二级密码修改成功');$('#sb_box2').hide();";
			}

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), "二级密码修改失败", args.toString(), LogUtils.getUserId(request));
			if (logger.isDebugEnabled()) {
				logger.debug("updateUserMm2(String, String, String, HttpServletRequest) - end");
			}
			return "alert('二级密码修改失败');$('#sb_box2').hide();";
		} catch (DataAccessException e) {
			logger.error("updateUserMm2(String, String, String, HttpServletRequest)", e);
			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("updateUserMm2(String, String, String, HttpServletRequest) - end");
			}
			return "alert('二级密码修改失败');$('#sb_box2').hide();";
		}
	}

	@POST
	@Path("resetMm")
	public String resetMm(@FormParam("zh") String zh, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("resetMm(String, HttpServletRequest) - start");
		}

		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();

		try {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", zh);

			int i = jdbcTemplate.update("update crm_user set mm1 = '123456',mm2 = '123456' where zh = ?",
					new Object[] { zh }, new int[] { Types.VARCHAR });
			if (i == 1) {
				LogUtils.writeLogDetail(logId, "重置密码", "", zh + "", "", "", LogUtils.getNowTime(), args.toString());
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));
				if (logger.isDebugEnabled()) {
					logger.debug("resetMm(String, HttpServletRequest) - end");
				}
				return "alert('重置成功');$('#sb_box2').hide();";
			}

			if (logger.isDebugEnabled()) {
				logger.debug("resetMm(String, HttpServletRequest) - end");
			}
			return "alert('重置失败');$('#sb_box2').hide();";
		} catch (Exception e) {
			logger.error("resetMm(String, HttpServletRequest)", e);

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("resetMm(String, HttpServletRequest) - end");
			}
			return "alert('重置失败');$('#sb_box2').hide();";

		}
	}

	@POST
	@Path("yhjy")
	public String yhjy(@FormParam("zh") String zh, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("yhjy(String, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();

		try {

			if("100000".equals(zh)){
				return "alert('不能仅用公司账号');$('#sb_box2').hide();";
			}

			if("100000".equals(zh)){
				return "alert('不能仅用公司账号');$('#sb_box2').hide();";
			}
			
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", zh);

			int i = jdbcTemplate.update("update crm_user set yxzt = '2' where zh = ? and yxzt = '1'", new Object[] { zh },
					new int[] { Types.VARCHAR });
			if (i == 1) {
				LogUtils.writeLogDetail(logId, "用户禁用", "", zh + "", "", "", LogUtils.getNowTime(), args.toString());
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));

				if (logger.isDebugEnabled()) {
					logger.debug("yhjy(String, HttpServletRequest) - end"); //$NON-NLS-1$
				}
				return "alert('用户禁用成功');load('yhjy.html');";
			}

			if (logger.isDebugEnabled()) {
				logger.debug("yhjy(String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return "alert('用户禁用失败');$('#sb_box2').hide();";
		} catch (Exception e) {
			logger.error("yhjy(String, HttpServletRequest)", e); //$NON-NLS-1$

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("yhjy(String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return "alert('用户禁用失败');$('#sb_box2').hide();";

		}
	}
	
	@POST
	@Path("yhjyhf")
	public String yhjyhf(@FormParam("zh") String zh, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("yhjyhf(String, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();

		try {
			
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", zh);

			int i = jdbcTemplate.update("update crm_user set yxzt = '1' where zh = ? and yxzt = '2'", new Object[] { zh },
					new int[] { Types.VARCHAR });
			if (i == 1) {
				LogUtils.writeLogDetail(logId, "用户禁用恢复", "", zh + "", "", "", LogUtils.getNowTime(), args.toString());
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));

				if (logger.isDebugEnabled()) {
					logger.debug("yhjyhf(String, HttpServletRequest) - end"); //$NON-NLS-1$
				}
				return "alert('用户禁用恢复成功');load('yhjy.html');";
			}

			if (logger.isDebugEnabled()) {
				logger.debug("yhjyhf(String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return "alert('用户禁用恢复失败');$('#sb_box2').hide();";
		} catch (Exception e) {
			logger.error("yhjyhf(String, HttpServletRequest)", e); //$NON-NLS-1$

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("yhjyhf(String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return "alert('用户禁用恢复失败');$('#sb_box2').hide();";

		}
	}

	@POST
	@Path("ffbdb")
	public String ffbdb(@FormParam("zh") final String zh, @FormParam("sl") final String sl,
			@Context final HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("ffbdb(String, String, HttpServletRequest) - start");
		}

		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		TransactionTemplate tt = new TransactionTemplate(getTransactionManager());
		TTReturnObj ttReturnObj = tt.execute(new TransactionCallback<TTReturnObj>() {
			public TTReturnObj doInTransaction(TransactionStatus ts) {
				if (logger.isDebugEnabled()) {
					logger.debug("doInTransaction(TransactionStatus) - start");
				}
				String msg = "";
				long logId = LogUtils.getLogId();
				String startTime = LogUtils.getNowTime();
				try {
					String userId = getUserId(request);
					if (userId.equals(zh)) {
						if (logger.isDebugEnabled()) {
							logger.debug("doInTransaction(TransactionStatus) - end");
						}
						return new TTReturnObj("alert('不能给自己发放');$('#sb_box2').hide();");
					}
					NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
					Map<String, Object> args = new HashMap<String, Object>();
					args.put("zh", zh);
					args.put("sl", new Double(sl));

					int i = template.update("update crm_user set bdb = bdb + :sl where zh = :zh", args);
					LogUtils.writeLogDetail(logId, "发放报单币", "增加", zh, "bdb", sl + "", LogUtils.getNowTime(),
							args.toString());

					args.put("zh", userId);
					int i2 = template.update("update crm_user set bdb = bdb - :sl where zh = :zh", args);
					LogUtils.writeLogDetail(logId, "发放报单币", "减少", userId, "bdb", sl + "", LogUtils.getNowTime(),
							args.toString());

					if (i == 1 && i2 == 1) {
						LogUtils.writeLogS(logId, SERVICE_NAME, "ffbdb", startTime, LogUtils.getNowTime(), msg,
								LogUtils.getUserId(request));
						if (logger.isDebugEnabled()) {
							logger.debug("doInTransaction(TransactionStatus) - end");
						}
						return new TTReturnObj("alert('发放成功');load('ffbdb.html');");
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("doInTransaction(TransactionStatus) - end");
						}
						return new TTReturnObj("alert('发放失败');$('#sb_box2').hide();");
					}

				} catch (Exception e) {
					logger.error("doInTransaction(TransactionStatus)", e);

					ts.setRollbackOnly();
					if (logger.isDebugEnabled()) {
						logger.debug("doInTransaction(TransactionStatus) - end");
					}
					return new TTReturnObj("alert('发放失败');$('#sb_box2').hide();");

				}
			}
		});
		if (ttReturnObj.hasException()) {
			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), ttReturnObj.getE().getMessage(),
					LogUtils.getStackTrace(ttReturnObj.getE()), LogUtils.getUserId(request));
		}
		if (logger.isDebugEnabled()) {
			logger.debug("ffbdb(String, String, HttpServletRequest) - end");
		}

		return ttReturnObj.getMsg();
	}

	@POST
	@Path("bdbhz")
	public String bdbhz(@FormParam("mymm2") final String mymm2, @FormParam("zh") final String zh,
			@FormParam("sl") final String sl, @Context final HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("bdbhz(String, String, HttpServletRequest) - start");
		}

		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		TransactionTemplate tt = new TransactionTemplate(getTransactionManager());
		TTReturnObj ttReturnObj = tt.execute(new TransactionCallback<TTReturnObj>() {
			public TTReturnObj doInTransaction(TransactionStatus ts) {
				if (logger.isDebugEnabled()) {
					logger.debug("doInTransaction(TransactionStatus) - start");
				}
				if (!checkMm2(mymm2, request)) {
					TTReturnObj returnTTReturnObj = new TTReturnObj("alert('二级密码不正确');$('#sb_box2').hide();");
					if (logger.isDebugEnabled()) {
						logger.debug("doInTransaction(TransactionStatus) - end");
					}
					return returnTTReturnObj;
				}
				String msg = "";
				long logId = LogUtils.getLogId();
				String startTime = LogUtils.getNowTime();
				try {
					if (zh == null || "".equals(zh)) {
						if (logger.isDebugEnabled()) {
							logger.debug("doInTransaction(TransactionStatus) - end");
						}
						return new TTReturnObj("alert('请输入账号');$('#sb_box2').hide();");
					}

					String userId = getUserId(request);
					if (userId.equals(zh)) {
						if (logger.isDebugEnabled()) {
							logger.debug("doInTransaction(TransactionStatus) - end");
						}
						return new TTReturnObj("alert('不能给自己转账');$('#sb_box2').hide();");
					}

					NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
					Map<String, Object> args = new HashMap<String, Object>();
					args.put("zh", zh);
					if (template.queryForInt("select count(1) from crm_user where zh = :zh and yxzt = '1'",
							args) == 0) {
						String returnString = "alert('账号:" + zh + "不存在');$('#sb_box2').hide();";

						if (logger.isDebugEnabled()) {
							logger.debug("doInTransaction(TransactionStatus) - end");
						}
						return new TTReturnObj(returnString);
					}

					args.put("zh", userId);
					args.put("zh2", zh);
					if (template.queryForInt(
							"select count(1) from (\n" + "        (\n"
									+ "        WITH recursive cte AS( SELECT a.* FROM crm_user a WHERE zh = :zh and yxzt = '1' UNION ALL SELECT b.* FROM crm_user b INNER JOIN cte c ON b.zh=c.jdr where b.yxzt = '1')\n"
									+ "        select * from cte\n" + "        )\n" + "        union\n" + "        (\n"
									+ "        WITH recursive cte2 AS( SELECT a.* FROM crm_user a WHERE zh = :zh and yxzt = '1' UNION ALL SELECT b.* FROM crm_user b INNER JOIN cte2 c ON b.jdr=c.zh where b.yxzt = '1')\n"
									+ "        select * from cte2\n" + "        )\n" + ") aaa where zh = :zh2",
							args) == 0) {
						String returnString = "alert('" + zh + "跟你不在一条主线上');$('#sb_box2').hide();";
						if (logger.isDebugEnabled()) {
							logger.debug("doInTransaction(TransactionStatus) - end");
						}
						return new TTReturnObj(returnString);
					}

					Map<String, Object> me = initMe(request);
					if (new Double(me.get("bdb").toString()) < new Double(sl)) {
						String returnString = "alert('剩余报单币币不够" + sl + "');$('#sb_box2').hide();";
						if (logger.isDebugEnabled()) {
							logger.debug("bdbzz(String, HttpServletRequest) - end");
						}
						return new TTReturnObj(returnString);
					}

					java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
					nf.setGroupingUsed(false);
					String newbdb = nf.format((new Double(me.get("bdb").toString()) - new Double(sl)));
					args.put("sl", new Double(sl));
					args.put("zh", zh);
					Map<String, Object> zhUser = getUserByZh(zh);
					String newbdb2 = nf.format((new Double(zhUser.get("bdb").toString()) + new Double(sl)));
					int i = template.update("update crm_user set bdb = bdb + :sl where zh = :zh", args);
					LogUtils.writeLogDetail(logId, "报单币互转", "增加", zh, "bdb", sl + "", LogUtils.getNowTime(),
							zhUser.get("bdb").toString(), userId + "转入", newbdb2);

					args.put("zh", userId);
					int i2 = template.update("update crm_user set bdb = bdb - :sl where zh = :zh", args);
					LogUtils.writeLogDetail(logId, "报单币互转", "减少", userId, "bdb", sl + "", LogUtils.getNowTime(),
							me.get("bdb").toString(), "转给" + zh, newbdb);

					if (i == 1 && i2 == 1) {
						LogUtils.writeLogS(logId, SERVICE_NAME, "bdbhz", startTime, LogUtils.getNowTime(), msg,
								LogUtils.getUserId(request));
						if (logger.isDebugEnabled()) {
							logger.debug("doInTransaction(TransactionStatus) - end");
						}
						return new TTReturnObj("alert('转账成功');load('bdbzz.html');");
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("doInTransaction(TransactionStatus) - end");
						}
						return new TTReturnObj("alert('转账失败');$('#sb_box2').hide();");
					}

				} catch (Exception e) {
					logger.error("doInTransaction(TransactionStatus)", e);

					ts.setRollbackOnly();
					if (logger.isDebugEnabled()) {
						logger.debug("doInTransaction(TransactionStatus) - end");
					}
					return new TTReturnObj("alert('转账失败');$('#sb_box2').hide();", e);
				}
			}
		});
		if (ttReturnObj.hasException()) {
			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), ttReturnObj.getE().getMessage(),
					LogUtils.getStackTrace(ttReturnObj.getE()), LogUtils.getUserId(request));

		}
		if (logger.isDebugEnabled()) {
			logger.debug("bdbhz(String, String, HttpServletRequest) - end");
		}

		return ttReturnObj.getMsg();
	}

	@POST
	@Path("zdfwzx")
	public String zdfwzx(@FormParam("zh") final String zh, @Context final HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("zdfwzx(String, HttpServletRequest) - start");
		}
		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		try {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", zh);
			int i = jdbcTemplate.update("update crm_user set sffwzx = '1' where zh = ?", new Object[] { zh },
					new int[] { Types.VARCHAR });
			if (i == 1) {
				LogUtils.writeLogDetail(logId, "指定服务中心", "", zh, "sffwzx", "1", LogUtils.getNowTime(), args.toString());
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));
				if (logger.isDebugEnabled()) {
					logger.debug("zdfwzx(String, HttpServletRequest) - end");
				}
				return "alert('操作成功');load('zdfwzx.html');";
			}

			if (logger.isDebugEnabled()) {
				logger.debug("zdfwzx(String, HttpServletRequest) - end");
			}
			return "alert('操作失败');$('#sb_box2').hide();";
		} catch (DataAccessException e) {
			logger.error("zdfwzx(String, HttpServletRequest)", e);

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("zdfwzx(String, HttpServletRequest) - end");
			}
			return "alert('操作失败');$('#sb_box2').hide();";
		}
	}

	@POST
	@Path("xyj")
	public String xyj(@FormParam("sjr") String sjr, @FormParam("bt") String bt, @FormParam("nr") String nr,
			@Context final HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("xyj(String, String, String, HttpServletRequest) - start");
		}

		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		try {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("sjr", sjr);
			if (template.queryForInt("select count(1) from crm_user where zh = :sjr and yxzt = '1'", args) == 0) {
				return "alert('收件人" + sjr + "不存在');$('#sb_box2').hide();";
			}
			args.put("bt", bt);
			args.put("nr", nr);
			args.put("fjr", getUserId(request));
			int i = template.update("INSERT INTO crm_msg(fjr, sjr, bt, nr) VALUES (:fjr, :sjr, :bt, :nr);", args);
			if (i == 1) {
				LogUtils.writeLogDetail(logId, "发邮件", "", getUserId(request), sjr, bt, LogUtils.getNowTime(),
						args.toString());
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));
				if (logger.isDebugEnabled()) {
					logger.debug("xyj(String, String, String, HttpServletRequest) - end");
				}
				return "alert('发送成功');load('fjx.html');";
			}
		} catch (DataAccessException e) {
			logger.error("xyj(String, String, String, HttpServletRequest)", e);

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("xyj(String, String, String, HttpServletRequest) - end");
			}
			return "alert('发送失败');$('#sb_box2').hide();";

		}

		if (logger.isDebugEnabled()) {
			logger.debug("xyj(String, String, String, HttpServletRequest) - end");
		}
		return "alert('发送失败');$('#sb_box2').hide();";
	}

	@POST
	@Path("deleteMsg")
	public String deleteMsg(@FormParam("mid") String mid, @FormParam("page") String page,
			@Context final HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("xyj(String, String, String, HttpServletRequest) - start");
		}
		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		try {
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("mid", new Long(mid));
			String bz = "yxbz_";
			if (page.equals("syj.html")) {
				bz += "s";
			} else {
				bz += "f";
			}
			int i = template.update("update crm_msg set " + bz + " = '0' where mid = :mid", args);
			if (i == 1) {
				LogUtils.writeLogDetail(logId, "删除消息", "", "crm_msg", "row", "", LogUtils.getNowTime(),
						args.toString());
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));
				String returnString = "alert('删除成功');load('" + page + "');";
				if (logger.isDebugEnabled()) {
					logger.debug("deleteMsg(String, String, HttpServletRequest) - end");
				}
				return returnString;
			}

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), args.toString(), "", LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("deleteMsg(String, String, HttpServletRequest) - end");
			}
			return "alert('删除失败');$('#sb_box2').hide();";
		} catch (Exception e) {
			logger.error("deleteMsg(String, String, HttpServletRequest)", e);

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("deleteMsg(String, String, HttpServletRequest) - end");
			}
			return "alert('删除失败');$('#sb_box2').hide();";
		}
	}

	@POST
	@Path("jjbzbdb")
	public String jjbzbdb(@FormParam("mymm2") final String mymm2, @FormParam("sl") String sl,
			@Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("jjbzbdb(String, HttpServletRequest) - start");
		}
		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		try {
			if (!checkMm2(mymm2, request)) {
				return "alert('二级密码不正确');$('#sb_box2').hide();";
			}
			Map<String, Object> args = new HashMap<String, Object>();
			if (sl == null || sl.equals("")) {
				if (logger.isDebugEnabled()) {
					logger.debug("jjbzbdb(String, HttpServletRequest) - end");
				}
				return "alert('请输入数量');$('#sb_box2').hide();";
			}
			Map<String, Object> me = initMe(request);
			if (new Double(me.get("jjb").toString()) < new Double(sl)) {
				String returnString = "alert('剩余奖金币不够" + sl + "');$('#sb_box2').hide();";
				if (logger.isDebugEnabled()) {
					logger.debug("jjbzbdb(String, HttpServletRequest) - end");
				}
				return returnString;
			}

			String newjjb = (new Double(me.get("jjb").toString()) - new Double(sl)) + "";
			String newbdb = (new Double(me.get("bdb").toString()) + new Double(sl)) + "";

			args.put("sl", new Double(sl));
			args.put("zh", me.get("zh"));
			int i = template.update("update crm_user set jjb = jjb - :sl , bdb = bdb + :sl where zh = :zh", args);
			if (i == 1) {
				LogUtils.writeLogDetail(logId, "奖金币转报单币", "减去奖金币", me.get("zh").toString(), "jjb", sl + "",
						LogUtils.getNowTime(), args.toString(), me.get("jjb").toString(), newjjb);
				LogUtils.writeLogDetail(logId, "奖金币转报单币", "增加报单币", me.get("zh").toString(), "bdb", sl + "",
						LogUtils.getNowTime(), args.toString(), me.get("bdb").toString(), newbdb);
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));
				if (logger.isDebugEnabled()) {
					logger.debug("jjbzbdb(String, HttpServletRequest) - end");
				}
				return "alert('操作成功');load('jjbzbdb.html');";
			}

			if (logger.isDebugEnabled()) {
				logger.debug("jjbzbdb(String, HttpServletRequest) - end");
			}
			return "alert('操作失败');$('#sb_box2').hide();";
		} catch (Exception e) {
			logger.error("jjbzbdb(String, HttpServletRequest)", e);

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("jjbzbdb(String, HttpServletRequest) - end");
			}
			return "alert('操作失败');$('#sb_box2').hide();";
		}
	}

	@POST
	@Path("yhqzthb")
	public String yhqzthb(@FormParam("mymm2") final String mymm2, @FormParam("sl") String sl,
			@Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("yhqzthb(String, HttpServletRequest) - start");
		}
		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		try {
			if (!checkMm2(mymm2, request)) {
				return "alert('二级密码不正确');$('#sb_box2').hide();";
			}
			Map<String, Object> args = new HashMap<String, Object>();
			if (sl == null || sl.equals("")) {
				if (logger.isDebugEnabled()) {
					logger.debug("yhqzthb(String, HttpServletRequest) - end");
				}
				return "alert('请输入数量');$('#sb_box2').hide();";
			}
			Map<String, Object> me = initMe(request);
			if (new Double(me.get("yhq").toString()) < new Double(sl)) {
				String returnString = "alert('剩余优惠券不够" + sl + "');$('#sb_box2').hide();";

				if (logger.isDebugEnabled()) {
					logger.debug("yhqzthb(String, HttpServletRequest) - end");
				}
				return returnString;
			}

			String newyhq = (new Double(me.get("yhq").toString()) - new Double(sl)) + "";
			String newthb = (new Double(me.get("thb").toString()) + new Double(sl)) + "";

			args.put("sl", new Double(sl));
			args.put("zh", me.get("zh"));
			int i = template.update("update crm_user set yhq = yhq - :sl , thb = thb + :sl where zh = :zh", args);
			if (i == 1) {
				LogUtils.writeLogDetail(logId, "优惠券转提货币", "减去优惠券", me.get("zh").toString(), "yhq", sl + "",
						LogUtils.getNowTime(), args.toString(), me.get("yhq").toString(), newyhq);
				LogUtils.writeLogDetail(logId, "优惠券转提货币", "增加提货币", me.get("zh").toString(), "thb", sl + "",
						LogUtils.getNowTime(), args.toString(), me.get("thb").toString(), newthb);
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));

				if (logger.isDebugEnabled()) {
					logger.debug("yhqzthb(String, HttpServletRequest) - end");
				}
				return "alert('操作成功');load('yhqzthb.html');";
			}

			if (logger.isDebugEnabled()) {
				logger.debug("yhqzthb(String, HttpServletRequest) - end");
			}
			return "alert('操作失败');$('#sb_box2').hide();";
		} catch (Exception e) {
			logger.error("yhqzthb(String, HttpServletRequest)", e);

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("yhqzthb(String, HttpServletRequest) - end");
			}
			return "alert('操作失败');$('#sb_box2').hide();";
		}
	}

	@POST
	@Path("cpAdd")
	public String cpAdd(@FormParam("cpid") String cpid, @FormParam("cpmc") String cpmc, @FormParam("jg") String jg,
			@FormParam("kc") String kc, @FormParam("ms") String ms, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("cpAdd(String, String, String, String, HttpServletRequest) - start");
		}
		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		try {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("cpmc", cpmc);
			args.put("jg", jg);
			args.put("kc", kc);
			args.put("ms", ms);
			int i = 0;
			boolean isUpdate = cpid != null && !cpid.equals("");
			if (isUpdate) {
				args.put("cpid", new Long(cpid));
				i = template.update("update crm_cp set cpmc = :cpmc,jg = :jg, ms = :ms, kc = :kc where cpid = :cpid",
						args);
			} else {
				i = template.update("INSERT INTO crm_cp(kc, cpmc, jg, ms) VALUES (:kc, :cpmc, :jg, :ms);", args);
			}
			if (i == 1) {
				args.put("ms", "-");
				LogUtils.writeLogDetail(logId, isUpdate ? "产品修改" : "添加产品", "", getUserId(request), cpmc, jg,
						LogUtils.getNowTime(), args.toString());
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));
				if (logger.isDebugEnabled()) {
					logger.debug("cpAdd(String, String, String, String, HttpServletRequest) - end");
				}
				return "alert('操作成功');load('cpgl.html');";
			}

			if (logger.isDebugEnabled()) {
				logger.debug("cpAdd(String, String, String, String, HttpServletRequest) - end");
			}
			return "alert('操作失败');$('#sb_box2').hide();";
		} catch (Exception e) {
			logger.error("cpAdd(String, String, String, String, HttpServletRequest)", e);

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("cpAdd(String, String, String, String, HttpServletRequest) - end");
			}
			return "alert('操作失败');$('#sb_box2').hide();";
		}
	}

	@POST
	@Path("ggAdd")
	public String ggAdd(@FormParam("bt") String bt, @FormParam("nr") String nr, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("ggAdd(String, String, HttpServletRequest) - start");
		}

		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		try {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("bt", bt);
			args.put("nr", nr);
			args.put("cjr", getUserId(request));
			int i = template.update("INSERT INTO crm_gg(bt, nr, cjr) VALUES (:bt, :nr, :cjr);", args);
			if (i == 1) {
				LogUtils.writeLogDetail(logId, "添加公告", "", "", "", "", LogUtils.getNowTime(), args.toString());
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));

				if (logger.isDebugEnabled()) {
					logger.debug("ggAdd(String, String, HttpServletRequest) - end");
				}
				return "alert('添加成功');load('gggl.html');";
			}

			if (logger.isDebugEnabled()) {
				logger.debug("ggAdd(String, String, HttpServletRequest) - end");
			}
			return "alert('添加失败');$('#sb_box2').hide();";
		} catch (Exception e) {
			logger.error("ggAdd(String, String, HttpServletRequest)", e);

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("ggAdd(String, String, HttpServletRequest) - end");
			}
			return "alert('添加失败');$('#sb_box2').hide();";
		}
	}

	@POST
	@Path("ggDel")
	public String ggDel(@FormParam("id") String id, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("ggDel(String, HttpServletRequest) - start");
		}

		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		try {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("id", new Long(id));
			int i = template.update("update crm_gg set yxbz = '0' where ggid = :id", args);
			if (i == 1) {
				LogUtils.writeLogDetail(logId, "删除公告", "", "", "", "", LogUtils.getNowTime(), args.toString());
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));

				if (logger.isDebugEnabled()) {
					logger.debug("ggDel(String, HttpServletRequest) - end");
				}
				return "alert('删除成功');load('gggl.html');";
			}

			if (logger.isDebugEnabled()) {
				logger.debug("ggDel(String, HttpServletRequest) - end");
			}
			return "alert('删除失败');$('#sb_box2').hide();";
		} catch (Exception e) {
			logger.error("ggDel(String, HttpServletRequest)", e);

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("ggDel(String, HttpServletRequest) - end");
			}
			return "alert('删除失败');$('#sb_box2').hide();";
		}
	}

	@POST
	@Path("cpDel")
	public String cpDel(@FormParam("id") String id, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("cpDel(String, HttpServletRequest) - start");
		}

		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		try {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("id", new Long(id));
			int i = template.update("update crm_cp set yxbz = '0' where cpid = :id", args);
			if (i == 1) {
				LogUtils.writeLogDetail(logId, "删除产品", "", "", "", "", LogUtils.getNowTime(), args.toString());
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));

				if (logger.isDebugEnabled()) {
					logger.debug("cpDel(String, HttpServletRequest) - end");
				}
				return "alert('删除成功');load('cpgl.html');";
			}

			if (logger.isDebugEnabled()) {
				logger.debug("cpDel(String, HttpServletRequest) - end");
			}
			return "alert('删除失败');$('#sb_box2').hide();";
		} catch (Exception e) {
			logger.error("cpDel(String, HttpServletRequest)", e);

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("cpDel(String, HttpServletRequest) - end");
			}
			return "alert('删除失败');$('#sb_box2').hide();";
		}
	}

	@POST
	@Path("txsq")
	public String txsq(@FormParam("mymm2") final String mymm2, @FormParam("sl") final String sl,
			@Context final HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("txsq(String, HttpServletRequest) - start");
		}
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		TransactionTemplate tt = new TransactionTemplate(getTransactionManager());
		TTReturnObj ttReturnObj = tt.execute(new TransactionCallback<TTReturnObj>() {
			public TTReturnObj doInTransaction(TransactionStatus ts) {
				if (logger.isDebugEnabled()) {
					logger.debug("doInTransaction(TransactionStatus) - start");
				}
				if (!checkMm2(mymm2, request)) {
					TTReturnObj returnTTReturnObj = new TTReturnObj("alert('二级密码不正确');$('#sb_box2').hide();");
					if (logger.isDebugEnabled()) {
						logger.debug("doInTransaction(TransactionStatus) - end");
					}
					return returnTTReturnObj;
				}
				String msg = "";
				long logId = LogUtils.getLogId();
				String startTime = LogUtils.getNowTime();
				try {
					if ("".equals(sl) || sl == null) {
						if (logger.isDebugEnabled()) {
							logger.debug("doInTransaction(TransactionStatus) - end");
						}
						return new TTReturnObj("alert('请输入数量');$('#sb_box2').hide();");
					}
					if (new Double(initMe(request).get("jjb").toString()) < new Double(sl)) {
						if (logger.isDebugEnabled()) {
							logger.debug("doInTransaction(TransactionStatus) - end");
						}
						return new TTReturnObj("alert('数量不能大于自己的奖金币');$('#sb_box2').hide();");
					}
					NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
					Map<String, Object> args = new HashMap<String, Object>();
					args.put("zh", getUserId(request));
					args.put("sl", new Double(sl));
					int i = template.update("INSERT INTO crm_user_tx(zh, sl) VALUES (:zh, :sl)", args);
					LogUtils.writeLogDetail(logId, "提现申请", "插入申请记录", getUserId(request), "", sl + "",
							LogUtils.getNowTime(), args.toString());
					int ii = template.update("update crm_user set jjb = jjb - :sl where zh = :zh", args);
					LogUtils.writeLogDetail(logId, "提现申请", "减去奖金币", getUserId(request), "jjb", sl + "",
							LogUtils.getNowTime(), args.toString());
					if (i == 1 && ii == 1) {
						LogUtils.writeLogS(logId, SERVICE_NAME, "txsq", startTime, LogUtils.getNowTime(), msg,
								LogUtils.getUserId(request));
						TTReturnObj returnTTReturnObj = new TTReturnObj("alert('申请成功');load('jjbtx.html');");
						if (logger.isDebugEnabled()) {
							logger.debug("doInTransaction(TransactionStatus) - end");
						}
						return returnTTReturnObj;
					}
					throw new Exception("插入数据失败");

				} catch (Exception e) {
					logger.error("doInTransaction(TransactionStatus)", e);

					ts.setRollbackOnly();
					if (logger.isDebugEnabled()) {
						logger.debug("doInTransaction(TransactionStatus) - end");
					}
					return new TTReturnObj("alert('申请失败')");
				}
			}
		});
		if (ttReturnObj.hasException()) {
			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), ttReturnObj.getE().getMessage(),
					LogUtils.getStackTrace(ttReturnObj.getE()), LogUtils.getUserId(request));

		}
		if (logger.isDebugEnabled()) {
			logger.debug("txsq(String, HttpServletRequest) - end");
		}

		return ttReturnObj.getMsg();
	}

	@POST
	@Path("thsq")
	public String thsq(@FormParam("mymm2") final String mymm2, @FormParam("cpmx") final String cpmx,
			@FormParam("cpmx2") final String cpmx2, @FormParam("je") final String je,
			@Context final HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("thsq(String, String, String, HttpServletRequest) - start");
		}

		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		TransactionTemplate tt = new TransactionTemplate(getTransactionManager());
		TTReturnObj ttReturnObj = tt.execute(new TransactionCallback<TTReturnObj>() {
			public TTReturnObj doInTransaction(TransactionStatus ts) {
				if (logger.isDebugEnabled()) {
					logger.debug("doInTransaction(TransactionStatus) - start");
				}
				if (!checkMm2(mymm2, request)) {
					TTReturnObj returnTTReturnObj = new TTReturnObj("alert('二级密码不正确');$('#sb_box2').hide();");
					if (logger.isDebugEnabled()) {
						logger.debug("doInTransaction(TransactionStatus) - end");
					}
					return returnTTReturnObj;
				}
				String msg = "";
				long logId = LogUtils.getLogId();
				String startTime = LogUtils.getNowTime();
				try {
					Map<String, Object> me = initMe(request);
					if (je == null || new Double(je) == 0) {
						if (logger.isDebugEnabled()) {
							logger.debug("doInTransaction(TransactionStatus) - end");
						}
						return new TTReturnObj("alert('请选择商品');$('#sb_box2').hide();");
					}
					if (new Double(me.get("thb").toString()) < new Double(je)) {
						if (logger.isDebugEnabled()) {
							logger.debug("doInTransaction(TransactionStatus) - end");
						}
						return new TTReturnObj("alert('提货币数量不够');$('#sb_box2').hide();");
					}

					NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
					Map<String, Object> args = new HashMap<String, Object>();
					args.put("zh", getUserId(request));
					args.put("je", new Double(je));
					args.put("mx", cpmx);
					args.put("mx2", cpmx2);
					int i = template.update("INSERT INTO crm_user_cp(zh, mx, mx2 ,je) VALUES (:zh, :mx, :mx2, :je)",
							args);
					LogUtils.writeLogDetail(logId, "提货申请", "插入申请记录", getUserId(request), "", je + "",
							LogUtils.getNowTime(), args.toString());

					int ii = template.update("update crm_user set thb = thb - :je where zh = :zh", args);
					LogUtils.writeLogDetail(logId, "提货申请", "减去提货币", getUserId(request), "thb", je + "",
							LogUtils.getNowTime(), args.toString());

					if (i == 1 && ii == 1) {
						LogUtils.writeLogS(logId, SERVICE_NAME, "thsq", startTime, LogUtils.getNowTime(), msg,
								LogUtils.getUserId(request));
						if (logger.isDebugEnabled()) {
							logger.debug("doInTransaction(TransactionStatus) - end");
						}
						return new TTReturnObj("alert('申请成功');load('sqjl.html');");
					}
					throw new Exception("插入数据失败");

				} catch (Exception e) {
					logger.error("doInTransaction(TransactionStatus)", e);

					ts.setRollbackOnly();
					if (logger.isDebugEnabled()) {
						logger.debug("doInTransaction(TransactionStatus) - end");
					}
					return new TTReturnObj("alert('申请失败');$('#sb_box2').hide();", e);
				}
			}
		});
		if (ttReturnObj.hasException()) {
			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), ttReturnObj.getE().getMessage(),
					LogUtils.getStackTrace(ttReturnObj.getE()), LogUtils.getUserId(request));

		}
		if (logger.isDebugEnabled()) {
			logger.debug("thsq(String, String, String, HttpServletRequest) - end");
		}

		return ttReturnObj.getMsg();
	}

	@POST
	@Path("readMsgReq")
	public String readMsgReq(@FormParam("mid") String mid, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("readMsgReq(String, HttpServletRequest) - start");
		}
		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		try {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("mid", new Long(mid));
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			int i = template.update("update crm_msg set sfck = '1' where mid = :mid", args);
			if (i == 1) {
				LogUtils.writeLogDetail(logId, "已读", "", getUserId(request), "crm_msg", mid, LogUtils.getNowTime(),
						args.toString());
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));
				if (logger.isDebugEnabled()) {
					logger.debug("readMsgReq(String, HttpServletRequest) - end");
				}
				return "$('#wdxxs').text(parseInt($('#wdxxs').text())>0?(parseInt($('#wdxxs').text())-1):0)";
			}

			if (logger.isDebugEnabled()) {
				logger.debug("readMsgReq(String, HttpServletRequest) - end");
			}
			return "";
		} catch (Exception e) {
			logger.error("readMsgReq(String, HttpServletRequest)", e);

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("readMsgReq(String, HttpServletRequest) - end");
			}
			return "";
		}

	}

	@POST
	@Path("fhqr")
	public String fhqr(@FormParam("id") String id, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("fhqr(String, HttpServletRequest) - start");
		}
		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		try {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("id", new Long(id));
			int i = template.update("update crm_user_cp set zt = '1',fhsj = now() where id = :id", args);
			if (i == 1) {
				LogUtils.writeLogDetail(logId, "发货确认", "", "crm_user_cp", "zt", id, LogUtils.getNowTime(),
						args.toString());
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));
				if (logger.isDebugEnabled()) {
					logger.debug("fhqr(String, HttpServletRequest) - end");
				}
				return "alert('操作成功');load('thgl.html')";
			}

			if (logger.isDebugEnabled()) {
				logger.debug("fhqr(String, HttpServletRequest) - end");
			}
			return "alert('操作失败');$('#sb_box2').hide();";
		} catch (Exception e) {
			logger.error("fhqr(String, HttpServletRequest)", e);

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("fhqr(String, HttpServletRequest) - end");
			}
			return "alert('操作失败');$('#sb_box2').hide();";
		}

	}

	@POST
	@Path("xgzl")
	public String fhqr(@FormParam("mymm2") final String mymm2, @FormParam("xb") final String xb,
			@FormParam("lxdh") final String lxdh, @FormParam("dzyx") final String dzyx,
			@FormParam("qq") final String qq, @FormParam("khyh") final String khyh,
			@FormParam("khhdz") final String khhdz, @FormParam("yhkh") final String yhkh,
			@FormParam("hz") final String hz, @FormParam("dz1") final String dz1, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug(
					"fhqr(String, String, String, String, String, String, String, String, String, HttpServletRequest) - start");
		}

		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		try {
			if (!checkMm2(mymm2, request)) {
				if (logger.isDebugEnabled()) {
					logger.debug(
							"fhqr(String, String, String, String, String, String, String, String, String, HttpServletRequest) - end");
				}
				return "alert('二级密码不正确');$('#sb_box2').hide();";
			}
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", getUserId(request));
			args.put("xb", xb);
			args.put("lxdh", lxdh);
			args.put("dzyx", dzyx);
			args.put("qq", qq);
			args.put("khyh", khyh);
			args.put("khhdz", khhdz);
			args.put("yhkh", yhkh);
			args.put("hz", hz);
			args.put("dz1", dz1);
			int i = template.update("update crm_user set xb = :xb," + "lxdh = :lxdh," + "dzyx = :dzyx," + "qq = :qq,"
					+ "khyh = :khyh," + "khhdz = :khhdz," + "yhkh = :yhkh," + "hz = :hz," + "dz1 = :dz1 where zh = :zh",
					args);
			if (i == 1) {
				LogUtils.writeLogDetail(logId, "修改资料", "", getUserId(request), "", "", LogUtils.getNowTime(),
						args.toString());
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));

				if (logger.isDebugEnabled()) {
					logger.debug(
							"fhqr(String, String, String, String, String, String, String, String, String, HttpServletRequest) - end");
				}
				return "alert('操作成功');load('wdzl.html');";
			}

			if (logger.isDebugEnabled()) {
				logger.debug(
						"fhqr(String, String, String, String, String, String, String, String, String, HttpServletRequest) - end");
			}
			return "alert('操作失败');$('#sb_box2').hide();";
		} catch (Exception e) {
			logger.error(
					"fhqr(String, String, String, String, String, String, String, String, String, HttpServletRequest)",
					e);

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug(
						"fhqr(String, String, String, String, String, String, String, String, String, HttpServletRequest) - end");
			}
			return "alert('操作失败');$('#sb_box2').hide();";
		}

	}

	@POST
	@Path("reg2")
	public String reg2(@FormParam("mymm2") final String mymm2, @FormParam("name") final String zh,
			@FormParam("userlevel") final String lx, @FormParam("yhq") final String yhq2,
			@FormParam("password1") final String mm1, @FormParam("password11") final String mm11,
			@FormParam("password2") final String mm2, @FormParam("password22") final String mm22,
			@FormParam("fwcenter") final String fwzx, @FormParam("ztuser") final String tjr,
			@FormParam("azuser") final String jdr, @FormParam("azsite") final String azwz,
			@FormParam("realname") final String xm, @FormParam("sex") final String xb,
			@FormParam("tel") final String lxdh, @FormParam("email") final String dzyx,
			@FormParam("qq") final String qq, @FormParam("BankName") final String khyh,
			@FormParam("bankaddr") final String khhdz, @FormParam("bankcard") final String yhkh,
			@FormParam("cardusername") final String hz, @FormParam("dz1") final String dz1,
			@Context final HttpServletRequest request) {
		reg(mymm2, zh, lx, yhq2, mm1, mm11, mm2, mm22, fwzx, tjr, jdr, azwz, xm, xb, lxdh, dzyx, qq, khyh, khhdz, yhkh,
				hz, dz1, request);
		return jhhy(mm2, zh, "0", request);
	}

	@POST
	@Path("jhhy")
	public String jhhy(@FormParam("mymm2") final String mymm2, @FormParam("zh") final String zh,
			@FormParam("yhq") final String yhq2, @Context final HttpServletRequest request) {

		final long logId = LogUtils.getLogId();
		final String startTime = LogUtils.getNowTime();
		TransactionTemplate tt = new TransactionTemplate(getTransactionManager());
		TTReturnObj ttReturnObj = tt.execute(new TransactionCallback<TTReturnObj>() {
			public TTReturnObj doInTransaction(TransactionStatus ts) {
				if (logger.isDebugEnabled()) {
					logger.debug("doInTransaction(TransactionStatus) - start");
				}

				String msg = "";

				try {
					if (!checkMm2(mymm2, request)) {
						TTReturnObj returnTTReturnObj = new TTReturnObj("alert('二级密码不正确');$('#sb_box2').hide();");
						if (logger.isDebugEnabled()) {
							logger.debug("doInTransaction(TransactionStatus) - end");
						}
						return returnTTReturnObj;
					}

					NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);

					Double yhq = null;
					try {
						yhq = new Double(yhq2);
					} catch (Exception e) {
						yhq = 0.0;
					}

					Map<String, Object> user = getUserByZh(zh);

					String yhqsy2 = CodeUtils.getValue("YHQSY", "2");

					String yhqsy1 = CodeUtils.getValue("YHQSY", "1");

					// 验证报单币,优惠券使用情况
					Object lx = user.get("lx");
					if ("1".equals(lx)) {
						if (new Double((yhq)) > Integer.parseInt(yhqsy1)) {
							LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime, LogUtils.getNowTime(),
									"金卡最多使用" + yhqsy1 + "优惠券", "", LogUtils.getUserId(request));
							if (logger.isDebugEnabled()) {
								logger.debug(
										"reg(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, HttpServletRequest) - end");
							}
							return new TTReturnObj("alert('金卡最多使用" + yhqsy1
									+ "优惠券');$('#button').attr('disabled',false);$('#sb_box2').hide();");
						}
					} else if ("2".equals(lx)) {
						if (new Double((yhq)) > Integer.parseInt(yhqsy2)) {
							LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime, LogUtils.getNowTime(),
									"普卡最多使用" + yhqsy2 + "优惠券", "", LogUtils.getUserId(request));
							if (logger.isDebugEnabled()) {
								logger.debug(
										"reg(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, HttpServletRequest) - end");
							}
							return new TTReturnObj("alert('普卡最多使用" + yhqsy2
									+ "优惠券');$('#button').attr('disabled',false);$('#sb_box2').hide();");
						}
					} else {
						LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime, LogUtils.getNowTime(), "会员类型获取失败", "",
								LogUtils.getUserId(request));
						if (logger.isDebugEnabled()) {
							logger.debug(
									"reg(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, HttpServletRequest) - end");
						}
						return new TTReturnObj(
								"alert('会员类型获取失败');$('#button').attr('disabled',false);$('#sb_box2').hide();");
					}

					Map<String, Object> me = initMe(request);
					if (new Double(yhq) > new Double(me.get("yhq").toString())) {
						LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime, LogUtils.getNowTime(), "优惠券余额不足", "",
								LogUtils.getUserId(request));
						if (logger.isDebugEnabled()) {
							logger.debug(
									"reg(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, HttpServletRequest) - end");
						}
						return new TTReturnObj(
								"alert('优惠券余额不足');$('#button').attr('disabled',false);$('#sb_box2').hide();");
					}

					// 报单币校验
					if ("1".equals(lx)) {
						if (new Double(me.get("bdb").toString()) < (7000 - new Double(yhq))) {
							String returnString = "alert('报单币不够,目前只有:" + me.get("bdb")
									+ "');$('#button').attr('disabled',false);$('#sb_box2').hide();";
							LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime, LogUtils.getNowTime(),
									"报单币不够,目前只有" + me.get("bdb"), "", LogUtils.getUserId(request));
							if (logger.isDebugEnabled()) {
								logger.debug(
										"reg(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, HttpServletRequest) - end");
							}
							return new TTReturnObj(returnString);
						}
					}
					if ("2".equals(lx)) {
						if (new Double(me.get("bdb").toString()) < (3500 - new Double(yhq))) {
							String returnString = "alert('报单币不够,目前只有:" + me.get("bdb")
									+ "');$('#button').attr('disabled',false);$('#sb_box2').hide();";
							LogUtils.writeLogE(logId, SERVICE_NAME, "reg", startTime, LogUtils.getNowTime(),
									"报单币不够,目前只有:" + me.get("bdb"), "", LogUtils.getUserId(request));
							if (logger.isDebugEnabled()) {
								logger.debug(
										"reg(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, HttpServletRequest) - end");
							}
							return new TTReturnObj(returnString);
						}
					}

					int lx_je = lx.equals("1") ? 7000 : 3500;
					String userId = getUserId(request);
					String jdr = (String) user.get("jdr");
					String tjr = (String) user.get("tjr");
					// 扣除报单币和优惠券
					kcBdbAndYhq(logId, template, yhq, me, lx_je, userId, zh);

					// 修改接点人,及上级业绩
					xgyj(zh, logId, template, lx_je);

					// 升级经理操作
					sjcz(jdr, logId, template, zh);

					// 修改推荐人数
					xgtjrs(tjr, logId, template, zh);

					Map<String, Object> args = new HashMap<String, Object>();
					args.put("zh", zh);
					args.put("jhr", userId);
					int i = template.update(
							"update crm_user set yxzt = '1',jhr = :jhr,jhsj = now() where zh = :zh and yxzt = '0'",
							args);
					if (i == 1) {
						LogUtils.writeLogDetail(logId, "会员激活", "修改会员状态", zh, "", "", LogUtils.getNowTime(),
								args.toString(), zh);
						LogUtils.writeLogS(logId, SERVICE_NAME, "jhhy", startTime, LogUtils.getNowTime(), msg,
								LogUtils.getUserId(request));

						TTReturnObj returnTTReturnObj = new TTReturnObj("alert('激活成功');load('hysh.html');");
						if (logger.isDebugEnabled()) {
							logger.debug("doInTransaction(TransactionStatus) - end");
						}
						return returnTTReturnObj;
					}

					TTReturnObj returnTTReturnObj = new TTReturnObj("alert('激活失败');$('#sb_box2').hide();");
					if (logger.isDebugEnabled()) {
						logger.debug("doInTransaction(TransactionStatus) - end");
					}
					return returnTTReturnObj;
				} catch (Exception e) {
					logger.error("doInTransaction(TransactionStatus)", e);

					ts.setRollbackOnly();

					TTReturnObj returnTTReturnObj = new TTReturnObj("alert('激活失败!');$('#sb_box2').hide();", e);
					if (logger.isDebugEnabled()) {
						logger.debug("doInTransaction(TransactionStatus) - end");
					}
					return returnTTReturnObj;
				}

			}

			private void kcBdbAndYhq(long logId, NamedParameterJdbcTemplate template, Double yhq,
					Map<String, Object> me, int lx_je, String userId, String zh) {
				Map<String, Object> args;
				args = new HashMap<String, Object>();
				args.put("zh", userId);
				Object yhq_new = new Double(me.get("yhq").toString()) - new Double((yhq));
				double d = lx_je - new Double(yhq);
				Object bdb_new = new Double(me.get("bdb").toString()) - d;
				args.put("yhq", yhq_new);
				args.put("bdb", bdb_new);
				template.update("update crm_user set bdb = :bdb , yhq = :yhq where zh = :zh", args);
				LogUtils.writeLogDetail(logId, "会员激活", "扣除报单币", userId, "bdb", d + "", LogUtils.getNowTime(),
						args.toString(), zh);
				if (new Double(yhq.toString()) > 0) {
					LogUtils.writeLogDetail(logId, "会员激活", "扣除优惠券", userId, "yhq", yhq + "", LogUtils.getNowTime(),
							args.toString(), zh);
				}
			}

			private void xgyj(final String zh, long logId, NamedParameterJdbcTemplate template, int lx_je) {
				Map<String, Object> args;
				args = new HashMap<String, Object>();
				args.put("zh", zh);
				// 业绩修改List
				List<Map<String, Object>> yjxgList = template.queryForList(
						"WITH recursive cte AS( SELECT 1 AS DEPTH, a.* FROM crm_user a WHERE zh=:zh" + " UNION ALL"
								+ " SELECT DEPTH + 1 AS DEPTH,b.* FROM crm_user b INNER JOIN cte c ON b.zh=c.jdr where b.yxzt = '1'"
								+ " ) "
								+ "select c.*,(select azwz from cte d where d.jdr = c. zh ) as qw from cte c where c.depth <> 1",
						args);
				for (Map<String, Object> map : yjxgList) {
					args.put("zh", map.get("zh"));
					args.put("sl", lx_je);
					args.put("sl2", !map.get("jb").equals("1") ? 0 : lx_je);
					args.put("qw", map.get("qw"));
					template.update(
							"UPDATE crm_user SET "
									+ " zqyj = zqyj + (CASE :qw WHEN '1' THEN :sl ELSE 0 END) ,                  "
									+ " yqyj = yqyj + (CASE :qw WHEN '2' THEN :sl ELSE 0 END) ,                  "
									+ " zqdryj = zqdryj + (CASE :qw WHEN '1' THEN :sl ELSE 0 END) ,              "
									+ " yqdryj = yqdryj + (CASE :qw WHEN '2' THEN :sl ELSE 0 END) ,              "
									+ " zqsyyj = zqsyyj + (CASE :qw WHEN '1' THEN :sl2 ELSE 0 END) ,              "
									+ " yqsyyj = yqsyyj + (CASE :qw WHEN '2' THEN :sl2 ELSE 0 END) where zh = :zh    ",
							args);
					LogUtils.writeLogDetail(logId, "会员激活", "业绩修改", map.get("zh").toString(), map.get("qw").toString(),
							lx_je + "", LogUtils.getNowTime(), args.toString(), zh);
				}
			}

			private void xgtjrs(final String tjr, long logId, NamedParameterJdbcTemplate template, String zh) {
				Map<String, Object> args;
				args = new HashMap<String, Object>();
				args.put("zh", tjr);
				template.update("update crm_user set tjrs = tjrs+1 where zh = :zh", args);
				LogUtils.writeLogDetail(logId, "会员激活", "修改推荐人数", tjr.toLowerCase(), "tjrs", "1", LogUtils.getNowTime(),
						args.toString(), zh);
			}

			private void sjcz(final String jdr, long logId, NamedParameterJdbcTemplate template, String zh)
					throws Exception {
				Map<String, Object> args;
				args = new HashMap<String, Object>();
				args.put("zh", jdr);
				// 两个区（大区和小区），其中小区业绩大于20万的可以升级经理
				List<Map<String, Object>> list = template.queryForList("WITH recursive cte AS\n"
						+ " (SELECT 1 AS DEPTH, a.*\n" + "    FROM crm_user a\n" + "   WHERE zh = :zh\n"
						+ "     and yxzt = '1'\n" + "  UNION ALL\n" + "  SELECT DEPTH + 1 AS DEPTH, b.*\n"
						+ "    FROM crm_user b\n" + "   INNER JOIN cte c\n" + "      ON b.zh = c.jdr\n"
						+ "   where b.yxzt = '1')\n" + "select *\n" + "  from (select cte.*,\n"
						+ "               (select min(zqyj + yqyj+(case lx when '1' then 7000 else 3500 end))\n"
						+ "                  from crm_user cc\n" + "                 where cc.jdr = cte.zh) as a1,\n"
						+ "               (select max(zqyj + yqyj+(case lx when '1' then 7000 else 3500 end))\n"
						+ "                  from crm_user cc\n" + "                 where cc.jdr = cte.zh) as a0,\n"
						+ "               (select count(1) from crm_user cc where cc.jdr = cte.zh) as a2\n"
						+ "          from cte) aaa\n" + " where cast(aaa.jb as int) = 1\n" + "   and aaa.a1 > 200000\n"
						+ "   and aaa.a2 = 2", args);
				if (list.size() > 0) {

					// 如果触发了升级总裁和董事的动作，则需要后续判定是否可以继续升级
					List<Map<String, Object>> sj_list = new ArrayList<Map<String, Object>>();

					// 升级_经理_账号
					Map<String, Object> jl = list.get(0);
					String zh_jl = (String) jl.get("zh");
					args = new HashMap<String, Object>();
					args.put("zh", zh_jl);
					int ii = template.update("update crm_user set jb = '2',zqsyyj = 0,yqsyyj = 0 where zh = :zh", args);
					if (ii != 1) {
						throw new Exception(zh_jl + " 升级经理操作失败");
					}
					LogUtils.writeLogDetail(logId, "会员激活", "升级经理", zh_jl, "jb", "1", LogUtils.getNowTime(),
							args.toString(), zh);
					// 当你小区完成20万元，则升为经理，碰奖封顶，开始享受 8%级差。
					//
					//
					//
					// 当经理市场中出现了1个经理，则升为总监，开始享受 12%级差。
					//
					//
					//
					// 当你2个市场中各出现了1个经理，则升为总裁 ，开始享受15%级差。
					//
					// 总裁的市场中出现了总裁，晋升为董事，开始享受18%级差。
					//
					// 注解:董事有资格开拓第3个以上市场，只享受级差奖励。
					// 升级经理成功 继续升级推荐人
					String zh_jl_jdr = (String) jl.get("jdr");
					args = new HashMap<String, Object>();
					args.put("zh", zh_jl_jdr);
					Map<String, Object> jl_jdr = template
							.queryForMap("select * from crm_user where zh = :zh and yxzt = '1'", args);
					if (jl_jdr.get("jb").equals("2") || jl_jdr.get("jb").equals("1")) { // 如果经理的上级是经理或者还是业务员,则升级为总监
						int i2 = template.update("update crm_user set jb = '3' where zh = :zh", args);
						if (i2 != 1) {
							throw new Exception(zh_jl_jdr + " 升级总监操作失败");
						}
						LogUtils.writeLogDetail(logId, "会员激活", "升级总监", zh_jl_jdr, "jb", "1", LogUtils.getNowTime(),
								args.toString(), zh);
						// 升级总监成功 结束

					} else if (jl_jdr.get("jb").equals("3")) {// 如果经理的上级是总监,则升级为总裁
						int i2 = template.update("update crm_user set jb = '4' where zh = :zh", args);
						if (i2 != 1) {
							throw new Exception(zh_jl_jdr + " 升级总裁操作失败");
						}
						LogUtils.writeLogDetail(logId, "会员激活", "升级总裁", zh_jl_jdr, "jb", "1", LogUtils.getNowTime(),
								args.toString(), zh);

						jl_jdr.put("jb", new Integer(jl_jdr.get("jb").toString()) + 1);
						sj_list.add(jl_jdr); // 添加被升级人

						// 升级总裁后继续升级推荐人
						String zh_zc_jdr = (String) jl_jdr.get("jdr");
						args = new HashMap<String, Object>();
						args.put("zh", zh_zc_jdr);
						Map<String, Object> user_jdr2 = template
								.queryForMap("select * from crm_user where zh = :zh and yxzt = '1'", args);
						if (user_jdr2.get("jb").equals("4")) { // 如果总裁的上级是总裁,则升级为董事
							int i3 = template.update("update crm_user set jb = '5' where zh = :zh", args);
							if (i3 != 1) {
								throw new Exception(zh_zc_jdr + " 升级董事操作失败");
							}
							LogUtils.writeLogDetail(logId, "会员激活", "升级董事", zh_zc_jdr, "jb", "1", LogUtils.getNowTime(),
									args.toString(), zh);

							user_jdr2.put("jb", new Integer(user_jdr2.get("jb").toString()) + 1);
							sj_list.add(user_jdr2); // 添加被升级人

							// 升级董事后继续升级推荐人
							args = new HashMap<String, Object>();
							args.put("zh", zh_zc_jdr);
							Map<String, Object> ds_jdr = template
									.queryForMap("select * from crm_user where zh = :zh and yxzt = '1'", args);
							String zh_ds_jdr = (String) ds_jdr.get("jdr");
							args = new HashMap<String, Object>();
							args.put("zh", zh_ds_jdr);
							Map<String, Object> user_jdr3 = template
									.queryForMap("select * from crm_user where zh = :zh and yxzt = '1'", args);
							if (new Integer(user_jdr3.get("jb").toString()) == 5) { // 董事级别以上有
								// 新董事晋升,则升级
								int i4 = template.update("update crm_user set jb = '6', fhjf = 1 where zh = :zh", args);
								if (i4 != 1) {
									throw new Exception(zh_ds_jdr + " 升级"
											+ (new Integer(user_jdr3.get("jb").toString()) - 4) + "星董事操作失败");
								}
								LogUtils.writeLogDetail(logId, "会员激活",
										"升级" + (new Integer(user_jdr3.get("jb").toString()) - 4) + "星董事", zh_ds_jdr,
										"jb", "1", LogUtils.getNowTime(), args.toString(), zh);

							} else if (new Integer(user_jdr3.get("jb").toString()) == 6) { // 董事级别以上有
								// 新董事晋升,则升级
								int i4 = template.update("update crm_user set jb = '7', fhjf = 3 where zh = :zh", args);
								if (i4 != 1) {
									throw new Exception(zh_ds_jdr + " 升级"
											+ (new Integer(user_jdr3.get("jb").toString()) - 4) + "星董事操作失败");
								}
								LogUtils.writeLogDetail(logId, "会员激活",
										"升级" + (new Integer(user_jdr3.get("jb").toString()) - 4) + "星董事", zh_ds_jdr,
										"jb", "1", LogUtils.getNowTime(), args.toString(), zh);

							} else if (new Integer(user_jdr3.get("jb").toString()) == 7) { // 董事级别以上有
								// 新董事晋升,则升级
								int i4 = template.update("update crm_user set jb = '8', fhjf = 6 where zh = :zh", args);
								if (i4 != 1) {
									throw new Exception(zh_ds_jdr + " 升级"
											+ (new Integer(user_jdr3.get("jb").toString()) - 4) + "星董事操作失败");
								}
								LogUtils.writeLogDetail(logId, "会员激活",
										"升级" + (new Integer(user_jdr3.get("jb").toString()) - 4) + "星董事", zh_ds_jdr,
										"jb", "1", LogUtils.getNowTime(), args.toString(), zh);

							} else if (new Integer(user_jdr3.get("jb").toString()) == 8) { // 董事级别以上有
								// 新董事晋升,则升级
								int i4 = template.update("update crm_user set jb = '9', fhjf = 10 where zh = :zh",
										args);
								if (i4 != 1) {
									throw new Exception(zh_ds_jdr + " 升级"
											+ (new Integer(user_jdr3.get("jb").toString()) - 4) + "星董事操作失败");
								}
								LogUtils.writeLogDetail(logId, "会员激活",
										"升级" + (new Integer(user_jdr3.get("jb").toString()) - 4) + "星董事", zh_ds_jdr,
										"jb", "1", LogUtils.getNowTime(), args.toString(), zh);

							} else if (new Integer(user_jdr3.get("jb").toString()) > 8) { // 董事级别以上有
								// 新董事晋升,则升级
								int i4 = template.update(
										"update crm_user set jb = '" + (new Integer(user_jdr3.get("jb").toString()) + 1)
												+ "' , fhjf = fhjf+5 where zh = :zh",
										args);
								if (i4 != 1) {
									throw new Exception(zh_ds_jdr + " 升级"
											+ (new Integer(user_jdr3.get("jb").toString()) - 4) + "星董事操作失败");
								}
								LogUtils.writeLogDetail(logId, "会员激活",
										"升级" + (new Integer(user_jdr3.get("jb").toString()) - 4) + "星董事", zh_ds_jdr,
										"jb", "1", LogUtils.getNowTime(), args.toString(), zh);
							}
						}
					}

					Map<String, Object> user3 = null;
					// 二次升级操作
					for (Map<String, Object> user : sj_list) {
						args = new HashMap<String, Object>();
						args.put("zh", user.get("zh"));
						list = template.queryForList("select * from crm_user where jdr = :zh and yxzt = '1'", args);
						for (Map<String, Object> child : list) {
							if (new Integer(child.get("jb").toString()) >= new Integer(user.get("jb").toString())) {
								if (user.get("jb").equals("4")) {
									// 三次升级操作
									user3 = user;
								}
								args = new HashMap<String, Object>();
								args.put("zh", user.get("zh"));
								int newJb = new Integer(user.get("jb").toString()) + 1;
								args.put("jb", newJb);
								args.put("fhjf", newJb == 6 ? 1 : 0);
								template.update("update crm_user set jb = :jb,fhjf = :fhjf where zh = :zh", args);
								LogUtils.writeLogDetail(logId, "会员激活", "升级2", user.get("zh").toString(), "jb",
										newJb + "", LogUtils.getNowTime(), args.toString(), zh);
							}
						}
					}
					// 三次升级
					if (user3 != null) {
						args = new HashMap<String, Object>();
						args.put("zh", user3.get("zh"));
						list = template.queryForList("select * from crm_user where jdr = :zh and yxzt = '1'", args);
						for (Map<String, Object> child : list) {
							if (new Integer(child.get("jb").toString()) >= new Integer(user3.get("jb").toString())) {
								args = new HashMap<String, Object>();
								args.put("zh", user3.get("zh"));
								int newJb = new Integer(user3.get("jb").toString()) + 1;
								args.put("jb", newJb);
								args.put("fhjf", newJb == 6 ? 1 : 0);
								template.update("update crm_user set jb = :jb,fhjf = :fhjf where zh = :zh", args);
								LogUtils.writeLogDetail(logId, "会员激活", "升级3", user3.get("zh").toString(), "jb",
										newJb + "", LogUtils.getNowTime(), args.toString(), zh);
							}
						}
					}

				}
			}

		});

		if (ttReturnObj.hasException()) {
			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), ttReturnObj.getE().getMessage(),
					LogUtils.getStackTrace(ttReturnObj.getE()), LogUtils.getUserId(request));

		}
		return ttReturnObj.getMsg();
	}

	@POST
	@Path("delhy")
	public String delhy(@FormParam("mymm2") final String mymm2, @FormParam("zh") final String zh,
			@Context HttpServletRequest request) {

		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		try {
			if (!checkMm2(mymm2, request)) {
				return "alert('二级密码不正确');$('#sb_box2').hide();";
			}
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", zh);
			int i = template.update("delete from crm_user where zh = :zh", args);
			if (i == 1) {
				LogUtils.writeLogDetail(logId, "删除会员", "", getUserId(request), "", "", LogUtils.getNowTime(),
						args.toString());
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));

				return "alert('操作成功');load('hysh.html');";
			}

			return "alert('操作失败');$('#sb_box2').hide();";
		} catch (Exception e) {

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			return "alert('操作失败');$('#sb_box2').hide();";
		}

	}

	@SuppressWarnings("unchecked")
	@GET
	@Path("loadHtml")
	public String loadHtml(@QueryParam("path") String path, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("loadHtml(String, HttpServletRequest) - start");
		}
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);

		// 获取 当前用户帐号
		String userId = getUserId(request);
		if (userId == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("loadHtml(String, HttpServletRequest) - end");
			}
			return "{msg:0}";
		}

		try {
			if (logger.isInfoEnabled()) {
				logger.info("loadHtml(String, HttpServletRequest) - " + userId + " --> " + path + "?"
						+ request.getQueryString());
			}
			Configuration cfg = new Configuration();
			cfg.setDirectoryForTemplateLoading(new File(request.getSession().getServletContext().getRealPath("")));
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
			String methodName = "initParam_" + path.substring(0, path.indexOf("."));
			Map<String, Object> root = (Map<String, Object>) this.getClass()
					.getMethod(methodName, new Class[] { HttpServletRequest.class })
					.invoke(this, new Object[] { request });

			Template temp = cfg.getTemplate(path);
			StringWriter sw = new StringWriter();
			temp.process(root, sw);
			String returnString = sw.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("loadHtml(String, HttpServletRequest) - end");
			}
			LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), path, LogUtils.getUserId(request));
			return returnString;
		} catch (Exception e) {
			logger.error("loadHtml(String, HttpServletRequest)", e);
			String returnString = errorHandler(e);
			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("loadHtml(String, HttpServletRequest) - end");
			}
			return returnString;
		}
	}

	@GET
	@Path("getTreeList")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Map<String, Object>> getTreeList(@QueryParam("userId") String userId,
			@Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getTreeList(String, HttpServletRequest) - start");
		}

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh",
					(userId == null || userId.equals("") || userId.equals("undefined")) ? getUserId(request) : userId);
			List<Map<String, Object>> returnList = template.queryForList("WITH recursive cte AS("
					+ " SELECT 1 AS DEPTH,a.* FROM crm_user a WHERE zh = :zh and yxzt = '1'" + " UNION ALL "
					+ " SELECT DEPTH + 1 AS DEPTH,b.* FROM crm_user b INNER JOIN cte c ON b.jdr=c.zh where b.yxzt = '1'"
					+ " ) SELECT  * FROM cte order by depth,azwz", args);
			for (Map<String, Object> map : returnList) {
				map.put("key", map.get("zh"));
				map.put("boss", map.get("jdr"));
				map.put("name", map.get("zh") + " " + map.get("depth") + " "
						+ CodeUtils.getValue("JB", (String) map.get("jb")));
				map.put("title", "左区业绩:" + map.get("zqyj") + " 右区业绩:" + map.get("yqyj") + " 奖金币:" + map.get("jjb")
						+ " 优惠券:" + map.get("yhq"));
			}
			if (logger.isDebugEnabled()) {
				logger.debug("getTreeList(String, HttpServletRequest) - end");
			}
			return returnList;
		} catch (Exception e) {
			logger.error("getTreeList(String, HttpServletRequest)", e);

			e.printStackTrace();

			if (logger.isDebugEnabled()) {
				logger.debug("getTreeList(String, HttpServletRequest) - end");
			}
			return list;
		}
	}

	@GET
	@Path("getTreeList2")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Map<String, Object>> getTreeList2(@QueryParam("userId") String userId,
			@Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getTreeList2(String, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh",
					(userId == null || userId.equals("") || userId.equals("undefined")) ? getUserId(request) : userId);
			List<Map<String, Object>> returnList = template.queryForList("WITH recursive cte AS("
					+ " SELECT 1 AS DEPTH,a.* FROM crm_user a WHERE zh = :zh and yxzt = '1'" + " UNION ALL "
					+ " SELECT DEPTH + 1 AS DEPTH,b.* FROM crm_user b INNER JOIN cte c ON b.tjr=c.zh where b.yxzt = '1'"
					+ " ) SELECT  * FROM cte order by depth,azwz", args);
			for (Map<String, Object> map : returnList) {
				map.put("key", map.get("zh"));
				map.put("boss", map.get("tjr"));
				map.put("name", map.get("zh") + " " + map.get("depth") + " "
						+ CodeUtils.getValue("JB", (String) map.get("jb")));
				map.put("title", "左区业绩:" + map.get("zqyj") + " 右区业绩:" + map.get("yqyj") + " 奖金币:" + map.get("jjb")
						+ " 优惠券:" + map.get("yhq"));
			}

			if (logger.isDebugEnabled()) {
				logger.debug("getTreeList2(String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return returnList;
		} catch (Exception e) {
			logger.error("getTreeList2(String, HttpServletRequest)", e); //$NON-NLS-1$

			if (logger.isDebugEnabled()) {
				logger.debug("getTreeList2(String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return list;
		}
	}

	private String getUserId(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getUserId(HttpServletRequest) - start");
		}

		String returnString = (String) request.getSession().getAttribute("userId");
		if (logger.isDebugEnabled()) {
			logger.debug("getUserId(HttpServletRequest) - end");
		}
		return returnString;
	}

	@GET
	@Path("getXmByZh")
	public String getXmByZh(@QueryParam("zh") String zh, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getXmByZh(String, HttpServletRequest) - start");
		}

		try {

			if (zh == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("getXmByZh(String, HttpServletRequest) - end");
				}
				return "";
			}

			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", zh);

			List<Map<String, Object>> queryForList = template
					.queryForList("select * from crm_user where zh = :zh and yxzt = '1'", args);
			if (queryForList.size() == 0) {
				String returnString2 = "<span style='color:red;'>没有找到 " + zh + "</span>";
				if (logger.isDebugEnabled()) {
					logger.debug("getXmByZh(String, HttpServletRequest) - end");
				}
				return returnString2;
			}
			Map<String, Object> user = queryForList.get(0);
			String returnString = (String) user.get("xm");
			if (logger.isDebugEnabled()) {
				logger.debug("getXmByZh(String, HttpServletRequest) - end");
			}
			return returnString;
		} catch (Exception e) {
			logger.error("getXmByZh(String, HttpServletRequest)", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getXmByZh(String, HttpServletRequest) - end");
		}
		return "<span style='color:red;'>没有找到 " + zh + "</span>";
	}

	@GET
	@Path("getXmAndBdbByZh")
	public String getXmAndBdbByZh(@QueryParam("zh") String zh, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getXmAndBdbByZh(String, HttpServletRequest) - start");
		}

		try {

			if (zh == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("getXmAndBdbByZh(String, HttpServletRequest) - end");
				}
				return "";
			}

			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", zh);

			List<Map<String, Object>> queryForList = template
					.queryForList("select * from crm_user where zh = :zh and yxzt = '1'", args);
			if (queryForList.size() == 0) {
				String returnString2 = "<span style='color:red;'>没有找到 " + zh + "</span>";
				if (logger.isDebugEnabled()) {
					logger.debug("getXmAndBdbByZh(String, HttpServletRequest) - end");
				}
				return returnString2;
			}
			Map<String, Object> user = queryForList.get(0);
			String returnString = user.get("xm") + " 剩余报单币:" + user.get("bdb");
			if (logger.isDebugEnabled()) {
				logger.debug("getXmByZh(String, HttpServletRequest) - end");
			}
			return returnString;
		} catch (Exception e) {
			logger.error("getXmAndBdbByZh(String, HttpServletRequest)", e);
		}

		String returnString = "<span style='color:red;'>没有找到 " + zh + "</span>";
		if (logger.isDebugEnabled()) {
			logger.debug("getXmAndBdbByZh(String, HttpServletRequest) - end");
		}
		return returnString;
	}

	@GET
	@Path("getXmAndBdbByZh2")
	public String getXmAndBdbByZh2(@QueryParam("zh") String zh, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getXmAndBdbByZh2(String, HttpServletRequest) - start");
		}
		try {
			if (zh == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("getXmAndBdbByZh2(String, HttpServletRequest) - end");
				}
				return "";
			}
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", zh);
			if (template.queryForInt("select count(1) from crm_user where zh = :zh and yxzt = '1'", args) == 0) {
				String returnString = "<span style='color:red;'>没有找到 " + zh + "</span>";

				if (logger.isDebugEnabled()) {
					logger.debug("getXmAndBdbByZh2(String, HttpServletRequest) - end");
				}
				return returnString;
			}

			args.put("zh", getUserId(request));
			args.put("zh2", zh);
			if (template.queryForInt("select count(1) from (\n" + "        (\n"
					+ "        WITH recursive cte AS( SELECT a.* FROM crm_user a WHERE zh = :zh and yxzt = '1' UNION ALL SELECT b.* FROM crm_user b INNER JOIN cte c ON b.zh=c.jdr where b.yxzt = '1' )\n"
					+ "        select * from cte\n" + "        )\n" + "        union\n" + "        (\n"
					+ "        WITH recursive cte2 AS( SELECT a.* FROM crm_user a WHERE zh = :zh and yxzt = '1' UNION ALL SELECT b.* FROM crm_user b INNER JOIN cte2 c ON b.jdr=c.zh where b.yxzt = '1' )\n"
					+ "        select * from cte2\n" + "        )\n" + ") aaa where zh = :zh2", args) == 0) {
				String returnString2 = "<span style='color:red;'>" + zh + "跟你不在一条主线上</span>";
				if (logger.isDebugEnabled()) {
					logger.debug("getXmAndBdbByZh2(String, HttpServletRequest) - end");
				}
				return returnString2;
			}

			args.put("zh", zh);
			List<Map<String, Object>> queryForList = template
					.queryForList("select * from crm_user where zh = :zh and yxzt = '1'", args);
			if (queryForList.size() == 0) {
				String returnString2 = "<span style='color:red;'>没有找到 " + zh + "</span>";

				if (logger.isDebugEnabled()) {
					logger.debug("getXmAndBdbByZh2(String, HttpServletRequest) - end");
				}
				return returnString2;
			}
			Map<String, Object> user = queryForList.get(0);
			String returnString = user.get("xm") + " 剩余报单币:" + user.get("bdb");

			if (logger.isDebugEnabled()) {
				logger.debug("getXmAndBdbByZh2(String, HttpServletRequest) - end");
			}
			return returnString;
		} catch (Exception e) {
			logger.error("getXmAndBdbByZh2(String, HttpServletRequest)", e);
		}

		String returnString = "<span style='color:red;'>没有找到 " + zh + "</span>";

		if (logger.isDebugEnabled()) {
			logger.debug("getXmAndBdbByZh2(String, HttpServletRequest) - end");
		}
		return returnString;
	}

	@GET
	@Path("checkJdrJb")
	public String checkJdrJb(@QueryParam("zh") String zh, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getXmByZh(String, HttpServletRequest) - start");
		}

		try {

			if (zh == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkJdrJb(String, HttpServletRequest) - end");
				}
				return "";
			}

			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", zh);

			if (logger.isInfoEnabled()) {
				logger.info("checkJdrJb(String, HttpServletRequest) - zh=" + zh);
			}

			List<Map<String, Object>> queryForList = template
					.queryForList("select * from crm_user where zh = :zh and yxzt = '1'", args);
			if (queryForList.size() == 0) {
				String returnString = "$('#pid_nm').html('<span style=\"color:red;\">没有找到 " + zh + "</span>');";

				if (logger.isDebugEnabled()) {
					logger.debug("checkJdrJb(String, HttpServletRequest) - end");
				}
				return returnString;
			}
			Map<String, Object> user = queryForList.get(0);
			String xm = (String) user.get("xm");
			if (new Integer(user.get("jb").toString()) > 4) {
				List<Map<String, Object>> list = template
						.queryForList("select * from crm_user where jdr = :zh and azwz <> '0' and yxzt = '1'", args);
				int i = list.size() + 1;
				String returnString2 = "$('#pid_nm').html('" + xm
						+ "');$('#azwd_td').html('接点人属于董事,自动安放<input name=\"azsite\" value=\"" + i
						+ "\" style=\"display:none;\" />');";
				if (logger.isDebugEnabled()) {
					logger.debug("checkJdrJb(String, HttpServletRequest) - end");
				}
				return returnString2;
			}
			String returnString2 = "$('#pid_nm').html('" + xm
					+ "');$('#azwd_td').html('<label><input type=\"radio\" name=\"azsite\" checked=\"checked\" value=\"1\" id=\"RadioGroup2_00\">1区</label><label><input type=\"radio\" name=\"azsite\" value=\"2\" id=\"RadioGroup2_0\">2区</label><span style=\"color: red;\">*</span>');";
			if (logger.isDebugEnabled()) {
				logger.debug("checkJdrJb(String, HttpServletRequest) - end");
			}
			return returnString2;
		} catch (Exception e) {
			logger.error("getXmByZh(String, HttpServletRequest)", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getXmByZh(String, HttpServletRequest) - end");
		}
		return "$('#pid_nm').html('<span style=\"color:red;\">没有找到 " + zh + "</span>');";
	}

	@GET
	@Path("checkZh")
	public String checkZh(@QueryParam("zh") String zh, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("checkZh(String, HttpServletRequest) - start");
		}

		try {
			if (zh == null || zh.equals("")) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkZh(String, HttpServletRequest) - end");
				}
				return "<span style='color:red;'>不能为空</span>";
			}
			if (zh.length() < 6) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkZh(String, HttpServletRequest) - end");
				}
				return "<span style='color:red;'>最少6个字符</span>";
			}
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", zh);
			if (template.queryForInt("select count(1) from crm_user where zh = :zh and yxzt = '1'", args) > 0) {
				String returnString = "<span style='color:red;'>" + zh + " 已经存在</span>";
				if (logger.isDebugEnabled()) {
					logger.debug("checkZh(String, HttpServletRequest) - end");
				}
				return returnString;
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("checkZh(String, HttpServletRequest) - end");
				}
				return "<span style='color:green;'>检查通过</span>";
			}

		} catch (Exception e) {
			logger.error("checkZh(String, HttpServletRequest)", e);

			String returnString = "<span style='color:red;'>检查失败," + e.getMessage() + "</span>";
			if (logger.isDebugEnabled()) {
				logger.debug("checkZh(String, HttpServletRequest) - end");
			}
			return returnString;
		}

	}

	@GET
	@Path("checkMm1")
	public String checkMm1(@QueryParam("mm1") String mm1, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("checkMm1(String, HttpServletRequest) - start");
		}

		try {
			if (mm1 == null || mm1.equals("")) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkMm1(String, HttpServletRequest) - end");
				}
				return "<span style='color:red;'>不能为空</span>";
			}
			if (mm1.length() < 6) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkMm1(String, HttpServletRequest) - end");
				}
				return "<span style='color:red;'>最少6个字符</span>";
			}
		} catch (Exception e) {
			logger.error("checkMm1(String, HttpServletRequest)", e);

			String returnString = "检查失败," + e.getMessage();
			if (logger.isDebugEnabled()) {
				logger.debug("checkMm1(String, HttpServletRequest) - end");
			}
			return returnString;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("checkMm1(String, HttpServletRequest) - end");
		}
		return "<span style='color:green;'>检查通过</span>";
	}

	@GET
	@Path("checkMm11")
	public String checkMm11(@QueryParam("mm1") String mm1, @QueryParam("mm11") String mm11,
			@Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("checkMm11(String, String, HttpServletRequest) - start");
		}

		try {
			if (mm11 == null || mm11.equals("")) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkMm11(String, String, HttpServletRequest) - end");
				}
				return "<span style='color:red;'>不能为空</span>";
			}
			if (!mm11.equals(mm1)) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkMm11(String, String, HttpServletRequest) - end");
				}
				return "<span style='color:red;'>两次密码不一致</span>";
			}
		} catch (Exception e) {
			logger.error("checkMm11(String, String, HttpServletRequest)", e);

			String returnString = "检查失败," + e.getMessage();
			if (logger.isDebugEnabled()) {
				logger.debug("checkMm11(String, String, HttpServletRequest) - end");
			}
			return returnString;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("checkMm11(String, String, HttpServletRequest) - end");
		}
		return "<span style='color:green;'>检查通过</span>";
	}

	@GET
	@Path("checkNone")
	public String checkNone(@QueryParam("value") String value, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("checkNone(String, HttpServletRequest) - start");
		}

		try {
			if (value == null || value.equals("")) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkNone(String, HttpServletRequest) - end");
				}
				return "<span style='color:red;'>不能为空</span>";
			}
		} catch (Exception e) {
			logger.error("checkNone(String, HttpServletRequest)", e);

			String returnString = "检查失败," + e.getMessage();
			if (logger.isDebugEnabled()) {
				logger.debug("checkNone(String, HttpServletRequest) - end");
			}
			return returnString;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("checkNone(String, HttpServletRequest) - end");
		}
		return "<span style='color:green;'>检查通过</span>";
	}

	private String errorHandler(Exception e) {
		if (logger.isDebugEnabled()) {
			logger.debug("errorHandler(Exception) - start");
		}

		String returnString = "get page error，" + e.getMessage();
		if (logger.isDebugEnabled()) {
			logger.debug("errorHandler(Exception) - end");
		}
		return returnString;
	}

	public Map<String, Object> initParam_wdzl(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_wdzl(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			Map<String, Object> user = initMe(request);
			user.put("jb_cn", CodeUtils.getValue("JB", (String) user.get("jb")));
			user.put("khyh", CodeUtils.getValue("YH", (String) user.get("khyh")));
			root.put("user", user);

		} catch (Exception e) {
			logger.error("initParam_wdzl(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_wdzl(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_wdzl(HttpServletRequest) - end");
		}
		return root;
	}

	/**
	 * 初始化修改登录密码
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_xgdlmm(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_xgdlmm(HttpServletRequest) - start");
		}

		Map<String, Object> returnMap = new HashMap<String, Object>();
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_xgdlmm(HttpServletRequest) - end");
		}
		return returnMap;
	}

	/**
	 * 初始化修改资料
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_xgzl(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_xgzl(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			Map<String, Object> user = initMe(request);
			user.put("jb_cn", CodeUtils.getValue("JB", (String) user.get("jb")));
			root.put("user", user);

		} catch (Exception e) {
			logger.error("initParam_xgzl(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_xgzl(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_xgzl(HttpServletRequest) - end");
		}
		return root;
	}

	/**
	 * 初始化修改二级密码
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_xgejmm(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_xgejmm(HttpServletRequest) - start");
		}

		Map<String, Object> returnMap = new HashMap<String, Object>();
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_xgejmm(HttpServletRequest) - end");
		}
		return returnMap;
	}

	/**
	 * 初始化会员注册
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_hyzc(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_hyzc(HttpServletRequest) - start");
		}
		Map<String, Object> root = new HashMap<String, Object>();
		try {
			Map<String, Object> user = initMe(request);
			user.put("jb_cn", CodeUtils.getValue("JB", (String) user.get("jb")));
			user.put("jdr", request.getParameter("jdr") == null ? "" : request.getParameter("jdr"));
			user.put("yhq_ky", new Double(user.get("yhq").toString()) > 1000 ? "1000" : user.get("yhq"));
			user.put("azwz1", "checked='checked'");
			user.put("azwz2", "2".equals(request.getParameter("azwz")) ? "checked='checked'" : "");
			if (user.get("sffwzx").equals("1")) {
				user.put("fwzx", user.get("zh"));
			}
			root.put("user", user);

		} catch (Exception e) {
			logger.error("initParam_hyzc(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_hyzc(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_hyzc(HttpServletRequest) - end");
		}
		return root;
	}

	/**
	 * 初始化我注册的会员
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_wzcdhy(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_wzcdhy(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			String userId = getUserId(request);
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", userId);
			List<Map<String, Object>> queryForList = template.queryForList(
					"select u.*,c.value as jb_cn from crm_user u,crm_code c where u.jb = c.key and c.type='JB' and cjr = :zh and u.yxzt = '1' order by cjsj desc",
					args);
			for (Map<String, Object> map : queryForList) {
				String string = map.get("cjsj").toString();
				map.put("cjsj", sjFormate19(string));
				map.put("lx", CodeUtils.getValue("HYLX", map.get("lx").toString()));
			}
			root.put("list", queryForList);
		} catch (Exception e) {
			logger.error("initParam_wzcdhy(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_wzcdhy(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_wzcdhy(HttpServletRequest) - end");
		}
		return root;
	}

	public Map<String, Object> initParam_hysh(HttpServletRequest request) {

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			String userId = getUserId(request);
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", userId);
			Object yxzt = "0";
			args.put("yxzt", yxzt);
			List<Map<String, Object>> queryForList = template.queryForList(
					"select * from crm_user where (fwzx = :zh or cjr = :zh) and yxzt = :yxzt order by cjsj desc", args);
			for (Map<String, Object> map : queryForList) {
				String string = map.get("cjsj").toString();
				map.put("cjsj", sjFormate19(string));
				map.put("lx_cn", CodeUtils.getValue("HYLX", map.get("lx").toString()));
			}
			root.put("list", queryForList);
			root.put("user", initMe(request));
		} catch (Exception e) {
			return root;
		}

		return root;
	}

	/**
	 * 初始化服务中心会员
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_fwzxhy(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_fwzxhy(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			String userId = getUserId(request);
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", userId);
			List<Map<String, Object>> queryForList = template.queryForList(
					"select u.*,c.value as jb_cn from crm_user u,crm_code c where u.jb = c.key and c.type='JB' and fwzx = :zh and u.yxzt = '1' order by cjsj desc",
					args);
			for (Map<String, Object> map : queryForList) {
				String string = map.get("cjsj").toString();
				map.put("cjsj", sjFormate19(string));
				map.put("lx", CodeUtils.getValue("HYLX", map.get("lx").toString()));
			}
			root.put("list", queryForList);
			args = new HashMap<String, Object>();
			args.put("zh", userId);
			Map<String, Object> user = template.queryForMap("select * from crm_user where zh = :zh and yxzt = '1'",
					args);
			if (user.get("sffwzx").equals("0")) {
				if (new Integer(user.get("jb").toString()) > 4) {
					root.put("fwzxMsg", "您是:" + CodeUtils.getValue("JB", (String) user.get("jb")) + ",可以申请服务中心");
				} else {
					root.put("fwzxMsg", "您还不是服务中心,董事级别以上,可以申请服务中心");
				}
			} else {
				root.put("fwzxMsg", "");
			}
		} catch (Exception e) {
			logger.error("initParam_fwzxhy(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_fwzxhy(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_fwzxhy(HttpServletRequest) - end");
		}
		return root;
	}

	/**
	 * 初始化安置系谱表
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_azxpb(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_azxpb(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			String userId = getUserId(request);
			String userId2 = userId;
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			if (request.getParameter("zh") != null && !request.getParameter("zh").equals("")) {
				userId2 = request.getParameter("zh");
			}
			args.put("zh2", userId);
			args.put("zh", userId2);
			root.put("zh", userId2);
			List<Map<String, Object>> queryForList = template.queryForList(
					"with recursive cte as (  select (with recursive cte as (  select 1 AS DEPTH,a.* from crm_user a "
							+ "where zh = :zh2 union all  select DEPTH + 1 AS DEPTH,b.* from crm_user b inner join cte c on b.jdr=c.zh) select depth from cte where zh = :zh) AS DEPTH,a.* from crm_user a "
							+ "where zh = :zh union all  select DEPTH + 1 AS DEPTH,b.* from crm_user b inner join cte c on b.jdr=c.zh)"
							+ " select cte.depth,u.*,c.value as jb_cn from crm_user u,crm_code c,cte where u.jb = c.key and u.zh = cte.zh and c.type='JB' and cte.depth <= (5+(with recursive cte as (  select 1 AS DEPTH,a.* from crm_user a where zh = :zh2 union all  select DEPTH + 1 AS DEPTH,b.* from crm_user b inner join cte c on b.jdr=c.zh) select depth from cte where zh = :zh)) order by cjsj desc",
					args);
			List<Map<String, Object>> addList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : queryForList) {
				addZcLink(map, queryForList, addList);
				if (map.get("yxzt").equals("0")) {
					map.put("zh", "<a style=\"cursor:pointer;color:blue;\" onclick=\"load('hysh.html')\">激活</a>");
				} else {
					map.put("zh", "<a style=\"cursor:pointer;color:blue;\" onclick=\"load('azxpb.html&zh="
							+ map.get("zh") + "')\">" + map.get("zh") + "</a>");
				}
				String string = map.get("cjsj").toString();
				map.put("cjsj", sjFormate19(string));
				map.put("lx", CodeUtils.getValue("HYLX", map.get("lx").toString()));
			}
			queryForList.addAll(addList);
			root.put("list", queryForList);
		} catch (Exception e) {
			logger.error("initParam_azxpb(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_azxpb(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_azxpb(HttpServletRequest) - end");
		}
		return root;
	}

	private void addZcLink(Map<String, Object> map, List<Map<String, Object>> queryForList,
			List<Map<String, Object>> addList) {
		if (map.get("yxzt").equals("0")) {
			return;
		}
		String zh = (String) map.get("zh");
		String testStr = "1,2";
		List<String> azwzList = new ArrayList<String>();
		for (String t : testStr.split(",")) {
			azwzList.add(t);
		}
		for (Map<String, Object> map2 : queryForList) {
			if (zh.equals(map2.get("jdr"))) {
				azwzList.remove(map2.get("azwz").toString());
			}
		}
		HashMap<String, Object> user = null;
		for (String azwz : azwzList) {
			user = new HashMap<String, Object>();
			user.put("depth", new Integer(map.get("depth").toString()) + 1);
			user.put("azwz", azwz);
			user.put("zh", "<a style=\"cursor:pointer;color:blue;\" onclick=\"load('hyzc.html&jdr=" + zh + "&azwz="
					+ azwz + "')\">注册</a>");
			user.put("jdr", zh);
			user.put("jb", "1");
			user.put("jb_cn", CodeUtils.getValue("JB", "1"));
			user.put("zqdryj", "0");
			user.put("yqdryj", "0");
			user.put("zqyj", "0");
			user.put("yqyj", "0");
			user.put("zqsyyj", "0");
			user.put("yqsyyj", "0");
			user.put("lx", "-");
			addList.add(user);
		}

	}

	/**
	 * 初始化业绩报告
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_yjbg(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_yjbg(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			String userId = getUserId(request);
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", userId);
			List<Map<String, Object>> queryForList = template.queryForList(
					"WITH recursive cte AS( SELECT 1 AS DEPTH, a.*,case a.lx when '1' then 7000 when '2' then 3500 end as yj FROM crm_user a WHERE jdr=:zh\n"
							+ "    UNION ALL\n"
							+ "    SELECT DEPTH + 1 AS DEPTH,b.*,case b.lx when '1' then 7000 when '2' then 3500 end as yj FROM crm_user b INNER JOIN cte c ON b.jdr=c.zh\n"
							+ ")\n" + "select depth,sum(zqall) zqall,sum(yqall) yqall,sum(zq) zq,sum(yq) yq from (\n"
							+ "select depth,case azwz when '1' then sum(zqyj)+sum(yqyj) else 0 end as zqall\n"
							+ "        ,case azwz when '2' then sum(zqyj)+sum(yqyj) else 0 end as yqall\n"
							+ "        ,case azwz when '1' then sum(yj) else 0 end as zq\n"
							+ "        ,case azwz when '2' then sum(yj) else 0 end as yq\n"
							+ "    from cte group by depth,azwz order by depth,azwz\n" + ") aa group by depth",
					args);
			for (Map<String, Object> map : queryForList) {
				map.put("hj", new Double(map.get("zq").toString()) + new Double(map.get("yq").toString()));
				map.put("hj2", new Double(map.get("zqall").toString()) + new Double(map.get("yqall").toString()));
			}
			root.put("list", queryForList);
		} catch (Exception e) {
			logger.error("initParam_yjbg(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_yjbg(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_yjbg(HttpServletRequest) - end");
		}
		return root;

	}

	/**
	 * 初始化新增业绩
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_xzyj(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_xzyj(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			String userId = getUserId(request);
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", userId);
			List<Map<String, Object>> queryForList = template.queryForList(
					"with recursive cte as (  select 1 AS DEPTH,a.* from crm_user a where zh=:zh\n"
							+ "              union all  select DEPTH + 1 AS DEPTH,b.* from crm_user b inner join cte c on b.jdr=c.zh)\n"
							+ "select cte.*,(case when cte.depth <=2 then azwz when cte.depth >2 then (with recursive cte2 as (  select 1 AS DEPTH,a.* from crm_user a where zh=cte.zh\n"
							+ "              union all  select DEPTH + 1 AS DEPTH,b.* from crm_user b inner join cte2 c on b.zh=c.jdr)\n"
							+ "              select azwz from cte2 where depth = (cte.depth-1)) end  ) as sswz from cte",
					args);
			for (Map<String, Object> map : queryForList) {
				String string = map.get("cjsj").toString();
				map.put("cjsj", sjFormate19(string));
				map.put("azwz", map.get("sswz"));
				map.put("lx", CodeUtils.getValue("HYLX", map.get("lx").toString()));
			}
			root.put("list", queryForList);
		} catch (Exception e) {
			logger.error("initParam_xzyj(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_xzyj(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_xzyj(HttpServletRequest) - end");
		}
		return root;
	}

	/**
	 * 初始化报单币转账
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_bdbzz(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_bdbzz(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			Map<String, Object> user = initMe(request);

			root.put("user", user);
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", getUserId(request));
			List<Map<String, Object>> queryForList = template.queryForList(
					"select a.*,COALESCE(a.n2,'') as n2,COALESCE(a.n3,'') as n3 from crm_log_mx a where yxxm = 'bdb' and czlx = '报单币互转' and yxdx = :zh",
					args);
			for (Map<String, Object> map : queryForList) {
				String string = map.get("sxsj").toString();
				map.put("sxsj", sjFormate19(string));
			}
			root.put("list", queryForList);
		} catch (Exception e) {
			logger.error("initParam_bdbzz(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_bdbzz(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_bdbzz(HttpServletRequest) - end");
		}
		return root;
	}

	/**
	 * 初始化奖金币转报单币
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_jjbzbdb(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_jjbzbdb(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			root.put("user", initMe(request));
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", getUserId(request));
			List<Map<String, Object>> queryForList = template.queryForList(
					"select a.*,COALESCE(a.n2,'') as n2,COALESCE(a.n3,'') as n3 from crm_log_mx a where yxxm = 'jjb' and czlx = '奖金币转报单币' and yxdx = :zh",
					args);
			for (Map<String, Object> map : queryForList) {
				String string = map.get("sxsj").toString();
				map.put("sxsj", sjFormate19(string));
			}
			root.put("list", queryForList);
		} catch (Exception e) {
			logger.error("initParam_jjbzbdb(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_jjbzbdb(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_jjbzbdb(HttpServletRequest) - end");
		}
		return root;
	}

	/**
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_yhqzthb(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_yhqzthb(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			root.put("user", initMe(request));
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", getUserId(request));
			List<Map<String, Object>> queryForList = template.queryForList(
					"select a.*,COALESCE(a.n2,'') as n2,COALESCE(a.n3,'') as n3 from crm_log_mx a where yxxm = 'yhq' and czlx = '优惠券转提货币' and yxdx = :zh",
					args);
			for (Map<String, Object> map : queryForList) {
				String string = map.get("sxsj").toString();
				map.put("sxsj", sjFormate19(string));
			}
			root.put("list", queryForList);
		} catch (Exception e) {
			logger.error("initParam_yhqzthb(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_yhqzthb(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_yhqzthb(HttpServletRequest) - end");
		}
		return root;
	}

	/**
	 * 初始化日奖金查询
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_rjjcx(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_rjjcx(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			String userId = getUserId(request);
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", userId);
			List<Map<String, Object>> queryForList = template.queryForList(
					"select rq , sum(fwf) fwf,sum(xffh) xffh,sum(jc) jc,sum(gljt) gljt,sum(dp) dp,sum(dp)+sum(jc)+sum(fwf) as jjbhj,sum(xffh)+sum(gljt) as yhqhj from (\n"
							+ "  select substr(sxsj,0,11) as rq,  0 as fwf,0 as xffh,0 as dp,0 as gljt,sum(cast(yxsl as numeric)) as jc from crm_log_mx where yxdx = :zh and czlx = '级差' group by substr(sxsj,0,11)\n"
							+ "  union\n"
							+ "  select substr(sxsj,0,11) as rq,  0,0,0,sum(cast(yxsl as numeric)),0 from crm_log_mx where yxdx = :zh and czlx = '管理津贴' group by substr(sxsj,0,11)\n"
							+ "  union\n"
							+ "  select substr(sxsj,0,11) as rq,  0,0,sum(cast(yxsl as numeric)),0,0 from crm_log_mx where yxdx = :zh and czlx = '对碰' group by substr(sxsj,0,11)\n"
							+ "  union\n"
							+ "  select substr(sxsj,0,11) as rq,  0,sum(cast(yxsl as numeric)),0,0,0 from crm_log_mx where yxdx = :zh and czlx = '消费分红' group by substr(sxsj,0,11)\n"
							+ "  union\n"
							+ "  select substr(sxsj,0,11) as rq,  sum(cast(yxsl as numeric)),0,0,0,0 from crm_log_mx where yxdx = :zh and czlx = '服务中心服务费' group by substr(sxsj,0,11)\n"
							+ "  ) aa group by rq",
					args);
			root.put("list", queryForList);
		} catch (Exception e) {
			logger.error("initParam_rjjcx(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_rjjcx(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_rjjcx(HttpServletRequest) - end");
		}
		return root;
	}

	/**
	 * 初始化旬奖金查询
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_xjjcx(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_xjjcx(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			String userId = getUserId(request);
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", userId);
			List<Map<String, Object>> queryForList = template
					.queryForList(
							"select b.* from crm_log_mx b, crm_log a where a.lid = b.lid\n" + "and yxdx = :zh\n"
									+ "and fwm = 'cn.com.crc.base.service.QuartzJobXj$1'\n" + "order by sxsj desc;",
							args);
			for (Map<String, Object> map : queryForList) {
				String string = map.get("sxsj").toString();
				map.put("sxsj", sjFormate19(string));
			}
			root.put("list", queryForList);

		} catch (Exception e) {
			logger.error("initParam_xjjcx(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_xjjcx(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_xjjcx(HttpServletRequest) - end");
		}
		return root;
	}

	/**
	 * 初始化奖金币明细
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_jjbmx(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_jjbmx(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			String userId = getUserId(request);
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", userId);
			List<Map<String, Object>> queryForList = template
					.queryForList("select b.* from crm_log_mx b, crm_log a where a.lid = b.lid\n" + "and yxdx = :zh\n"
							+ "and yxxm = 'jjb'\n" + "order by sxsj desc;", args);
			for (Map<String, Object> map : queryForList) {
				String string = map.get("sxsj").toString();
				map.put("sxsj", sjFormate19(string));
			}
			Map<String, Object> me = initMe(request);
			root.put("list", queryForList);
			root.put("yhqyf", me.get("yhqyf"));
			root.put("gljtyf", me.get("gljtyf"));

		} catch (Exception e) {
			logger.error("initParam_jjbmx(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_jjbmx(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_jjbmx(HttpServletRequest) - end");
		}
		return root;
	}

	/**
	 * 初始化优惠券明细
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_yhqmx(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_yhqmx(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			String userId = getUserId(request);
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", userId);
			List<Map<String, Object>> queryForList = template
					.queryForList("select b.* from crm_log_mx b, crm_log a where a.lid = b.lid\n" + "and yxdx = :zh\n"
							+ "and fwm = 'cn.com.crc.base.service.QuartzJobRj_gljt$1'\n" + "order by sxsj desc;", args);
			for (Map<String, Object> map : queryForList) {
				String string = map.get("sxsj").toString();
				map.put("sxsj", sjFormate19(string));
			}
			Map<String, Object> me = initMe(request);
			root.put("list", queryForList);
			root.put("yhqyf", me.get("yhqyf"));
			root.put("gljtyf", me.get("gljtyf"));

		} catch (Exception e) {
			logger.error("initParam_yhqmx(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_yhqmx(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_yhqmx(HttpServletRequest) - end");
		}
		return root;
	}

	/**
	 * 初始化公司公告
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_gsgg(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_gsgg(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			List<Map<String, Object>> queryForList = template
					.queryForList("select * from crm_gg where yxbz = '1' order by sj desc", args);
			for (Map<String, Object> map : queryForList) {
				map.put("sj", map.get("sj").toString().length() > 19 ? map.get("sj").toString().substring(0, 19)
						: map.get("sj").toString());
			}
			root.put("list", queryForList);

		} catch (Exception e) {
			logger.error("initParam_gsgg(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_gsgg(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_gsgg(HttpServletRequest) - end");
		}
		return root;
	}

	/**
	 * 初始化写邮件
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_xyj(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_xyj(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			Map<String, Object> user = initMe(request);
			root.put("gly", CodeUtils.getValue("GLYZH", "1"));
			root.put("fwzx", user.get("fwzx"));
			String sjr = request.getParameter("sjr");
			String msgId = request.getParameter("msgId");
			if ("1".equals(sjr)) {
				root.put("sjr", CodeUtils.getValue("GLYZH", "1"));
				root.put("bt", "");
				root.put("nr", "");
			} else if (msgId != null) {
				NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
				Map<String, Object> args = new HashMap<String, Object>();
				args.put("mid", new Long(msgId));
				Map<String, Object> msg = template.queryForMap("select * from crm_msg where mid = :mid ", args);
				root.put("sjr", msg.get("fjr"));
				root.put("bt", "回复:" + msg.get("bt"));
				root.put("nr", "上次您提到:" + msg.get("nr"));
			} else {
				root.put("sjr", "");
				root.put("bt", "");
				root.put("nr", "");
			}
		} catch (Exception e) {
			logger.error("initParam_xyj(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_xyj(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_xyj(HttpServletRequest) - end");
		}
		return root;
	}

	/**
	 * 初始化收邮件
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_syj(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_syj(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			String userId = getUserId(request);
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", userId);
			List<Map<String, Object>> queryForList = template
					.queryForList("select * from crm_msg where sjr = :zh and yxbz_s = '1' order by fssj desc", args);
			for (Map<String, Object> map : queryForList) {
				map.put("fssj", map.get("fssj").toString().length() > 19 ? map.get("fssj").toString().substring(0, 19)
						: map.get("fssj").toString());
				map.put("nr", map.get("nr").toString().length() > 30 ? map.get("nr").toString().substring(0, 30) + "..."
						: map.get("nr").toString());
				map.put("sfck", CodeUtils.getValue("SFCK", map.get("sfck").toString()));
			}
			root.put("list", queryForList);
		} catch (Exception e) {
			logger.error("initParam_syj(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_syj(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_syj(HttpServletRequest) - end");
		}
		return root;
	}

	/**
	 * 初始化发件箱
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_fjx(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_fjx(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			String userId = getUserId(request);
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", userId);
			List<Map<String, Object>> queryForList = template
					.queryForList("select * from crm_msg where fjr = :zh and yxbz_f = '1' order by fssj desc", args);
			for (Map<String, Object> map : queryForList) {
				map.put("fssj", map.get("fssj").toString().length() > 19 ? map.get("fssj").toString().substring(0, 19)
						: map.get("fssj").toString());
				map.put("nr", map.get("nr").toString().length() > 30 ? map.get("nr").toString().substring(0, 30) + "..."
						: map.get("nr").toString());
			}
			root.put("list", queryForList);

		} catch (Exception e) {
			logger.error("initParam_fjx(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_fjx(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_fjx(HttpServletRequest) - end");
		}
		return root;
	}

	/**
	 * 初始化提货申请
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_thsq(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_thsq(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			Map<String, Object> user = initMe(request);
			root.put("user", user);
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			List<Map<String, Object>> queryForList = template.queryForList("select * from crm_cp where yxbz = '1'",
					args);
			root.put("list", queryForList);
		} catch (Exception e) {
			logger.error("initParam_thsq(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_thsq(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_thsq(HttpServletRequest) - end");
		}
		return root;
	}

	/**
	 * 初始化申请记录
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_sqjl(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_sqjl(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			// Map<String, Object> user = initMe(request);
			// root.put("user", user);
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", getUserId(request));
			List<Map<String, Object>> queryForList = template
					.queryForList("select * from crm_user_cp where zh = :zh and yxbz = '1'", args);
			for (Map<String, Object> map : queryForList) {
				map.put("zt", CodeUtils.getValue("FHZT", map.get("zt").toString()));
				map.put("thsj", sjFormate19(map.get("thsj") + ""));
				map.put("fhsj", sjFormate19(map.get("fhsj") + ""));
			}
			root.put("list", queryForList);
		} catch (Exception e) {
			logger.error("initParam_sqjl(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_sqjl(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_sqjl(HttpServletRequest) - end");
		}
		return root;
	}

	/**
	 * 初始化申请记录 管理员用
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> initParam_thgl(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_thgl(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			String query = "";
			String parameter = request.getParameter("zt");
			if (parameter != null && !"".equals(parameter)) {
				query += "and zt = '" + parameter + "'";
			}
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			List<Map<String, Object>> queryForList = template.queryForList(
					"select * from crm_user_cp a,crm_user b where a.zh = b.zh and a.yxbz = '1' " + query, args);
			for (Map<String, Object> map : queryForList) {
				map.put("zt", CodeUtils.getValue("FHZT", map.get("zt").toString()));
				map.put("thsj", sjFormate19(map.get("thsj") + ""));
				map.put("fhsj", sjFormate19(map.get("fhsj") + ""));
			}
			root.put("list", queryForList);
		} catch (Exception e) {
			logger.error("initParam_thgl(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_thgl(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_thgl(HttpServletRequest) - end");
		}
		return root;
	}

	public Map<String, Object> initParam_ffbdb(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_ffbdb(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {

			Map<String, Object> user = initMe(request);

			root.put("user", user);

		} catch (Exception e) {
			logger.error("initParam_ffbdb(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_ffbdb(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_ffbdb(HttpServletRequest) - end");
		}
		return root;
	}

	private Map<String, Object> initMe(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initMe(HttpServletRequest) - start");
		}

		// 获取 当前用户帐号
		String userId = getUserId(request);

		// 获取数据库 ， crm_user
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("zh", userId);
		Map<String, Object> user = template.queryForMap("select * from crm_user where zh = :zh", args);

		if (logger.isDebugEnabled()) {
			logger.debug("initMe(HttpServletRequest) - end");
		}
		return user;
	}

	private Map<String, Object> getUserByZh(String zh) {
		if (logger.isDebugEnabled()) {
			logger.debug("getUserByZh(String) - start");
		}

		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("zh", zh);
		Map<String, Object> user = template.queryForMap("select * from crm_user where zh = :zh", args);

		if (logger.isDebugEnabled()) {
			logger.debug("getUserByZh(String) - end");
		}
		return user;
	}

	public Map<String, Object> initParam_czmm(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_czmm(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_czmm(HttpServletRequest) - end");
		}
		return root;
	}

	public Map<String, Object> initParam_cpgl(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_cpgl(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			List<Map<String, Object>> queryForList = template
					.queryForList("select * from crm_cp where yxbz = '1' order by cjsj desc", args);
			for (Map<String, Object> map : queryForList) {
				String string = map.get("cjsj").toString();
				map.put("cjsj", sjFormate19(string));
			}
			root.put("list", queryForList);
		} catch (Exception e) {
			logger.error("initParam_cpgl(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_cpgl(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_cpgl(HttpServletRequest) - end");
		}
		return root;
	}

	private String sjFormate19(String string) {
		return string.length() > 19 ? string.substring(0, 19) : string;
	}

	public Map<String, Object> initParam_gggl(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_gggl(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			List<Map<String, Object>> queryForList = template
					.queryForList("select * from crm_gg where yxbz = '1' order by sj desc", args);
			for (Map<String, Object> map : queryForList) {
				map.put("sj", map.get("sj").toString().length() > 19 ? map.get("sj").toString().substring(0, 19)
						: map.get("sj").toString());
			}
			root.put("list", queryForList);
		} catch (Exception e) {
			logger.error("initParam_gggl(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_gggl(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_gggl(HttpServletRequest) - end");
		}
		return root;
	}

	public Map<String, Object> initParam_backUpDB(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_backUpDB(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			List<String> fileList = new PGBackUpUtils().fileList();
			root.put("list", fileList);
		} catch (Exception e) {
			logger.error("initParam_backUpDB(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_backUpDB(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_backUpDB(HttpServletRequest) - end");
		}
		return root;
	}

	public Map<String, Object> initParam_cpgl_add(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_cpgl_add(HttpServletRequest) - start");
		}
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("cpid", "");
		root.put("cpmc", "");
		root.put("jg", "");
		root.put("kc", "");
		root.put("ms", " <p>输入产品描述</p>");
		String cpid = request.getParameter("cpid");
		if (cpid != null && !cpid.equals("")) {
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("cpid", new Long(cpid));
			List<Map<String, Object>> queryForList = template.queryForList("select * from crm_cp where cpid = :cpid",
					args);
			if (queryForList.size() > 0) {
				Map<String, Object> obj = queryForList.get(0);
				root.put("cpmc", obj.get("cpmc"));
				root.put("cpid", obj.get("cpid"));
				root.put("jg", obj.get("jg"));
				root.put("kc", obj.get("kc"));
				root.put("ms", obj.get("ms"));
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_cpgl_add(HttpServletRequest) - end");
		}
		return root;
	}

	public Map<String, Object> initParam_gggl_add(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_gggl_add(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_gggl_add(HttpServletRequest) - end");
		}
		return root;
	}

	public Map<String, Object> initParam_cxtj(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_cxtj(HttpServletRequest) - start");
		}

		String lx = request.getParameter("lx");
		String zlx = request.getParameter("zlx");
		String xm = request.getParameter("xm");
		String dx = request.getParameter("dx");
		String sjq = request.getParameter("sjq");
		String sjz = request.getParameter("sjz");
		String user = request.getParameter("user");
		Map<String, Object> root = new HashMap<String, Object>();
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		HashMap<String, Object> args = new HashMap<String, Object>();
		String queryStr = "";
		root.put("lx", "");
		root.put("zlx", "");
		root.put("dx", "");
		root.put("xm", "");
		root.put("sjq", "");
		root.put("sjz", "");
		root.put("display", "");
		if (lx != null && !lx.equals("")) {
			args.put("lx", lx);
			queryStr += " and fwm = :lx";
			root.put("lx", lx);
		}
		if (zlx != null && !zlx.equals("")) {
			args.put("zlx", formatZlx(zlx));
			queryStr += " and czlx = :zlx";
			root.put("zlx", zlx);
		}
		if (xm != null && !xm.equals("")) {
			args.put("xm", xm);
			queryStr += " and yxxm = :xm";
			root.put("xm", xm);
		}
		if (dx != null && !dx.equals("")) {
			args.put("dx", dx);
			queryStr += " and yxdx = :dx";
			root.put("dx", dx);
		}
		if (user != null) {
			args.put("dx", getUserId(request));
			queryStr += " and yxdx = :dx";
			root.put("display", "none");
		}
		if (sjq != null && !sjq.equals("")) {
			args.put("sjq", sjq);
			queryStr += " and sxsj >= :sjq";
			root.put("sjq", sjq);
		}
		if (sjz != null && !sjz.equals("")) {
			args.put("sjz", sjz);
			queryStr += " and sxsj <= :sjz";
			root.put("sjz", sjz);
		}
		String sql = "select a.lid,a.fwm,a.ffm,b.*,a.kssj,a.jssj,COALESCE(b.n1,'-') as n1,COALESCE(b.n2,'-') as n2,COALESCE(b.n3,'-') as n3,COALESCE(b.n8,'正常') as n8 from crm_log_mx b, crm_log a where a.lid = b.lid "
				+ queryStr + " order by sxsj desc limit 3000";
		List<Map<String, Object>> queryForList = template.queryForList(sql, args);

		root.put("list", queryForList);

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_cxtj(HttpServletRequest) - end");
		}
		return root;

	}

	public Map<String, Object> initParam_bdbmx(HttpServletRequest request) {
		return initParam_cxtj(request);
	}

	// public Map<String, Object> initParam_mx(HttpServletRequest request) {
	// return initParam_cxtj(request);
	// }

	private Object formatZlx(String zlx) {
		if (logger.isDebugEnabled()) {
			logger.debug("formatZlx(String) - start");
		}

		if (zlx.equals("1")) {
			if (logger.isDebugEnabled()) {
				logger.debug("formatZlx(String) - end");
			}
			return "对碰";
		}
		if (zlx.equals("2")) {
			if (logger.isDebugEnabled()) {
				logger.debug("formatZlx(String) - end");
			}
			return "管理津贴";
		}
		if (zlx.equals("3")) {
			if (logger.isDebugEnabled()) {
				logger.debug("formatZlx(String) - end");
			}
			return "消费分红";
		}
		if (zlx.equals("4")) {
			if (logger.isDebugEnabled()) {
				logger.debug("formatZlx(String) - end");
			}
			return "旬奖发放";
		}
		if (zlx.equals("5")) {
			if (logger.isDebugEnabled()) {
				logger.debug("formatZlx(String) - end");
			}
			return "级差";
		}

		if (logger.isDebugEnabled()) {
			logger.debug("formatZlx(String) - end");
		}
		return "";
	}

	public Map<String, Object> initParam_zdfwzx(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_zdfwzx(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_zdfwzx(HttpServletRequest) - end");
		}
		return root;
	}

	public Map<String, Object> initParam_tree(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_tree(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_tree(HttpServletRequest) - end");
		}
		return root;
	}

	public Map<String, Object> initParam_ejmmyz(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_ejmmyz(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		root.put("user", initMe(request));

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_ejmmyz(HttpServletRequest) - end");
		}
		return root;
	}

	public Map<String, Object> initParam_yhjy(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_yhjy(HttpServletRequest) - start"); //$NON-NLS-1$
		}
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String, Object> args = new HashMap<String, Object>();
		Map<String, Object> root = new HashMap<String, Object>();
		List<Map<String, Object>> queryForList = template.queryForList("select * from crm_user where yxzt = '2'", args);
		root.put("list", queryForList);
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_yhjy(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return root;
	}

	public Map<String, Object> initParam_help(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_help(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_help(HttpServletRequest) - end");
		}
		return root;
	}

	public Map<String, Object> initParam_jjbtx(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_jjbtx(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", getUserId(request));
			List<Map<String, Object>> queryForList = template
					.queryForList("select * from crm_user_tx where zh = :zh order by sqsj desc", args);
			for (Map<String, Object> map : queryForList) {
				map.put("sqsj", sjFormate19(map.get("sqsj").toString()));
				map.put("ffsj", sjFormate19(map.get("ffsj").toString()));
				map.put("zt", CodeUtils.getValue("JJBTXZT", map.get("zt").toString()));
			}
			Map<String, Object> map = template.queryForMap(
					"select COALESCE(sum(sl),0) as ysq, COALESCE(sum(case zt when '1' then sl else 0 end),0) as ycg from crm_user_tx where zh = :zh",
					args);
			root.put("list", queryForList);
			root.put("user", initMe(request));
			root.put("map", map);
		} catch (Exception e) {
			logger.error("initParam_jjbtx(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("initParam_jjbtx(HttpServletRequest) - end");
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_jjbtx(HttpServletRequest) - end");
		}
		return root;
	}

	public Map<String, Object> initParam_gggl_read(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_gggl_read(HttpServletRequest) - start");
		}

		String ggid = request.getParameter("ggid");
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String, Object> root = new HashMap<String, Object>();
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("ggid", new Long(ggid));
		List<Map<String, Object>> queryForList = template.queryForList("select * from crm_gg where ggid = :ggid", args);
		if (queryForList.size() > 0) {
			root.put("bt", queryForList.get(0).get("bt"));
			root.put("nr", queryForList.get(0).get("nr"));
			root.put("sj", sjFormate19(queryForList.get(0).get("sj").toString()));
		} else {
			root.put("bt", "获取公告失败");
			root.put("nr", "");
			root.put("sj", "");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_gggl_read(HttpServletRequest) - end");
		}
		return root;
	}

	public Map<String, Object> initParam_msg_read(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_msg_read(HttpServletRequest) - start");
		}

		String msgId = request.getParameter("msgId");
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String, Object> root = new HashMap<String, Object>();
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("msgId", new Long(msgId));
		List<Map<String, Object>> queryForList = template.queryForList("select * from crm_msg where mid = :msgId",
				args);
		if (queryForList.size() > 0) {
			root.put("bt", queryForList.get(0).get("bt"));
			root.put("nr", queryForList.get(0).get("nr"));
			root.put("fssj", sjFormate19(queryForList.get(0).get("fssj").toString()));
			root.put("fjr", queryForList.get(0).get("fjr"));
		} else {
			root.put("bt", "获取失败");
			root.put("nr", "");
			root.put("fssj", "");
			root.put("fjr", "");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_msg_read(HttpServletRequest) - end");
		}
		return root;
	}

	public Map<String, Object> initParam_jjjs(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initParam_jjjs(HttpServletRequest) - start");
		}

		Map<String, Object> root = new HashMap<String, Object>();
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String, Object> args = new HashMap<String, Object>();
		List<Map<String, Object>> list = template.queryForList(
				"select bz from crm_log where zt= '1' and fwm = 'cn.com.crc.base.service.QuartzJobXj$1' order by bz  desc limit 1",
				args);
		String sj = null;
		String now = LogUtils.getNowTime();
		if (list.size() == 0) {
			sj = "0";
		} else {
			sj = (String) list.get(0).get("bz");
		}
		args.put("sj", sj);
		args.put("now", now);
		long je = template.queryForLong(
				"select sum(case lx when '1' then 7000 when '2' then 3500 end)*0.03 from crm_user where cjsj > :sj and cjsj <= :now",
				args);
		long zfs = template.queryForLong("select sum(fhjf) from crm_user where cast(jb as int) > 5", args);
		list = template.queryForList("select zh as 用户,fhjf as 分红积分 from crm_user where cast(jb as int) > 5 ", args);
		root.put("fhmsg", "从" + sj + "到" + now + "<br/>总业绩为:" + je + ",分红人数:" + list.size() + ",总份数:" + zfs
				+ "<br/>分红明细:<br/>" + list);

		// log query
		args = new HashMap<String, Object>();
		List<Map<String, Object>> queryForList = template.queryForList(
				"select a.lid,a.fwm,a.ffm,b.*,a.kssj,a.jssj from crm_log_mx b, crm_log a where a.lid = b.lid and yxxm in ('jjb','yhq') order by sxsj desc limit 200",
				args);
		root.put("list", queryForList);

		if (logger.isDebugEnabled()) {
			logger.debug("initParam_jjjs(HttpServletRequest) - end");
		}
		return root;
	}

	@GET
	@Path("loginS")
	public String loginS(@Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("loginS(HttpServletRequest) - start");
		}

		if (CodeUtils.getValue("GLYZH", "1").equals(getUserId(request))) {
			if (logger.isDebugEnabled()) {
				logger.debug("loginS(HttpServletRequest) - end");
			}
			return ("$('#sMenu').show();$('#glyM').html('管理后台').click(function(){});");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("loginS(HttpServletRequest) - end");
		}
		return "$('#glyM').click(function(){load('help.html');});";
	}

	@GET
	@Path("backUpDB")
	public String backUpDB(@Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("backUpDB(HttpServletRequest) - start");
		}

		try {
			new PGBackUpUtils().backUpPg();
		} catch (Exception e) {
			logger.error("backUpDB(HttpServletRequest)", e);

			if (logger.isDebugEnabled()) {
				logger.debug("backUpDB(HttpServletRequest) - end");
			}
			return "alert('备份失败');load('backUpDB.html')";
		}

		if (logger.isDebugEnabled()) {
			logger.debug("backUpDB(HttpServletRequest) - end");
		}
		return "alert('备份成功');load('backUpDB.html')";
	}

	@GET
	@Path("logout")
	public String logout(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("logout(HttpServletRequest) - start");
		}

		request.getSession().removeAttribute("userId");

		if (logger.isDebugEnabled()) {
			logger.debug("logout(HttpServletRequest) - end");
		}
		return "<script>window.location.href='../../login.html';</script>";
	}

	@GET
	@Path("downloadDbBackFile")
	public String downloadDbBackFile(@QueryParam("file") String file, @Context HttpServletRequest request,
			@Context HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("downloadDbBackFile(String, HttpServletRequest, HttpServletResponse) - start");
		}

		DownloadUtils.download("c:\\back\\" + file, response);

		if (logger.isDebugEnabled()) {
			logger.debug("downloadDbBackFile(String, HttpServletRequest, HttpServletResponse) - end");
		}
		return null;
	}

	@GET
	@Path("sn")
	public String sn(@QueryParam("sn") String sn, @Context HttpServletRequest request,
			@Context HttpServletResponse response) {
		if (sn != null) {
			request.getSession().getServletContext().setAttribute("SN", sn);
		}
		return (String) request.getSession().getServletContext().getAttribute("SN");
	}

	@GET
	@Path("reloadCode")
	public String reloadCode() {
		return CodeUtils.reload();
	}

	public static void main(String[] args) {
		// 两个区（大区和小区），其中小区业绩大于20万的可以升级经理
		String sql = "WITH recursive cte AS\n" + " (SELECT 1 AS DEPTH, a.*\n" + "    FROM crm_user a\n"
				+ "   WHERE zh = :zh\n" + "     and yxzt = '1'\n" + "  UNION ALL\n"
				+ "  SELECT DEPTH + 1 AS DEPTH, b.*\n" + "    FROM crm_user b\n" + "   INNER JOIN cte c\n"
				+ "      ON b.zh = c.jdr\n" + "   where b.yxzt = '1')\n" + "select *\n" + "  from (select cte.*,\n"
				+ "               (select min(zqyj + yqyj)\n" + "                  from crm_user cc\n"
				+ "                 where cc.jdr = cte.zh) as a1,\n" + "               (select max(zqyj + yqyj)\n"
				+ "                  from crm_user cc\n" + "                 where cc.jdr = cte.zh) as a0,\n"
				+ "               (select count(1) from crm_user cc where cc.jdr = cte.zh) as a2\n"
				+ "          from cte) aaa\n" + " where cast(aaa.jb as int) = 1\n" + "   and aaa.a1 > 200000\n"
				+ "   and aaa.a2 = 2";
		System.out.println(sql);
	}
}
