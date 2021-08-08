

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class ScheduleDAO {

	static Connection con;
	Statement stmt = null;
	PreparedStatement pStmt = null;
	 public ArrayList<HashMap<String,Object>> getCompletedTrades(String date,String time, String source) throws SQLException, ClassNotFoundException
	 {
		 PreparedStatement pstmt = null;
		  time=time+":00";
		  String query = "";
		  ArrayList<HashMap<String,Object>> al=new ArrayList<>();
		 //time="16:21"+":00";
		//JDBCConnection connref =new JDBCConnection();
		 if (con == null ) {
				con = JDBCConnection.getOracleConnection();
		 }
		 if (source.equalsIgnoreCase("BC")) {
			System.out.println("select aso.sell_order_id,ubc.private_key,ubc.user_address,abc.order_id from all_sell_orders aso,all_blockchain_orders abc, user_blockchain_keys ubc where aso.transfer_end_ts ='"+date+" "+time+"' and abc.general_order_id=aso.sell_order_id and abc.order_type='SELL_ORDER' and ubc.user_id  = aso.seller_id and aso.order_status_id=5");
			query="select aso.sell_order_id,ubc.private_key,ubc.user_address,abc.order_id,abc.all_blockchain_orders_id from all_sell_orders aso,all_blockchain_orders abc, user_blockchain_keys ubc where aso.transfer_end_ts ='"+date+" "+time+"' and abc.general_order_id=aso.sell_order_id and abc.order_type='SELL_ORDER' and ubc.user_id  = aso.seller_id and aso.order_status_id=5";
			//String query="select aso.sell_order_id,ubc.private_key,ubc.public_key,abc.order_id,abc.all_blockchain_orders_id from all_sell_orders aso,all_blockchain_orders abc, user_blockchain_keys ubc where aso.transfer_end_ts ='2020-05-03 11:00:00' and abc.general_order_id=aso.sell_order_id and abc.order_type='SELL_ORDER' and ubc.user_id  = aso.seller_id and aso.order_status_id=5";
		 pstmt=con.prepareStatement(query);
		// pstmt.setString(1,controllerId);
		 ResultSet rs= pstmt.executeQuery();
		 while(rs.next())
		 {
			 HashMap<String,Object> data=new HashMap<>();
			 data.put("orderId",(rs.getString("order_id")));
			 data.put("privateKey",rs.getString("private_key"));
			 data.put("userAddress",rs.getString("user_address"));
			 data.put("generalOrderId",Integer.toString(rs.getInt("sell_order_id")));
			 data.put("blockChainOrderId",Integer.toString(rs.getInt("all_blockchain_orders_id")));
			 al.add(data);
			
		 }
		 }else {
			 System.out.println("select aso.sell_order_id,ud.meter_id,aso.transfer_start_ts,aso.transfer_end_ts\r\n"
			 		+ " from all_sell_orders aso,user_devices ud \r\n"
			 		+ " where aso.transfer_end_ts ='"+date+" "+time+"'  \r\n"
			 		+ " and aso.order_status_id = 5 and aso.user_device_id = ud.user_device_id");
				query="select aso.sell_order_id,ud.meter_id,aso.transfer_start_ts,aso.transfer_end_ts\r\n"
						+ " from all_sell_orders aso,user_devices ud \r\n"
						+ " where aso.transfer_end_ts ='"+date+" "+time+"'  \r\n"
						+ " and aso.order_status_id = 5 and aso.user_device_id = ud.user_device_id";
				//String query="select aso.sell_order_id,ubc.private_key,ubc.public_key,abc.order_id,abc.all_blockchain_orders_id from all_sell_orders aso,all_blockchain_orders abc, user_blockchain_keys ubc where aso.transfer_end_ts ='2020-05-03 11:00:00' and abc.general_order_id=aso.sell_order_id and abc.order_type='SELL_ORDER' and ubc.user_id  = aso.seller_id and aso.order_status_id=5";
			 pstmt=con.prepareStatement(query);
			// pstmt.setString(1,controllerId);
			 ResultSet rs= pstmt.executeQuery();
			 while(rs.next())
			 {
				 HashMap<String,Object> data=new HashMap<>();
				 data.put("orderId",(rs.getInt("sell_order_id")));
				 data.put("meterId",rs.getInt("meter_id"));
				 data.put("startTs",(rs.getString("transfer_start_ts")).toString());
				 data.put("endTs",(rs.getString("transfer_end_ts")).toString());
				 al.add(data);
				
			 }	 
		 }
		 System.out.println("AL --- >" +al.size());
		return  al;
	 }
	 
	 public void updateOrderStatus(int orderId) throws SQLException, ClassNotFoundException
	 {
		 PreparedStatement pstmt = null;
		//JDBCConnection connref =new JDBCConnection();
		 if (con == null ) {
				con = JDBCConnection.getOracleConnection();
		 }
		 String query="update all_contracts set contract_status_id=8 where sell_order_id =?";
		 pstmt=con.prepareStatement(query);
		pstmt.setInt(1,orderId);
		 pstmt.executeUpdate();
		 
		 query="update all_sell_orders set order_status_id=6 where sell_order_id =?";
		 pstmt=con.prepareStatement(query);
		pstmt.setInt(1,orderId);
		 pstmt.executeUpdate();
		 
	 }
	 
	 public void updateOrderStatus(int orderId,int contractStatus, int orderStatus) throws SQLException, ClassNotFoundException
	 {
		 PreparedStatement pstmt = null;
		//JDBCConnection connref =new JDBCConnection();
		 if (con == null ) {
				con = JDBCConnection.getOracleConnection();
		 }
		 String query="update all_contracts set contract_status_id="+contractStatus+" where sell_order_id =?";
		 pstmt=con.prepareStatement(query);
		pstmt.setInt(1,orderId);
		 pstmt.executeUpdate();
		 
		 query="update all_sell_orders set order_status_id="+orderStatus+" where sell_order_id =?";
		 pstmt=con.prepareStatement(query);
		pstmt.setInt(1,orderId);
		 pstmt.executeUpdate();
		 
	 }
	 
	 public void autoUpdateTrades() throws SQLException, ClassNotFoundException {
			PreparedStatement pstmt = null;
			// JDBCConnection connref =new JDBCConnection();
			if (con == null) {
				con = JDBCConnection.getOracleConnection();
			}
			final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
			  final long HOUR = 3600*1000; // in milli-seconds.
			  final long HALFHOUR = 1800*1000;
		        Date d1=new Date(new Date().getTime() +5*HOUR+HALFHOUR - 5*ONE_MINUTE_IN_MILLIS);
		     			// Date d1 = new Date(new Date().getTime() + HOUR);

			SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
			// SimpleDateFormat dt1 = new SimpleDateFormat("2018-03-06");
			System.out.println(dt1.format(d1));
			SimpleDateFormat dt2 = new SimpleDateFormat("HH:mm");
			// SimpleDateFormat dt2 = new SimpleDateFormat("22:10");
			System.out.println(dt2.format(d1));
			String date = dt1.format(d1) + " " + dt2.format(d1) + ":00";

			String query = "update all_sell_orders set order_status_id=6 where  order_status_id=5";
			pstmt = con.prepareStatement(query);
			// pstmt.setString(1, date);
			pstmt.executeUpdate();
			
			 query = "update all_contracts set contract_status_id=8 where sell_order_id in (select sell_order_id from all_sell_orders where order_status_id=6)";
			pstmt = con.prepareStatement(query);
			// pstmt.setString(1, date);
			pstmt.executeUpdate();

		}

	 
	 public String getAuthToken(String pbkey, String pvkey) throws SQLException, ClassNotFoundException
	 {
		 PreparedStatement pstmt = null;
		 String authToken="";
		
		//JDBCConnection connref =new JDBCConnection();
		 if (con == null ) {
				con = JDBCConnection.getOracleConnection();
		 }
			//System.out.println("select aso.sell_order_id,ubc.private_key,ubc.public_key,abc.order_id from all_sell_orders aso,all_blockchain_orders abc, user_blockchain_keys ubc where aso.transfer_start_ts =STR_TO_DATE('"+date+time+"','%Y-%m-%d %h:%m:%s') and abc.general_order_id=aso.sell_order_id and abc.order_type='SELL_ORDER' and ubc.user_id  = aso.seller_id and aso.order_status_id=1");
		 String query="select auth_token from user_blockchain_keys  where private_key=? and public_key=?";
		 pstmt=con.prepareStatement(query);
		 pstmt.setString(1,pvkey);
		 pstmt.setString(2,pbkey);
		 ResultSet rs= pstmt.executeQuery();
		 ArrayList<HashMap<String,String>> al=new ArrayList<>();
		 while(rs.next())
		 {
			 authToken = rs.getString("auth_token");
			 
			// initiateActions(rs.getString("user_id"),rs.getString("status"),rs.getString("controller_id"),rs.getInt("device_id"),"Timer");
			//topic=rs.getString(1);
		 }
		return  authToken;
	 }
	 
	 
	 public HashMap<String,Object> getBuyerDeviceId(int sellOrderId) throws SQLException, ClassNotFoundException
	 {
		 PreparedStatement pstmt = null;
		 HashMap<String,Object> data = new HashMap<>();
		 int meterId=0;
		
		//JDBCConnection connref =new JDBCConnection();
		 if (con == null ) {
				con = JDBCConnection.getOracleConnection();
		 }
			//System.out.println("select aso.sell_order_id,ubc.private_key,ubc.public_key,abc.order_id from all_sell_orders aso,all_blockchain_orders abc, user_blockchain_keys ubc where aso.transfer_start_ts =STR_TO_DATE('"+date+time+"','%Y-%m-%d %h:%m:%s') and abc.general_order_id=aso.sell_order_id and abc.order_type='SELL_ORDER' and ubc.user_id  = aso.seller_id and aso.order_status_id=1");
			String query = "select ud.meter_id,aso.power_to_sell,aso.rate_per_unit from user_devices ud, all_contracts ac,all_sell_orders aso\r\n"
					+ "					where ac.sell_order_id =?"
					+ "					and ac.buyer_id = ud.user_id \r\n"
					+ "                    and aso.sell_order_id = ac.sell_order_id limit 1";
		 pstmt=con.prepareStatement(query);
		 pstmt.setInt(1,sellOrderId);
		 
		 ResultSet rs= pstmt.executeQuery();
		 ArrayList<HashMap<String,String>> al=new ArrayList<>();
		 while(rs.next())
		 {
			 data.put("meterId", rs.getInt("meter_id"));
			 data.put("units", rs.getFloat("power_to_sell"));
			 data.put("price", rs.getFloat("rate_per_unit"));
			 
			 
			// initiateActions(rs.getString("user_id"),rs.getString("status"),rs.getString("controller_id"),rs.getInt("device_id"),"Timer");
			//topic=rs.getString(1);
		 }
		return  data;
	 }
	 
	 public String getBlockChainSettings() throws ClassNotFoundException, SQLException {
			PreparedStatement pstmt = null;
			String val = "";
			if (con == null) {
				con = JDBCConnection.getOracleConnection();
			}
			String query = "select value from general_config where name='p2p_blockchain_enabled'";
			// String query = "select
			// aso.sell_order_id,ubc.private_key,ubc.public_key,abc.order_id,abc.all_blockchain_orders_id
			// from all_sell_orders aso,all_blockchain_orders abc, user_blockchain_keys ubc
			// where aso.transfer_start_ts ='2020-05-14 03:00:00' and
			// abc.general_order_id=aso.sell_order_id and abc.order_type='SELL_ORDER' and
			// ubc.user_id = aso.seller_id and aso.order_status_id=3";
			pstmt = con.prepareStatement(query);
			// pstmt.setString(1,controllerId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				val = rs.getString(1);
			}
			if (val.equalsIgnoreCase("N")) {
			//	autoUpdateTrades();
			}
			System.out.println("val"+val);
			return val;

		}
	
}
