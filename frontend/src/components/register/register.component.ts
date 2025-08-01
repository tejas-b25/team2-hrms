// register.component.ts
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../app/auths/auth.service';
import { MessageService } from 'primeng/api';
// import { AuthService } from '../auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  imports: [CommonModule, FormsModule],
   providers: [MessageService] 
})
export class RegisterComponent {
  user = { username: '', email: '', role: '' };
  roles = ['HR', 'MANAGER', 'FINANCE', 'EMPLOYEE'];
  isLoading = false;
  errorMessage = '';

  constructor(
     private authService: AuthService,
    private router: Router,
    private messageService: MessageService
  ) {}

  onSubmit() {
    this.isLoading = true;
    this.errorMessage = '';

    if (!this.authService.isLoggedIn()) {
      this.isLoading = false;
      this.errorMessage = 'Admin must be logged in.';
      return;
    }

    this.authService.register(this.user).subscribe({
      next: () => {
        this.isLoading = false;
        // this.router.navigate(['/login']);

      this.messageService.add({
        severity: 'success',
      summary: 'Registration Successful',
      detail: 'You can now log in.',

      });

      setTimeout(() => {
      this.router.navigate(['/login']);
      }, 2000);

       },
      error: err => {
        this.isLoading = false;
        this.errorMessage = err?.error || err?.message || 'Registration failed.';

          this.messageService.add({
      severity: 'error',
      summary: 'Registration Failed',
      detail: this.errorMessage,
     });
      }
    });
  }
}
