import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService, Compliance } from '../../app/auths/auth.service';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';

@Component({
  selector: 'app-compliance',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './compliance.component.html',
  styleUrls: ['./compliance.component.css'],
})
export class ComplianceComponent implements OnInit {
  complianceForm!: FormGroup;
  compliances: Compliance[] = [];
  editMode: boolean = false;
  selectedComplianceId: string | null = null;

  frequencies = ['DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY'];
  types = ['STATUTORY', 'INTERNAL', 'OTHER', 'REGULATORY'];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadCompliances();
  }

  initForm(): void {
    this.complianceForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      type: ['', Validators.required],
      frequency: ['', Validators.required],
      dueDate: ['', Validators.required],
      penalty: ['', Validators.required],
      documentRequired: [false],
      isActive: [true]
    });
  }

  loadCompliances(): void {
    this.authService.getAllCompliance().subscribe({
      next: (data) => {
        this.compliances = data;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load compliances' });
      }
    });
  }

  onSubmit(): void {
    if (this.complianceForm.invalid) return;

    const complianceData: Compliance = this.complianceForm.value;

    if (this.editMode && this.selectedComplianceId) {
      this.authService.updateCompliance(this.selectedComplianceId, complianceData).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Updated', detail: 'Compliance updated successfully' });
          this.resetForm();
          this.loadCompliances();
        },
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Update failed' });
        }
      });
    } else {
      this.authService.addComplaince(complianceData).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Created', detail: 'Compliance added successfully' });
          this.resetForm();
          this.loadCompliances();
        },
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Creation failed' });
        }
      });
    }
  }

  onEdit(compliance: Compliance): void {
  if (!compliance.complianceId) return;

  this.selectedComplianceId = compliance.complianceId;
  this.complianceForm.patchValue(compliance);
  this.editMode = true;
}

  onDelete(id: string): void {
    if (confirm('Are you sure you want to delete this compliance?')) {
      this.authService.deleteComplaince(id).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Compliance deleted' });
          this.loadCompliances();
        },
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Delete failed' });
        }
      });
    }
  }

  resetForm(): void {
    this.complianceForm.reset();
    this.editMode = false;
    this.selectedComplianceId = null;
  }
}
