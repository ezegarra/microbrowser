package microbrowser.data.io;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import mdsj.MDSJ;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.distance.CosineDistance;
import net.sf.javaml.distance.CosineSimilarity;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

public class SimilarityGeneratorByTopic {
	
	//Map<String, ArrayList<String>> topics = new HashMap<String, ArrayList<String>>();
	final int QUESTION_COUNT = 500;
	ArrayList<String> topics[] = new ArrayList[QUESTION_COUNT];
	double dissim_matrix [][] = new double[QUESTION_COUNT][QUESTION_COUNT];
	String question_ids[] = new String[QUESTION_COUNT];
	boolean SKIP_DB = false;
	
	public SimilarityGeneratorByTopic() {
		try {
			File file = new File(
					"C:/projects/proposal/workspace.microbrowse.0.1/MicroBrowserData/mallet/tutorial_composition.txt");

			LineIterator iterator = FileUtils.lineIterator(file, "UTF-8");
			StringTokenizer tokenizer;
			int linecount = 0;

			
			try {

				while (iterator.hasNext()) {
					String line = iterator.nextLine();
					
					if ( linecount != 0 ) {
						tokenizer = new StringTokenizer(line);
						
						String docnum = tokenizer.nextToken();
						String filename = tokenizer.nextToken();
						ArrayList<String>t = new ArrayList<String>();
						
						while ( tokenizer.hasMoreTokens()) {

							String topic = tokenizer.nextToken();
							double percentage = Double.parseDouble(tokenizer.nextToken());

							// skip percentage that are less than 20%
							if ( percentage > 0.09 ) {
								t.add(topic); // add topic
							}
						} 

						// record question id
						question_ids[linecount-1] = getQuestionId(filename);

						//topics.put(filename, t);
						topics[Integer.parseInt(docnum)] = t;

						// report topics per record
						System.out.println("discussion=" + Integer.parseInt(docnum) + ", id = " + question_ids[linecount-1] + " has " + t.size() + " topics: " + Arrays.toString(t.toArray(new String[t.size()])));
					}
					
					linecount++;
				}

			} finally {
				LineIterator.closeQuietly(iterator);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SimilarityGeneratorByTopic similarityGeneratorByTopic = new SimilarityGeneratorByTopic();
		similarityGeneratorByTopic.computeSimilarities();
		System.out.println("Dissimilarity=" + similarityGeneratorByTopic.dissim_matrix[0][0]);
		System.out.println("Dissimilarity=" + similarityGeneratorByTopic.dissim_matrix[123][12]);
		System.out.println("Dissimilarity=" + similarityGeneratorByTopic.dissim_matrix[15][312]);
	}

	private void computeSimilarities() {
		long timein = System.currentTimeMillis();
		
		double similarity;
		int i = 0, j = 0;
		int nosimilaritycount = 0;
		int count_topics_i = 0;
		
		for ( i = 0; i < topics.length; i++) {			
			for ( j = 0; j < topics.length; j++) {

				similarity = 0;
				count_topics_i = 0;
				Iterator<String> topics_i = topics[i].iterator();
				ArrayList<String> topics_j = topics[j];

				while ( topics_i.hasNext()) {
					if ( topics_j.contains(topics_i.next())) {
						similarity++;
					}
					
					count_topics_i++;
				}
				//dissim_matrix[i][j] = 100 - (100 * similarity)/count_topics_i; // compute how dissimilar two documents are
				dissim_matrix[i][j] = 1 - ( similarity/count_topics_i);
				//dissim_matrix[i][j] = count_topics_i - similarity;
				//dissim_matrix[i][j] = similarity;
				if ( similarity == 0) {
					nosimilaritycount++;
				}
			}
			
			//i++;
		}
		
		System.out.println("Found " + nosimilaritycount + " items that were different");
		System.out.println(Arrays.toString(dissim_matrix));
		
		if ( !SKIP_DB ) {
			
		/*
		 * SAVE SIMILARITIES
		 */
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		String delete_sql = "DELETE FROM VISUAL.SIMILARITIES WHERE 1=1";
		String insert_sql = "INSERT INTO VISUAL.SIMILARITIES VALUES( ?, ?, ?)";

		try {
			// empty the table
			conn = VDBConnector.getConnection();
			pstmt = conn.prepareStatement(delete_sql);
			
			System.out.println("Begin deleting records in similarity table.");
			timein = System.currentTimeMillis();
			pstmt.executeUpdate();
			System.out.println("End deleting records in similarity table." + getTime(timein));


			System.out.println("done computing similarities.. start MDS");
			timein = System.currentTimeMillis();
			double[][] output=MDSJ.classicalScaling(dissim_matrix);
			System.out.println(Arrays.toString(output));
			
			CosineSimilarity cosineSimilarity = new CosineSimilarity();
			//CosineDistance cosineSimilarity = new CosineDistance();
			
			// repopulate the table
			timein = System.currentTimeMillis();
			System.out.println("Begin inserting records into similarities table based on MDS calculation");
			pstmt = conn.prepareStatement(insert_sql);
				
			for ( i = 0; i < QUESTION_COUNT; i++ ) {
				for ( j = 0; j < QUESTION_COUNT; j++ ) {
					double vector_i[] = {output[0][i], output[1][i]};
					double vector_j[] = {output[0][j], output[1][j]};
					//double similarity_2 = cosineSimilarity.measure(new DenseInstance(vector_i), new DenseInstance(vector_j));
					double similarity_2 = 1 - dissim_matrix[i][j];
			
					try {
	
						pstmt.setInt(1, Integer.parseInt(question_ids[i]));
						pstmt.setInt(2, Integer.parseInt(question_ids[j]));
						//pstmt.setDouble(3, dissim_matrix[i][j]);
						pstmt.setDouble(3, similarity_2);
						pstmt.executeUpdate();						
					} catch ( SQLDataException e) {
						System.out.println("similarity2 " + similarity_2 + ", " + i + "=" + Arrays.toString(vector_i) + ", " + j + "=" + Arrays.toString(vector_j));
						System.exit(1);
					}
				}
			}
			System.out.println("End inserting records into similarities table. "  + getTime(timein));
			
			System.out.println("done computing MDS.  Start saving MDS Coordinates to Database."  + getTime(timein));
			timein = System.currentTimeMillis();
			// SAVE THE MDS LOCATIONS
			pstmt = conn.prepareStatement("DELETE FROM VISUAL.SVD_THREADS WHERE 1=1");
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement("INSERT INTO VISUAL.SVD_THREADS VALUES( ?, ? ,?)");

			
			for( i = 0; i< QUESTION_COUNT; i++ ) {
					
				int id = Integer.parseInt(question_ids[i]);
				pstmt.setInt(1, id);			
				pstmt.setDouble(2, output[0][i]);
				pstmt.setDouble(3, output[1][i]);
				pstmt.executeUpdate();
			}
			System.out.println("done saving MDS " + getTime(timein));
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

		
	}
	
	private String getTime(long timein) {
		long time = System.currentTimeMillis()-timein;
		return " [" + (time/1000) + "." + (time%1000) + " secs]";
	}
	// file:/C:/projects/proposal/workspace.microbrowse.0.1/MicroBrowserData/text/10042.txt
	// 10042
	private String getQuestionId(String input) {
		return input.substring(input.lastIndexOf("/")+1, input.lastIndexOf("."));
	}
}
