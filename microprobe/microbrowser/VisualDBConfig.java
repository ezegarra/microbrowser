package microbrowser;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;

import microbrowser.util.DateLib;

import prefuse.util.TimeLib;


public class VisualDBConfig {
	
    public static final Calendar CAL = GregorianCalendar.getInstance();
    
    public static Level LOGGING_LEVEL_PREFUSE = Level.SEVERE;
    public static Level LOGGING_LEVEL_MICROPROBE = Level.ALL;
    
	public static int DETAIL_VIEW_MAX_RELATED = 70;
	
	public static final int AGGREGATE_TYPE 	= VisualDBConstants.AGGREGATE_TYPE_PATTERN;
	//public static final int AGGREGATE_TYPE 	= VisualDBConstants.AGGREGATE_TYPE_TAG;
	
    public static int DISPLAY_SIZE_WIDTH = 700;
    public static int DISPLAY_SIZE_HEIGHT = 450;
    
	public static int SLIDER_INITIAL_VALUE 	= 60;
	public static int SLIDER_MAX_VALUE 		= 500;
	public static double SLIDER_SIMILARITY_VALUE = 0.95;
	
	public static int RANGE_SLIDER_MIN_VALUE = DateLib.getDateAsInt(TimeLib.getDate(CAL, 2008, 0, 1));
	public static int RANGE_SLIDER_MAX_VALUE = DateLib.getDateAsInt(TimeLib.getDate(CAL, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE)));
	public static int RANGE_SLIDER_LOW_VALUE = DateLib.getDateAsInt(TimeLib.getDate(CAL, 2013, 5, 1));
	public static int RANGE_SLIDER_HIGH_VALUE = RANGE_SLIDER_MAX_VALUE;
	
	public static String EXPERIMENT_MODE = VisualDBConstants.EXPERIMENT_MODE_PATTERN_LEADERBOARD;

	public static String DISPLAY_MODE = VisualDBConstants.DISPLAY_MODE_SIMILARITY;

	
	public static final boolean DATABASE_ENABLED 		= true;	
	
	public static final String DATABASE_DRIVER_MYSQL 	= "com.mysql.jdbc.Driver";
	public static final String DATABASE_URL_MYSQL 		= "jdbc:mysql://mysql.cs.pitt.edu:3306/ezegarraDB";
	public static final String DATABASE_USER_MYSQL		= "ezegarra";
	public static final String DATABASE_PASSWORD_MYSQL	= "peru1995";
	
	public static final String DATABASE_DRIVER_DERBY 	= "org.apache.derby.jdbc.ClientDriver";
	public static final String DATABASE_URL_DERBY 		= "jdbc:derby://localhost:1527/MICROBROWSERDB;create=false";
	public static final String DATABASE_USER_DERBY		= "user";
	public static final String DATABASE_PASSWORD_DERBY	= "password";	
}
