
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class ScheduleChunk {

	public ArrayList<HashMap<String, Object>> getCompletedTrades(String source) throws ClassNotFoundException, SQLException {

		final long ONE_MINUTE_IN_MILLIS = 60000;// millisecs
		final long HOUR = 3600 * 1000; // in milli-seconds
		final long HALFHOUR = 1800 * 1000;
		Date d1 = new Date(new Date().getTime() + 5 * HOUR + HALFHOUR - 15 * ONE_MINUTE_IN_MILLIS);

		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		// SimpleDateFormat dt1 = new SimpleDateFormat("2018-03-06");
		System.out.println(dt1.format(d1));
		SimpleDateFormat dt2 = new SimpleDateFormat("HH:mm");
		// SimpleDateFormat dt2 = new SimpleDateFormat("22:10");
		System.out.println(dt2.format(d1));
		return (compareData(dt1.format(d1), dt2.format(d1),source));

	}

	public ArrayList<HashMap<String, Object>> compareData(String date, String time, String source)
			throws ClassNotFoundException, SQLException

	{

		ScheduleDAO scheduledao = new ScheduleDAO();
		ArrayList<HashMap<String, Object>> responseFromSchedules = scheduledao.getCompletedTrades(date, time, source);
		Date d1 = new Date();
		return responseFromSchedules;

	}
}
