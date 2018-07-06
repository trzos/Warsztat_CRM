package pl.coderslab.controller;

import pl.coderslab.DbUtil;
import pl.coderslab.dao.EmployeeDao;
import pl.coderslab.model.Employee;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet("/AllEmployees")
public class AllEmployees extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=utf-8;");

        EmployeeDao listOfAll = new EmployeeDao();
        List<Employee> employees;
        employees = listOfAll.loadAll();
        request.setAttribute("employees", employees);
        response.getWriter().append("witaj swiecie");//test

        request.getRequestDispatcher("/views/allEmployees.jsp").forward(request, response);
    }
}
















