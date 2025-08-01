import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { User, Department, AuthService } from '../../app/auths/auth.service';

@Component({
  selector: 'app-add-employee',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './add-employee.component.html',
  styleUrls: ['./add-employee.component.css'],
  providers: [MessageService]
})
export class AddEmployeeComponent implements OnInit {
  employeeForm!: FormGroup;
  selectedPhoto: File | null = null;
  allUsers: User[] = [];
  managers: any[] = [];
  departments: Department[] = [];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.buildForm();
    this.loadAllUsers();
    this.loadDepartments();
    this.loadManagers();
  }

  buildForm() {
    this.employeeForm = this.fb.group({
      userId: ['', Validators.required],
      employeeCode: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      contactNumber: ['', Validators.required],
      address: ['', Validators.required],
      gender: ['', Validators.required],
      dob: ['', Validators.required],
      emergencyContact: ['', Validators.required],
      joiningDate: ['', Validators.required],
      probationEndDate: ['', Validators.required],
      exitReason: [''],
      status: ['ACTIVE'],
      designation: ['', Validators.required],
      jobType: ['FULL_TIME'],
      education: [''],
      experience: [''],
      certifications: [''],
      location: ['', Validators.required],
      managerId: [''],
      departmentId: [null]
    });

    this.employeeForm.get('joiningDate')?.valueChanges.subscribe(joiningDate => {
      if (joiningDate) {
        const jd = new Date(joiningDate);
        jd.setMonth(jd.getMonth() + 3);
        const probationDate = jd.toISOString().substring(0, 10);
        this.employeeForm.get('probationEndDate')?.setValue(probationDate);
      } else {
        this.employeeForm.get('probationEndDate')?.setValue('');
      }
    });
  }

  loadAllUsers(): void {
    this.authService.getAllUsers().subscribe({
      next: (users) => (this.allUsers = users),
      error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load users' })
    });
  }

  loadDepartments(): void {
    this.authService.getAllDepartments().subscribe({
      next: (departments) => this.departments = departments,
      error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load departments' })
    });
  }

  loadManagers(): void {
  this.authService.getManagersByEmployee().subscribe({
    next: (managers) => {
      console.log('Managers loaded:', managers);
      this.managers = managers;
    },
    error: () =>
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Failed to load managers',
      }),
  });
}

 


  onPhotoChange(event: any): void {
    if (event.target.files.length > 0) {
      this.selectedPhoto = event.target.files[0];
    }
  }

  submitForm(): void {
    if (this.employeeForm.invalid || !this.selectedPhoto) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Incomplete',
        detail: 'Please fill all required fields and upload a photo.'
      });
      return;
    }

    const formValue = this.employeeForm.value;
    const userId = formValue.userId && formValue.userId !== 'undefined' ? formValue.userId : null;
    const managerId = formValue.managerId ? { empId: Number(formValue.managerId) } : null;


    const payload: any = {
      employeeCode: formValue.employeeCode,
      firstName: formValue.firstName,
      lastName: formValue.lastName,
      email: formValue.email,
      contactNumber: formValue.contactNumber,
      address: formValue.address,
      gender: formValue.gender,
      dob: formValue.dob,
      emergencyContact: formValue.emergencyContact,
      joiningDate: formValue.joiningDate,
      probationEndDate: formValue.probationEndDate,
      exitReason: formValue.exitReason,
      status: formValue.status,
      designation: formValue.designation,
      jobType: formValue.jobType,
      education: formValue.education,
      experience: formValue.experience,
      certifications: formValue.certifications,
      location: formValue.location,
      department: { departmentId: formValue.departmentId },
      user: userId ? { userId } : null,
      manager: managerId
    };

    const formData = new FormData();
    formData.append('employee', new Blob([JSON.stringify(payload)], { type: 'application/json' }));
    formData.append('photo', this.selectedPhoto!);

    this.authService.createEmployeeFormData(formData).subscribe({
      next: (res) => {
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: `Employee ${res.firstName} ${res.lastName} created.`
        });
        this.employeeForm.reset();
        this.selectedPhoto = null;
      },
      error: () => this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Failed to create employee.'
      })
    });
  }
}