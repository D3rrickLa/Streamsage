import { AfterViewChecked, Component, ElementRef, Input, SimpleChanges, ViewChild } from '@angular/core';
import { SuggestionPackage } from '../../../models/domains/suggestion-package';
import { NgbCarouselModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { FeedbackComponent } from '../../users/feedback/feedback.component';
@Component({
  selector: 'app-recommendations',
  imports: [NgbCarouselModule, CommonModule, FeedbackComponent],
  templateUrl: './recommendations.component.html',
  styleUrl: './recommendations.component.css'
})
export class RecommendationsComponent implements AfterViewChecked {
  @Input() item: SuggestionPackage | null = null;
  @ViewChild('recommendationsSection') recommendationsSection!: ElementRef;
  
  isVisible: boolean = false;
  isLoggedIn: boolean = false;
  hasSubmittedFeedback = false;
  showPopup: boolean = false;
  expandedStates: { [key: string]: boolean } = {}; //tracking what title desc you expanded
  private hasScrolled = false; // so we don't scroll multiple times unnecessarily
  
  ngOnInit() {
    this.isRecVisible();
    this.isLoggedIn = this.hasToken(sessionStorage.getItem("token"));
  }
  ngOnChanges(changes: SimpleChanges) {
    if (changes['item'] && changes['item'].currentValue != null) {
      this.isRecVisible()
      this.onRecommendationGenerated()
    }
  }
  
  ngAfterViewChecked(): void {
     if (this.isVisible && this.recommendationsSection && !this.hasScrolled) {
      this.recommendationsSection.nativeElement.scrollIntoView({ behavior: 'smooth' });
      this.hasScrolled = true; // only scroll once per visibility toggle
    }

    if (!this.isVisible) {
      this.hasScrolled = false; // reset if hidden again
    }
  }


  isRecVisible(): void {
    if (this.item?.recommendationList != null) {
      this.isVisible = true;
    }
    else {
      this.isVisible = false;

    }
  }

  hide() {
    this.isVisible = false;
  }

  hasToken(token: any): boolean {
    if (token == null) {
      return false;
    }
    else { return true };
  }

  isExpanded(title: string | null = null): boolean {
    if(!title) return false;
    return this.expandedStates[title];
  }

  toggleExpanded(title: string | null = null): void {
    if (!title) return;
    this.expandedStates[title] = !this.expandedStates[title];
  }


  onRecommendationGenerated() {
    this.hasSubmittedFeedback = false // resets feedback vis
  }

  onFeedbackSubmitted() {
    this.showPopup = false;
    this.hasSubmittedFeedback = true;
  }
}
