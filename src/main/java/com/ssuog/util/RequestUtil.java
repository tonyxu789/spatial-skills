package com.ssuog.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class RequestUtil {

	public static String getStringDef(HttpServletRequest request, String key, String def) throws Exception {
		String sVal = request.getParameter(key);
		if (StringUtils.isEmpty(sVal)) {
			return def;
		} else {
			return sVal;
		}
	}

	public static List<Long> getLongList(HttpServletRequest request, String key, String split) throws Exception {
		String sVal = request.getParameter(key);
		if (StringUtils.isEmpty(sVal)) {
			return null;
		}
		return getLongList(sVal, split);
	}

	public static List<Long> getLongList(String sVal, String split) {
		List<Long> list = new ArrayList<Long>();
		String[] item = sVal.split(split);
		if (item.length >= 1) {
			for (int i = 0; i < item.length; i++) {
				if (StringUtils.isEmpty(item[i])) {
					continue;
				} else {
					list.add(Long.parseLong(item[i]));
				}
			}
		} else {
			list = null;
		}
		return list;
	}

	public static Long getLong(HttpServletRequest request, String key, Long def) {
		String sVal = request.getParameter(key);
		if (StringUtils.isEmpty(sVal)) {
			return def;
		} else {
			return Long.parseLong(sVal);
		}
	}
	
	public static Double getDouble(HttpServletRequest request, String key, Double def) {
		String sVal = request.getParameter(key);
		if (StringUtils.isEmpty(sVal)) {
			return def;
		} else {
			return Double.parseDouble(sVal);
		}
	}

}
