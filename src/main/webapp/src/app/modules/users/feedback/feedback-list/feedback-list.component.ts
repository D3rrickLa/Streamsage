import { Component } from '@angular/core';
import { FeedbackService } from '../../../../services/feedback.service';
import { Feedback } from '../../../../models/feedback';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-feedback-list',
  imports: [CommonModule],
  templateUrl: './feedback-list.component.html',
  styleUrl: './feedback-list.component.css'
})
export class FeedbackListComponent {
  user_feedback: Feedback[] | null = null
  constructor(private feedbackService: FeedbackService) { }

  ngOnInit() {
    this.getListOfFeedback()
  }

  getListOfFeedback() {
    this.feedbackService.getFeedback().subscribe({
      next: (data: Feedback[]) => {
        this.user_feedback = data
      },
      error: (err) => {
        console.error('Error fetching feedback:', err);
      }
    })
  }
}
