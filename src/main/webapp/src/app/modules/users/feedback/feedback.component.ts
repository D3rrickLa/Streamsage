import { Component, EventEmitter, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { StarRatingComponent } from '../../star-rating/star-rating.component';
import { Feedback } from '../../../models/feedback';
import { FormsModule } from '@angular/forms';
import { FeedbackService } from '../../../services/feedback.service';

@Component({
  selector: 'app-feedback',
  imports: [StarRatingComponent, FormsModule],
  templateUrl: './feedback.component.html',
  styleUrl: './feedback.component.css'
})
export class FeedbackComponent {
  @Output() close = new EventEmitter<void>();
  @Output() submitted = new EventEmitter<void>();
  userRating = 3;
  feedback: Feedback = new Feedback();
  constructor(private dialog: MatDialog, private feedbackService: FeedbackService) { }
  openFeedbackPopup() {
    this.dialog.open(FeedbackComponent, {
      width: '400px',
      data: { /* pass data here if needed */ }
    })
  }

  onSubmit() {
    this.feedback.rating = this.userRating;
    this.feedbackService.sendFeedback(this.feedback).subscribe({
      next: (data: Feedback) => {
        console.log(data)
        this.submitted.emit();
        this.close.emit();
      }
    })
  }
}
