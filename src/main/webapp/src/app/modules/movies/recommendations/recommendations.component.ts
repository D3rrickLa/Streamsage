import { Component, Input, SimpleChanges } from '@angular/core';
import { SuggestionPackage } from '../../../models/domains/suggestion-package';
import { NgbCarouselModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-recommendations',
  imports: [NgbCarouselModule, CommonModule],
  templateUrl: './recommendations.component.html',
  styleUrl: './recommendations.component.css'
})
export class RecommendationsComponent {
  @Input() item: SuggestionPackage | null = null;
  isVisible: boolean = false;
  isLoggedIn: boolean = false;
  expandedStates: { [key: string]: boolean } = {}; //tracking what title desc you expanded

  ngOnInit() {
    this.isRecVisible();
    this.isLoggedIn = this.hasToken(sessionStorage.getItem("token"));
  }
  ngOnChanges(changes: SimpleChanges) {
    if (changes['item'] && changes['item'].currentValue != null) {
      this.isRecVisible()
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

}
