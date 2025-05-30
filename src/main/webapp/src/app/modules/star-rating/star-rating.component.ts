import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-star-rating',
  imports: [CommonModule],
  templateUrl: './star-rating.component.html',
  styleUrl: './star-rating.component.css'
})
export class StarRatingComponent {
  @Input() rating = 0;
  @Input() max = 5;
  @Output() ratingChange = new EventEmitter<number>();

  setRating(value: number) {
    this.rating = value;
    this.ratingChange.emit(this.rating);
  }
}
