package microbrowser.data.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;

public class TextDataExporter {

	private static final String JAVA_TAG = "java>";
	private static final boolean TAGS_ONLY = false;
	
	public TextDataExporter() {

	}

	public static void main(String[] args) {
		TextDataExporter dataExporter = new TextDataExporter();
		dataExporter.run();
	}

	private void run() {

		Connection conn = null;
		PreparedStatement pstmt = null, pstmt_a = null;
		ResultSet rs = null, rs_a = null;
		String sql = "SELECT ID, TAGS, TITLE, BODY FROM VISUAL.THREADS WHERE POSTTYPEID = 1 ORDER BY ID DESC FOR READ ONLY";
		String answers_sql = "SELECT BODY FROM VISUAL.THREADS WHERE POSTTYPEID = 2 AND PARENTID = ? FOR READ ONLY";
		
		try {

			conn = VDBConnector.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt_a = conn.prepareStatement(answers_sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				StringBuffer content = new StringBuffer();

				// save main information
				int id = rs.getInt("ID");
				
				if ( TAGS_ONLY ) {
					content.append(processTags(rs.getString("TAGS")));
				} else {
					
					content.append(rs.getString("title")).append(" ");
					//content.append(Jsoup.parse(rs.getString("body")).text()).append(" ");
					
					// Save the tags
					String tags = processTags(rs.getString("TAGS"));
					content.append(tags).append(" ");
	
					// retrieve answers
					pstmt_a.setInt(1, id);
					rs_a = pstmt_a.executeQuery();
					
					while ( rs_a.next() ) {
						content.append(rs_a.getString("BODY")).append(" ");
					}

				}

				// export records
				exportRecord(id,Jsoup.parse(content.toString()).text());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void exportRecord(int threadid, String content) {
		FileOutputStream fop = null;
		File file;

		try {

			file = new File(
					//"C:/projects/proposal/workspace.microbrowse.0.1/MicroBrowserData/text/"
					"setup/text/"+ threadid + ".txt");
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
	
	/**
	 * The function will receive as input a set of <tags> and will
	 * proceed to convert them into tag1 tag2
	 * @param string
	 * @return
	 */
	private static String processTags(String string) {
		StringBuffer tags = new StringBuffer();
		StringTokenizer st = new StringTokenizer(string, "<");
		while ( st.hasMoreTokens()) {
			String tag = st.nextToken();
			System.out.println(tag);
			if ( !tag.equalsIgnoreCase(JAVA_TAG))
				tags.append( tag.substring(0, tag.length()-1)).append(" ");
			else
				System.out.println("found tag");
		}
		System.out.println(tags);
		return tags.toString();
	}
}
