import { Component, OnInit } from "@angular/core";
import { Employee } from "../employee";
import { ActivatedRoute } from "@angular/router";
import { EmployeeService } from "../employee.service";

@Component({
  selector: "app-employee-details",
  templateUrl: "./employee-details.component.html",
  styleUrls: ["./employee-details.component.css"],
})
export class EmployeeDetailsComponent implements OnInit {
  id: number;
  employee: Employee;
  uploadedFiles: any;
  constructor(
    private route: ActivatedRoute,
    private employeService: EmployeeService
  ) {}

  ngOnInit(): void {
    this.id = this.route.snapshot.params["id"];
    this.employeService.getEmployeeById(this.id).subscribe((data) => {
      this.employee = data;
    });
    this.employeService.getUploadedFiles(this.id).subscribe((data) => {
      this.uploadedFiles = data;
    });
  }

  getUploadedFilesNames() {
    if (this.uploadedFiles?.length) {
      return this.uploadedFiles;
    } else {
      return [];
    }
  }
}
