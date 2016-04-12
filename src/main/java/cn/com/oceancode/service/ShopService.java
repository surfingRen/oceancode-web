package cn.com.oceancode.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.sun.jersey.spi.resource.Singleton;

public class ShopService extends OCService {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ShopService.class);

	static final String SERVICE_NAME = "shop";

	public Map<String, Object> init_dashboard(HttpServletRequest request) {

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			Map<String, Object> user = initMe(request);
			root.put("user", user);
			// root.put("nav", nav);
		} catch (Exception e) {
			return root;
		}

		return root;
	}

	public Map<String, Object> init_rolemgr(HttpServletRequest request) {

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			Map<String, Object> user = initMe(request);
			root.put("user", user);
			// root.put("nav", nav);
		} catch (Exception e) {
			return root;
		}

		return root;
	}

	public Map<String, Object> init_usermgr(HttpServletRequest request) {

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			Map<String, Object> user = initMe(request);
			root.put("user", user);
			// root.put("nav", nav);
		} catch (Exception e) {
			return root;
		}

		return root;
	}

	public Map<String, Object> init_stuffmgr(HttpServletRequest request) {

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			Map<String, Object> user = initMe(request);
			root.put("user", user);
			// root.put("nav", nav);
		} catch (Exception e) {
			return root;
		}

		return root;
	}

	public Map<String, Object> init_user(HttpServletRequest request) {

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			Map<String, Object> user = initMe(request);
			root.put("user", user);
			// root.put("nav", nav);
		} catch (Exception e) {
			return root;
		}

		return root;
	}
}
