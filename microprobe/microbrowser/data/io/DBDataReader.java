package microbrowser.data.io;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import microbrowser.data.model.Answer;



public class DBDataReader {
	private static Logger logger = Logger.getLogger(DBDataReader.class.getName());
	
	public static ArrayList<Answer> getAnswers(int threadid, int answerid) {
		ArrayList<Answer> answers = new ArrayList<Answer>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//String sql = "SELECT * FROM VISUAL.THREADS T WHERE POSTTYPEID = 2 AND PARENTID = ? FOR READ ONLY";
		String sql = "SELECT * FROM VISUAL_THREADS T WHERE POSTTYPEID = 2 AND PARENTID = ?";
		Answer answer = null;

		try {
			conn = VDBConnector.getMySqlConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, threadid);
			rs = pstmt.executeQuery();

			while ( rs.next()) {
				answer = new Answer();
				answer.setOwner(rs.getString("OWNERDISPLAYNAME"));
				answer.setOwnerId(rs.getString("OWNERUSERID"));
				answer.setScore(Integer.parseInt(rs.getString("SCORE")));
				answer.setId(rs.getInt("ID"));
				answer.setAccepted(answer.getId() == answerid);				
				answer.setBody(rs.getString("BODY"));
				answer.setCreateDate(rs.getString("CREATIONDATE"));
				answers.add(answer);
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
		
		logger.info("Found " + answers.size() + " answers for threadid=" + threadid);
		return answers;
	}
}
