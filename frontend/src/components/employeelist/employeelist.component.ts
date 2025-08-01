import { Component, OnInit } from '@angular/core';
import { EmployeeService } from '../../app/services/employee.service';
import { AuthService } from '../../app/auths/auth.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-employeelist',
  templateUrl: './employeelist.component.html',
  styleUrls: ['./employeelist.component.css']
})
export class EmployeelistComponent implements OnInit {
  employeelist: any[] = [];
  expandedBankEmployeeId: string | null = null;

  constructor(
    private employeeService: EmployeeService,
    private authService: AuthService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.getAllEmployees();
  }

  getAllEmployees(): void {
    this.employeeService.getAllEmployees().subscribe({
      next: (data: any[]) => {
        this.employeelist = data;
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to fetch employees.'
        });
      }
    });
  }

//  getManagerNameById(managerId: number | string | null): string {
//   if (!managerId) return 'N/A';
//   const manager = this.employeelist.find(emp => emp.empId === +managerId);
//   return manager ? `${manager.firstName} ${manager.lastName}` : 'N/A';
// }

  getBankTooltip(bank: any): string {
    if (!bank) return 'No bank details available';
    return `Account Holder: ${bank.accountHolderName || 'N/A'}
Bank: ${bank.bankName || 'N/A'}
Account #: ${bank.accountNumber || 'N/A'}
IFSC: ${bank.ifscCode || 'N/A'}
Branch: ${bank.branch || 'N/A'}
Type: ${bank.accountType || 'N/A'}`;
  }

  canModify(): boolean {
    return this.authService.hasRole('ADMIN') || this.authService.hasRole('HR');
  }

  editEmployeeList(emp: any): void {
    console.log('Edit employee:', emp);
    // Logic to open edit form/modal
  }

  deleteEmployee(empId: string): void {
    if (!confirm('Are you sure you want to delete this employee?')) return;
    this.employeeService.deleteEmployee(empId).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Deleted',
          detail: 'Employee deleted successfully.'
        });
        this.getAllEmployees();
      },
      error: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to delete employee.'
        });
      }
    });
  }

  toggleBankDetails(empId: string): void {
    this.expandedBankEmployeeId = this.expandedBankEmployeeId === empId ? null : empId;
  }

  isBankExpanded(empId: string): boolean {
    return this.expandedBankEmployeeId === empId;
  }
}
