import { Component } from '@angular/core';
import { User } from '../../../models/user';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-profile',
  imports: [FormsModule, CommonModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  user: User = new User();
  errorMessage: string | null = null
  updateMessage: string | null = null

  constructor(private userService: UserService) {
    this.fetchUserData()
  }



  fetchUserData() {
    this.userService.getUserInfo().subscribe({
      next: (data) => {
        this.user = data;
      },
      error: (err) => {
        this.errorMessage = 'Failed to load user data.';
        console.error(err);
      }
    });
  }

  onSubmit() {
    const data = {
      firstName: this.user.firstName,
      lastName: this.user.lastName,
      email: this.user.email
    }

    this.userService.updateUserInfo(data).subscribe({
      next: (data: { token: string }) => {
        console.log(data.token)
        sessionStorage.setItem("token", data.token)
        this.updateMessage = "Updated information successfully!";
        setTimeout(() => {
          this.updateMessage = null;
        }, 5000);
      },
      error: (err) => {
        console.error("error occurred", err)
        this.errorMessage = err.error?.message || 'An unexpected error occurred';
        setTimeout(() => {
          this.errorMessage = null;
        }, 5000);
      }
    })
  }

  onChangePassword(currentPassword: string, newPassword: string, confirmPassword: string, form: NgForm) {
    if (newPassword !== confirmPassword) {
      this.errorMessage = "Passwords do not match";
      setTimeout(() => this.errorMessage = null, 5000);
      return;
    }

    const data = {
      oldPassword: currentPassword,
      newPassword: newPassword
    }

    this.userService.updateUserPassword(data).subscribe({
      next: (message: string) => {
        this.updateMessage = message;

        // Clear inputs manually using ElementRef or native DOM:
        const currentInput = document.getElementById('currentPassword') as HTMLInputElement;
        const newPwInput = document.getElementById('newPassword') as HTMLInputElement;
        const confirmInput = document.getElementById('confirmPassword') as HTMLInputElement;

        if (currentInput) currentInput.value = '';
        if (newPwInput) newPwInput.value = '';
        if (confirmInput) confirmInput.value = '';

        setTimeout(() => this.updateMessage = null, 5000);
      },
      error: err => {
        this.errorMessage = err.error?.message || 'An unexpected error occurred';
        setTimeout(() => this.errorMessage = null, 5000);
      }
    });

  }


  showPopup() {
    
  }
}
