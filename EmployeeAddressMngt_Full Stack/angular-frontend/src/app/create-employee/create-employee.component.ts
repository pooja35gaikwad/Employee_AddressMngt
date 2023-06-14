import { Component, OnInit } from "@angular/core";
import { Address, Employee } from "../employee";
import { EmployeeService } from "../employee.service";
import { Router } from "@angular/router";

@Component({
  selector: "app-create-employee",
  templateUrl: "./create-employee.component.html",
  styleUrls: ["./create-employee.component.css"],
})
export class CreateEmployeeComponent implements OnInit {
  employee: Employee = new Employee();
  perAddress: Address = new Address();
  currAddress: Address = new Address();
  private employeeDoc: File;
  constructor(
    private employeeService: EmployeeService,
    private router: Router
  ) {}

  resetNgModels() {
    this.employee = new Employee();
    this.perAddress = new Address();
    this.currAddress = new Address();
    this.perAddress.type = "Permanent";
    this.currAddress.type = "Current";
  }

  ngOnInit(): void {
    this.resetNgModels();
  }

  saveEmployee() {
    this.employeeService.createEmployee(this.employee).subscribe(
      (data: Employee) => {
        const formData = new FormData();
        formData.append("file", this.employeeDoc);
        this.employeeService.uploadFile(formData, data.id).subscribe(
          (res) => {
            this.resetNgModels();
            this.goToEmployeeList();
          },
          (err) => {
            alert("Files upload failed");
          }
        );
      },
      (error) => {
        alert("Unable to save employee");
        console.log(error);
      }
    );
  }

  goToEmployeeList() {
    this.router.navigate(["/employees"]);
    window.scrollTo(0, 0);
  }

  onFileSelected(event) {
    this.employeeDoc = event.target.files[0];
  }

  onSubmit() {
    this.employee.addresses = [this.currAddress, this.perAddress];
    console.log(this.employee);
    console.log(this.employeeDoc);
    this.saveEmployee();
  }
}
