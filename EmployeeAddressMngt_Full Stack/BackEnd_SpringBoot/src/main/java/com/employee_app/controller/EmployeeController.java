package com.employee_app.controller;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import com.employee_app.model.Address;
import com.employee_app.model.Employee;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.employee_app.repository.impl.EmployeeDao;
import org.springframework.core.io.UrlResource;

import java.io.File;



@RestController
@RequestMapping("/employees")
@CrossOrigin
public class EmployeeController {

	@Autowired
	EmployeeDao employeeService;

	@PostMapping
	public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
		try {
			if (employee.getFirstName() == null || employee.getFirstName().trim().equals("")) {
				throw new Exception("Employee must have first name");
			}
			if (employee.getAddresses() == null || employee.getAddresses().size() == 0) {
				throw new Exception("Employee must have atleast one address");
			}
			return ResponseEntity.ok(employeeService.addEmployee(employee));

		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.badRequest().build();

		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable("id") Long employeeId,
			@RequestBody Employee employee) {
		try {
			if (employee.getFirstName() == null || employee.getFirstName().trim().equals("")) {
				throw new Exception("Employee must have first name");
			}
			if (employee.getAddresses() == null || employee.getAddresses().size() == 0) {
				throw new Exception("Employee must have atleast one address");
			}
			employee.setId(employeeId);
			return ResponseEntity.ok(employeeService.updateEmployee(employee));
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable("id") Long employeeId) {
		try {
			employeeService.deleteEmployee(employeeId);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@GetMapping
	public ResponseEntity<List<Employee>> getAllEmployees() {
		try {
			return ResponseEntity.ok(employeeService.getAllEmployees());
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long employeeId) {
		try {
			return ResponseEntity.ok(employeeService.getEmployeeById(employeeId));
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping("/{id}/addresses")
	public ResponseEntity<Address> addAddress(@PathVariable("id") Long employeeId, @RequestBody Address address) {
		try {
			return ResponseEntity.ok(employeeService.addAddress(employeeId, address));
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/addresses/{id}")
	public void deleteAddress(@PathVariable("id") Long addressId) {
		try {
			employeeService.deleteAddressesByEmployeeId(addressId);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@PostMapping("/upload/{id}")
	public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable("id") Long empId) {

		try {
			if (file.isEmpty()) {
				return ResponseEntity.ok().build();
			}
			if(!Files.exists(Path.of("files/" + empId + "/"))){
				Files.createDirectories(Path.of("files/" + empId + "/"));
			}
			String filename = StringUtils.cleanPath(file.getOriginalFilename());
			Path destination = Path.of("files/" + empId + "/" + filename);
			Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
			return ResponseEntity.ok().build();
		} catch (IOException ex) {
			System.out.println(ex);
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/files/{id}")
    public ResponseEntity<List<String>> getAllFiles(@PathVariable("id") Long empId) {
        File directory = new File("files/" + empId + "/");
        File[] files = directory.listFiles();

        List<String> fileNames = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                fileNames.add(file.getName());
            }
        }

        return ResponseEntity.ok(fileNames);
    }

}
