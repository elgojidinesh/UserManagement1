package com.xadmin.usermanagement.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.xadmin.usermanagement.bean.User;
import com.xadmin.usermanagement.dao.UserDao;

/**
 * Servlet implementation class USerServlet
 */
@WebServlet("/")
public class USerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       private UserDao userDao;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public USerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		userDao=new UserDao();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action=request.getServletPath();
		switch (action) {
		case "/new": 
			showNewForm(request, response);
			break;
		case "/insert": 
			try {
				insertUser(request,response);
			} catch (Exception e) {
				System.out.println("data was not insert ");
				e.printStackTrace();
			}
			break;
		case "/delete": 
			try {
				deleteUser(request,response);
			} catch (IOException | SQLException e) {
				System.out.println("data not deleted");
				e.printStackTrace();
			}
			break;
		case "/edit": 
			try {
				showEditForm(request, response);
			} catch (IOException | SQLException e) {
				System.out.println("data was not edited");
				e.printStackTrace();
			}
			break;
		case "/update": 
			try {
				updateUser(request, response);
			} catch (Exception e) {
				System.out.println("data was not updated");
				e.printStackTrace();
			}
			break;
		default:
			try {
				listUser(request, response);
			} catch (Exception e) {
				System.out.println("data id out of range");
				e.printStackTrace();
			}
			break;
			}
			
		}
		private void showNewForm(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
			RequestDispatcher dispatcher=request.getRequestDispatcher("user-form.jsp");
			dispatcher.forward(request, response);
		}//insert user
		private void insertUser(HttpServletRequest request,HttpServletResponse response) throws SQLException,IOException, Exception{
			String name=request.getParameter("name");
			String email=request.getParameter("email");
			String country=request.getParameter("Country");
			User newUser=new User(name, email, country);
			
			userDao.insertUser(newUser);
			response.sendRedirect("list");
			
		}//delete user
private void deleteUser(HttpServletRequest request,HttpServletResponse response) throws IOException , SQLException{
	int id=Integer.parseInt(request.getParameter("id"));
	try {
		userDao.deleteUser(id);
	}catch (Exception e) {
		e.printStackTrace();
	}
	response.sendRedirect("list");
	}
//edit
		private void showEditForm(HttpServletRequest request,HttpServletResponse response)throws IOException , SQLException {
			int id=Integer.parseInt(request.getParameter("id"));
			User existingUser;
			try {
				existingUser=userDao.selectUser(id);
				RequestDispatcher dispatcher=request.getRequestDispatcher("user-form.jsp");
			request.setAttribute("user", existingUser);
			dispatcher.forward(request, response);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		//update
		private void updateUser(HttpServletRequest request,HttpServletResponse response)throws IOException , SQLException, Exception {
	int id=Integer.parseInt(request.getParameter("id"));
	String name=request.getParameter("name");
	String email=request.getParameter("email");
	String country=request.getParameter("country");
	User user=new User(id, name, email, country);
	userDao.updateUser(user);
	response.sendRedirect("list");
}
		//defalte
		private void listUser(HttpServletRequest request,HttpServletResponse response)throws IOException , SQLException, Exception {
		try {
			List<User> listUser=userDao.selectAllUsers();
			request.setAttribute("listUser", listUser);
		RequestDispatcher dispature=request.getRequestDispatcher("user-list.jsp");
		dispature.forward(request, response);
		
		}catch (Exception e) {
			e.printStackTrace();
		}
}
}