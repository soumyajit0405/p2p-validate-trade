

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

	// Connection con;
	Statement stmt = null;
	PreparedStatement pStmt = null;
	 public synchronized ArrayList<HashMap<String,Object>> getCompletedTrades(String date,String time, String source) throws SQLException, ClassNotFoundException
	 {
		 Connection con = null;
		 ArrayList<HashMap<String,Object>> al=new ArrayList<>();
		 try {
		 PreparedStatement pstmt = null;
		  time=time+":00";
		  String query = "";
		  
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
			 System.out.println("select aso.seller_id,aso.sell_order_id,ud.meter_id,aso.energy,aso.transfer_start_ts,aso.transfer_end_ts\\r\\n\"\r\n"
			 		+ "						+ \" from all_sell_orders aso,user_devices ud \\r\\n\"\r\n"
			 		+ "						+ \" where aso.transfer_end_ts ='"+date+" "+time+"'  \\r\\n\"\r\n"
			 		+ "						+ \" and aso.order_status_id = 5 and aso.user_device_id = ud.user_device_id\";");
				query="select aso.seller_id,aso.sell_order_id,"
						+ "ud.meter_id,aso.energy,aso.transfer_start_ts,aso.transfer_end_ts\r\n"
						+ " from all_sell_orders aso,user_devices ud \r\n"
						+ " where aso.transfer_end_ts ='"+date+" "+time+"'  \r\n"
						//+ " where aso.transfer_end_ts ='2021-10-04 13:15:00'  \r\n"
						+ " and aso.order_status_id = 5 and aso.user_device_id = ud.user_device_id";
				//String query="select aso.sell_order_id,ubc.private_key,ubc.public_key,abc.order_id,abc.all_blockchain_orders_id from all_sell_orders aso,all_blockchain_orders abc, user_blockchain_keys ubc where aso.transfer_end_ts ='2020-05-03 11:00:00' and abc.general_order_id=aso.sell_order_id and abc.order_type='SELL_ORDER' and ubc.user_id  = aso.seller_id and aso.order_status_id=5";
			 pstmt=con.prepareStatement(query);
			// pstmt.setString(1,controllerId);
			 
			 ResultSet rs= pstmt.executeQuery();
			 System.out.println("Query : -> "+rs.getStatement().toString());
			 System.out.println("Connection :->"+con.getMetaData().getURL());
			 while(rs.next())
			 {
				 HashMap<String,Object> data=new HashMap<>();
				 data.put("orderId",(rs.getInt("sell_order_id")));
				 data.put("meterId",rs.getInt("meter_id"));
				 data.put("startTs",(rs.getString("transfer_start_ts")).toString());
				 data.put("endTs",(rs.getString("transfer_end_ts")).toString());
				 data.put("energy",(rs.getFloat("energy")));
				 data.put("power",0f);
				 data.put("sellerId",(rs.getInt("seller_id")));
				 al.add(data);
				 System.out.println("TimeStamp : -> "+data.get("startTs") + " ---  "+data.get("endTs"));	
			 }	 
		 }
		 }
		 catch(Exception e) {
			 e.printStackTrace();
		 }finally {
				if (con != null) {
					try {
						con.close();
						System.out.println("Connection Closed");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		 }
		 System.out.println("AL --- >" +al.size());
		return  al;
	 
	 }
	 
	 public synchronized void updateOrderStatus(int orderId) throws SQLException, ClassNotFoundException
	 {
		 PreparedStatement pstmt = null;
		//JDBCConnection connref =new JDBCConnection();
		 Connection con = null;
		 try {
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
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 finally {
				if (con != null) {
					try {
						con.close();
						System.out.println("Connection Closed");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		 }
		 
	 }
	 
	 public synchronized void updateOrderStatus(int orderId,int contractStatus, int orderStatus) throws SQLException, ClassNotFoundException
	 {
		 PreparedStatement pstmt = null;
		//JDBCConnection connref =new JDBCConnection();
		 Connection con = null;
		 try {
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
		 }catch (Exception e )
		 {
			 e.printStackTrace();
		 } 
		 finally {
				if (con != null) {
					try {
						con.close();
						System.out.println("Connection Closed");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		 }

		 
	 }
	 
	 public synchronized void updateSellOrder(int orderId, int orderStatus) throws SQLException, ClassNotFoundException
	 {
		 PreparedStatement pstmt = null;
		//JDBCConnection connref =new JDBCConnection();
		 Connection con = null;
		 try {
		 if (con == null ) {
				con = JDBCConnection.getOracleConnection();
		 }

		 String query="update all_sell_orders set order_status_id="+orderStatus+" where sell_order_id =?";
		 pstmt=con.prepareStatement(query);
		pstmt.setInt(1,orderId);
		 pstmt.executeUpdate(); 
		
		 }catch (Exception e )
		 {
			 e.printStackTrace();
		 } 
		 finally {
				if (con != null) {
					try {
						con.close();
						System.out.println("Connection Closed");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		 }

		 
	 }
	 
	 
	 public synchronized void updateContract(int orderId,int contractStatus) throws SQLException, ClassNotFoundException
	 {
		 PreparedStatement pstmt = null;
		//JDBCConnection connref =new JDBCConnection();
		 Connection con = null;
		 try {
		 if (con == null ) {
				con = JDBCConnection.getOracleConnection();
		 }
		 String query="update all_contracts set contract_status_id="+contractStatus+" where sell_order_id =?";
		 pstmt=con.prepareStatement(query);
		pstmt.setInt(1,orderId);
		 pstmt.executeUpdate();
		 
		 }catch (Exception e )
		 {
			 e.printStackTrace();
		 } 
		 finally {
				if (con != null) {
					try {
						con.close();
						System.out.println("Connection Closed");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		 }

		 
	 }
	 
	 public void autoUpdateTrades() throws SQLException, ClassNotFoundException {
			PreparedStatement pstmt = null;
			// JDBCConnection connref =new JDBCConnection();
			Connection con = null;
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
		Connection con = null;
		try {
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
		}catch(Exception e) {
			e.printStackTrace();
		}
		 finally {
				if (con != null) {
					try {
						con.close();
						System.out.println("Connection Closed");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		 }
		return  authToken;
	 }
	 
	 
	 public synchronized HashMap<String,Object> getBuyerDeviceId(int sellOrderId) throws SQLException, ClassNotFoundException
	 {
		 PreparedStatement pstmt = null;
		 HashMap<String,Object> data = new HashMap<>();
		 int meterId=0;
		Connection con = null;
		try {
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
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if (con != null) {
				try {
					con.close();
					System.out.println("Connection Closed");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
	}
		return  data;
	 }
	 
	 public synchronized ArrayList<HashMap<String,Object>> getOlpSell(int sellOrderId, String startTime, String endTime, int sellerId) throws SQLException, ClassNotFoundException
	 {
		 ArrayList<HashMap<String,Object>> list = new ArrayList<>();
		 PreparedStatement pstmt = null;
		 HashMap<String,Object> data = new HashMap<>();
		 int meterId=0;
		Connection con = null;
		try {
		//JDBCConnection connref =new JDBCConnection();
		 if (con == null ) {
				con = JDBCConnection.getOracleConnection();
		 }
			//System.out.println("select aso.sell_order_id,ubc.private_key,ubc.public_key,abc.order_id from all_sell_orders aso,all_blockchain_orders abc, user_blockchain_keys ubc where aso.transfer_start_ts =STR_TO_DATE('"+date+time+"','%Y-%m-%d %h:%m:%s') and abc.general_order_id=aso.sell_order_id and abc.order_type='SELL_ORDER' and ubc.user_id  = aso.seller_id and aso.order_status_id=1");
			String query = "			select *  from all_sell_orders where transfer_start_ts <= ? and transfer_end_ts >=? \r\n"
					+ "			and transfer_end_ts <= ?\r\n"
					+ "	 		and seller_id =? and order_status_id in (3,4,5,6)  and sell_order_id not in ( ?)\r\n"
					+ "	 		union \r\n"
					+ "	 		select *  from all_sell_orders where transfer_start_ts <= ? and transfer_end_ts >=? \r\n"
					+ "            and transfer_start_ts >= ? \r\n"
					+ "	 		and seller_id =? and order_status_id in (3,4,5,6) and sell_order_id not in ( ?)\r\n"
					+ "	 		union  \r\n"
					+ "	 		select * from all_sell_orders where transfer_start_ts >= ? and transfer_end_ts <=? \r\n"
					+ "	 		and seller_id =? and order_status_id in (3,4,5,6) and sell_order_id not in ( ?)\r\n"
					+ "            union  \r\n"
					+ "	 		select * from all_sell_orders where transfer_start_ts <= ? and transfer_end_ts >=? \r\n"
					+ "	 		and seller_id =? and order_status_id in (3,4,5,6) and sell_order_id not in ( ?)";
		 pstmt=con.prepareStatement(query);
		 pstmt.setString(1,startTime);
		 pstmt.setString(2,startTime);
		 pstmt.setString(3,endTime);
		 pstmt.setInt(4,sellerId);
		 pstmt.setInt(5,sellOrderId);
		 
		 pstmt.setString(6,endTime);
		 pstmt.setString(7,endTime);
		 pstmt.setString(8,startTime);
		 pstmt.setInt(9,sellerId);
		 pstmt.setInt(10,sellOrderId);
		 
		 pstmt.setString(11,startTime);
		 pstmt.setString(12,endTime);
		 pstmt.setInt(13,sellerId);
		 pstmt.setInt(14,sellOrderId);
		 
		 pstmt.setString(15,startTime);
		 pstmt.setString(16,endTime);
		 pstmt.setInt(17,sellerId);
		 pstmt.setInt(18,sellOrderId);
		 
		 ResultSet rs= pstmt.executeQuery();
		 ArrayList<HashMap<String,String>> al=new ArrayList<>();
		 final long ONE_MINUTE_IN_MILLIS = 60000;// millisecs
			final long HOUR = 3600 * 1000; // in milli-seconds
			final long HALFHOUR = 1800 * 1000;
			Date d1 = new Date(new Date().getTime() + 5 * HOUR + HALFHOUR - 15 * ONE_MINUTE_IN_MILLIS);
		 while(rs.next())
		 {
			 HashMap<String,Object> overlapTrade = new HashMap<String, Object>();
			 overlapTrade.put("startDate", new Date(rs.getTimestamp("transfer_start_ts").getTime()- 5 * HOUR - HALFHOUR));
			 overlapTrade.put("endDate", new Date(rs.getTimestamp("transfer_end_ts").getTime()- 5 * HOUR - HALFHOUR));
			 overlapTrade.put("powerToSell", rs.getFloat("power_to_sell"));
			 
			 list.add(overlapTrade);
			// initiateActions(rs.getString("user_id"),rs.getString("status"),rs.getString("controller_id"),rs.getInt("device_id"),"Timer");
			//topic=rs.getString(1);
		 }
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if (con != null) {
				try {
					con.close();
					System.out.println("Connection Closed");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
	}
		return  list;
	 }
	 
	 
	 public synchronized ArrayList<HashMap<String,Object>> getOlpContracts(int sellOrderId, String startTime, String endTime, int buyerId) throws SQLException, ClassNotFoundException
	 {
		 ArrayList<HashMap<String,Object>> list = new ArrayList<>();
		 PreparedStatement pstmt = null;
		 HashMap<String,Object> data = new HashMap<>();
		 int meterId=0;
		Connection con = null;
		try {
		//JDBCConnection connref =new JDBCConnection();
		 if (con == null ) {
				con = JDBCConnection.getOracleConnection();
		 }
			//System.out.println("select aso.sell_order_id,ubc.private_key,ubc.public_key,abc.order_id from all_sell_orders aso,all_blockchain_orders abc, user_blockchain_keys ubc where aso.transfer_start_ts =STR_TO_DATE('"+date+time+"','%Y-%m-%d %h:%m:%s') and abc.general_order_id=aso.sell_order_id and abc.order_type='SELL_ORDER' and ubc.user_id  = aso.seller_id and aso.order_status_id=1");
			String query = "	 		select aso.*  from all_sell_orders aso,all_contracts ac where aso.sell_order_id = ac.sell_order_id  and aso.transfer_start_ts <= ? and aso.transfer_end_ts >=? \r\n"
					+ "			and aso.transfer_end_ts <= ?\r\n"
					+ "	 		and ac.buyer_id =? and aso.order_status_id in (3,4,5,6)  and aso.sell_order_id not in ( ?)\r\n"
					+ "	 		union \r\n"
					+ "	 		select aso.*  from all_sell_orders aso,all_contracts ac where aso.sell_order_id = ac.sell_order_id  and aso.transfer_start_ts <= ? and aso.transfer_end_ts >=? \r\n"
					+ "            and aso.transfer_start_ts >= ? \r\n"
					+ "	 		and ac.buyer_id =? and aso.order_status_id in (3,4,5,6)  and aso.sell_order_id not in ( ?)\r\n"
					+ "	 		union  \r\n"
					+ "	 		select aso.* from all_sell_orders aso,all_contracts ac where aso.sell_order_id = ac.sell_order_id and aso.transfer_start_ts >= ? and aso.transfer_end_ts <=? \r\n"
					+ "	 		and ac.buyer_id =? and aso.order_status_id in (3,4,5,6)  and aso.sell_order_id not in ( ?)\r\n"
					+ "            union  \r\n"
					+ "	 		select aso.* from all_sell_orders aso,all_contracts ac where aso.sell_order_id = ac.sell_order_id and aso.transfer_start_ts <= ? and aso.transfer_end_ts >=? \r\n"
					+ "	 		and ac.buyer_id =? and aso.order_status_id in (3,4,5,6)  and aso.sell_order_id not in ( ?)\r\n";
		 pstmt=con.prepareStatement(query);
		 pstmt.setString(1,startTime);
		 pstmt.setString(2,startTime);
		 pstmt.setString(3,endTime);
		 pstmt.setInt(4,buyerId);
		 pstmt.setInt(5,sellOrderId);
		 
		 pstmt.setString(6,endTime);
		 pstmt.setString(7,endTime);
		 pstmt.setString(8,startTime);
		 pstmt.setInt(9,buyerId);
		 pstmt.setInt(10,sellOrderId);
		 
		 pstmt.setString(11,startTime);
		 pstmt.setString(12,endTime);
		 pstmt.setInt(13,buyerId);
		 pstmt.setInt(14,sellOrderId);
		 
		 pstmt.setString(15,startTime);
		 pstmt.setString(16,endTime);
		 pstmt.setInt(17,buyerId);
		 pstmt.setInt(18,sellOrderId);
		 
		 ResultSet rs= pstmt.executeQuery();
		 ArrayList<HashMap<String,String>> al=new ArrayList<>();
		 final long ONE_MINUTE_IN_MILLIS = 60000;// millisecs
			final long HOUR = 3600 * 1000; // in milli-seconds
			final long HALFHOUR = 1800 * 1000;
			Date d1 = new Date(new Date().getTime() + 5 * HOUR + HALFHOUR - 15 * ONE_MINUTE_IN_MILLIS);
		 while(rs.next())
		 {
			 HashMap<String,Object> overlapTrade = new HashMap<String, Object>();
			 overlapTrade.put("startDate", new Date(rs.getTimestamp("transfer_start_ts").getTime()- 5 * HOUR - HALFHOUR));
			 overlapTrade.put("endDate", new Date(rs.getTimestamp("transfer_end_ts").getTime()- 5 * HOUR - HALFHOUR));
			 overlapTrade.put("powerToSell", rs.getFloat("power_to_sell"));
			 
			 list.add(overlapTrade);
			// initiateActions(rs.getString("user_id"),rs.getString("status"),rs.getString("controller_id"),rs.getInt("device_id"),"Timer");
			//topic=rs.getString(1);
		 }
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if (con != null) {
				try {
					con.close();
					System.out.println("Connection Closed");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
	}
		return  list;
	 }
	 
	 public synchronized String getBlockChainSettings() throws ClassNotFoundException, SQLException {
			PreparedStatement pstmt = null;
			Connection con = null;
			String val = "";
			try {
			
			if (con == null) {
				con = JDBCConnection.getOracleConnection();
			}
			String query = "select value from general_config where name='p2p_blockchain_enabled'";
			
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
			
			} catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				if (con != null) {
					try {
						con.close();
						System.out.println("Connection Closed");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
		}
			return val;
	
	 }
	 
	 public synchronized float getPowerForOrder(int orderId) throws ClassNotFoundException, SQLException {
			PreparedStatement pstmt = null;
			Connection con = null;
			float power = 0f;
			try {
			
			if (con == null) {
				con = JDBCConnection.getOracleConnection();
			}
			String query = "select power_to_sell from all_sell_orders where sell_order_id=?";
			
			pstmt = con.prepareStatement(query);
			 pstmt.setInt(1,orderId);
			ResultSet rs = pstmt.executeQuery();
			System.out.println("Before  0->"+power);
			while (rs.next()) {
				power = rs.getFloat(1);
			}
			System.out.println("power ->"+power);
			
			} catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				if (con != null) {
					try {
						con.close();
						System.out.println("Connection Closed");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
		}
			return power;
	
	 }
}
