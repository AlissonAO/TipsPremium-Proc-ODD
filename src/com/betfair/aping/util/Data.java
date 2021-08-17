package com.betfair.aping.util;

import java.util.Calendar;
import java.util.Date;

public class Data {
	
	public static final int hora = 0;
	
	public static Date addHoursToJavaUtilDate(Date date, int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		return calendar.getTime();
	}
	

	public Date horarioVerao(Date date, int hours) {
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    calendar.add(Calendar.HOUR_OF_DAY, hours);
		    return calendar.getTime();
		}
}
