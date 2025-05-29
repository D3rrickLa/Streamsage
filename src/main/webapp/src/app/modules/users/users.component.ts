import { Component } from '@angular/core';
import { ProfileComponent } from './profile/profile.component';
import { FeedbackComponent } from './feedback/feedback.component';
import { CommonModule } from '@angular/common';
import { FeedbackListComponent } from './feedback/feedback-list/feedback-list.component';

@Component({
  selector: 'app-users',
  imports: [ProfileComponent, FeedbackComponent, CommonModule, FeedbackListComponent],
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
