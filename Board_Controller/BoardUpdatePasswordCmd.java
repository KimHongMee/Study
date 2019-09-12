package board.command;

import javax.servlet.http.*;


public class BoardUpdatePasswordCmd implements BoardCmd {

	
	public void execute(HttpServletRequest request, 
	 		HttpServletResponse response){
		
		String inputNum = request.getParameter("num");
		request.setAttribute("num", inputNum);
		
		
	}

	

}
