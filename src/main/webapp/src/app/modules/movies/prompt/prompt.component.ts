import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RecommendationsComponent } from '../recommendations/recommendations.component';


@Component({
  selector: 'app-prompt',
  imports: [FormsModule, CommonModule, RecommendationsComponent],
  templateUrl: './prompt.component.html',
  styleUrl: './prompt.component.css'
})
export class PromptComponent {
  message: string = '';
  showButton: boolean = false;
  @ViewChild('autoResizeTextarea') textarea!: ElementRef;
  
  adjustHeight(event: Event) {
    const target = event.target as HTMLTextAreaElement;
    target.style.height = 'auto'; // Reset height
    target.style.height = Math.min(target.scrollHeight, 200) + 'px'; // Grow until max-height
    target.style.overflowY = target.scrollHeight > 200 ? 'scroll' : 'hidden'; // Show scrollbar if needed
  }

  handleInput(event: Event) {
    this.showButton = this.message.length > 0;
    this.adjustHeight(event); // Calls height adjustment function
  }

  handleKeyDown(event: KeyboardEvent) {
      if (event.key === 'Enter' && !event.shiftKey) {
        event.preventDefault(); // Prevents new line
        this.onSubmit(); // Calls the submit function
      }
    }

  onSubmit() {
    console.log('Message sent:', this.message);
    
    const data = {
      prompt: this.message
    }
    
    this.message = ''; // Clears the textarea after submission
    this.showButton = false; // Hides the button again
    
    
  }
}
