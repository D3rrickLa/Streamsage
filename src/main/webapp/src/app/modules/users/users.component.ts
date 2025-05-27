import { Component } from '@angular/core';
import { ProfileComponent } from './profile/profile.component';
import { FeedbackComponent } from './feedback/feedback.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-users',
  imports: [ProfileComponent, FeedbackComponent, CommonModule],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent {
  // we need to send the jwt token, or else throw an error. users not signed in can't access this page
  currentProfile = 0;
  changeLayout(n: number) {
    this.currentProfile = n;

  }
}
