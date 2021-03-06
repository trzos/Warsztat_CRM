package pl.coderslab.dao;

import pl.coderslab.DbUtil;
import pl.coderslab.model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomerDao {


    public static void saveToDb(Customer customer) throws SQLException {
        if (customer == null) {
            throw new NullPointerException("Not possible to save a customer that does not exist");
        }
        if (customer.getId() != 0) {
            updateCustomerInDb(customer);
        } else {
            addCustomerToDb(customer);
        }
    }

    public static void addCustomerToDb(Customer customer) throws SQLException {
        String birthdayDateS = customer.getBirthdayDate();
        Date birthdayDate = null;
        try {
            birthdayDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthdayDateS);
        } catch (ParseException e) {
            System.out.println("Wrong date format!");
        }
        String generatedColumns[] = {"ID"};
        PreparedStatement stmt = DbUtil.getConn().prepareStatement("INSERT INTO customers(name,surname,birthday,email,password) VALUES (?,?,?,?,?)", generatedColumns);
        stmt.setString(1, customer.getName());
        stmt.setString(2, customer.getSurname());
        stmt.setDate(3, new java.sql.Date(birthdayDate.getTime()));
        stmt.setString(4, customer.getEmail());
        stmt.setString(5, customer.getPassword());
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            customer.setId(rs.getInt(1));
        }
        DbUtil.getConn().close();
    }

    public static void updateCustomerInDb(Customer customer) throws SQLException {
        int id = customer.getId();
        String birthdayDateS = customer.getBirthdayDate();
        Date birthdayDate = null;
        try {
            birthdayDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthdayDateS);
        } catch (ParseException e) {
            System.out.println("Wrong date format!");
        }
        PreparedStatement stmt = DbUtil.getConn().prepareStatement("UPDATE customers SET name= ?, surname= ?, birthday= ?, email = ?, password = ? WHERE id= ?");
        stmt.setString(1, customer.getName());
        stmt.setString(2, customer.getSurname());
        stmt.setDate(3, new java.sql.Date(birthdayDate.getTime()));
        stmt.setString(4, customer.getEmail());
        stmt.setString(5, customer.getPassword());
        stmt.setInt(6, id);
        stmt.executeUpdate();
        DbUtil.getConn().close();
    }

    public void delete(Customer customer) throws SQLException {
        int id = customer.getId();
        String sql = "UPDATE customers SET active='inactive' WHERE id= ?";
        try {
            if (id != 0) {
                PreparedStatement stmt = DbUtil.getConn().prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        DbUtil.getConn().close();
    }

    public static Customer loadById(int id)  {
        try {
            String sql = "SELECT * FROM customers where id=? AND active='active'";
            PreparedStatement stmt = DbUtil.getConn().prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Customer loadedCustomer = new Customer();
                loadedCustomer.setId(resultSet.getInt("id"));
                loadedCustomer.setName(resultSet.getString("name"));
                loadedCustomer.setSurname(resultSet.getString("surname"));
                loadedCustomer.setBirthdayDate(resultSet.getString("birthday"));
                loadedCustomer.setEmail(resultSet.getString("email"));
                loadedCustomer.setPassword(resultSet.getString("password"));

                return loadedCustomer;
            }
            DbUtil.getConn().close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    public static ArrayList<Customer> loadAll() {
        String sql = "SELECT * FROM customers WHERE active='active'";
        PreparedStatement stmt = null;
        try {
            stmt = DbUtil.getConn().prepareStatement(sql);
            DbUtil.getConn().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getCustomersFromStatement(stmt);
    }

    private static ArrayList<Customer> getCustomersFromStatement(PreparedStatement stmt) {
        try {
            ArrayList<Customer> customers = new ArrayList<Customer>();
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Customer loadedCustomer = new Customer();
                loadedCustomer.setId(resultSet.getInt("id"));
                loadedCustomer.setName(resultSet.getString("name"));
                loadedCustomer.setSurname(resultSet.getString("surname"));
                loadedCustomer.setBirthdayDate(resultSet.getString("birthday"));
                loadedCustomer.setEmail(resultSet.getString("email"));
                loadedCustomer.setPassword(resultSet.getString("password"));
                customers.add(loadedCustomer);
            }
            DbUtil.getConn().close();
            return customers;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
