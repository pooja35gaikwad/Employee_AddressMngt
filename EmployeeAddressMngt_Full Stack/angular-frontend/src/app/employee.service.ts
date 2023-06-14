import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { Employee } from "./employee";

@Injectable({
  providedIn: "root",
})
export class EmployeeService {
  private baseURL = "http://localhost:8081/employees";

  constructor(private httpClient: HttpClient) {}

  getEmployeesList(): Observable<Employee[]> {
    return this.httpClient.get<Employee[]>(`${this.baseURL}`);
  }

  createEmployee(employee: Employee): Observable<Object> {
    return this.httpClient.post(`${this.baseURL}`, employee);
  }

  uploadFile(files: FormData, employeeId: number): Observable<Object> {
    return this.httpClient.post(`${this.baseURL}/upload/${employeeId}`, files);
  }

  getUploadedFiles(employeeId: number): Observable<Object> {
    return this.httpClient.get(`${this.baseURL}/files/${employeeId}`);
  }

  getEmployeeById(id: number): Observable<Employee> {
    return this.httpClient.get<Employee>(`${this.baseURL}/${id}`);
  }

  updateEmployee(id: number, employee: Employee): Observable<Object> {
    return this.httpClient.put(`${this.baseURL}/${id}`, employee);
  }

  deleteEmployee(id: number): Observable<Object> {
    return this.httpClient.delete(`${this.baseURL}/${id}`);
  }
}
