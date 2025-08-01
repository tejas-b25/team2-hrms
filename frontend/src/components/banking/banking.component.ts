import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { AuthService, Employee } from '../../app/auths/auth.service';
import { ToastModule } from 'primeng/toast';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';

@Component({
  selector: 'app-banking',
  standalone: true, // ✅ standalone
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    ToastModule,
    InputTextModule,
    InputNumberModule
  ],
  templateUrl: './banking.component.html',
  styleUrls: ['./banking.component.css'],
  providers: [MessageService]
})
export class BankingComponent implements OnInit {
  bankForm!: FormGroup;
  allEmployees: Employee[] = [];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.buildForm();
    this.loadEmployees();
  }

  buildForm() {
    this.bankForm = this.fb.group({
      employeeId: ['', Validators.required],
      accountHolderName: ['', Validators.required],
      bankName: ['', Validators.required],
      accountNumber: ['', [Validators.required, Validators.pattern(/^\d+$/)]],
      ifscCode: ['', Validators.required],
      branch: ['', Validators.required],
      accountType: ['', Validators.required]
    });
  }

  loadEmployees() {
    this.authService.getAllEmployees().subscribe({
      next: (employees) => {
        this.allEmployees = employees;
      },
      error: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to load employees'
        });
      }
    });
  }

  onSubmit() {
    if (this.bankForm.invalid) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Invalid',
        detail: 'Please fill all required fields correctly.'
      });
      return;
    }

    const formValue = this.bankForm.value;

    const payload = {
      accountHolderName: formValue.accountHolderName,
      bankName: formValue.bankName,
      accountNumber: formValue.accountNumber,
      ifscCode: formValue.ifscCode,
      branch: formValue.branch,
      accountType: formValue.accountType
    };

    const empId = formValue.employeeId
    console.log('About to submit employeeId:', empId);

    // const formData = new FormData();
    // formData.append('bankDetails', new Blob([JSON.stringify(payload)], { type: 'application/json' }));

 
    this.authService.createBankDetails(empId, payload).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Bank details saved successfully.'
        });
        this.bankForm.reset();
      },
      error: () =>
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to save bank details.'
        })
    });

  }
}