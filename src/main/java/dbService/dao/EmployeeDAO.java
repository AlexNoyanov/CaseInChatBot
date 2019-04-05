package dbService.dao;

import dbService.entity.Employee;

import java.util.List;

public interface EmployeeDAO extends DAO<Employee> {
    List<Employee> getByFirstName(String firstName);
    List<Employee> getBySurName(String surName);
    List<Employee> getByMiddleName(String middleName);
    List<Employee> getByPosition(String pos);
    List<Employee> getByDepartment(String department);
    Employee getByLogin(String login);
    Employee getByChatId(String chatId);
}
