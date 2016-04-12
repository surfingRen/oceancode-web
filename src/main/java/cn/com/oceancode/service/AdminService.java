package cn.com.oceancode.service;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.sun.jersey.spi.resource.Singleton;

import cn.com.oceancode.utils.LogUtils;
import cn.com.oceancode.utils.SqlInjectUtils;

@Singleton
@Component
@Path("/admin")
public class AdminService extends OCService {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AdminService.class);

	static final String SERVICE_NAME = "admin";

	public static Map<String, Object> init_index(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("init_index(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			Map<String, Object> user = initMe(request);
			root.put("user", user);
			// root.put("nav", nav);
		} catch (Exception e) {
			logger.error("init_index(HttpServletRequest)", e); //$NON-NLS-1$

			if (logger.isDebugEnabled()) {
				logger.debug("init_index(HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("init_index(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return root;
	}

	public static Map<String, Object> init_cache(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("init_cache(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			Map<String, Object> user = initMe(request);
			root.put("user", user);
			// root.put("nav", nav);
		} catch (Exception e) {
			logger.error("init_cache(HttpServletRequest)", e); //$NON-NLS-1$

			if (logger.isDebugEnabled()) {
				logger.debug("init_cache(HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("init_cache(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return root;
	}

	public static Map<String, Object> init_code(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("init_code(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			Map<String, Object> user = initMe(request);
			root.put("user", user);
			// root.put("nav", nav);
		} catch (Exception e) {
			logger.error("init_code(HttpServletRequest)", e); //$NON-NLS-1$

			if (logger.isDebugEnabled()) {
				logger.debug("init_code(HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("init_code(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return root;
	}

	public static Map<String, Object> init_menu(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("init_menu(HttpServletRequest) - start"); //$NON-NLS-1$
		}
		Map<String, Object> root = new HashMap<String, Object>();
		try {
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			List<Map<String, Object>> queryForList = template.queryForList("select * from t_menu where status = '1'",
					args);
			// for (Map<String, Object> map : queryForList) {
			// String string = map.get("cjsj").toString();
			// map.put("cjsj", sjFormate19(string));
			// }
			root.put("list", queryForList);
			Map<String, Object> user = initMe(request);
			root.put("user", user);
			// root.put("nav", nav);
		} catch (Exception e) {
			logger.error("init_menu(HttpServletRequest)", e); //$NON-NLS-1$

			if (logger.isDebugEnabled()) {
				logger.debug("init_menu(HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("init_menu(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return root;
	}

	public static Map<String, Object> init_menu_add(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("init_menu_add(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		Map<String, Object> root = new HashMap<String, Object>();
		root.put("id", "");
		root.put("text", "");
		root.put("pid", "");
		root.put("nav", "");
		root.put("note", "");
		root.put("url", "");
		String id = request.getParameter("id");
		if (id != null && !id.equals("")) {
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("id", new Integer(id));
			List<Map<String, Object>> queryForList = template
					.queryForList("select * from t_menu where id = :id and status = '1'", args);
			if (queryForList.size() > 0) {
				Map<String, Object> obj = queryForList.get(0);
				root.put("id", obj.get("id"));
				root.put("text", obj.get("text"));
				if ("1".equals(request.getParameter("d"))) {//非复制
					root.put("id", "");
					root.put("text", obj.get("text")+"_复制");
				}
				root.put("pid", obj.get("pid"));
				root.put("nav", obj.get("nav"));
				root.put("note", obj.get("note"));
				root.put("url", obj.get("url"));
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("init_menu_add(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return root;
	}

	@POST
	@Path("delMenu")
	public String delMenu(@FormParam("id") String id, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("delMenu(String, HttpServletRequest) - start " + id); //$NON-NLS-1$
		}

		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		try {
			Map<String, Object> args = new HashMap<String, Object>();
			String sql = "update t_menu set status = '0' where id in (" + SqlInjectUtils.cleanSQLInject(id) + ")";
			int i = template.update(sql, args);
			LogUtils.writeLogDetail(logId, "删除菜单", "", "", "", "", LogUtils.getNowTime(), args.toString());
			LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));
			String returnString = "alert('删除" + i + "条数据');load('admin.menu.html');";
			if (logger.isDebugEnabled()) {
				logger.debug("delMenu(String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return returnString;
		} catch (Exception e) {
			logger.error("delMenu(String, HttpServletRequest)", e); //$NON-NLS-1$

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("delMenu(String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return "alert('操作失败');cancelLoading();";
		}
	}

	@POST
	@Path("addMenu")
	public String addMenu(@FormParam("menuText") String menuText, @FormParam("menuNav") String menuNav,
			@FormParam("menuNote") String menuNote, @FormParam("menuUrl") String menuUrl,
			@FormParam("menuPid") String menuPid, @Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("addMenu(String, String, String, String, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		try {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("menuText", menuText);
			args.put("menuNav", menuNav);
			args.put("menuNote", menuNote);
			args.put("menuUrl", menuUrl);
			args.put("menuPid", menuPid.replaceAll(",", ""));
			String sql = "insert into t_menu(text,nav,note,url,pid) values (:menuText, :menuNav, :menuNote, :menuUrl, :menuPid)";
			int i = template.update(sql, args);
			if (i == 1) {
				LogUtils.writeLogDetail(logId, "添加菜单", "", "", "", "", LogUtils.getNowTime(), args.toString());
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));
				String returnString = "alert('操作成功');load('admin.menu.html');";

				if (logger.isDebugEnabled()) {
					logger.debug("addMenu(String, String, String, String, HttpServletRequest) - end"); //$NON-NLS-1$
				}
				return returnString;
			} else {
				throw new Exception("添加数据条数为" + i);
			}
		} catch (Exception e) {
			logger.error("addMenu(String, String, String, String, HttpServletRequest)", e); //$NON-NLS-1$

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			if (logger.isDebugEnabled()) {
				logger.debug("addMenu(String, String, String, String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return "alert('操作失败, " + URLEncoder.encode(e.getMessage()) + "');cancelLoading();";
		}
	}

	@POST
	@Path("saveMenu")
	public String addMenu(@FormParam("menuId") String menuId, @FormParam("menuText") String menuText,
			@FormParam("menuNav") String menuNav, @FormParam("menuNote") String menuNote,
			@FormParam("menuUrl") String menuUrl, @FormParam("menuPid") String menuPid,
			@Context HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("addMenu(String, String, String, String, String, String, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String msg = "";
		long logId = LogUtils.getLogId();
		String startTime = LogUtils.getNowTime();
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		try {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("id", new Integer(menuId));
			args.put("text", menuText);
			args.put("nav", menuNav);
			args.put("note", menuNote);
			args.put("url", menuUrl);
			args.put("pid", new Integer(menuPid.replaceAll(",", "")));
			String sql = "update t_menu set text=:text,nav=:nav,note=note,url=:url,pid=:pid where id=:id";
			int i = template.update(sql, args);
			if (i == 1) {
				LogUtils.writeLogDetail(logId, "修改菜单", "", "", "", "", LogUtils.getNowTime(), args.toString());
				LogUtils.writeLogS(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
						startTime, LogUtils.getNowTime(), msg, LogUtils.getUserId(request));
				String returnString = "alert('操作成功');load('admin.menu.html');";

				if (logger.isDebugEnabled()) {
					logger.debug("addMenu(String, String, String, String, String, String, HttpServletRequest) - end"); //$NON-NLS-1$
				}
				return returnString;
			} else {
				throw new Exception("保存数据条数为" + i);
			}
		} catch (Exception e) {
			logger.error("addMenu(String, String, String, String, String, String, HttpServletRequest)", e); //$NON-NLS-1$

			LogUtils.writeLogE(logId, SERVICE_NAME, Thread.currentThread().getStackTrace()[1].getMethodName(),
					startTime, LogUtils.getNowTime(), e.getMessage(), LogUtils.getStackTrace(e),
					LogUtils.getUserId(request));

			String returnString = "alert('操作失败, " + URLEncoder.encode(e.getMessage()) + "');cancelLoading();";
			if (logger.isDebugEnabled()) {
				logger.debug("addMenu(String, String, String, String, String, String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
	}

}
