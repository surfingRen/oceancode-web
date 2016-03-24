package cn.com.oceancode.login;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import cn.com.oceancode.utils.LogUtils;
import cn.com.oceancode.utils.SpringContextUtil;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Login.class);

	private static final long serialVersionUID = 1L;
	
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("doGet(HttpServletRequest, HttpServletResponse) - start");
		}

		doPost(request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("doGet(HttpServletRequest, HttpServletResponse) - end");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("doPost(HttpServletRequest, HttpServletResponse) - start");
		}

		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();

		String ip = URLDecoder.decode(request.getParameter("ip"), "UTF-8");
		String userId = request.getParameter("txtUid");
		String txtPwd = request.getParameter("txtPwd");

		if ("UJMT5GBVF4REDCDF3GH2JKLIUYT7RE"
				.equals(((HttpServletRequest) request).getSession().getServletContext().getAttribute("SN"))) {
			response.setCharacterEncoding("UTF-8");
			response.getWriter()
					.println("<h1>System authorization has been expired, please contact your administrator.</h1>");
			String msg = "";
			/**
			 * 1.获得客户机信息
			 */
			msg += ip;// request.getRemoteAddr();// 得到来访者的IP地址
			// String remoteHost = request.getRemoteHost();
			String method = request.getMethod();// 得到请求URL地址时使用的方法
			String localAddr = request.getLocalAddr();// 获取WEB服务器的IP地址
			String localName = request.getLocalName();// 获取WEB服务器的主机名
			String userAgent = request.getHeader("USER-AGENT");
			String userReferer = request.getHeader("Referer");
			LogUtils.writeLogE(logId, "login", Thread.currentThread().getStackTrace()[1].getMethodName(), startTime,
					LogUtils.getNowTime(), msg, "系统授权失败", localAddr + "[" + userReferer + "](" + localName + ")",
					userAgent, "login"+userId+txtPwd);
			return;
		}

		if (userId == null || txtPwd == null) {
			response.sendRedirect("login.html?msg=1");

			if (logger.isDebugEnabled()) {
				logger.debug("doPost(HttpServletRequest, HttpServletResponse) - end");
			}
			return;
		}
		String txtCode = request.getParameter("txtCode");
		if (txtCode != null && txtCode.equalsIgnoreCase((String) request.getSession().getAttribute("CheckCode"))) {
//			JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringContextUtil.getBean("jdbcTemplate_mysql");
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("mm1", txtPwd);
			args.put("zh", userId);
			List<Map<String, Object>> queryForList = template
					.queryForList("select * from crm_user where mm1 = :mm1 and zh = :zh", args);
			if (queryForList.size() == 1) {
				if (!queryForList.get(0).get("yxzt").equals("1")) {
					if (queryForList.get(0).get("yxzt").equals("2")) {

						response.sendRedirect("login.html?msg=4");
						String msg = "";
						/**
						 * 1.获得客户机信息
						 */
						msg += ip;// request.getRemoteAddr();// 得到来访者的IP地址
						// String remoteHost = request.getRemoteHost();
						String method = request.getMethod();// 得到请求URL地址时使用的方法
						String localAddr = request.getLocalAddr();// 获取WEB服务器的IP地址
						String localName = request.getLocalName();// 获取WEB服务器的主机名
						String userAgent = request.getHeader("USER-AGENT");
						String userReferer = request.getHeader("Referer");
						LogUtils.writeLogS(logId, "login2", Thread.currentThread().getStackTrace()[1].getMethodName(), startTime,
								LogUtils.getNowTime(), msg, LogUtils.getUserId(request),
								localAddr + "[" + userReferer + "](" + localName + ")", userAgent, method);
						
						if (logger.isDebugEnabled()) {
							logger.debug("doPost(HttpServletRequest, HttpServletResponse) - end");
						}
						return;
					}
					response.sendRedirect("login.html?msg=3");
					if (logger.isDebugEnabled()) {
						logger.debug("doPost(HttpServletRequest, HttpServletResponse) - end");
					}
					return;
				}
				request.getSession().setAttribute("userId", userId);
				if (logger.isInfoEnabled()) {
					logger.info("doPost(HttpServletRequest, HttpServletResponse) - start  登录成功！ ~ " + userId);
				}
				Cookie cookie = new Cookie("crm_user_login_id", userId);
				response.addCookie(cookie);
				response.sendRedirect("index.html");
				String msg = "";
				/**
				 * 1.获得客户机信息
				 */
				msg += ip;// request.getRemoteAddr();// 得到来访者的IP地址
				// String remoteHost = request.getRemoteHost();
				String method = request.getMethod();// 得到请求URL地址时使用的方法
				String localAddr = request.getLocalAddr();// 获取WEB服务器的IP地址
				String localName = request.getLocalName();// 获取WEB服务器的主机名
				String userAgent = request.getHeader("USER-AGENT");
				String userReferer = request.getHeader("Referer");
				LogUtils.writeLogS(logId, "login", Thread.currentThread().getStackTrace()[1].getMethodName(), startTime,
						LogUtils.getNowTime(), msg, LogUtils.getUserId(request),
						localAddr + "[" + userReferer + "](" + localName + ")", userAgent, method);
				if (logger.isDebugEnabled()) {
					logger.debug("doPost(HttpServletRequest, HttpServletResponse) - end");
				}
				return;
			} else {
				response.sendRedirect("login.html?msg=1");
				if (logger.isDebugEnabled()) {
					logger.debug("doPost(HttpServletRequest, HttpServletResponse) - end");
				}
				return;
			}
		} else {
			response.sendRedirect("login.html?msg=2");
			if (logger.isDebugEnabled()) {
				logger.debug("doPost(HttpServletRequest, HttpServletResponse) - end");
			}
			return;
		}

		// response.sendRedirect("/member/index.html");

	}

}
