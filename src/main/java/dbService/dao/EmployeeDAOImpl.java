package dbService.dao;

import dbService.entity.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO{
    private Connection connection;

    public EmployeeDAOImpl() {
        connection = ConnectionProvider.getConnection();
    }

    @Override
    public void close() throws Exception {
        try {
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<Employee> getEmployees(String selectQuery) {
        List<Employee> list = new ArrayList<>();
        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(selectQuery);
            while(rs.next()) {
                Employee employee = new Employee(rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9));
                employee.setId(rs.getInt(1));
                list.add(employee);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    @Override
    public List<Employee> getByFirstName(String firstName) {
        return getEmployees("select * from Employees where firstName = "
                                        + "'" + firstName + "'");
    }

    @Override
    public List<Employee> getBySurName(String surName) {
        return getEmployees("select * from Employees where surName = "
                + "'" + surName + "'");
    }

    @Override
    public List<Employee> getByMiddleName(String middleName) {
        return getEmployees("select * from Employees where middleName = "
                + "'" + middleName + "'");
    }

    @Override
    public List<Employee> getByPosition(String pos) {
        return getEmployees("select * from Employees where pos = "
                + "'" + pos + "'");
    }

    @Override
    public List<Employee> getByDepartment(String department) {
        return getEmployees("select * from Employees where department = "
                + "'" + department + "'");
    }

    @Override
    public List<Employee> getAll() {
        return getEmployees("select * from Employees");
    }

    @Override
    public Employee getByLogin(String login) {
        List<Employee> list = getEmployees("select * from Employees where login = "
                + "'" + login + "'");
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Employee getByChatId(String chatId) {
        List<Employee> list = getEmployees("select * from Employees where chatId = " + chatId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Employee getById(int id) {
        List<Employee> list = getEmployees("select * from Employees where chatId = " + id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void insert(Employee entity) {
        try(PreparedStatement statement = connection.prepareStatement("insert into Employees" +
                        " (firstName, surName, middleName, pos, department, login," +
                        " pass, chatId) values (?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getSurName());
            statement.setString(3, entity.getMiddleName());
            statement.setString(4, entity.getPosition());
            statement.setString(5, entity.getDepartment());
            statement.setString(6, entity.getLogin());
            statement.setString(7, entity.getPass());
            statement.setString(8, entity.getChatId());
            statement.execute();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if(generatedKeys.next())
                entity.setId(generatedKeys.getInt(1));
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(Employee entity) {
        try(PreparedStatement statement = connection.prepareStatement("update Employees set" +
                        " firstName = ?, surName = ?, middleName = ?, pos = ?, department = ?, login = ?," +
                        " pass = ?, chatId = ? where idEmployee = ?")) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getSurName());
            statement.setString(3, entity.getMiddleName());
            statement.setString(4, entity.getPosition());
            statement.setString(5, entity.getDepartment());
            statement.setString(6, entity.getLogin());
            statement.setString(7, entity.getPass());
            statement.setString(8, entity.getChatId());
            statement.setInt(9, entity.getId());
            statement.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteById(int id) {
        try(Statement statement = connection.createStatement()) {
            statement.execute("delete from Employees" +
                    " where idEmployee = " + id);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
