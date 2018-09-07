package com.bzhang.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bzhang.pojo.Airport;
import com.bzhang.service.AirportService;
import com.bzhang.service.impl.AirportServiceImpl;

/**
 * Servlet implementation class showTakeServlet
 */
@WebServlet("/showTakeServlet")
public class showTakeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AirportService airportService=new AirportServiceImpl();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public showTakeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		List<Airport> takeport=airportService.showTakePort();
		List<Airport> landport=airportService.showLandPort();
		request.setAttribute("takeport", takeport);
		request.setAttribute("landport", landport);
		request.getRequestDispatcher("ShowAirplaneServlet").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
