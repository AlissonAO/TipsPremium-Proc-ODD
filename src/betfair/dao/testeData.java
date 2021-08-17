package betfair.dao;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class testeData {

	private String data;  
	Locale locale = new Locale("pt","BR");  
	GregorianCalendar calendar = new GregorianCalendar();  

	 public void data(String formato){  
		 Date formatada = new Date();
		 long diferenca = 10800000; 
		 Date data = new Date();
		 data.setTime(formatada.getTime() - diferenca);
		 System.out.println(data);
	 } 
	   /** 
	    * Retorna a data formatada 
	    * @return 
	    */  
	   public String getData() {  
	     return data;  
	   }  
	     
	   /** 
	    * Seta a data do log 
	    * @param newDataLog 
	    */  
	   public void setData(String newData) {  
	     this.data = newData;  
	   }  

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testeData d=new testeData();
		d.data("dd/MM/yyyy" + "-" + " " + "h:mm - a");
	}
	
}
