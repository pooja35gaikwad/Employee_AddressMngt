package com.employee_app.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.employee_app.model.Address;
import com.employee_app.model.Employee;

@Repository
public class EmployeeDao {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

    public Employee addEmployee(Employee employee) {
    	System.out.println(employee);
        String sql = "INSERT INTO employees (first_name, last_name) VALUES (?, ?)";
        jdbcTemplate.update(sql, employee.getFirstName(), employee.getLastName());

        // Get the generated employee ID
        Long employeeId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        employee.setId(employeeId);

        // Insert addresses
        for (Address address : employee.getAddresses()) {
            addAddress(employeeId, address);
            //address.setId(employeeId);
        }
        
		return employee;
    }

    public Employee updateEmployee(Employee employee) {
        String sql = "UPDATE employees SET first_name = ?, last_name = ? WHERE id = ?";
        jdbcTemplate.update(sql, employee.getFirstName(), employee.getLastName(), employee.getId());

        // Delete existing addresses
        deleteAddressesByEmployeeId(employee.getId());

        // Insert addresses
        for (Address address : employee.getAddresses()) {
            addAddress(employee.getId(), address);
        }
		return employee;
    }

    public void deleteEmployee(Long employeeId) {
        deleteAddressesByEmployeeId(employeeId);

        String sql = "DELETE FROM employees WHERE id = ?";
        jdbcTemplate.update(sql, employeeId);
    }

    public List<Employee> getAllEmployees() {
        String sql = "SELECT * FROM employees";
        return (List<Employee>) jdbcTemplate.query(sql, new EmployeeRowMapper());
    }

    public Address addAddress(Long employeeId, Address address) {
        String sql = "INSERT INTO addresses (employee_id, type, line1, line2, city, state, postal_code) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, employeeId, address.getType(), address.getLine1(), address.getLine2(),
                address.getCity(), address.getState(), address.getPostalCode());
        
		return address;
    }
    
    public Employee getEmployeeById(Long employeeId) {
    	List<Employee> empList =  getAllEmployees().stream().filter((emp) -> emp.getId() == employeeId ).collect(Collectors.toList());
    	if(empList != null && empList.size() > 0) {
    		return empList.get(0);
    	}
    	return null;
    }

    public void deleteAddressesByEmployeeId(Long employeeId) {
        String sql = "DELETE FROM addresses WHERE employee_id = ?";
        jdbcTemplate.update(sql, employeeId);
    }

    private class EmployeeRowMapper implements RowMapper<Employee> {
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            Employee employee = new Employee();
            employee.setId(rs.getLong("id"));
            employee.setFirstName(rs.getString("first_name"));
            employee.setLastName(rs.getString("last_name"));

            // Retrieve addresses for the employee
            List<Address> addresses = getAddressesByEmployeeId(employee.getId());
            employee.setAddresses(addresses);

            return employee;
        }

        private List<Address> getAddressesByEmployeeId(Long employeeId) {
            String sql = "SELECT * FROM addresses WHERE employee_id = ?";
            return jdbcTemplate.query(sql, new AddressRowMapper(), employeeId);
        }
    }

    private class AddressRowMapper implements RowMapper<Address> {
        @Override
        public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
            Address address = new Address();
            address.setId(rs.getLong("id"));
            address.setType(rs.getString("type"));
            address.setLine1(rs.getString("line1"));
            address.setLine2(rs.getString("line2"));
            address.setCity(rs.getString("city"));
            address.setState(rs.getString("state"));
            address.setPostalCode(rs.getString("postal_code"));
            address.setEmployeeId(rs.getLong("employee_id"));
            return address;
        }
    }
}
