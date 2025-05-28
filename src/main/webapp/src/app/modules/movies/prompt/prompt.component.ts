import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RecommendationsComponent } from '../recommendations/recommendations.component';
import { PromptService } from '../../../services/prompt.service';
import { SuggestionPackage } from '../../../models/domains/suggestion-package';


@Component({
  selector: 'app-prompt',
  standalone: true,
  imports: [FormsModule, CommonModule, RecommendationsComponent],
  templateUrl: './prompt.component.html',
  styleUrl: './prompt.component.css'
})
export class PromptComponent {
  message: string = '';
  showButton: boolean = false;
  loading: boolean = false;
  errorMessage: string | null = null
  recData: SuggestionPackage = new SuggestionPackage;
  @ViewChild('autoResizeTextarea') textarea!: ElementRef;


  constructor(private promptService: PromptService) {}
  
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
    this.loading = true;
    if (this.recData != null) {
      this.recData == null
    }
    
    const data = {
      prompt: this.message
    }
    
    
    this.promptService.postPrompt(data).subscribe({
      next: (data: SuggestionPackage) => {
        console.log("Response from API:", data);
        this.promptService.onAIResponse.emit(data);
        this.recData = data;
        this.loading = false;
      },
      error: (err) => {
        console.error("Error occurred:", err)
        this.loading = false;
        this.errorMessage = err.error?.message || 'An problem on our end occurred, please try again later.';
    }})

    this.message = ''; // Clears the textarea after submission
    this.showButton = false; // Hides the button again
  }
}
