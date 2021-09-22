import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class DBHelper {

	static Connection con;
	 
	 public void createBlockchainTx(String txId,String status, int blockChainOrderId) throws SQLException, ClassNotFoundException
		{
		try {	
		 //JDBCConnection connref =new JDBCConnection();
		 if (con == null ) {
				con = JDBCConnection.getOracleConnection();
		 } 
			PreparedStatement pstmt = null;
			String deviceName="";
			if(con!=null)
			{
					String query="insert into all_blockchain_transactions(blockchain_trx_id,transaction_type,blockchain_order_id) values(?,?,?)";
					  pstmt=con.prepareStatement(query);
					  pstmt.setString(1,txId);
					  pstmt.setString(2,status);
					  pstmt.setInt(3,blockChainOrderId); 
					 
					  pstmt.execute();
					 
					 
					
			} else {
				String query="insert into all_blockchain_transactions(blockchain_trx_id,transaction_type,blockchain_order_id) values(?,?,?)";
				  pstmt=con.prepareStatement(query);
				  pstmt.setString(1,txId);
				  pstmt.setString(2,status);
				  pstmt.setInt(3,blockChainOrderId); 
				 
				  pstmt.execute();
				
			}
			}
		
	 catch(Exception e) {
		 e.printStackTrace();
	 }

		}

	 
	 public synchronized void updateOrderAmount(double sellerTrxAmount, double sellerFine, double buyerTrxAmount, double buyerFine, int orderId) throws SQLException, ClassNotFoundException
		{
		 Connection con = null;
		try {	
		 //JDBCConnection connref =new JDBCConnection();
		 if (con == null ) {
				con = JDBCConnection.getOracleConnection();
		 } 
			PreparedStatement pstmt = null;
			String deviceName="";
			if(con!=null)
			{
					String query="update all_sell_orders set seller_energy_tfr=?,seller_fine=?,is_fine_applicable=? where sell_order_id=?";
					  pstmt=con.prepareStatement(query);
					  pstmt.setDouble(1,sellerTrxAmount);
					  pstmt.setDouble(2,sellerFine);
					  if(sellerFine > 0) {
						  pstmt.setString(3,"Y");
					  } else {
						  pstmt.setString(3,"N");  
					  }
					  pstmt.setInt(4,orderId);
					 
					  pstmt.execute();
					 
					  query="update all_contracts set seller_energy_tfr=?,seller_fine=?,buyer_energy_tfr=?,buyer_fine=?,is_fine_applicable=? where sell_order_id=?";
					  pstmt=con.prepareStatement(query);
					  pstmt.setDouble(1,sellerTrxAmount);
					  pstmt.setDouble(2,sellerFine);
					  pstmt.setDouble(3,buyerTrxAmount);
					  pstmt.setDouble(4,buyerFine);
					  if(buyerFine > 0) {
						  pstmt.setString(5,"Y");
					  } else {
						  pstmt.setString(5,"N");  
					  }
					  pstmt.setInt(6,orderId);
					 
					  pstmt.execute();
					 
					 
					
			} else {
				String query="update all_sell_orders set seller_energy_tfr=?,seller_fine=?,is_fine_applicable=? where sell_order_id=?";
				  pstmt=con.prepareStatement(query);
				  pstmt.setDouble(1,sellerTrxAmount);
				  pstmt.setDouble(2,sellerFine);
				  if(sellerFine > 0) {
					  pstmt.setString(3,"Y");
				  } else {
					  pstmt.setString(3,"N");  
				  }
				  pstmt.setInt(4,orderId);
				 
				  pstmt.execute();
				 
				  query="update all_contracts set seller_energy_tfr=?,seller_fine=?,buyer_energy_tfr=?,buyer_fine=?,is_fine_applicable=? where sell_order_id=?";
				  pstmt=con.prepareStatement(query);
				  pstmt.setDouble(1,sellerTrxAmount);
				  pstmt.setDouble(2,sellerFine);
				  pstmt.setDouble(3,buyerTrxAmount);
				  pstmt.setDouble(4,buyerFine);
				  if(buyerFine > 0) {
					  pstmt.setString(5,"Y");
				  } else {
					  pstmt.setString(5,"N");  
				  }
				  pstmt.setInt(6,orderId);
				 
				  pstmt.execute();
				
			}
			}
		
	 catch(Exception e) {
		 e.printStackTrace();
	 }
		finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	 }

		}

}
