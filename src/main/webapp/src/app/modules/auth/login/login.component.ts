import { Component } from '@angular/core';
import { User } from '../../../models/user';
import { AuthService } from '../auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  user: User = new User();
  errorMessage: string | null = null
  constructor(private authService: AuthService, private router: Router) {}


  onSubmit() {
    this.authService.loginUser(this.user).subscribe({
      next: (data: {token: string}) => {
        console.log('JWT TOKEN: ', data);
        sessionStorage.setItem("token", data.token)
        this.router.navigate(["/"])
        // change the 'login' tag to log out
      },
      error: (err) => {
        console.error("error occurred", err)
        this.errorMessage = err.error?.message || 'An unexpected error occurred';

      }
    })
  }
}
