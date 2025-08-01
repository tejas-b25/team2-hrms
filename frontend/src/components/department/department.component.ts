
import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { AuthService, Department, Employee, User } from "../../app/auths/auth.service";
import { forkJoin } from "rxjs";
import { HttpErrorResponse } from "@angular/common/http";
 
@Component({
  selector: 'app-department',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './department.component.html',
  styleUrls: ['./department.component.css'],
})
export class DepartmentComponent implements OnInit {
  departments: Department[] = [];
  departmentForm!: FormGroup;
  isEditMode = false;
  currentEditId: number | null = null;
  userRole = '';
  managers: Employee[] = [];
  users: User[]=[];
 
  constructor(private fb: FormBuilder, public authService: AuthService) {}
 
  ngOnInit(): void {
    this.userRole = this.authService.getUserRole();
    this.initForm();
 
    // this.loadManagersAndDepartments();
    // this.loadDepartments();
    this.authService.getManagersByEmployee().subscribe({
    next: (managers: Employee[]) => {
      this.managers = managers;
      this.loadDepartments();
    },
    error: (err) => {
      console.error('Error fetching managers', err);
      if (err instanceof HttpErrorResponse) {
      console.error('Status:', err.status);
      console.error('Status Text:', err.statusText);
      console.error('URL:', err.url);
      console.error('Message:', err.message);
      console.error('Error:', err.error);
    }
      this.loadDepartments();
    }
  });
 
    if (this.authService.isAdmin()) {
      this.loadUsers(); // you already use managers
    }
  }
 
  initForm() {
    this.departmentForm = this.fb.group({
      departmentCode: ['', Validators.required],
      name: ['', Validators.required],
      description: [''],
      location: [''],
      departmentHead: [null]
    });
  }
 
  loadManagersAndDepartments() {
  this.authService.getManagersByEmployee().subscribe({
    next: (managers: Employee[]) => {
      this.managers = managers;
      this.loadDepartments(); // Now safe to load departments
    },
    error: (err) => {
      console.error('Error fetching managers', err);
    }
  });
}
  loadDepartments() {
    this.authService.getAllDepartments().subscribe({
      next: (data) => {
        this.departments = data;
      },
      error: (err) => {
        console.error('Failed to fetch departments', err);
      },
    });
  }
 
  loadUsers() {
  this.authService.getAllUsers().subscribe(data => {
    this.users = data;
  });
}
 
loadManagers(): void {
  this.authService.getManagersByEmployee().subscribe({
    next: (data: Employee[]) => {
      this.managers = data;
      console.log('Managers:', data); // For debug. You should see an array here.
    },
    error: (err: any) => {
      console.error('Error fetching managers', err);
    }
  });
}
 
 
  onSubmit() {
    if (this.departmentForm.invalid) {
      console.warn('Invalid form', this.departmentForm.value);
      return;
    }
 
  const fv = this.departmentForm.value;
  
  const selectedManager = this.managers.find(
    mgr => mgr.empId === fv.departmentHeadId
  );
  const payload = {
  departmentCode: fv.departmentCode,
  name: fv.name!,
  description: fv.description,
  location: fv.location,
  departmentHead:  fv.departmentHead ? { empId: fv.departmentHead.empId } : null
} as any;
 
    const action$ = this.isEditMode && this.currentEditId != null
      ? this.authService.updateDepartment(this.currentEditId, payload)
      : this.authService.createDepartment(payload as Department);
 
    action$.subscribe(() => {
      this.resetForm();
      this.loadDepartments();
    });
  }
 
 editDepartment(dept: Department) {
  this.currentEditId = dept.departmentId ?? null;
  this.isEditMode = true;
  this.departmentForm.patchValue({
    departmentCode: dept.departmentCode,
    name: dept.name,
    description: dept.description,
    location: dept.location,
    departmentHead: dept.departmentHead ?? null
  });
}
 
 
  deleteDepartment(id: number) {
    if (confirm('Delete this department?')) {
      this.authService.deleteDepartment(id).subscribe(() => {
        this.loadDepartments();
      });
    }
  }
 
  resetForm() {
    this.isEditMode = false;
    this.currentEditId = null;
    this.departmentForm.reset();
  }
 
  canModify(): boolean {
    return this.authService.isAdmin() || this.authService.isHR();
  }
 
getManagerNameById(empId: number | null | undefined): string {
  const mgr = this.managers.find(m => m.empId === empId);
  return mgr ? `${mgr.firstName} ${mgr.lastName}(${mgr.employeeCode})` : 'N/A';
}

 
}
 
 