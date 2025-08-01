import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../app/auths/auth.service';
import { CommonModule } from '@angular/common';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-salary-structure',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './salary-structure.component.html',
  styleUrls: ['./salary-structure.component.css']
})
export class SalaryStructureComponent implements OnInit {
  salaryForm!: FormGroup;
  fetchedData: any = null;
  updateMode = false;
  employees: any[] = [];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.salaryForm = this.fb.group({
      salaryStructureId: [''],
      employeeId: ['', Validators.required],
      basicSalary: ['', [Validators.required, Validators.min(0)]],
      hra: ['', [Validators.required, Validators.min(0)]],
      specialAllowance: ['', [Validators.required, Validators.min(0)]],
      bonus: ['', [Validators.required, Validators.min(0)]],
      pfDeduction: ['', [Validators.required, Validators.min(0)]],
      taxDeduction: ['', [Validators.required, Validators.min(0)]],
      effectiveFrom: ['', Validators.required],
      effectiveTo: ['', Validators.required],
    });

    this.loadEmployees();
  }

  loadEmployees(): void {
    this.authService.getAllEmployees().subscribe({
      next: (res) => this.employees = res,
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load employees' });
      }
    });
  }

  assignSalary() {
    if (this.salaryForm.invalid) {
      this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'Please fill all required fields correctly.' });
      return;
    }
    const employeeId = this.salaryForm.get('employeeId')?.value;
    const payload = {
      basicSalary: Number(this.salaryForm.get('basicSalary')?.value),
      hra: Number(this.salaryForm.get('hra')?.value),
      specialAllowance: Number(this.salaryForm.get('specialAllowance')?.value),
      bonus: Number(this.salaryForm.get('bonus')?.value),
      pfDeduction: Number(this.salaryForm.get('pfDeduction')?.value),
      taxDeduction: Number(this.salaryForm.get('taxDeduction')?.value),
      effectiveFrom: this.salaryForm.get('effectiveFrom')?.value,
      effectiveTo: this.salaryForm.get('effectiveTo')?.value
    };

    this.authService.assignSalaryStructure(employeeId, payload).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Salary structure created!' });
        this.salaryForm.reset();
        this.updateMode = false;
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to assign salary structure.' });
      }
    });
  }

 fetchSalaryStructureByEmpId() {
  const empId = this.salaryForm.get('employeeId')?.value;
  if (!empId) {
    this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'Please select an employee.' });
    return;
  }

  this.authService.getSalaryStructureById(empId).subscribe({
    next: (data: any[]) => {
      if (data.length > 0) {
        const structure = data[0];  // take the first salary structure
        this.fetchedData = structure;
        this.salaryForm.patchValue({
          salaryStructureId: structure.id, // make sure your form uses 'id' instead of 'salaryStructureId'
          basicSalary: structure.basicSalary,
          hra: structure.hra,
          specialAllowance: structure.specialAllowance,
          bonus: structure.bonus,
          pfDeduction: structure.pfDeduction,
          taxDeduction: structure.taxDeduction,
          effectiveFrom: structure.effectiveFrom,
          effectiveTo: structure.effectiveTo
        });
        this.updateMode = true;
      } else {
        this.messageService.add({ severity: 'info', summary: 'Not found', detail: 'No salary structure found for this employee.' });
        this.salaryForm.patchValue({ salaryStructureId: '' });
        this.fetchedData = null;
        this.updateMode = false;
      }
    },
    error: () => {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to fetch salary structure.' });
    }
  });
}


  updateSalaryStructure() {
    if (this.salaryForm.invalid) {
      this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'Please fill all required fields correctly.' });
      return;
    }
    const id = this.salaryForm.get('salaryStructureId')?.value;
    const empId = this.salaryForm.get('employeeId')?.value;
    if (!id || !empId) return;

    const payload = {
      basicSalary: Number(this.salaryForm.get('basicSalary')?.value),
      hra: Number(this.salaryForm.get('hra')?.value),
      specialAllowance: Number(this.salaryForm.get('specialAllowance')?.value),
      bonus: Number(this.salaryForm.get('bonus')?.value),
      pfDeduction: Number(this.salaryForm.get('pfDeduction')?.value),
      taxDeduction: Number(this.salaryForm.get('taxDeduction')?.value),
      effectiveFrom: this.salaryForm.get('effectiveFrom')?.value,
      effectiveTo: this.salaryForm.get('effectiveTo')?.value
    };

    this.authService.updateSalaryStructure(id, payload).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Updated', detail: 'Salary structure updated successfully!' });
        this.salaryForm.reset();
        this.updateMode = false;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to update salary structure.' });
      }
    });
  }

  deleteSalaryStructure() {
    const id = this.salaryForm.get('salaryStructureId')?.value;
    if (!id) {
      this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'No salary structure selected to delete.' });
      return;
    }
    this.authService.deleteSalaryStructure(id).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Salary structure deleted successfully!' });
        this.salaryForm.reset();
        this.updateMode = false;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to delete salary structure.' });
      }
    });
  }

  onSubmit() {
  if (this.updateMode) {
    this.updateSalaryStructure();
  } else {
    this.assignSalary();
  }
}
}
