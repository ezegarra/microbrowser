package microbrowser.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TraceService {

	private static String 	trace_file_name = null;
	private static File 	trace_file 		= null;
	private static SimpleDateFormat sdf2	= null;
	
	public static final String EVENT_SETUP_RESIZEWIN			= "setup_resizewin";
	public static final String EVENT_SETUP_LOADDATA_BEGIN		= "setup_loaddata_begin";
	public static final String EVENT_SETUP_LOADDATA_END			= "setup_loaddata_end";	
	public static final String EVENT_SETUP_CREATEACTIONS_BEGIN	= "setup_createactions_begin";
	public static final String EVENT_SETUP_CREATEACTIONS_END	= "setup_createactions_end";	
	public static final String EVENT_DISCUSSION_OPEN			= "discussion_open";
	public static final String EVENT_QUESTION_CREATE_OPEN		= "question_create_open";
	public static final String EVENT_QUESTION_CREATE_CLOSE		= "question_create_close";
	public static final String EVENT_QUESTION_CREATE			= "question_create";
	public static final String EVENT_EDGE_CREATE				= "edge_create";
	public static final String EVENT_EDGE_REMOVE				= "edge_remove";
	public static final String EVENT_ANSWER_OPEN				= "answer_open";
	public static final String EVENT_ANSWER_CREATE_OPEN			= "answer_create_open";
	public static final String EVENT_ANSWER_CREATE				= "answer_create";
	public static final String EVENT_ANSWER_CREATE_CANCEL		= "answer_create_cancel";
	public static final String EVENT_PATTERN_CREATE_OPEN		= "pattern_create_open";
	public static final String EVENT_PATTERN_CREATE_CLOSE		= "pattern_create_close";
	public static final String EVENT_PATTERN_CREATE				= "pattern_create";
	public static final String EVENT_PATTERN_CHANGE_OPEN		= "pattern_change_open";
	public static final String EVENT_PATTERN_CHANGE_CANCEL		= "pattern_change_cancel";
	public static final String EVENT_PATTERN_OPEN				= "pattern_open";
	public static final String EVENT_LIST_MOUSEENTERED			= "list_mouseentered";
	public static final String EVENT_LIST_MOUSEEXITED			= "list_mouseexited";
	public static final String EVENT_LIST_MOUSEMOVED			= "list_mousemoved";
	public static final String EVENT_LIST_ITEMCLICKED 			= "list_pattern_itemclicked";	
	public static final String EVENT_PATTERNLIST_MOUSEENTERED	= "list_pattern_mouseentered";
	public static final String EVENT_PATTERNLIST_MOUSEEXITED	= "list_pattern_mouseexited";
	public static final String EVENT_PATTERNLIST_MOUSEMOVED		= "list_pattern_mousemoved";
	public static final String EVENT_PATTERNLIST_ITEMCLICKED 	= "list_pattern_itemclicked";
	public static final String EVENT_DIAG_ITEMENTERED			= "diag_itementered";
	public static final String EVENT_DIAG_ITEMCLICKED			= "diag_itemclicked";
	public static final String EVENT_DIAG_ITEMEXITED			= "diag_itemexited";
	public static final String EVENT_DIAG_ITEMDRAGGED 			= "diag_itemdragged";
	public static final String EVENT_DIAG_DETAIL_ITEMENTERED 	= "diag_detail_itementered";
	public static final String EVENT_DIAG_DETAIL_ITEMEXITED 	= "diag_detail_itemexited";
	public static final String EVENT_DIAG_DETAIL_ITEMCLICKED 	= "diag_detail_itemclicked";
	public static final String EVENT_SEARCH_UPDATE				= "search_update";
	public static final String EVENT_LEADERBOARD_OPEN			= "leaderboard_open";
	public static final String EVENT_LEADERBOARD_CLOSE			= "leaderboard_close";
	public static final String EVENT_DATERANGE_CHANGE 			= "daterange_change";
	
	static {
		Calendar cal 			= Calendar.getInstance();
		SimpleDateFormat sdf 	= new SimpleDateFormat("yyyy-MM-dd-hh-mm"); 
		sdf2					= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSSS");
		String dateStr 			= sdf.format(cal.getTime());
		trace_file_name 		= "data/trace/trace_" + dateStr + ".data";
		trace_file				= new File(trace_file_name);
	}
	
	public static void log(String event) {
		log(event, null);
	}
	public static void log(String event, Object data) {
		try {
			// if file does not exists, then create it
			if (!trace_file.exists()) {
				trace_file.getParentFile().mkdirs();
				trace_file.createNewFile();
			}

			FileWriter fw = new FileWriter(trace_file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sdf2.format(Calendar.getInstance().getTime()) + ", " + event + (data == null ? "" : ", " + data.toString()) + "\n");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
