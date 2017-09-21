package hu.caiwan.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Basic date time adater for jaxb
 * http://javarevisited.blogspot.hu/2017/04/jaxb-date-format-example-using-
 * annotation-XMLAdapter.html
 * 
 * @author caiwan
 *
 */

public class DateTimeAdapter extends XmlAdapter<String, Date> {
	private static final DateFormat dateFormat;
	static {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		dateFormat.setTimeZone(tz);
	}

	@Override
	public Date unmarshal(String xml) throws Exception {
		return dateFormat.parse(xml);
	}

	@Override
	public String marshal(Date object) throws Exception {
		return dateFormat.format(object);
	}

}