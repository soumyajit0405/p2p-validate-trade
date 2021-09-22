
import java.io.IOException;
import java.security.Timestamp;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

class AgentWorker implements Runnable {
	private int orderId;
	private int meterId;
	private String startTs;
	private String endTs;
	private float energy;

	private static String url = "http://139.59.30.90:3020/data_warehouse/meter/fetchDatasnapshot";
	final long ONE_MINUTE_IN_MILLIS = 60000;// millisecs
	final long HOUR = 3600 * 1000; // in milli-seconds
	final long HALFHOUR = 1800 * 1000;
	
	public AgentWorker(int orderId, int meterId, String startTs, String endTs, float energy) {
		this.orderId = orderId;
		this.meterId = meterId;
		this.startTs = startTs;
		this.endTs = endTs;
		this.energy = energy;
	}

	public void run() {
		float sellerStartReading=0,sellerEndReading=0,buyerStartReading=0,buyerEndReading=0;
		JSONObject inputDetails1 = new JSONObject();
		ScheduleDAO sdao = new ScheduleDAO();
		DBHelper dbhelper= new DBHelper();
		int continueStatus = 1;
		HttpConnectorHelper httpconnectorhelper= new HttpConnectorHelper();
		 System.out.println(Thread.currentThread().getName()+" ----> Inside Agent Worker");
		// processmessage();//call processmessage method that sleeps the thread for 2
		// seconds
		try {
			HashMap<String,Object> order = sdao.getBuyerDeviceId(orderId);
			float unit = (float)order.get("units");
			float price = (float)order.get("price");
		ArrayList<String> dateRanges = getStartAndEndDates();
		JSONObject requestObject = new JSONObject();
		requestObject.put("meterId", this.meterId);
		requestObject.put("timeslot", dateRanges.get(1));
		requestObject.put("queryDate", dateRanges.get(0));
		
			
		
		JSONObject response = httpconnectorhelper.sendRequestToAgent(url, requestObject, 1);
		if( response == null ||  response.has("msg") 
				|| response.has("errormessage") ) {
			sdao.updateOrderStatus(orderId,9,8);
			continueStatus = 0;
		} else {
			JSONObject data = (JSONObject)response.get("dataSanpshot");
			sellerStartReading = (int)data.get("cumulativeEnergy-kWh(I)");
			
		}
		System.out.println("continueStatus1" + continueStatus);
		requestObject = new JSONObject();
		requestObject.put("meterId", this.meterId);
		requestObject.put("timeslot", dateRanges.get(2));
		requestObject.put("queryDate", dateRanges.get(0));
		if (continueStatus != 0) {
		response = httpconnectorhelper.sendRequestToAgent(url, requestObject, 1);
		if( response == null || response.has("msg") 
				|| response.has("errormessage") ) {
			sdao.updateOrderStatus(orderId,9,8);
			continueStatus = 0;
		} else {
			JSONObject data = (JSONObject)response.get("dataSanpshot");
			sellerEndReading = (int)data.get("cumulativeEnergy-kWh(I)");
			
		}
		System.out.println("continueStatus2" + continueStatus);
		requestObject = new JSONObject();
		requestObject.put("meterId", (int)order.get("meterId"));
		requestObject.put("timeslot", dateRanges.get(1));
		requestObject.put("queryDate", dateRanges.get(0));
		if (continueStatus != 0) {
		response = httpconnectorhelper.sendRequestToAgent(url, requestObject, 1);
		if(  response == null ||  response.has("msg") 
				|| response.has("errormessage") ) {
			sdao.updateOrderStatus(orderId,9,8);
		continueStatus =0;
		} else {
			JSONObject data = (JSONObject)response.get("dataSanpshot");
			buyerStartReading = (int)data.get("cumulativeEnergy-kWh(I)");
			
		}
		
		}
		System.out.println("continueStatus3" + continueStatus);
		requestObject = new JSONObject();
		requestObject.put("meterId", (int)order.get("meterId"));
		requestObject.put("timeslot", dateRanges.get(2));
		requestObject.put("queryDate", dateRanges.get(0));
		if (continueStatus != 0) {
		response = httpconnectorhelper.sendRequestToAgent(url, requestObject, 1);
		if( response == null ||  response.has("msg") 
				|| response.has("errormessage") ) {
			sdao.updateOrderStatus(orderId,9,8);
			continueStatus =0;
		} else {
			JSONObject data = (JSONObject)response.get("dataSanpshot");
			buyerEndReading = (int)data.get("cumulativeEnergy-kWh(I)");
			
		}
		
		}
		System.out.println("continueStatus4" + continueStatus);
		if(continueStatus != 0) {
		float p_consumed =  buyerEndReading - buyerStartReading;
		float p_produced =  sellerEndReading - sellerStartReading;
		float s_fine =0, b_fine =0;
		if(p_produced < energy) {
			s_fine = (energy-p_produced) *(price+2.5f);
		}else if (p_produced > energy) {
			s_fine = (p_produced - energy) *(2.5f);
		}
//		}else if (p_consumed == energy ) {
//			s_fine = (p_produced - energy) *(2.5f);
//		}
		 if (p_consumed > energy) {
			b_fine = (p_consumed - energy) *(2.5f);
		}
		System.out.println("  p_produced "+p_produced);
		System.out.println("  p_consumed "+p_consumed);
		System.out.println("  b_fine "+b_fine);
		System.out.println("  s_fine "+s_fine);
		dbhelper.updateOrderAmount(p_produced, s_fine, p_consumed, b_fine, this.orderId);
		System.out.println("  Status completed   ");
		sdao.updateOrderStatus(this.orderId);
		System.out.println("continueStatus5" + continueStatus);
		}
		}
		System.out.println("  -----------Completed the validation----------   ");
	}catch(Exception e) {
		e.printStackTrace();
	}
//		finally {
//			if (ScheduleDAO.con != null) {
//				try {
//					ScheduleDAO.con.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//			if (DBHelper.con != null) {
//				try {
//					DBHelper.con.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
		System.out.println(Thread.currentThread().getName() + " (End)");// prints thread name
	}
	
	private ArrayList<String> getStartAndEndDates() throws ParseException {
		ArrayList<String> dateRanges = new ArrayList<>();
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// SimpleDateFormat dt1 = new SimpleDateFormat("2018-03-06");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		Date startDate = dateTimeFormat.parse(this.startTs);
		String sR1 = timeFormat.format(startDate);
		Date startDateR1 = new Date(startDate.getTime()-15*ONE_MINUTE_IN_MILLIS);
		String sR2 = timeFormat.format(startDateR1);
		String startDateRange = sR2+"-"+sR1;
		
		
		Date endDate = dateTimeFormat.parse(endTs);
		String eR1 = timeFormat.format(endDate);
		Date endDateR1 = new Date(endDate.getTime()-15*ONE_MINUTE_IN_MILLIS);
		String eR2 = timeFormat.format(endDateR1);
		String endDateRange = eR2+"-"+eR1;
		dateRanges.add(dateFormat.format(startDate));
		dateRanges.add(startDateRange);
		dateRanges.add(endDateRange);
		return dateRanges;
		
	}

	private void processmessage() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}