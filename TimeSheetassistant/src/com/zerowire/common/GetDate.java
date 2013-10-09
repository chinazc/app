package com.zerowire.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetDate {

	public static final String GenerateDate() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String time = sf.format(date);
		return time;
	}

	public static String GenerateDate(long time) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date(time);
		return sf.format(date);
	}

	public static String getSelectsDate(String strDate) {
		String temp = "";
		if (strDate.length() >= 14) {
			temp = strDate.substring(0, 4);
			temp += "年";
			temp += strDate.substring(4, 6);
			temp += "月";
			temp += strDate.substring(6, 8);
			temp += "日";
			temp += strDate.substring(8, 10);
			temp += ":";
			temp += strDate.substring(10, 12);
			temp += ":";
			temp += strDate.substring(12, 14);
		}
		return temp;
	}
	
	public static String getSelectsDate1(String strDate) {
		String temp = "";
		if (strDate.length() >= 14) {
			temp = strDate.substring(0, 4);
			temp += "-";
			temp += strDate.substring(4, 6);
			temp += "-";
			temp += strDate.substring(6, 8);
			temp += " ";
			temp += strDate.substring(8, 10);
			temp += ":";
			temp += strDate.substring(10, 12);
			temp += ":";
			temp += strDate.substring(12, 14);
		}
		return temp;
	}

	public static String getSelects12Date(String strDate) {
		String temp = "";
		if (strDate.length() >= 14) {
			temp = strDate.substring(0, 4);
			temp += "年";
			temp += strDate.substring(4, 6);
			temp += "月";
			temp += strDate.substring(6, 8);
			temp += "日";
			temp += strDate.substring(8, 10);
			temp += ":";
			temp += strDate.substring(10, 12);
		}
		return temp;
	}

	public static String getSelectsTime(String strDate) {
		String temp = "";
		if (strDate.length() >= 14) {
			temp += strDate.substring(8, 10);
			temp += ":";
			temp += strDate.substring(10, 12);
			temp += ":";
			temp += strDate.substring(12, 14);
		}
		return temp;
	}

	public static String getSelects4Time(String strDate) {
		String temp = "";
		if (strDate.length() >= 14) {
			temp += strDate.substring(8, 10);
			temp += ":";
			temp += strDate.substring(10, 12);
		}
		return temp;
	}

	public static String getSelectsDate() {
		String systime = GetDate.GenerateDate();
		String temp = "";
		if (systime.length() >= 14) {
			temp = systime.substring(0, 4);
			temp += "年";
			temp += systime.substring(4, 6);
			temp += "月";
			temp += systime.substring(6, 8);
			temp += "日";
			temp += systime.substring(8, 10);
			temp += ":";
			temp += systime.substring(10, 12);
			temp += ":";
			temp += systime.substring(12, 14);
		}
		return temp;
	}

	public static String getSelectsDateString(String strDate) {
		StringBuilder temp = new StringBuilder();
		if (strDate.length() >= 14) {
			temp.append(strDate.substring(0, 4));
			temp.append("年");
			temp.append(strDate.substring(4, 6));
			temp.append("月");
			temp.append(strDate.substring(6, 8));
			temp.append("日");
		}
		return temp.toString();
	}

	public static String getEditTextDate(int year, int monthOfYear,
			int dayOfMonth) {
		return year
				+ "-"
				+ (monthOfYear < 10 ? "0" + String.valueOf((monthOfYear))
						: (monthOfYear))
				+ "-"
				+ (dayOfMonth < 10 ? "0" + String.valueOf(dayOfMonth)
						: dayOfMonth);
	}

	// dateStr = 2011-12-02
	// return 20111202
	public static String getChar14Date(String dateStr) {
		if (null != dateStr && !"".equals(dateStr)) {
			dateStr = dateStr.replaceAll("-", "");
			return dateStr;
		} else {
			return "";
		}

	}

	public static String getEditTextDate() {
		String date = GetDate.GenerateDate();
		return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
				+ date.substring(6, 8);

	}
	/**
	 *  dateStr = 20111202120945
	 * @return  2011-12-02
	 */
	public static String getEditTextDate(String dateStr) {
		return dateStr.substring(0, 4) + "-" + dateStr.substring(4, 6) + "-"
				+ dateStr.substring(6, 8);

	}
	/**
	 * 将现在时间转换为指定格式
	 * @param type 如yyyyMMddHHmmss
	 */
	public static String nowDate2String(String pattern){
		String result = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = new Date();
		result = sdf.format(date);
		return result;
	}
	/**
	 * 将现在时间转换为指定格式
	 * @param type 如yyyyMMddHHmmss
	 */
	public static String date2String(Date date, String pattern){
		String result = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		result = sdf.format(date);
		return result;
	}
	/**
	 *取得几天后的日期 
	 */
	public static String getSpecvDay(int days){
		Calendar oldDay = Calendar.getInstance();
		oldDay.add(Calendar.DAY_OF_MONTH, days);
		String prevDay = "" + oldDay.get(Calendar.YEAR) +
			((oldDay.get(Calendar.MONTH)+1)<10?"0"+(oldDay.get(Calendar.MONTH)+1):""+(oldDay.get(Calendar.MONTH)+1))
			+(oldDay.get(Calendar.DAY_OF_MONTH)<10?"0"+oldDay.get(Calendar.DAY_OF_MONTH):""+oldDay.get(Calendar.DAY_OF_MONTH))
			+(oldDay.get(Calendar.HOUR_OF_DAY)<10?"0"+oldDay.get(Calendar.HOUR_OF_DAY):""+oldDay.get(Calendar.HOUR_OF_DAY))
			+(oldDay.get(Calendar.MINUTE)<10?"0"+oldDay.get(Calendar.MINUTE):""+oldDay.get(Calendar.MINUTE))
			+(oldDay.get(Calendar.SECOND)<10?"0"+oldDay.get(Calendar.SECOND):""+oldDay.get(Calendar.SECOND));
		return prevDay;		
	}

}
