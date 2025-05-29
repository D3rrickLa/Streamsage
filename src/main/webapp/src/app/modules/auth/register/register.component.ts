import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { User } from '../../../models/user';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [FormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  user: User = new User();
  errorMessage: string | null = null
  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    this.authService.registerUser(this.user).subscribe({
      next: (data: {token: string}) => {
        console.log('JWT TOKEN: ', data);
        sessionStorage.setItem("token", data.token)
        this.router.navigate(["/"])
      },
      error: (err) => {
        console.error("error occurred", err)
        this.errorMessage = err.error?.message || 'An unexpected error occurred';

      }
    })
  }
}
