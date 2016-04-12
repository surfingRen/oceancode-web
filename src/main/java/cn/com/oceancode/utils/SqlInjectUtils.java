package cn.com.oceancode.utils;

import org.apache.log4j.Logger;

public class SqlInjectUtils {
	/**
	* Logger for this class
	*/
	private static final Logger logger = Logger.getLogger(SqlInjectUtils.class);

	//需要增加通配，过滤大小写组合  
    public static String cleanSQLInject(String src) {  
        String temp =src;  
        src = src.replaceAll("insert", "forbidI")  
            .replaceAll("select", "forbidS")  
            .replaceAll("update", "forbidU")  
            .replaceAll("delete", "forbidD")  
            .replaceAll("and", "forbidA")  
            .replaceAll("or", "forbidO");  
          
        if(!temp.equals(src)){  
			if (logger.isDebugEnabled()) {
				logger.debug("cleanSQLInject(String) - 输入信息存在SQL攻击！"); //$NON-NLS-1$
				logger.debug("cleanSQLInject(String) - 原始输入信息-->" + temp); //$NON-NLS-1$
				logger.debug("cleanSQLInject(String) - 处理后信息-->" + src); //$NON-NLS-1$
			}  
        }  
        return src;  
    }  
}
