package board.command;

import javax.servlet.http.*;

import board.model.*;

public class BoardUpdateCheckCmd implements BoardCmd {
	
	public boolean password_check;
	
	
	@Override
	public void execute(HttpServletRequest request, 
	 		HttpServletResponse response){
		
		String inputNum = request.getParameter("num");
		String inputPassword = request.getParameter("password");  
		
		request.setAttribute("num", inputNum);
		
		BoardDAO dao = new BoardDAO();
		password_check = dao.boardPasswordCheck(inputNum, inputPassword);
		
		// ↑DAO의 결과에 따라서 passwordOk값이 [password_check]에 들어갈것이다. true or false
		
	}

}
