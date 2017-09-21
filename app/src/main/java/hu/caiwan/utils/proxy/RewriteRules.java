package hu.caiwan.utils.proxy;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.entity.UrlEncodedFormEntity;

/**
 * Interface for rewrite and filter HTTP requests and modify them before sending towards the server
 * @author caiwan
 *
 */

public interface RewriteRules {
	/**
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, String[]> getServletRequestParams(HttpServletRequest request);
	
    public UrlEncodedFormEntity getQueryFromMap(Map<String, String[]> query, String encoding) throws Exception;
	
}
