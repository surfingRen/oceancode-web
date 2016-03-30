package cn.com.oceancode.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.sun.jersey.spi.resource.Singleton;

import cn.com.oceancode.utils.CheckCodeUtils;
import cn.com.oceancode.utils.CodeUtils;

@Singleton
@Component
@Path("/test")
public class ServiceTest {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ServiceTest.class);

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@GET
	@Path("get")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Map<String, Object>> getCalendar(@QueryParam("userId") String userId, @Context HttpServletRequest request) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			Map<String, Object> e = new HashMap<String, Object>();
			e.put("key1", formatter.format(new Date()));
			e.put("key2", formatter.format(new Date()));
			list.add(e);
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("uid", 1);
			return template.queryForList("select * from crm_user", args);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("getUserInfo")
	@Produces({ MediaType.APPLICATION_JSON })
	public String getUserInfo(@Context HttpServletRequest request) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {

			String userId = (String) request.getSession().getAttribute("userId");
			if (userId == null)
				return "session is timeout";
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", userId);
			Map<String, Object> user = template.queryForMap("select * from crm_user where zh = :zh", args);

			String ss = "<ul>                             " + "	<li>用户名：" + user.get("zh") + "</li>          " + "	<li>我的服务中心："
					+ user.get("fwzx") + "</li>    " + "	<li>报单币：" + user.get("bdb") + "</li>           " + "	<li>奖金币：" + user.get("jjb")
					+ "</li>           " + "</ul>                            " + "<div style=\"clear:both\"></div>"
					+ "<ul>                             " + "	<li>优惠券：" + user.get("yhq") + "</li>           " + "	<li>提货币："
					+ user.get("thb") + "</li>           " + "	<li>级别：" + CodeUtils.getValue("JB", (String) user.get("jb"))
					+ "</li>                  "+ "	<li>推荐人数：" + user.get("tjrs")
					+ "</li>                  ";
			if (new Integer((String) user.get("jb")) > 5) {
				ss += "	<li>分红积分：" + user.get("fhjf") + "</li>               ";
			}
			String substring;
			try {
				substring = user.get("cjsj").toString().substring(0, 19);
			} catch (Exception e) {
				substring = "-";
			}
			ss += "</ul>                            " + "<div style=\"clear:both\"></div>" + "<ul>                             "
					+ "	<li>电子邮箱：" + user.get("dzyx") + "</li>             " + "	<li>QQ号：" + user.get("qq") + "</li>                 "
					+ "	<li>注册时间：" + substring + "</li>              " + "	<li>是否服务中心：" + (user.get("sffwzx").equals("1") ? "是" : "否")
					+ "</li>              " + "</ul>                            " + "<div style=\"clear: both\"></div>";
			;

			return "document.write('" + ss + "');";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@GET
	@Path("getLastGg")
	public String getLastGg(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		try {
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			List<Map<String, Object>> list = template.queryForList("select * from crm_gg where yxbz = '1' order by sj desc", args);
			Map<String, Object> gg = null;
			if (list.size() > 0) {
				gg = list.get(0);
				return "document.write('<marquee  onclick=\"readGG(" + gg.get("ggid") + ")\"><a onclick=\"readGG(" + gg.get("ggid")
						+ ")\" href=\"" + gg.get("ggid") + "\"></a>" + gg.get("bt") + "</marquee>');";
			}

			return "";

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("rtopMenu")
	public String rtopMenu(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		try {
			String userId = (String) request.getSession().getAttribute("userId");
			if (userId == null)
				return "session is timeout";
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("zh", userId);
			System.out.println("rtopMenu :  " + args);
			Map<String, Object> user = template.queryForMap("select * from crm_user where zh = :zh", args);

			long wckNum = 0l;
			// 未读信息数
			List<Map<String, Object>> list = template.queryForList(
					"select count(1) as wckNum from crm_msg where sjr = :zh and sfck = '0' and yxbz_s = '1'", args);
			if (list.size() > 0) {
				wckNum = (Long) list.get(0).get("wckNum");
			}
			return "document.write('<li class=\"t\">欢迎您: " + user.get("xm") + "(" + user.get("zh") + ")<span>|</span>"
					+ CodeUtils.getValue("HYLX", (String) user.get("lx")) + "<span>|</span>"
					+ CodeUtils.getValue("JB", (String) user.get("jb")) + "<span>|</span><a onclick=\"goSjx();\" href=\"#\">你有(<span style=\"color:#fff;\" id=\"wdxxs\">"
					+ wckNum + "</span>)条未读消息</a><span>|</span><a href=\"#\" onclick=\"logout()\">安全退出</a></li>');";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("hasLogin")
	public String hasLogin(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		try {
			return request.getSession().getAttribute("userId") != null ? "1" : "0";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Context
	@GET
	@Path("getCheckCodeImage")
	public String getCheckCodeImage(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("getCheckCodeImage(HttpServletRequest request, HttpServletResponse response) - start");
		}

		// 频繁登录
		OutputStream ops = null;
		try {
			// 频繁登录
			// 设置页面不缓存
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);

			String checkCode = CheckCodeUtils.getCheckCode();
			BufferedImage image = CheckCodeUtils.getCheckCodeImage(checkCode);

			// 将认证码存入SESSION
			request.getSession().setAttribute("CheckCode", checkCode);

			// 输出图象到页面
			ops = response.getOutputStream();
			javax.imageio.ImageIO.write(image, "jpeg", ops);

			return null;
		} catch (IOException e) {
			logger.error("getCheckCodeImage(HttpServletRequest, HttpServletResponse) - ERROR", e);
		} finally {
			if (null != ops) {
				try {
					ops.close();
				} catch (IOException e) {
					logger.error("getCheckCodeImage(HttpServletRequest, HttpServletResponse) - ERROR", e);
				}// try
			}// if
		}// finally
		return null;
	}

	public static void main(String[] args) {
		ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
		JdbcTemplate jdbcTemplate_oralce = (JdbcTemplate) ctx.getBean("jdbcTemplate_oralce");
		// JdbcTemplate jdbcTemplate_sqlserver = (JdbcTemplate)
		// ctx.getBean("jdbcTemplate_sqlserver");

		List<Map<String, Object>> list = jdbcTemplate_oralce
				.queryForList("select * from all_tables where owner = 'WLF' order by table_name ");
		for (Map<String, Object> map : list) {
			System.out.println(map.get("table_name"));
			List<Map<String, Object>> list2 = jdbcTemplate_oralce.queryForList("select * from " + map.get("table_name"));
			System.out.println(list2.size());
		}
	}
}
