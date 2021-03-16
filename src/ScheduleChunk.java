
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class ScheduleChunk {
	



	  public ArrayList<HashMap<String,String>> getPendingTrades() throws ClassNotFoundException, SQLException {
	       
		  final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
		  final long HOUR = 3600*1000; // in milli-seconds
		  final long HALFHOUR = 1800*1000;
	        Date d1=new Date(new Date().getTime() +5*HOUR+HALFHOUR- 15*ONE_MINUTE_IN_MILLIS);
	       
	        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
	       //SimpleDateFormat dt1 = new SimpleDateFormat("2018-03-06");
	        System.out.println(dt1.format(d1));
	        SimpleDateFormat dt2 = new SimpleDateFormat("HH:mm");
	       // SimpleDateFormat dt2 = new SimpleDateFormat("22:10");
	        System.out.println(dt2.format(d1));
	        return(compareData(dt1.format(d1),dt2.format(d1)));
	     
	    }

	   /* static Date toNearestWholeMinute(Date d) {
	        Calendar c = new GregorianCalendar();
	        c.setTime(d);

	        if (c.get(Calendar.SECOND) >= 30)
	            c.add(Calendar.MINUTE, 1);

	        c.set(Calendar.SECOND, 0);

	        return c.getTime();
	    }

	    static Date toNearestWholeHour(Date d) {
	        Calendar c = new GregorianCalendar();
	        c.setTime(d);
	System.out.println(c.get(Calendar.MINUTE));

	        if (c.get(Calendar.MINUTE) >= 30)
	            c.add(Calendar.HOUR,1);

	        c.set(Calendar.MINUTE, 0);
	        c.set(Calendar.SECOND, 0);

	        return c.getTime();
	    }
*/
	    
	    public ArrayList<HashMap<String,String>> compareData(String date,String time) throws ClassNotFoundException, SQLException
	    
	    {
	    	
	    	/*if(spData[1].equalsIgnoreCase("0") || spData[1].equalsIgnoreCase("30"))
	    	{
	    		
	    	}*/
	    	/*else if(Integer.parseInt(spData[1])<30)
	    		
	    	{*/
	    	//	sb=sb.append(spData[0]+":00");
	    		ScheduleDAO scheduledao=new ScheduleDAO();
	    	//	scheduledao.getPendingSchedules(date, sb.toString());
	    		ArrayList<HashMap<String,String>> responseFromSchedules=scheduledao.getPendingTrades(date, time);
	    		 Date d1=new Date();
	  	       
	 	      
	 	      
	    		
	    	/*//}
	    	else
	    	{*/
	    		
	 	     
	    	//}
			return responseFromSchedules;
	    	
	    }
	}


