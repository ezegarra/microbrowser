package microbrowser;

public interface VisualDBConstants {
	public static final String EXPERIMENT_MODE_PATTERN_LEADERBOARD 	= "PATTERN + LEADERBOARD";
	public static final String EXPERIMENT_MODE_PATTERN_ONLY			= "PATTERN ONLY";
	public static final String EXPERIMENT_MODE_LEADERBOARD_ONLY		= "LEADERBOARD ONLY";
	public static final String EXPERIMENT_MODE_NO_CONDITION			= "NO CONDITIONS";
	
	public static final String GRAPH_FILE_NAME_COMPLETE = "data/graphml-microbrowser.xml"; 			// repository of all nodes
	public static final String GRAPH_FILE_NAME 			= "data/graphml-microbrowser-input.xml";	// repository of active nodes
	
	public static final int NODE_TYPE_PATTERN			= 0;
	public static final int NODE_TYPE_DISCUSSION 		= 1;
	public static final int NODE_TYPE_ANSWER			= 20;
	public static final int NODE_TYPE_ANSWER_ACCEPTED	= 21;
	public static final int NODE_TYPE_LABEL				= 3;
	public static final int NODE_TYPE_AGGREGATE			= 4;
	
	public static final int EDGE_TYPE_LABEL2PATTERN 	= 4;
	
	public static final int EDGE_TYPE_THREAD2THREAD 	= 1;
	public static final int EDGE_TYPE_PATTERN2THREAD	= 2;
	public static final int EDGE_TYPE_PATTERN2PATTERN 	= 3;
	public static final int EDGE_TYPE_THREAD2ANSWER 	= 4;
	public static final int EDGE_TYPE_LABEL2LABEL 		= 5;
	
	public static final int SHAPE_QUESTION_ANSWERED		= 10;
	public static final int SHAPE_QUESTION_UNANSWERED	= 11;
	public static final int SHAPE_PATTERN				= 0;
	public static final int SHAPE_ANSWER				= 2;
	
	public static final int AGGREGATE_TYPE_PATTERN 		= 100;
	public static final int AGGREGATE_TYPE_TAG 			= 200;
	
	public static String DISPLAY_MODE_SIMILARITY = "SIMILARITY";
	public static String DISPLAY_MODE_PATTERN = "PATTERN";
}
