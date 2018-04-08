package entity;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
	 private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

	    public static void main(String[] args) throws ParseException {

	        //method 1
	        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	        System.out.println(timestamp);

	        //method 2 - via Date
	        Date date = new Date();
	        System.out.println(new Timestamp(date.getTime()));

	        //return number of milliseconds since January 1, 1970, 00:00:00 GMT
	        System.out.println(timestamp.getTime());

	        //format timestamp
	        System.out.println(sdf.format(timestamp));
	        
//	        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//	        Date date = dateFormat.parse("23/09/2007");
//	        long time = date.getTime();
//	        System.out.println(time);
//	        System.out.println(new Timestamp(time));
	        
	    }
}
