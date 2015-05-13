package microbrowser.data.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Logger;

import microbrowser.VisualDBConstants;
import microbrowser.util.DateLib;
import microbrowser.util.PredicatesUtil;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Tuple;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.io.DataIOException;
import prefuse.data.tuple.TableTuple;
import prefuse.data.util.Sort;
import prefuse.util.ArrayLib;
import prefuse.visual.VisualItem;

public class GraphMLDataReader {
	private static final String SOURCE_CLASS = GraphMLDataReader.class.getName();
	private static Logger logger = Logger.getLogger(SOURCE_CLASS);
	
	private static Graph m_graph_source = null;
	static {
		try {
			m_graph_source = new prefuse.data.io.GraphMLReader().readGraph(VisualDBConstants.GRAPH_FILE_NAME_COMPLETE);
			
			System.out.println("There are " + m_graph_source.getNodeCount() + " nodes.");
			System.out.println("there are " + m_graph_source.getEdgeCount() + " edges.");
			
		} catch (DataIOException e) {
			e.printStackTrace();
		}		
	}
	public GraphMLDataReader() {
	}
	
	public GraphMLDataReader(String datafile) {
		try {
			m_graph_source = new prefuse.data.io.GraphMLReader().readGraph(datafile);
			
			System.out.println("There are " + m_graph_source.getNodeCount() + " nodes.");
			System.out.println("there are " + m_graph_source.getEdgeCount() + " edges.");
			
		} catch (DataIOException e) {
			e.printStackTrace();
		}
	}
	
	public static Graph getSourceGraph() {
		return m_graph_source;
	}
	
	/**
	 * Creates a new groups by loading up to the specified number of nodes
	 * 
	 * @param level
	 * @return
	 */
	public Graph getGraph(int level, boolean loadPatterns) {
		// create the empty graph
		Graph m_graph = new Graph();
		m_graph.getNodeTable().addColumns(GraphMLGenerator.NODE_SCHEMA);
		m_graph.getEdgeTable().addColumns(GraphMLGenerator.EDGE_SCHEMA);
		
		// load the nodes
		Iterator<TableTuple> thread_iterator = m_graph_source.getNodes().tuples((Predicate)ExpressionParser.parse("type = " + VisualDBConstants.NODE_TYPE_DISCUSSION, true));
		
		for ( int i = 0; i < level && thread_iterator.hasNext(); i++ ) {
			microbrowser.data.io.GraphMLGenerator.addNode(m_graph,thread_iterator.next());
		}
		
		// load the patterns
		if ( loadPatterns ) {
			Iterator<TableTuple> pattern_iterator = m_graph_source.getNodes().tuples((Predicate)ExpressionParser.parse("type = " + VisualDBConstants.NODE_TYPE_PATTERN, true));
			
			while( pattern_iterator.hasNext() ) {
				microbrowser.data.io.GraphMLGenerator.addNode(m_graph, pattern_iterator.next());
			}			
		}
		
		// load the edges
        for (Iterator all_nodes = m_graph.nodes(); all_nodes.hasNext();) {
        	TableTuple n1			= (TableTuple) all_nodes.next();
            Iterator related_items 	= this.getRelatedItemIds(m_graph_source, n1.getInt("id"));

    		while (  related_items.hasNext() ) {
    			int relatedThreadId = (Integer)related_items.next();

				// it is possible that a related ID is not visible currently in the graph
				// in this case, the iterator should return null
				Iterator<?>iter2 = m_graph.getNodes().tuples((Predicate) ExpressionParser.parse("id = " + relatedThreadId ));

				while ( iter2.hasNext() ) {
					TableTuple t2 = (TableTuple) iter2.next();
					Node n2 = m_graph.getNode(t2.getRow());
					double similarity = getSimilarity(n1.getString("id"), n2.getString("id"));
					
					if ( similarity > 0) {
						Edge e = m_graph.getEdge(m_graph.addEdge(n1.getRow(), n2.getRow()));
						e.setDouble("similarity", getSimilarity(n1.getString("id"), n2.getString("id")));						
					}
				}
    		}
        }
        
		logger.info("There are " + m_graph.getNodeCount() + " nodes out of " + m_graph_source.getNodeCount());
		logger.info("there are " + m_graph.getEdgeCount() + " edges out of " + m_graph_source.getEdgeCount());

		return m_graph;
	}
	
	/**
	 * Creates a new groups by loading up to the specified number of nodes
	 * 
	 * @param level
	 * @return
	 */
	public Graph getGraph(int level, boolean loadPatterns, int lowdate, int highdate) {
		// create the empty graph
		Graph m_graph = new Graph();
		m_graph.getNodeTable().addColumns(GraphMLGenerator.NODE_SCHEMA);
		m_graph.getEdgeTable().addColumns(GraphMLGenerator.EDGE_SCHEMA);
		
		// load the nodes
		Iterator<TableTuple> thread_iterator = m_graph_source.getNodes().tuples(PredicatesUtil.TYPE_DISCUSSION);
		
		long lowval 	= DateLib.getDateAsLong(lowdate);
		long highval 	= DateLib.getDateAsLong(highdate);

		for ( int i = 0; i < level && thread_iterator.hasNext(); i++ ) {
			TableTuple n = thread_iterator.next();
			long currval 	= DateLib.removeTime(n.getLong("lastactivitydate"));
			
			if ( lowval <= currval && currval <= highval) {
				microbrowser.data.io.GraphMLGenerator.addNode(m_graph,n);				
			} else {
				i--;
			}
		}
		
		// load the patterns
		if ( loadPatterns ) {
			Iterator<TableTuple> pattern_iterator = m_graph_source.getNodes().tuples((Predicate)ExpressionParser.parse("type = " + VisualDBConstants.NODE_TYPE_PATTERN, true));
			
			while( pattern_iterator.hasNext() ) {
				microbrowser.data.io.GraphMLGenerator.addNode(m_graph, pattern_iterator.next());
			}			
		}
		
		// load the edges
        for (Iterator all_nodes = m_graph.nodes(); all_nodes.hasNext();) {
        	TableTuple n1			= (TableTuple) all_nodes.next();
            Iterator related_items 	= this.getRelatedItemIds(m_graph_source, n1.getInt("id"));

    		while (  related_items.hasNext() ) {
    			int relatedThreadId = (Integer)related_items.next();

				// it is possible that a related ID is not visible currently in the graph
				// in this case, the iterator should return null
				Iterator<?>iter2 = m_graph.getNodes().tuples((Predicate) ExpressionParser.parse("id = " + relatedThreadId ));

				while ( iter2.hasNext() ) {
					TableTuple t2 = (TableTuple) iter2.next();
					Node n2 = m_graph.getNode(t2.getRow());
					double similarity = getSimilarity(n1.getString("id"), n2.getString("id"));
					
					if ( similarity > 0) {
						Edge e = m_graph.getEdge(m_graph.addEdge(n1.getRow(), n2.getRow()));
						e.setDouble("similarity", getSimilarity(n1.getString("id"), n2.getString("id")));						
					}
				}
    		}
        }
        
        logger.info("There are " + m_graph.getNodeCount() + " nodes out of " + m_graph_source.getNodeCount());
        logger.info("there are " + m_graph.getEdgeCount() + " edges out of " + m_graph_source.getEdgeCount());

		return m_graph;
	}
	@SuppressWarnings("unchecked")
	public static Iterator<Edge> getEdgesToRelatedItem(Graph g, VisualItem item ) {
		Node n = g.getNode(item.getRow());
		ArrayList<Edge> edge_list = new ArrayList<Edge>();
		Iterator<Edge> edges = null;

		// first process incoming references
		edges = n.inEdges();
		while ( edges.hasNext()) {
			Edge e = edges.next();
			edge_list.add(e);
		}

		// now process outgoing references
		edges = n.outEdges();
		while ( edges.hasNext()) {
			Edge e = edges.next();
			if ( !edge_list.contains(e)) {						
				edge_list.add(e);
			}
		}
		
		logger.info("getEdgesToRelatedItem::Found " + edge_list.size() + " edges for threadId " + item.getString("id"));
		
		return edge_list.iterator();
	}
	
	public Iterator<Integer> getRelatedItemIds(int threadId) {
		return getRelatedItemIds(m_graph_source, threadId);
	}
	
	@SuppressWarnings("unchecked")
	public static Iterator<Integer> getRelatedItemIds(Graph graph, int threadId) {
        // clock in
        long timein = System.currentTimeMillis();

		Iterator<Tuple> iter = graph.getNodes().tuples((Predicate)ExpressionParser.parse("id = " + threadId, true));
		ArrayList<Integer> node_list = new ArrayList<Integer>();
		ArrayList<Integer> similarity_scores = new ArrayList<Integer>();
		
		while ( iter.hasNext()) {
			Node n = graph.getNode(iter.next().getRow());
			
			if ( n.get("id").equals( threadId ) ) {
				Iterator<Edge> edges = null;
				
				// first process incoming references
				edges = n.inEdges();
				n.edges();
				while ( edges.hasNext()) {
					Edge e = edges.next();
					if ( !node_list.contains((Integer)e.getSourceNode().get("id"))) {						
						node_list.add((Integer) e.getSourceNode().get("id"));
						int similarity = (int) Math.round(e.getDouble("similarity") * 100);
						similarity_scores.add(new Integer(similarity));
					}
				}
				
				// now process outgoing references
				edges = n.outEdges();
				while ( edges.hasNext()) {
					Edge e = edges.next();
					if ( !node_list.contains((Integer)e.getTargetNode().get("id"))) {						
						node_list.add((Integer) e.getTargetNode().get("id"));
						int similarity = (int) Math.round(e.getDouble("similarity") * 100);
						similarity_scores.add(new Integer(similarity));
					}
				}
				break;
				
			}
		}

		// now sort the elements
		int[] values = new int[similarity_scores.size()];
		Iterator<Integer> iter2 = similarity_scores.iterator();
		int index = 0;
		while ( iter2.hasNext()) {
			values[index] = ((Integer)iter2.next()).intValue();
			index++;
		}
        // now sort
		Integer[] items = (Integer[]) node_list.toArray(new Integer[node_list.size()]);
        ArrayLib.sort(values, items, 0, values.length);
        
		// clock out
        long time = System.currentTimeMillis()-timein;

        logger.info("getRelatedItemIds: Found " + node_list.size() + " related items for threadId " + threadId  + " in " + (time/1000) + "." + (time%1000) + " seconds.");
		
		return Arrays.asList(items).iterator();
	}
	
	/*
	 * Returns the similarity of the given source and target which
	 * is equivalent to the similarity value of the edge between
	 * the two nodes
	 */
	public double getSimilarity(String sourceId, String targetId) {
		double similarity = 0;
		
		// lets find the edge based on the 'id's provided
		@SuppressWarnings("unchecked")
		Iterator<Edge> iter = m_graph_source.edges();
		
		while ( iter.hasNext()) {
			Edge e = iter.next();
			if ( sourceId.equals(e.getSourceNode().getString("id")) && targetId.equals(e.getTargetNode().getString("id")) ) {
				similarity = e.getDouble("similarity");
				break;
			}
		}
		
		return similarity;
	}
	
	/** The method extracts tuples of type Threads and sorts the results by answercount
	 *  it returns up to count elements
	 *  
	 * @param count
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TableTuple> getTuplesSortedByAnswercount(int count) {
		ArrayList<TableTuple> l = new ArrayList<TableTuple>();
		Iterator<TableTuple> iter = this.getSourceGraph().getNodes().tuples((Predicate)ExpressionParser.parse("type = " + VisualDBConstants.NODE_TYPE_DISCUSSION), new Sort(new String[]{"answercount"}, new boolean[]{false}));
		
		int i = 0;
		while ( iter.hasNext() && i < count) {
			TableTuple t = iter.next();
			l.add(t);
			i++;
		}
		return l;
	}
	
	public static Node getNodeById(Graph graph, int id) {
		logger.info("id=" + id);
		Node n = null;
		
		Iterator<?> iter = graph.getNodes().tuples((Predicate) ExpressionParser.parse("id = " + id ));
		
		if ( iter.hasNext() ) {			
			Tuple ts = (Tuple)iter.next();
			n = graph.getNode(ts.getRow());
		}
		return n;
	}
	
	public static Node getNodeByField(Graph graph, String field, String value) {
		Node n = null;
		
		Iterator<?> iter = graph.getNodes().tuples((Predicate) ExpressionParser.parse(field + " = '" + value + "'"));
		
		if ( iter.hasNext() ) {			
			Tuple ts = (Tuple)iter.next();
			n = graph.getNode(ts.getRow());
		}

		return n;
	}
	
	public static Tuple getTupleById(Graph graph, Integer id) {
		Tuple t = null;
		
		Iterator<?> iter = graph.getNodes().tuples((Predicate) ExpressionParser.parse("id = " + id.intValue() ));
		
		if ( iter.hasNext() ) {			
			t = (Tuple)iter.next();
		}

		return t;
	}
	
	public static Iterator<?> getPatterns(Graph graph) {
		Iterator<?> iter = graph.getNodes().tuples((Predicate) ExpressionParser.parse("type = " + VisualDBConstants.NODE_TYPE_PATTERN ));
		
		return iter;
	}
	
	public static Node getPatternForNode(Graph graph, Node node) {
		
		Node pattern = null;
		
		Iterator <?>edges = node.edges();
	
		while ( edges.hasNext() ) {
			Edge e = (Edge)edges.next();
			
			if ( e.getSourceNode() == node ) {
				if ( e.getTargetNode().getInt("type") == VisualDBConstants.NODE_TYPE_PATTERN) {
					pattern = e.getTargetNode();
					break;
				}
			} else if ( e.getTargetNode() == node ) {
				if ( e.getSourceNode().getInt("type") == VisualDBConstants.NODE_TYPE_PATTERN) {
					pattern = e.getSourceNode();
					break;
				}
			}
		}
		
		return pattern;
	}

	public static void main(String[] args) {
    	GraphMLDataReader reader = new GraphMLDataReader(GraphMLGenerator.dataFileIn);
    	
    	reader.getTuplesSortedByAnswercount(10);

    	//Iterator<?> iter = reader.getRelatedItemIds("10801111");
    	
    	logger.info("similarity is " + reader.getSimilarity("40028","66423"));

	}

}
