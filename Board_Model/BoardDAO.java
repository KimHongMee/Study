package board.model;

import java.sql.*;
import java.util.ArrayList;

import javax.naming.*;
import javax.sql.DataSource;



public class BoardDAO {

	DataSource ds ;
	public static final int WRITING_PER_PAGE = 10;
	
	public BoardDAO(){
		try{
			Context initContext = (Context)new InitialContext().lookup("java:comp/env/");
			ds = (DataSource) initContext.lookup("jdbc/mysql");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// ★ [게시판 목록 조회] 기능을 수행
	
	public ArrayList<BoardDTO> boardList(String curPage){
		
		ArrayList<BoardDTO> list = new ArrayList<BoardDTO>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			conn = ds.getConnection();
			
			String sql = "SELECT num, name, password, subject, content,  "
						+"		 write_date, write_time, ref, step, lev, "
					    +"		 read_cnt, child_cnt				     "
						+"FROM BOARD 								     "
					    +"ORDER BY ref desc, step asc					 "
						+"LIMIT ?,?										 ";	
		
			pstmt = conn.prepareStatement(sql);
		  
		  // 현재페이지(curPage)가 1페이지일 경우 10*0 = 0 이 될거임 ,
		  // LIMIT [0,10] [10,10] [20,10] ... 이런식으로 진행되겠지?
		  // 글을 10개씩 보여줄 수 있게 됨 !!
		    pstmt.setInt(1, WRITING_PER_PAGE*(Integer.parseInt(curPage)-1));
			pstmt.setInt(2, WRITING_PER_PAGE);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				
			int    num 		 = rs.getInt("num");
			String name 	 = rs.getString("name");
			String password  = rs.getString("password");
		    String subject   = rs.getString("subject");
		    String content   = rs.getString("content");
		    Date   writeDate = rs.getDate("write_date");
		    Time   writeTime = rs.getTime("write_time");
		    int    ref       = rs.getInt("ref");
		    int    step      = rs.getInt("step");
		    int    lev       = rs.getInt("lev");
		    int    readCnt   = rs.getInt("read_cnt");
		    int    childCnt  = rs.getInt("child_cnt");
		    
			BoardDTO writing = new BoardDTO();
			
			writing.setNum(num);
			writing.setName(name);
			writing.setPassword(password);
			writing.setSubject(subject);
			writing.setContent(content);
			writing.setWriteDate(writeDate);
			writing.setWriteTime(writeTime);
			writing.setRef(ref);
			writing.setStep(step);
			writing.setLev(lev);
			writing.setReadCnt(readCnt);
			writing.setChildCnt(childCnt);
			
			list.add(writing);
			
			} 
		}catch(Exception e){
				e.printStackTrace();
			} finally {
				try{
					if(rs!=null){rs.close();}
					if(pstmt!=null){pstmt.close();}
					if(conn!=null){conn.close();}
				} 
				catch (SQLException e){
					e.printStackTrace();
				}
					  }
			return list;
	}
	
	// ★ [게시판의 페이징 처리]를 수행하기 위한 기능
	
	public int boardPageCnt(){
		
		int pageCnt = 0 ;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = ds.getConnection();

			String sql = "SELECT COUNT(*) AS num FROM BOARD";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				// 위의 sql문을 통해 받아온 num(글 갯수)가 10이라면
				// [10/11]이 되니까 pageCnt의 값이 0일 것이고,
				// num(글 갯수)가 11이라면 [11/11]이 되니까 pageCnt의 값이 1일 것이다
				pageCnt = rs.getInt("num")/WRITING_PER_PAGE+1;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(rs!=null){rs.close();}
				if(pstmt!=null){pstmt.close();}
				if(conn!=null){conn.close();}
				}catch(SQLException e){
					e.printStackTrace();
			}
		}
		return pageCnt;
	}
	
	
	// ★ [게시글을 등록]하는 기능 수행
	
	public void boardWrite(String name, String subject, String content, String password){
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int num = 1;
		
		try{
			conn = ds.getConnection();
			
			String sql = "SELECT IFNULL(MAX(num),0)+1 AS NUM FROM BOARD";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				num = rs.getInt("num");
			}
			
			sql = "INSERT INTO BOARD (num, name, password, subject, content,   "
				+ "write_date, write_time, ref, step, lev, read_cnt, child_cnt)"
				+ "values(?,?,?,?,?,curdate(),curtime(),?,0,0,0,0)             ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			pstmt.setString(2, name);
			pstmt.setString(3, password);
			pstmt.setString(4, subject);
			pstmt.setString(5, content);
			pstmt.setInt(6, num);
			
			pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(rs != null){rs.close();}
				if(pstmt != null){pstmt.close();}
				if(conn != null){conn.close();}
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}	
		
	// ★ [게시글 읽기] 기능을 담당하는 부분
		
		public BoardDTO boardRead(String inputNum){
			
			BoardDTO writing = new BoardDTO();
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try{
				conn = ds.getConnection();
				
				String sql = "UPDATE BOARD                 "
						   + "   SET READ_CNT = READ_CNT+1 "
						   + " WHERE NUM =?                ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(inputNum));
				pstmt.executeUpdate();
				
				
				sql = "SELECT num,name,password,subject,content,    "
					+ "       write_date,write_time,ref,step,lev,   "
					+ "       read_cnt,child_cnt                    "
					+ "  FROM BOARD                                 "
					+ " WHERE NUM=?                                 ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(inputNum));
				rs = pstmt.executeQuery();
				
				
				if(rs.next()){
			
					int    num 		 = rs.getInt("num");
					String name 	 = rs.getString("name");
					String password  = rs.getString("password");
				    String subject   = rs.getString("subject");
				    String content   = rs.getString("content");
				    Date   writeDate = rs.getDate("write_date");
				    Time   writeTime = rs.getTime("write_time");
				    int    ref       = rs.getInt("ref");
				    int    step      = rs.getInt("step");
				    int    lev       = rs.getInt("lev");
				    int    readCnt   = rs.getInt("read_cnt");
				    int    childCnt  = rs.getInt("child_cnt");
				    
					writing.setNum(num);
					writing.setName(name);
					writing.setPassword(password);
					writing.setSubject(subject);
					writing.setContent(content);
					writing.setWriteDate(writeDate);
					writing.setWriteTime(writeTime);
					writing.setRef(ref);
					writing.setStep(step);
					writing.setLev(lev);
					writing.setReadCnt(readCnt);
					writing.setChildCnt(childCnt);
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally{
				try{
					if(rs != null ){rs.close();}
					if(pstmt != null){pstmt.close();}
					if(conn != null){conn.close();}
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
			return writing;	
		}
		
				
		// ★ (글수정)(글삭제) [비밀번호 확인] 기능 수행
		// 이 기능을 수행하면, paaswordOk의 값이 true혹은 false로 정해진다
		// passowordCheck는 SQL문을 사용하기 위해 필요한 인수이다(값은 비번입력창에서 넘어온다)
				public boolean boardPasswordCheck(String inputNum,String inputPassword){
					
					boolean passwordOk = false;
					int passwordCheck = 0;
					
					Connection conn = null;
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					
					try{
						conn = ds.getConnection();
						
						String sql = "SELECT COUNT(*)                 "
								   + "    AS password_check           "
								   + "  FROM BOARD                    "
								   + " WHERE num=? AND password=?     ";
						pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, Integer.parseInt(inputNum));
						pstmt.setString(2, inputPassword);
						rs=pstmt.executeQuery();
					
						if(rs.next()){
							passwordCheck = rs.getInt("password_check");
						}
						
						if(passwordCheck > 0) {
							passwordOk = true;
						}
						
						} catch(Exception e){
							e.printStackTrace();
						} finally{
							try{
								if(rs != null ){rs.close();}
								if(pstmt != null){pstmt.close();}
								if(conn != null){conn.close();}
							}catch(SQLException e){
								e.printStackTrace();
							}
						}
					return passwordOk ;
					
				}
				
		//★(글수정) 글 수정을 위해 원 글을 로드하는 기능
				
		public BoardDTO boardUpdateForm(String inputNum){

			
		BoardDTO writing = new BoardDTO();	
			
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		try{
			
			conn = ds.getConnection();
			
		String sql = "SELECT num, name, password, subject, content,   "
				   + "       write_date, write_time, ref, step, lev,  "
				   + "       read_cnt, child_cnt                      "
				   + "  FROM BOARD                                    "
				   + " WHERE num=?                                    ";
		
		pstmt=conn.prepareStatement(sql);
		pstmt.setInt(1, Integer.parseInt(inputNum));
		rs = pstmt.executeQuery();
		
		if(rs.next()){
			
			int    num 		 = rs.getInt("num");
			String name 	 = rs.getString("name");
			String password  = rs.getString("password");
		    String subject   = rs.getString("subject");
		    String content   = rs.getString("content");
		    Date   writeDate = rs.getDate("write_date");
		    Time   writeTime = rs.getTime("write_time");
		    int    ref       = rs.getInt("ref");
		    int    step      = rs.getInt("step");
		    int    lev       = rs.getInt("lev");
		    int    readCnt   = rs.getInt("read_cnt");
		    int    childCnt  = rs.getInt("child_cnt");
		    
			writing.setNum(num);
			writing.setName(name);
			writing.setPassword(password);
			writing.setSubject(subject);
			writing.setContent(content);
			writing.setWriteDate(writeDate);
			writing.setWriteTime(writeTime);
			writing.setRef(ref);
			writing.setStep(step);
			writing.setLev(lev);
			writing.setReadCnt(readCnt);
			writing.setChildCnt(childCnt);
			
		}
			
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			try{
				if (rs != null){rs.close();}
				if (pstmt != null){pstmt.close();}
				if (conn != null){conn.close();}
				} catch(Exception e){
					e.printStackTrace();
			}
		}
		
		return writing;
		
		
			
		}

		
		// ★(글 수정) 게시글을 수정하는 본 기능
		
		public void boardUpdate(String inputNum, String inputSubject,
				String inputContent, String inputName, String inputPassword){
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			
			
			try{
				
			conn = ds.getConnection();
			
			String sql = "UPDATE BOARD 					                   "
					   + "   SET subject=?, content=?, name=?, password=?  "
					   + " WHERE num=?                                     ";
			
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, inputSubject);
			pstmt.setString(2, inputContent);
			pstmt.setString(3, inputName);
			pstmt.setString(4, inputPassword);
			pstmt.setInt(5, Integer.parseInt(inputNum));
			
			pstmt.executeUpdate();
			
			}catch (Exception e){
				e.printStackTrace();
			} finally {
				try{
					if (pstmt != null){pstmt.close();}
					if (conn != null){conn.close();}
					} catch(Exception e){
						e.printStackTrace();
				}
			}
		
		}
		
	// ★(글삭제) 삭제 대상인 게시글에 답글이 있는지 검사합니다.
	//         replyCnt 는 SQL문 결과(리플이 몇개 달려있는지)가 저장된 변수이며
	//         이 replyCnt의 결과를 통해 replyCheck의 값이 결정되어 return됩니다.	
		public boolean boardReplyCheck(String inputNum){
			boolean replyCheck = false;
			int replyCnt = 0;
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try{
				
				conn = ds.getConnection();
				
			String sql = "SELECT child_cnt       "
					   + "    AS reply_check     "
					   + "  FROM BOARD           "
					   + " WHERE num=?           ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			
			rs=pstmt.executeQuery();
			
			if(rs.next()){ replyCnt = rs.getInt("reply_check"); }
			if(replyCnt == 0) { replyCheck = true; }
			
			} catch(Exception e){
				e.printStackTrace();
			}finally{
				try{
					if(pstmt != null) { pstmt.close();}
					if(conn!=null){conn.close();}
				}catch(SQLException e){
					e.printStackTrace();}
				}
		return replyCheck;
		
		}
		
	// ★(글 삭제) 실질적으로 글을 삭제하는 기능
		
		public void boardDelete(String inputNum){
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try{
				
				conn = ds.getConnection();
				
			String sql = "SELECT ref, lev, step  "
					   + "  FROM BOARD           "
					   + " WHERE num=?           ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			rs=pstmt.executeQuery();
			
			if(rs.next()){
				int ref = rs.getInt(1);
				int lev = rs.getInt(2);
				int step = rs.getInt(3);
			boardDeleteChildCntUpdate(ref,lev,step);
			
			}
			
			sql = "DELETE FROM BOARD  "
			    + " WHERE num =?      ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			
			pstmt.executeUpdate();
			
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try{
					if(pstmt != null) { pstmt.close();}
					if(conn!=null){conn.close();}
					if(rs!=null){rs.close();}
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		
		// ★(글삭제) 지우려는 글이 답글일 경우, 원 게시글의 답글수를 줄여주는 메서드
		
		public void boardDeleteChildCntUpdate(int ref, int lev, int step){
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			String sql = null;
			
			try{
				conn = ds.getConnection();
				
			for(int updateLev = lev-1 ; updateLev >= 0 ; updateLev--){
				sql = "SELECT MAX(step)                  "
					+ "  FROM BOARD                      "
					+ " WHERE ref=? and lev=? and step<? ";
				pstmt=conn.prepareStatement(sql);
				pstmt.setInt(1, ref);
				pstmt.setInt(2, updateLev);
				pstmt.setInt(3, step);
				
			}
			}
		}
		
		
}
	
	
	
	
