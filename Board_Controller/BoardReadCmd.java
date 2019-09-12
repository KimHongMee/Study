package board.command;

import board.model.*;
import javax.servlet.http.*;

public class BoardReadCmd implements BoardCmd {

	public void execute(HttpServletRequest request, 
	 		HttpServletResponse response){
		
		String inputNum = request.getParameter("num");
		
		BoardDAO dao = new BoardDAO();
		
		BoardDTO writing = dao.boardRead(inputNum);
		
		request.setAttribute("boardRead", writing);
	}
	
}
