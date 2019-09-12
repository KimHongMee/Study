package board.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import board.model.*;

public class BoardDeleteCheckCmd implements BoardCmd{
	
	public boolean password_check;
	public boolean reply_check;
	
	
	public void execute(HttpServletRequest request, 
	 		HttpServletResponse response){
		
		
		String inputNum = request.getParameter("num");
		String inputPassword = request.getParameter("password");
		
		request.setAttribute("num", inputNum);
		
		BoardDAO dao = new BoardDAO();
		
		password_check = dao.boardPasswordCheck(inputNum, inputPassword);
		reply_check = dao.boardReplyCheck(inputNum);
		
		
	}

}
