package microbrowser.data.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import mdsj.MDSJ;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.distance.CosineDistance;


/*
 * Compute the similarity values of all the items in the database
 * 
 */
public class SimilarityGenerator {
	Set<String>[] 	tags_by_question 	= null;
	Set<String>		all_tags			= new TreeSet<String>();
	double[][] 		sim_matrix 			= null;
	double[][]		dsim_matrix			= null;
	int 			questionCnt			= 0;
	int[]			questionIds 		= null;
	Dataset			sim_dataset			= new DefaultDataset();
	
	private final String JAVA_TAG = "java";
	
	private void getAllTags() {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String	sql = "SELECT ID, TAGS, TITLE, BODY FROM VISUAL.THREADS WHERE POSTTYPEID = 1 ORDER BY ID DESC";
		try {

			conn = VDBConnector.getConnection();
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			int index = 0;
			while ( rs.next() ) {
				// Save the Ids
				questionIds[index] = rs.getInt("ID");
								
				// Save the tags
				String s = rs.getString("TAGS");
				StringTokenizer t = new StringTokenizer(s, "<");
				
				if ( tags_by_question[index] == null)
					tags_by_question[index] = new TreeSet<String>();
				
				while ( t.hasMoreTokens()) { 
					String token = t.nextToken();
					String tag = token.substring(0, token.lastIndexOf(">"));
					System.out.println("processing " + tag);
					if ( !tag.equalsIgnoreCase(JAVA_TAG)) {
						//tags_by_question[index].add(tag.substring(0, tag.lastIndexOf(">")));	
						tags_by_question[index].add(tag);
						
						// add tag to global list
						//all_tags.add(tag.substring(0, tag.lastIndexOf(">")));	
						all_tags.add(tag);	
					} else {
						System.out.println("Skipping " + tag);
					}
				}

				index++;
				
				// export records
				exportRecord(rs.getInt("ID"), rs.getString("title") + " " + rs.getString("body") + " " + rs.getString("TAGS"));
			}
			
			System.out.println("Returning a total of " + all_tags.size() + " tags.");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void init() {
		
		// compute how many questions there are
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String	sql = "SELECT COUNT(ID) Q_COUNT FROM VISUAL.THREADS WHERE POSTTYPEID = 1";
		try {

			conn = VDBConnector.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while ( rs.next() ) {
				questionCnt 		= rs.getInt("Q_COUNT");
				sim_matrix 			= new double[questionCnt][questionCnt];
				dsim_matrix			= new double[questionCnt][questionCnt];
				questionIds 		= new int[questionCnt];
				tags_by_question	= new TreeSet[questionCnt];
				System.out.println("found " + questionCnt + " questions");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public static void main(String [] args) {
		SimilarityGenerator generator = new SimilarityGenerator();
		generator.computeSimilarities();
	}

	private void computeSimilarities() {
		long timein = System.currentTimeMillis();
		
		this.init();
		this.getAllTags();
		
		// create dataset to compute similarities
		for ( int i = 0; i < questionCnt; i ++ ) {
			Iterator<String>all_tags_iterator = all_tags.iterator();
			double[] values = new double[all_tags.size()];
			int tag_idx = 0;
			while (all_tags_iterator.hasNext()) {
				String tag = all_tags_iterator.next();
				values[tag_idx] = (tags_by_question[i].contains(tag)? 1.0 : 0.0);
				tag_idx++;
			}
			
			sim_dataset.add(i, new DenseInstance(values));
		}

		// COMPUTE SIMILARITIES
		CosineDistance similarity = new CosineDistance();
		//EuclideanDistance similarity = new EuclideanDistance();
		for ( int i = 0; i < questionCnt; i++ ) {
			for ( int j = 0; j < questionCnt; j++ ) {
//				if ( i == j && false ) {
//					sim_matrix[i][j] = 0.0;
//				} else {
//					sim_matrix[i][j] = Math.sqrt(all_tags.size() - similarity.measure(sim_dataset.get(i), sim_dataset.get(j)));					
//				}
				
				int cnt = 0;
				if ( i == j) {
					sim_matrix[i][j] = 0.0;					
				} else {
					Iterator it = tags_by_question[i].iterator();
					cnt = 0;
					while ( it.hasNext()) {
						String tag = (String) it.next();
						if ( tags_by_question[j].contains(tag)) {
							cnt ++;
						}
					}
					//sim_matrix[i][j] = all_tags.size() - cnt;
					if ( cnt == 0) {
						sim_matrix[i][j] = 0.0;
					} else if ( cnt == tags_by_question[i].size()) {
						sim_matrix[i][j] = 1.0;
					}else {
						sim_matrix[i][j] =  (double)cnt / tags_by_question[i].size();						
					}
				}
				
				//System.out.println("Computing simlarity of " + sim_dataset.get(i) + ", " + sim_dataset.get(j) + ", " + sim_matrix[i][j]);
				System.out.println("Computing simlarity of " + i + ", " + j + ", " + sim_matrix[i][j]);
				if ( Double.isNaN(sim_matrix[i][j])) {
					//System.out.println(sim_dataset.get(i) + ", " + sim_dataset.get(j-1) + ", " + sim_matrix[i][j-1]);
					System.out.println(sim_dataset.get(i) + ", " + sim_dataset.get(j) + ", " + sim_matrix[i][j]);
					System.out.println(tags_by_question[i] + ", " + tags_by_question[j] + ", cnt=" + cnt + ", length=" +tags_by_question[i].size()+ ", sim=" + sim_matrix[i][j]);
					System.exit(1);
					sim_matrix[i][j] = 0;
				}
			}
		}
		int indexA = 29;//5;
		int indexB = 194;//3;
		System.out.println("Found "+ tags_by_question[indexA].size() + " tags:: " + tags_by_question[indexA]);
		System.out.println("Found "+ tags_by_question[indexB].size() + " tags:: " + tags_by_question[indexB]);
		System.out.println("Similar tags " + sim_matrix[indexA][indexB] + getTime(timein));

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
			
			// repopulate the table
			timein = System.currentTimeMillis();
			System.out.println("Begin inserting records into similarities table");
			pstmt = conn.prepareStatement(insert_sql);
			
			for ( int i = 0; i < questionCnt; i++ ) {
				for ( int j = 0; j < questionCnt; j++ ) {
					pstmt.setInt(1, questionIds[i]);
					pstmt.setInt(2, questionIds[j]);
					pstmt.setDouble(3, sim_matrix[i][j]);
					pstmt.executeUpdate();						
				}
			}
			System.out.println("End inserting records into similarities table. "  + getTime(timein));
			
			System.out.println("done computing similarities.. start MDS");
			timein = System.currentTimeMillis();
			double[][] output=MDSJ.classicalScaling(sim_matrix);

			double max_x = Double.MIN_VALUE, max_y = Double.MIN_VALUE, min_x = Double.MAX_VALUE, min_y = Double.MAX_VALUE;
			
			for ( int i = 0; i < output.length; i++) {
					double x = output[0][i];
					double y = output[1][i];
					
					if ( Double.isNaN(x)) { x = 0.0; }
					if ( Double.isNaN(y)) { y = 0.0; }
					
					if ( max_x < x) {
						max_x = x;
					}
					
					if ( min_x > x) {
						min_x = x;
					}

					if ( max_y < y) {
						max_y = y;
					}
					
					if ( min_y > y) {
						min_y = y;
					}
					
					if ( max_x < min_x) {
						System.out.println("i=" + i + ", max_x < min_x");
						System.exit(-1);
					}
					
					if ( max_y < min_y) {
						System.out.println("i=" + i + ", max_y < min_y " + ", y=" + y + ", max_y=" + max_y + ", min_y=" + min_y);
						System.exit(-2);
					}
			}
			System.out.println("max_x=" + max_x + ", min_x=" + min_x + ", max_y=" + max_y + ", min_y=" + min_y);
			
			System.out.println("done computing MDS.  Start saving MDS Coordinates to Database."  + getTime(timein));
			timein = System.currentTimeMillis();
			// SAVE THE MDS LOCATIONS
			pstmt = conn.prepareStatement("DELETE FROM VISUAL.SVD_THREADS WHERE 1=1");
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement("INSERT INTO VISUAL.SVD_THREADS VALUES( ?, ? ,?)");

			
			for( int i = 0; i< questionCnt; i++ ) {
					
				int id = questionIds[i];
				pstmt.setInt(1, id);
				if ( Double.isNaN(output[0][i])) {
					output[0][i] = 0;
					System.out.println("index 0=" + i + " is null");
				}
				if ( Double.isNaN(output[1][i])) {
					System.out.println("index 1= " + i + " is " + output[1][i]);
					output[1][i] = 0;
					System.out.println("index 1= " + i + " is null");
				}

				// perform x-y normalization
				

								//double max_x = 0.7188207078305171;
				//double min_x = -0.16943150902454876;
				//double max_y = 89351.36633659083;
				//double min_y = -378104.94035538344;
				double max_new = 100;
				double min_new = 0;
				
				//double x = output[0][i];
				//double y = output[1][i];
				//double x = ( output[0][i] - min_x) * ((max_new - min_new ) / (max_x - min_x )) + min_new;
				//double y = ( output[1][i] - min_y) * ((max_new - min_new ) / (max_y - min_y )) + min_new;
				double x = ( output[0][i] - min_x ) / ( max_x - min_x );
				double y = ( output[1][i] - min_y ) / ( max_y - min_y );
				
				if ( y < 0 || x < 0) {
					System.out.println("i=" + i + ", x=" + output[0][i] + ", y=" + output[1][i]);
					//System.exit(1);
				}
				 //MAX_X              MIN_X                MAX_Y             MIN_Y
				 //------------------ -------------------- ----------------- -------------------
				 //0.7188207078305171 -0.16943150902454876 89351.36633659083 -378104.94035538344
				
				
				pstmt.setDouble(2, x);
				pstmt.setDouble(3, y);
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
	
	private String getTime(long timein) {
		long time = System.currentTimeMillis()-timein;
		return " [" + (time/1000) + "." + (time%1000) + " secs]";
	}
	
	private void exportRecord(int threadid, String content) {
		FileOutputStream fop = null;
		File file;
		
		try {
			 
			file = new File("C:/projects/proposal/workspace.microbrowse.0.1/MicroBrowserData/text/" + threadid + ".txt");
			fop = new FileOutputStream(file);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
