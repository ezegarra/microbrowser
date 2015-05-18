package microbrowser.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;

import microbrowser.VisualDBConfig;
import microbrowser.VisualDBConstants;
import microbrowser.data.model.Answer;
import microbrowser.data.model.Discussion;
import microbrowser.data.model.Pattern;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Tuple;
import prefuse.data.expression.Expression;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.tuple.TableTuple;

public abstract class GraphUtil {
	private static Logger logger = Logger.getLogger(GraphUtil.class.getName());
	
	private static Random random = new Random();

	public static Node createLabelNode(Graph graph, String title) {
		Node n = graph.addNode();

		n.setInt("type", VisualDBConstants.NODE_TYPE_LABEL);
		n.setString("title", title);
		n.setInt("id", random.nextInt());
		
		return n;
	}
	

	public static Node createQuestionNode(Graph graph, Node source) {
		Node n = graph.addNode();
		for ( int i = 0; i < source.getColumnCount(); i++) {
			n.set(i, source.get(i));
		}
		
		return n;
	}
	

	public static Node createQuestionNode(Graph graph, Discussion source) {
		Node n = createQuestionNode(graph, source.getOriginalNode());
				
		TraceService.log(TraceService.EVENT_QUESTION_CREATE, n.getString("id"));

		return n;
	}
	
	
	public static Node createQuestionNode(Graph graph, String title, String body) {
		Random random = new Random();
		Node n = graph.addNode();
		
		n.setInt("type", VisualDBConstants.NODE_TYPE_DISCUSSION);
		n.setInt("id", random.nextInt());
		n.setDouble("x", Math.random());
		n.setDouble("y", Math.random());
		n.setInt("viewcount", 0);
		n.setInt("answercount", 0);
		n.setString("title", title);
		n.setString("body", body);
		Calendar currdate = Calendar.getInstance();
		currdate.set(Calendar.HOUR_OF_DAY, currdate.getActualMinimum(Calendar.HOUR_OF_DAY));
		currdate.set(Calendar.MINUTE, currdate.getActualMinimum(Calendar.MINUTE));
		currdate.set(Calendar.SECOND, currdate.getActualMinimum(Calendar.SECOND));
		currdate.set(Calendar.MILLISECOND, currdate.getActualMinimum(Calendar.MILLISECOND));
		n.setLong("lastactivitydate", currdate.getTimeInMillis());
		n.setLong("lasteditdate", currdate.getTimeInMillis());
		n.set( "searchfield"		, title + " " + body);

		TraceService.log(TraceService.EVENT_QUESTION_CREATE, n.getString("id"));
		logger.info("node=" + n);
		return n;
	}
	
	public static Node createPatternNode(Graph g, Pattern pattern) {
		return createPatternNode(g, pattern.getId(), pattern.getTitle(), pattern.getProblem(), pattern.getSolution());
	}
	
	public static Node createPatternNode(Graph graph, String title, String description, String solution) {
		return createPatternNode(graph, -1000, title, description, solution);
	}

	public static Node createPatternNode(Graph graph, int id, String title, String description, String solution) {
		Node n = graph.addNode();

		Random random = new Random();
		n.setInt("type", VisualDBConstants.NODE_TYPE_PATTERN);
		n.setInt("id", id == -1000? random.nextInt(): id);
		n.setDouble("x", 0);
		n.setDouble("y", 0);
		n.setInt("viewcount", 0);
		n.setInt("answercount", 0);
		n.setString("title", title);
		n.setString("body", description);
		n.setString("solution", solution);
		
		return n;
	}
	
	public static Node createAnswerNode(Graph g, Answer answer) {
        // clock in
        long timein = System.currentTimeMillis();

		Node n = g.addNode();

		n.setInt("type"		, answer.isAccepted() ? VisualDBConstants.NODE_TYPE_ANSWER_ACCEPTED : VisualDBConstants.NODE_TYPE_ANSWER);
		n.setInt("id"		, answer.getId());
		n.setString("body"	, answer.getBody());
		n.setInt("score"	, answer.getScore());

		TraceService.log(TraceService.EVENT_ANSWER_CREATE, n.getInt("id"));
		
        // clock out
        long time = System.currentTimeMillis()-timein;
        logger.info("createAnswerNode: completed " + (time/1000) + "." + (time%1000) + " seconds." );				
        return n;
	}
	
	public static Edge createEdge(Graph graph, int type, Node sourceNode, Node targetNode) {
		
		
		// Discussion threads can only be associated to a single pattern
		// the following code removes any prior thread-pattern relationship
		if ( sourceNode.getInt("type") == VisualDBConstants.NODE_TYPE_DISCUSSION ) {
			// find all edges
			Iterator<?> edges = sourceNode.edges();
			
			// remove edges that are patterns
			while ( edges.hasNext()) {
				Edge _edge = (Edge) edges.next();

				if ( ( _edge.getSourceNode() == sourceNode && _edge.getTargetNode().getInt("type") == VisualDBConstants.NODE_TYPE_PATTERN ) || 
						(_edge.getTargetNode() == sourceNode && _edge.getSourceNode().getInt("type") == VisualDBConstants.NODE_TYPE_PATTERN ) ) {
					graph.removeEdge(_edge);
				}
			}					
		}
		
		Edge e = graph.addEdge(sourceNode, targetNode);
		e.setString("id", String.valueOf(Math.random()));
		e.setInt("type", type);
		e.setDouble("similarity", type == VisualDBConstants.EDGE_TYPE_PATTERN2PATTERN || type==VisualDBConstants.EDGE_TYPE_PATTERN2THREAD ? 1.0 : 0.0);
		TraceService.log(TraceService.EVENT_EDGE_CREATE, e);

		return e;
	}
	
	public static Object findEdge(Graph graph, int sourceNodeId, int targetNodeId ) {
		Predicate p = (Predicate) ExpressionParser.parse("type=1 && id = '" + sourceNodeId + "_" + targetNodeId + "'");

		Iterator<?> iter = graph.getEdges().tuples(p);
		TableTuple o = null;
		Edge e = null;
		if ( iter.hasNext()) {
			o = (TableTuple)iter.next();
			e = graph.getEdge(o.getRow());
		}
		return e;
	}
	
	public static void removeEdge(Graph graph, Node sourceNode, Node targetNode) {
		
		
		Iterator<Edge> edges = graph.edges(sourceNode);
		Edge e = null;
		
		while ( edges.hasNext()) {
			e = (Edge) edges.next();
			
			if ( (e.getSourceNode().getString("id").equals(sourceNode.getString("id")) && e.getTargetNode().getString("id").equals(targetNode.getString("id"))) ||
					(e.getSourceNode().getString("id").equals(targetNode.getString("id")) && e.getTargetNode().getString("id").equals(sourceNode.getString("id")))) {
				
				logger.info("Found edge " + e);
				// remove the edge
				graph.removeEdge(e);							
				TraceService.log(TraceService.EVENT_EDGE_REMOVE, e);
			}
		}
	}
	
	public static Iterator getRelatedPatterns(Graph graph, Node node) {
        // clock in
        long timein = System.currentTimeMillis();
        
        Iterator<Object> edges = node.outEdges();
        ArrayList<Object> list = new ArrayList<Object>();
        while ( edges.hasNext()) {
        	Edge edge = (Edge) edges.next();
        	if ( edge.getTargetNode().getInt("type") == VisualDBConstants.NODE_TYPE_PATTERN) {
        		list.add(edge.getTargetNode());
        	}
        }
		
		// clock out
        long time = System.currentTimeMillis()-timein;

        logger.info("getRelatedPatterns: Found " + list.size() + " related patterns in " + (time/1000) + "." + (time%1000) + " seconds.");		
		return list.iterator();
	}

	
	/**
	 * Find all the patterns defined in the Graph
	 * 
	 * @param graph
	 * @return patterns list
	 */
	public static Iterator<Pattern> getAllPatterns(Graph graph) {
		ArrayList<Pattern>list = new ArrayList<Pattern>();
		
		Iterator<?> iter = graph.getNodes().tuples((Predicate) ExpressionParser.parse("type = " + VisualDBConstants.NODE_TYPE_PATTERN ));
		while ( iter.hasNext()) {
			TableTuple t = (TableTuple) iter.next();
			Node n = graph.getNode(t.getRow());
			Pattern p = new Pattern(n);
			list.add(p);
		}
		return list.iterator();
	}
	
	/**
	 * Find all the discussions defined in the Graph
	 * 
	 * @param graph
	 * @return discussions list
	 */
	public static Iterator<Discussion> getAllDiscussions(Graph graph) {
		ArrayList<Discussion>list = new ArrayList<Discussion>();
		
		Iterator<?> iter = graph.getNodes().tuples((Predicate) ExpressionParser.parse("type = " + VisualDBConstants.NODE_TYPE_DISCUSSION ));
		while ( iter.hasNext()) {
			TableTuple t = (TableTuple) iter.next();
			Node n = graph.getNode(t.getRow());
			Discussion p = new Discussion(n);
			list.add(p);
		}
		return list.iterator();
	}	
	
	public static Iterator<Discussion> getDiscussionsByPattern(Graph graph, Pattern pattern, boolean filterdate) {
		ArrayList<Discussion> list = new ArrayList<Discussion>();
		
		long lowval 	= DateLib.getDateAsLong(VisualDBConfig.RANGE_SLIDER_LOW_VALUE);
		long highval 	= DateLib.getDateAsLong(VisualDBConfig.RANGE_SLIDER_HIGH_VALUE);

		Iterator<?> iter = graph.getEdges().tuples((Predicate) ExpressionParser.parse("type = " + VisualDBConstants.EDGE_TYPE_PATTERN2THREAD ));
		
		while (iter.hasNext()) {
			TableTuple o =(TableTuple) iter.next();
			Edge e = graph.getEdge(o.getRow());
			
			if ( pattern.getId() == e.getTargetNode().getInt("id")) {
				
				// check for activity date filtering
				long lastactivitydate = e.getSourceNode().getLong("lastactivitydate");
				
				if ( filterdate ) {
					if (lowval <= lastactivitydate && lastactivitydate <= highval) {
						list.add(new Discussion(e.getSourceNode()));
					}
				} else {
					list.add(new Discussion(e.getSourceNode()));
					
				}
			}
		}
		return list.iterator();
	}
	
	public static Iterator<Discussion> getDiscussionsWithoutPattern(Graph graph, boolean filterdate) {
		ArrayList<Discussion> list = new ArrayList<Discussion>();
		int count = 0;
		
		long lowval 	= DateLib.getDateAsLong(VisualDBConfig.RANGE_SLIDER_LOW_VALUE);
		long highval 	= DateLib.getDateAsLong(VisualDBConfig.RANGE_SLIDER_HIGH_VALUE);
		
		Predicate p = null;
		if ( filterdate ) {
			p = (Predicate) ExpressionParser.parse("type = " + VisualDBConstants.NODE_TYPE_DISCUSSION + " && lastactivitydate <= " + highval + "L && lastactivitydate >= " + lowval + "L");
		} else {			
			p = (Predicate) ExpressionParser.parse("type = " + VisualDBConstants.NODE_TYPE_DISCUSSION);
		}
		
		Iterator<?> iter = graph.getNodes().tuples(p);
		
		while (iter.hasNext() && count < VisualDBConfig.SLIDER_INITIAL_VALUE) {
			TableTuple o =(TableTuple) iter.next();
			Node n = graph.getNode(o.getRow());
			Iterator a = n.outNeighbors();
			boolean hasPattern = false;
			while (a.hasNext() && !hasPattern) {
				TableTuple b =(TableTuple) a.next();
				Edge e = graph.getEdge(b.getRow());

				if ( e.getInt("type") == VisualDBConstants.EDGE_TYPE_PATTERN2THREAD) {
					hasPattern = true;
				}
			}
			
			if ( !hasPattern ) {
				list.add(new Discussion(n));
				count++;
			}
		}
		return list.iterator();
	}

	public static Node getNodeById(Graph graph, int id) {
		Node n = null;
		
		Iterator<?> iter = graph.getNodes().tuples((Predicate) ExpressionParser.parse("id = " + id ));
		
		if ( iter.hasNext() ) {			
			Tuple ts = (Tuple)iter.next();
			n = graph.getNode(ts.getRow());
		}

		return n;
	}	
}
