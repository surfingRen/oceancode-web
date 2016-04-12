package cn.com.oceancode.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Component
@Path("/user")
public class UserService extends OCService {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UserService.class);

	static final String SERVICE_NAME = "user";

	@GET
	@Path("/list/{shopid}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	public Map<String, Object> list_shopid(@PathParam("shopid") String shopid) {
		if (logger.isDebugEnabled()) {
			logger.debug("list_shopid(String) - start"); //$NON-NLS-1$
		}

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("shopid", shopid);
			List<Map<String, Object>> queryForList = template
					.queryForList("select * from t_stuff where status = '1' and shop_id = :shopid", args);
			root.put("list", queryForList);
		} catch (Exception e) {
			logger.error("list_shopid(String)", e); //$NON-NLS-1$

			if (logger.isDebugEnabled()) {
				logger.debug("list_shopid(String) - end"); //$NON-NLS-1$
			}
			return root;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("list_shopid(String) - end"); //$NON-NLS-1$
		}
		return root;

	}

}
