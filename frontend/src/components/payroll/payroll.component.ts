import { Component, OnInit } from '@angular/core';
import { AuthService, Payroll, Employee } from '../../app/auths/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-payroll',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './payroll.component.html',
  styleUrls: ['./payroll.component.css'],
})
export class PayrollComponent implements OnInit {
  form!: FormGroup;
  payroll?: Payroll;
  errorMessage: string = '';
  allEmployees: Employee[] = [];

  constructor(private payrollService: AuthService, private fb: FormBuilder) {}

  ngOnInit() {
    this.form = this.fb.group({
      employeeId: [''],
      month: [''],
      year: ['']
    });

    this.payrollService.getAllEmployees().subscribe({
      next: (data) => {
        this.allEmployees = data;
      },
      error: (err) => {
        this.errorMessage = err.error?.message || err.message || 'Failed to load employees';
      }
    });
  }

  generate() {
    const { employeeId, month, year } = this.form.value;
    if (!employeeId || !month || !year) {
      this.errorMessage = 'Please fill all fields.';
      return;
    }

    this.payrollService.generatePayroll(employeeId, month, year).subscribe({
      next: (payroll) => {
        this.payroll = payroll;
        this.errorMessage = '';
      },
      error: (err) => {
        this.errorMessage = err.error?.message || err.message || 'Error generating payroll';
      },
    });
  }

  viewPayroll() {
    const { employeeId, month, year } = this.form.value;
    if (!employeeId || !month || !year) {
      this.errorMessage = 'Please fill all fields.';
      return;
    }

    this.payrollService.viewPayroll(employeeId, month, year).subscribe({
      next: (payroll) => {
        this.payroll = payroll;
        this.errorMessage = '';
      },
      error: (err) => {
        this.errorMessage = err.error?.message || err.message || 'Error fetching payroll';
        this.payroll = undefined;
      },
    });
  }

  downloadPayslip() {
    const { employeeId, month, year } = this.form.value;
    if (!employeeId || !month || !year) {
      this.errorMessage = 'Please fill all fields.';
      return;
    }

    this.payrollService.downloadPayslip(employeeId, month, year).subscribe({
    next: (response) => {
      const blob = new Blob([response.body!], { type: 'application/pdf' });

      // Extract filename from content-disposition header
      let filename = '';
      const contentDisposition = response.headers.get('Content-Disposition');
      const employee = this.allEmployees.find(e => e.empId.toString() === employeeId);
      console.log('Employee found:', employee);

      if (contentDisposition) {
        const match = contentDisposition.match(/filename="?([^"]+)"?/);
        filename = match && match[1] ? match[1] : 'payslip.pdf';
      } else if(employee){
        // 👇 fallback filename like Gunjan_Sherkar(1)_Jun_2025.pdf
        const firstName = employee.firstName.replace(/\s+/g, '_');
        const lastName = employee.lastName.replace(/\s+/g, '_');
        filename = `${firstName}_${lastName}(${employeeId})_${month}_${year}.pdf`;
      } else {
        filename = `payslip_${employeeId}_${month}_${year}.pdf`;
      }

      // 🠗 Create a download link
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = filename;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);
    },
    error: (err) => {
      this.errorMessage = err.error?.message || err.message || 'Error downloading payslip';
    }
  });
}
}
