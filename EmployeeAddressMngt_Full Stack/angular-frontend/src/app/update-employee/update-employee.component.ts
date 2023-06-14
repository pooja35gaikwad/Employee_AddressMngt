import { Component, OnInit } from "@angular/core";
import { EmployeeService } from "../employee.service";
import { Address, Employee } from "../employee";
import { ActivatedRoute, Router } from "@angular/router";

@Component({
  selector: "app-update-employee",
  templateUrl: "./update-employee.component.html",
  styleUrls: ["./update-employee.component.css"],
})
export class UpdateEmployeeComponent implements OnInit {
  id: number;
  employee: Employee;
  perAddress: Address;
  currAddress: Address;
  constructor(
    private employeeService: EmployeeService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.id = this.route.snapshot.params["id"];
    this.resetNgModels();
    this.employeeService.getEmployeeById(this.id).subscribe(
      (data) => {
        this.employee = data;
        this.perAddress = data.addresses.filter(
          (address: Address) => address.type?.toUpperCase() === "PERMANENT"
        )[0];
        this.currAddress = data.addresses.filter(
          (address: Address) => address.type?.toUpperCase() === "CURRENT"
        )[0];
      },
      (error) => {
        alert("Unable to update employee");
        console.log(error);
      }
    );
  }

  resetNgModels() {}

  onSubmit() {
    this.employeeService.updateEmployee(this.id, this.employee).subscribe(
      (data) => {
        this.goToEmployeeList();
      },
      (error) => console.log(error)
    );
  }

  updateAddress() {
    this.employeeService.updateEmployee(this.id, this.employee).subscribe(
      (data) => {
        this.goToEmployeeList();
      },
      (error) => console.log(error)
    );
  }

  goToEmployeeList() {
    this.router.navigate(["/employees"]);
  }
}
