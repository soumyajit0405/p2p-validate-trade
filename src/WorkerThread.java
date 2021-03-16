
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

class WorkerThread implements Runnable {
	private String orderId;
	private String privateKey;
	private String userAddress;
	private int generalOrderId;
	private int blockChainOrderId;

	public WorkerThread(String orderId, String privateKey, String userAddress, int generalOrderId, int blockChainOrderId) {
		this.orderId = orderId;
		this.privateKey = privateKey;
		this.userAddress = userAddress;
		this.generalOrderId = generalOrderId;
		this.blockChainOrderId = blockChainOrderId;
	}

	public void run() {
		JSONObject inputDetails1 = new JSONObject();
		ScheduleDAO sdao = new ScheduleDAO();
		DBHelper dbhelper= new DBHelper();
		HttpConnectorHelper httpconnectorhelper= new HttpConnectorHelper();
		// System.out.println(Thread.currentThread().getName()+" (Start) message =
		// "+message);
		// processmessage();//call processmessage method that sleeps the thread for 2
		// seconds
		try {
			ScheduleDAO sdc= new ScheduleDAO();
			System.out.println(Thread.currentThread().getName() + " (Start)");
			inputDetails1.put("orderId", orderId);
			//inputDetails1.put("userpbkey", userAddress);
			//inputDetails1.put("userpvkey", privateKey);
			//String token = sdc.getAuthToken(userAddress, privateKey);
			System.out.println("http://159.89.162.194:6380/validatetrade");
			HashMap<String, String> responseFrombcnetwork = httpconnectorhelper
					.sendPostWithToken("http://159.89.175.110:3000/api/validateTrade", inputDetails1, 1,"");
			// HashMap<String,String> responseAfterParse =
			// cm.parseInput(responseFrombcnetwork);
			if (responseFrombcnetwork.get("Status").equalsIgnoreCase("Order Validated")) {
				// AllBlockchainOrder allbcorder=
				// bcdao.createBlockchainOrder(responseFrombcnetwork.get("Batch_id"),responseFrombcnetwork.get("order_id"),count1);
				// // Call BC API and put it in another method
				dbhelper.createBlockchainTx("abc", "TRADE_VALIDATED",  blockChainOrderId);
				sdao.updateOrderStatus(generalOrderId);
				//Commented Fines Section
				/*
				 * try { Thread.sleep(60000); } catch (InterruptedException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); } inputDetails1 = new
				 * JSONObject(); inputDetails1.put("orderid", orderId); JSONObject jsResponse=
				 * httpconnectorhelper
				 * .sendPostWithTokenForOrder("http://159.89.175.110:6380/getOrder",
				 * inputDetails1, 1,token);
				 * 
				 * dbhelper.updateOrderAmount((int)jsResponse.get("Seller_METER_READING_S")-(int
				 * )jsResponse.get("Seller_METER_READING_E"),(int)jsResponse.get("S_FINE"),(int)
				 * jsResponse.get("Buyer_METER_READING_S")-(int)jsResponse.get(
				 * "Buyer_METER_READING_E"),(int)jsResponse.getDouble("B_FINE"),generalOrderId);
				 */				
			}
	} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (ScheduleDAO.con != null) {
				try {
					ScheduleDAO.con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (DBHelper.con != null) {
				try {
					DBHelper.con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println(Thread.currentThread().getName() + " (End)");// prints thread name
	}

	private void processmessage() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}