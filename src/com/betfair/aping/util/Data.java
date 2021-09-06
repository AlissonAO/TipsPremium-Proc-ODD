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
	
	
	public static String capitalizer(String nome) {
		        String words[]=nome.split("\\s");
		        String capitalizeStr="";
		 
		        for(String word:words){
		            // Capitalize first letter
		            String firstLetter=word.substring(0,1);
		            // Get remaining letter
		            String remainingLetters=word.substring(1);
		            capitalizeStr+=firstLetter.toUpperCase()+remainingLetters+" ";
		        }
			return capitalizeStr;
		    }
}
