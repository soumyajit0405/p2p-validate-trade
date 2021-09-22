
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActionThreadPoolExecutor {

	public void getStarted() throws ClassNotFoundException, SQLException {
		ScheduleChunk scd = new ScheduleChunk();
		System.out.println("Before start of all threads");
		ScheduleDAO sdo = new ScheduleDAO();
		String bcStatus = sdo.getBlockChainSettings();
		System.out.println("bcStatus"+bcStatus);
		if (bcStatus.equalsIgnoreCase("Y")) {
			ArrayList<HashMap<String, Object>> pendingTrades = scd.getCompletedTrades("BC");
			System.out.println("List of Pending Trades");
			if (pendingTrades.size() > 0) {

				ExecutorService executor = Executors.newFixedThreadPool(pendingTrades.size());// creating a pool of 1000
																								// threads
				for (int i = 0; i < pendingTrades.size(); i++) {
					Runnable worker = new WorkerThread((String)pendingTrades.get(i).get("orderId"),
							((String)pendingTrades.get(i).get("privateKey")), (String)pendingTrades.get(i).get("userAddress"),
							Integer.parseInt((String)pendingTrades.get(i).get("generalOrderId")),
							Integer.parseInt((String)pendingTrades.get(i).get("blockChainOrderId")));
					System.out.println("List of run workers");
					executor.execute(worker);// calling execute method of ExecutorService
				}
				executor.shutdown();
				while (!executor.isTerminated()) {
				}
 
			}
		} else {
			
			ArrayList<HashMap<String, Object>> completedTrades = scd.getCompletedTrades("AG");
			System.out.println("List of Pending Trades");
			if (completedTrades.size() > 0) {

				ExecutorService executor = Executors.newFixedThreadPool(completedTrades.size());// creating a pool of 1000
																								// threads
				for (int i = 0; i < completedTrades.size(); i++) {
					Runnable worker = new AgentWorker((int)completedTrades.get(i).get("orderId"),
							((int)completedTrades.get(i).get("meterId")), (String)completedTrades.get(i).get("startTs"),
							(String)completedTrades.get(i).get("endTs"),(float)completedTrades.get(i).get("energy"));
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
