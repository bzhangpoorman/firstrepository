package com.bzhang.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bzhang.pojo.Airplane;
import com.bzhang.service.AirplaneService;
import com.bzhang.service.impl.AirplaneServiceImpl;

/**
 * Servlet implementation class ShowAirplaneServlet
 */
@WebServlet("/ShowAirplaneServlet")
public class ShowAirplaneServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private AirplaneService airplaneService= new AirplaneServiceImpl();
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowAirplaneServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String takeIdStr=request.getParameter("takeid");
		String landIdStr=request.getParameter("landid");
		System.out.println(takeIdStr+"##########");
		System.out.println(landIdStr+"##########");
		int takeid=0;
		int landid=0;
		if (takeIdStr!=null&&!takeIdStr.equals("")) {
			takeid=Integer.parseInt(takeIdStr);
		}
		if (landIdStr!=null&&!landIdStr.equals("")) {
			landid=Integer.parseInt(landIdStr);
		}
		System.out.println(takeid+"************"+landid);
		List<Airplane> list=airplaneService.show(takeid, landid);
		request.setAttribute("list", list);
		request.getRequestDispatcher("show.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
