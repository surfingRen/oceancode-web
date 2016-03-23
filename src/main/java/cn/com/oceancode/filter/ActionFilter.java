package cn.com.oceancode.filter;

import org.apache.log4j.Logger;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpRequest;

/**
 * Servlet Filter implementation class ActionFilter
 */
public class ActionFilter implements Filter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ActionFilter.class);

	/**
	 * Default constructor.
	 */
	public ActionFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		if (((HttpServletRequest) request).getSession().getAttribute("userId") == null) {
			if (((HttpServletRequest) request).getRequestURI().indexOf("getCheckCodeImage") > 0
					|| ((HttpServletRequest) request).getRequestURI().indexOf("logout") > 0
					|| ((HttpServletRequest) request).getRequestURI().indexOf("sn") > 0) {
			} else {
				response.getWriter().print("{msg:0}");
				if (logger.isInfoEnabled()) {
					logger.info("doFilter(ServletRequest, ServletResponse, FilterChain) - session 超时 !!!!!!!!!!!~~~~~~~~~~~~~~~~~~~~~");
				}
				return;
			}
		}

		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
