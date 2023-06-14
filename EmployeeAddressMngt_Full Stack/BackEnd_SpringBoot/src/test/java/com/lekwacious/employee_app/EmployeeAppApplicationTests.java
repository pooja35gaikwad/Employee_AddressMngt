package com.lekwacious.employee_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.employee_app.model.Address;
import com.employee_app.model.Employee;
import com.employee_app.repository.impl.EmployeeDao;
import com.employee_app.EmployeeAppApplication;
import com.employee_app.controller.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(value = SpringExtension.class)
@SpringBootTest(classes = {EmployeeAppApplication.class})
@ComponentScan(basePackages = "com.employee_app")
public class EmployeeAppApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private EmployeeDao employeeDao;

	@InjectMocks
	private EmployeeController employeeController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testAddEmployee() throws Exception {
		Employee employee = new Employee();
		employee.setId(1L);
		employee.setFirstName("John Doe");

		when(employeeDao.addEmployee(employee)).thenReturn(employee);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/employees").contentType(MediaType.APPLICATION_JSON)
						.content("{\"id\": 1, \"firstName\": \"John Doe\"}"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		assertEquals(result.getResponse().getContentAsString(), "{\"id\":1,\"name\":\"John Doe\"}");
	}

	@Test
	public void testUpdateEmployee() throws Exception {
		Employee employee = new Employee();
		employee.setId(1L);
		employee.setFirstName("John Doe");

		when(employeeDao.updateEmployee(employee)).thenReturn(employee);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.put("/employees/1").contentType(MediaType.APPLICATION_JSON)
						.content("{\"id\": 1, \"firstName\": \"John Doe\"}"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		assertEquals(result.getResponse().getContentAsString(), "{\"id\":1,\"firstName\":\"John Doe\"}");
	}

	@Test
	public void testDeleteEmployee() throws Exception {
		doNothing().when(employeeDao).deleteEmployee(1L);

		mockMvc.perform(MockMvcRequestBuilders.delete("/employees/1")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testGetAllEmployees() throws Exception {
		List<Employee> employees = new ArrayList<>();
		Employee employee1 = new Employee();
		employee1.setId(1L);
		employee1.setFirstName("John Doe");
		employees.add(employee1);

		when(employeeDao.getAllEmployees()).thenReturn(employees);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		assertEquals(result.getResponse().getContentAsString(), "[{\"id\":1,\"firstName\":\"John Doe\"}]");
	}

	@Test
	public void testGetEmployeeById() throws Exception {
		Employee employee = new Employee();
		employee.setId(1L);
		employee.setFirstName("John Doe");

		when(employeeDao.getEmployeeById(1L)).thenReturn(employee);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employees/1"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		assertEquals(result.getResponse().getContentAsString(), "{\"id\":1,\"firstName\":\"John Doe\"}");
	}

	@Test
	public void testAddAddress() throws Exception {
		Address address = new Address();
		address.setId(1L);
		address.setCity("New York");

		when(employeeDao.addAddress(1L, address)).thenReturn(address);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/employees/1/addresses").contentType(MediaType.APPLICATION_JSON)
						.content("{\"id\": 1, \"city\": \"New York\"}"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		assertEquals(result.getResponse().getContentAsString(), "{\"id\":1,\"city\":\"New York\"}");
	}

	@Test
	public void testDeleteAddress() throws Exception {
		doNothing().when(employeeDao).deleteAddressesByEmployeeId(1L);

		mockMvc.perform(MockMvcRequestBuilders.delete("/employees/addresses/1"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
