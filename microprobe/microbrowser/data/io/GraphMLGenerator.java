package microbrowser.data.io;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import microbrowser.VisualDBConstants;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLWriter;
import prefuse.data.tuple.TableTuple;



public class GraphMLGenerator {
	private static Logger logger = Logger.getLogger(GraphMLGenerator.class.getName());
	public static String dataFile 		= VisualDBConstants.GRAPH_FILE_NAME_COMPLETE;
	public static String dataFileIn 	= VisualDBConstants.GRAPH_FILE_NAME;
	
	private static GraphMLWriter graphWriter = new GraphMLWriter();
	
    public static final Schema NODE_SCHEMA = new Schema();

    public static final Schema EDGE_SCHEMA = new Schema();
    
    public static final Calendar CAL = GregorianCalendar.getInstance();
    
    static {
    	NODE_SCHEMA.addColumn("id"				, int.class);
    	NODE_SCHEMA.addColumn("type"			, int.class);
    	NODE_SCHEMA.addColumn("title"			, String.class		, "");
    	NODE_SCHEMA.addColumn("body"			, String.class		, "");
    	NODE_SCHEMA.addColumn("solution"		, String.class		, "");
    	NODE_SCHEMA.addColumn("x"				, double.class		, 0);
    	NODE_SCHEMA.addColumn("y"				, double.class		, 0);
    	NODE_SCHEMA.addColumn("viewcount"		, int.class			, 0);
    	NODE_SCHEMA.addColumn("answercount"		, int.class			, 0);
    	NODE_SCHEMA.addColumn("acceptedanswerid", int.class);
    	NODE_SCHEMA.addColumn("owner"			, String.class		, "Anonymous");
    	NODE_SCHEMA.addColumn("lasteditdate"    , long.class		, CAL.getTimeInMillis());
    	NODE_SCHEMA.addColumn("lastactivitydate", long.class		, null);
    	NODE_SCHEMA.addColumn("tags"			, String.class		, "");
    	NODE_SCHEMA.addColumn("score"			, int.class			, 0);
    	NODE_SCHEMA.addColumn("searchfield"		, String.class      , "");
    	EDGE_SCHEMA.addColumn("id"				, String.class);
    	EDGE_SCHEMA.addColumn("type"			, int.class			, 1); 	// 1 = Thread2Thread, 2=Pattern2Thread, 3=Pattern2Pattern
    	EDGE_SCHEMA.addColumn("similarity"		, double.class		, 0.0);
    	EDGE_SCHEMA.addColumn("lasteditdate"    , long.class		, CAL.getTimeInMillis());
    	EDGE_SCHEMA.addColumn("lastactivitydate", long.class		, CAL.getTimeInMillis());
    }
    
    public static void writeGraph(Graph graph, String filename) {
		try {
			graphWriter.writeGraph(graph, new File(filename));

		} catch (DataIOException e) {
			e.printStackTrace();
		}    	
    }
	
	public static void main(String argv[]) {
		long timein = System.currentTimeMillis();
		Graph graph = new Graph();
		graph.getNodeTable().addColumns(NODE_SCHEMA);
		graph.getEdgeTable().addColumns(EDGE_SCHEMA);
		
		// load thread nodes
		loadThreadNodes(graph);
		
		// load all pattern nodes
		loadPatternNodes(graph);
		
		// load all the pattern to pattern edges
		loadPattern2PatternEdges(graph);

		// load all the thread edges
		loadThread2ThreadEdges(graph);
		
		// load all the pattern to thread edges
		loadPattern2ThreadEdges(graph);
		
		writeGraph(graph, dataFileIn);
		writeGraph(graph, dataFile);
		
		// clock out
		long time = System.currentTimeMillis()-timein;

		logger.info("Graph generation completed in : " + (time/1000) + "." + (time%1000) + " seconds. ");
		
		readGraph(dataFileIn);
	}

	private static void readGraph(String datafile) {
		new GraphMLDataReader(datafile);

	}
	
	//------------------------------------------------------------------------------------------------------------------
	private static void loadThreadNodes(Graph graph) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT T.ID, T.TITLE, T.BODY, S.X, S.Y, T.VIEWCOUNT, COALESCE(T.ANSWERCOUNT, 0) ANSWERCOUNT,  T.ACCEPTEDANSWERID, COALESCE(T.OWNERDISPLAYNAME, 'Anonymous') OWNERDISPLAYNAME, LASTEDITDATE, LASTACTIVITYDATE, TAGS FROM VISUAL.CLASSIFIED_THREAD T LEFT JOIN VISUAL.SVD_THREADS S ON (T.ID = S.THREADID) WHERE TITLE IS NOT NULL AND POSTTYPEID = 1 ORDER BY LASTACTIVITYDATE DESC FOR READ ONLY";
		try {
			 conn = VDBConnector.getConnection();
			 pstmt = conn.prepareStatement(sql);
			 rs = pstmt.executeQuery();
			 
			 while ( rs.next()) {
				 String answercount = rs.getString("ANSWERCOUNT");
				 answercount = answercount == null || answercount.trim().equals("") ? "0" : answercount;
				 
				 prefuse.data.Node n = graph.addNode();
				 String tags = processTags(rs.getString("TAGS"));
				// graph.appendChild(createNode(doc, rs.getString("ID"), 1, rs.getString("TITLE"), rs.getString("BODY"), rs.getDouble("X"), rs.getDouble("Y"), rs.getInt("VIEWCOUNT"), Integer.parseInt(answercount), rs.getString("ACCEPTEDANSWERID")));
					n.set( "id"					, rs.getInt("ID"));
					n.set( "type"				, VisualDBConstants.NODE_TYPE_DISCUSSION);
					n.set( "title"				, rs.getString("TITLE"));
					n.set( "body"				, rs.getString("BODY"));
					n.set( "x"					, rs.getDouble("X"));
					n.set( "y"					, rs.getDouble("Y"));
					n.set( "viewcount"			, rs.getInt("VIEWCOUNT"));
					n.set( "answercount"		, Integer.parseInt(answercount));
					n.set( "acceptedanswerid"	, rs.getInt("ACCEPTEDANSWERID"));
					n.set( "owner"              , rs.getString("OWNERDISPLAYNAME"));
					n.set( "tags" 				, tags);
					n.set( "searchfield"		, rs.getString("TITLE") + " " + tags.replaceAll(",", " ") + " " + rs.getString("body"));
					try {
						String datein = rs.getString("LASTEDITDATE");
						if ( datein != null ) {
					    	n.set( "lasteditdate"    	, (new SimpleDateFormat("MM/d/yyyy H:m").parse(datein)).getTime());													
						}
					} catch ( ParseException p) {
						System.err.println("ERROR " + p);
					}
					try {
						String datein = rs.getString("LASTACTIVITYDATE");
						if ( datein != null ) {
					    	n.set( "lastactivitydate"    	, (new SimpleDateFormat("MM/d/yyyy H:m").parse(datein)).getTime());
						}
						else if ( rs.getString("LASTEDITDATE") != null ) { // if the last activity date is not set, use the edit date if available
							System.err.println("ERROR: LastActivityDate is null, defaulting to last edit date");
					    	n.set( "lastactivitydate"    	, (new SimpleDateFormat("MM/d/yyyy H:m").parse(rs.getString("LASTEDITDATE"))).getTime());																				
						}
						else {
							System.err.println("ERROR: LastActivityDate is null");
							System.exit(1);
						}
					} catch ( ParseException p) {
						System.err.println("ERROR " + p);
					}								
			 }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if ( conn != null ) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * The function will receive as input a set of <tags> and wil
	 * proceed to convert them into tag1,tag2
	 * @param string
	 * @return
	 */
	private static String processTags(String string) {
		String tags = "";
		StringTokenizer st = new StringTokenizer(string, "<");
		while ( st.hasMoreTokens()) {
			String tag = st.nextToken();
			
			tags += tag.substring(0, tag.length()-1) + ",";
		}
		return tags.substring(0, tags.length()-1);
	}

	private static void loadPatternNodes(Graph graph) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM VISUAL.PATTERNS ORDER BY PATTERNNAME ASC FOR READ ONLY";
		try {
			 conn = VDBConnector.getConnection();
			 pstmt = conn.prepareStatement(sql);
			 rs = pstmt.executeQuery();
			 
			 while ( rs.next()) {

				 prefuse.data.Node n = graph.addNode();
				 n.set( "id"		, rs.getInt("ID"));
				 n.set( "type"		, VisualDBConstants.NODE_TYPE_PATTERN);
				 n.set( "title"		, rs.getString("PATTERNNAME"));
				 n.set( "body"		, rs.getString("PATTERNDESCRIPTION"));
				 n.set( "solution"	, rs.getString("PATTERNSOLUTION"));
			 }
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(-1);
		} finally {
			if ( conn != null ) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//------------------------------------------------------------------------------------------------------------------
	private static void loadThread2ThreadEdges(Graph graph) {

        
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM VISUAL.SIMILARITIES WHERE THREADID = ? AND THREADID != THREADID2 AND THREADID2 IS NOT NULL AND SIMILARITY > 0 AND THREADID2 IN (SELECT ID FROM VISUAL.CLASSIFIED_THREAD) ORDER BY SIMILARITY FETCH FIRST 20 ROWS ONLY FOR READ ONLY ";
		//String sql = "SELECT * FROM VISUAL.SIMILARITIES WHERE THREADID = ? AND THREADID != THREADID2 AND THREADID2 IS NOT NULL AND SIMILARITY > 0 AND THREADID2 IN (SELECT ID FROM VISUAL.CLASSIFIED_THREAD) ORDER BY SIMILARITY FOR READ ONLY ";

		Iterator <?> list = graph.getNodes().tuples(ExpressionParser.predicate("type = " + VisualDBConstants.NODE_TYPE_DISCUSSION));
		
		try {
			conn = VDBConnector.getConnection();
			pstmt = conn.prepareStatement(sql);
			while ( list.hasNext() ) {		

				// clock in
				long timein = System.currentTimeMillis();

				TableTuple tt = (TableTuple) list.next();
				prefuse.data.Node sourceNode = graph.getNode(tt.getRow());
				String threadid = String.valueOf(sourceNode.getLong("id"));

				pstmt.setString(1, threadid);

				rs = pstmt.executeQuery();

				while ( rs.next() ) {
					Iterator <?> matches = graph.getNodes().tuples(ExpressionParser.predicate("type = " + VisualDBConstants.NODE_TYPE_DISCUSSION + " and id=" + rs.getString("THREADID2") ));
					while ( matches.hasNext()) {
						prefuse.data.Node targetNode = graph.getNode(((TableTuple)matches.next()).getRow());
						Edge edge = graph.addEdge(sourceNode, targetNode);
						edge.setString("id", threadid + "_" + rs.getString("THREADID2"));
						edge.setInt( "type", VisualDBConstants.EDGE_TYPE_THREAD2THREAD);
						edge.setDouble( "similarity", rs.getDouble("SIMILARITY"));
					}
				}
				// clock out
				long time = System.currentTimeMillis()-timein;

				logger.info("External query processing completed: " + (time/1000) + "." + (time%1000) + " seconds. ");//progress = " + (index+1) + " out of " + list.getLength());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if ( conn != null ) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			}
		}
		
		
	}
	
	//------------------------------------------------------------------------------------------------------------------
	private static void loadPattern2PatternEdges(Graph graph) {

        
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM VISUAL.PATTERN_2_PATTERN R WHERE R.PATTERNNAMESOURCE != R.PATTERNNAMETARGET AND R.PATTERNNAMESOURCE = ? FOR READ ONLY ";

		Iterator <?> list = graph.getNodes().tuples(ExpressionParser.predicate("type = " + VisualDBConstants.NODE_TYPE_PATTERN));
		
		try {
			conn = VDBConnector.getConnection();
			pstmt = conn.prepareStatement(sql);

			while ( list.hasNext()) {
		        // clock in
		        long timein = System.currentTimeMillis();

				TableTuple tt = (TableTuple) list.next();
				prefuse.data.Node sourceNode = graph.getNode(tt.getRow());

				String patternNameSource = sourceNode.getString("id");

				pstmt.setString(1, patternNameSource);

				rs = pstmt.executeQuery();

				while ( rs.next() ) {
					Iterator <?> matches = graph.getNodes().tuples(ExpressionParser.predicate("type = " + VisualDBConstants.NODE_TYPE_PATTERN + " and id=" + rs.getString("PATTERNNAMETARGET") ));
					while ( matches.hasNext()) {
						prefuse.data.Node targetNode = graph.getNode(((TableTuple)matches.next()).getRow());
						Edge edge = graph.addEdge(sourceNode, targetNode);
						edge.setString("id", patternNameSource + "_" + rs.getString("PATTERNNAMETARGET"));
						edge.setInt( "type", VisualDBConstants.EDGE_TYPE_PATTERN2PATTERN);
						edge.setDouble("similarity", 1.0);
					}
				}

				// clock out
				long time = System.currentTimeMillis()-timein;

				logger.info("External query processing completed: " + (time/1000) + "." + (time%1000) + " seconds.");// progress = " + (index+1) + " out of " + list.);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if ( conn != null ) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			}
		}
	}
	
	//------------------------------------------------------------------------------------------------------------------
	private static void loadPattern2ThreadEdges(Graph graph) {

        
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM VISUAL.PATTERN_2_POST R WHERE R.POSTPARENTID IS NOT NULL AND R.PATTERNNAME = ? FOR READ ONLY ";

		Iterator <?> list = graph.getNodes().tuples(ExpressionParser.predicate("type = " + VisualDBConstants.NODE_TYPE_PATTERN));

		try {
			conn = VDBConnector.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			while (list.hasNext()){
				// clock in
				long timein = System.currentTimeMillis();

				TableTuple tt = (TableTuple) list.next();
				prefuse.data.Node sourceNode = graph.getNode(tt.getRow());
				String patternNameSource = sourceNode.getString("id");

				pstmt.setString(1, patternNameSource);

				rs = pstmt.executeQuery();

				while ( rs.next() ) {
//					graph.appendChild(createEdge(doc, patternNameSource, rs.getString("POSTPARENTID"), 0.0, String.valueOf(VisualDBConstants.EDGE_TYPE_PATTERN2THREAD)));	
					Iterator <?> matches = graph.getNodes().tuples(ExpressionParser.predicate("type = " + VisualDBConstants.NODE_TYPE_DISCUSSION + " and id=" + rs.getString("POSTPARENTID")));
					while ( matches.hasNext()) {
						prefuse.data.Node targetNode = graph.getNode(((TableTuple)matches.next()).getRow());
						Edge edge = graph.addEdge(sourceNode, targetNode);
						edge.setString("id", patternNameSource + "_" + rs.getString("POSTPARENTID"));
						edge.setInt( "type", VisualDBConstants.EDGE_TYPE_PATTERN2THREAD);
						edge.setDouble("similarity", 1.0);
					}
				}

				// clock out
				long time = System.currentTimeMillis()-timein;

				logger.info("External query processing completed: " + (time/1000) + "." + (time%1000) + " seconds.");//progress = " + (index+1) + " out of " + list.getLength());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if ( conn != null ) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			}
		}
	}
	
	/**
	 * Adds a new node to Graph g using the information from tuple tuple and returns the new node
	 * @param g
	 * @param tuple
	 * @return new node
	 */
	public static prefuse.data.Node addNode(Graph g, TableTuple tuple) {
		prefuse.data.Node n = g.addNode();
		Schema nodeSchema = n.getSchema();
		for ( int i = 0; i < nodeSchema.getColumnCount(); i++ ) {
			n.set(nodeSchema.getColumnName(i), tuple.get(i));
		}	
		return n;
	}
}