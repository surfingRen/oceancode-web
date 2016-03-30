package cn.com.oceancode.service;

import java.io.File;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
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
@Path("/oc")
public class OCService {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(OCService.class);

	static final String SERVICE_NAME = "oc";

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	private JdbcTemplate jdbcTemplate;
	private PlatformTransactionManager transactionManager;

	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
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
		root.put("user", "sd");
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

	@SuppressWarnings("unchecked")
	@GET
	@Path("loadHtml")
	public String loadHtml(@QueryParam("path") String path, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("loadHtml(String, HttpServletRequest) - start");
		}
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
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

	private String errorHandler(Exception e) {
		String returnString = "get page error，" + e.getMessage();
		return returnString;
	}

	private String getUserId(HttpServletRequest request) {
		String returnString = (String) request.getSession().getAttribute("userId");
		return returnString;
	}

	public static void main(String[] args) {
	}

}