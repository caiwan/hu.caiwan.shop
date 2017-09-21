package hu.caiwan.utils.proxy;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

/**
 * Passes request though as is
 */

public class BasicRewriteRules implements RewriteRules{
	
	public Map<String, String[]> getServletRequestParams(HttpServletRequest request){
		Map<String, String[]> modifiedQuery = new HashMap<>(request.getParameterMap());
		return modifiedQuery;
	}
	
    public UrlEncodedFormEntity getQueryFromMap(Map<String, String[]> query, String encoding) throws UnsupportedEncodingException {
        List<NameValuePair> nvps = new ArrayList<>();
        
        for(Map.Entry<String, String[]> entry : query.entrySet()) {
            for(String currValue : entry.getValue()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), currValue));
            }
        }
        return new UrlEncodedFormEntity(nvps, encoding);
    }
	
}
