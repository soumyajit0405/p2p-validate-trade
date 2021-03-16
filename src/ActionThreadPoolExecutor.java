
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActionThreadPoolExecutor {

	public void getStarted() throws ClassNotFoundException, SQLException {
		ScheduleChunk scd = new ScheduleChunk();
		System.out.println("Before start of all threads");
		ScheduleDAO sdo= new ScheduleDAO();
		if (sdo.getBlockChainSettings().equalsIgnoreCase("Y") ) {
		ArrayList<HashMap<String, String>> pendingTrades = scd.getPendingTrades();
		System.out.println("List of Pending Trades");
		if (pendingTrades.size() > 0) {

			ExecutorService executor = Executors.newFixedThreadPool(pendingTrades.size());// creating a pool of 1000
																							// threads
			for (int i = 0; i < pendingTrades.size(); i++) {
				Runnable worker = new WorkerThread(pendingTrades.get(i).get("orderId"),
						(pendingTrades.get(i).get("privateKey")), pendingTrades.get(i).get("userAddress"),
						Integer.parseInt(pendingTrades.get(i).get("generalOrderId")),
						Integer.parseInt(pendingTrades.get(i).get("blockChainOrderId")));
				System.out.println("List of run workers");
				executor.execute(worker);// calling execute method of ExecutorService
			}
			executor.shutdown();
			while (!executor.isTerminated()) {
			}

		}
		}
		System.out.println("Finished all threads");
	}
}
