package microbrowser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import mdsj.MDSJ;
import microbrowser.data.io.VDBConnector;

public class MDSLocator {
	
	public static void main(String[] args) {
		MDSLocator ml = new MDSLocator();
		ml.calcLocations();
	}
	public void calcLocations() {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {		
			//jsonStr = obj.serialize(true);
			
			Hashtable<String,Integer> idMap = new Hashtable<String,Integer>();
			Hashtable<Integer,String> reverseIDMap = new Hashtable<Integer,String>();
			conn = VDBConnector.getConnection();
			pstmt = conn.prepareStatement("SELECT ID FROM VISUAL.THREADS");
			
			rs = pstmt.executeQuery();
			int index = 0;
			while(rs.next()) {
				String id = rs.getString("ID");
				idMap.put(id, index);
				reverseIDMap.put(index, id);
				index++;
			}
			
			double[][] simMatrix = new double[index+1][index+1];
			pstmt = conn.prepareStatement("SELECT THREADID,THREADID2,SIMILARITY FROM VISUAL.SIMILARITIES" );								
		

			rs = pstmt.executeQuery();
		
			while ( rs.next()) {
				String threadid = rs.getString("THREADID");
				//obj.put("body", rs.getString(8));
				String threadid2 =  rs.getString("THREADID2");
				double similarity = rs.getDouble("SIMILARITY");
				if ( threadid != null && idMap.get(threadid) !=  null && threadid2 != null && idMap.get(threadid2) != null) {
					int i = idMap.get(threadid);
					int j = idMap.get(threadid2);
					simMatrix[i][j] = similarity;
				}
			}
			
			double[][] output=MDSJ.classicalScaling(simMatrix);
			
			pstmt = conn.prepareStatement("UPDATE VISUAL.SVD_THREADS SET X = ?, Y = ? WHERE THREADID = ?");
			for(int i = 0;i<=index;i++) {
				String id = reverseIDMap.get(i);
				pstmt.setDouble(1, output[0][i]);
				pstmt.setDouble(2, output[1][i]);
				pstmt.setString(3,id);
				pstmt.executeUpdate();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if ( rs != null )
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if ( pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if ( conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
}
