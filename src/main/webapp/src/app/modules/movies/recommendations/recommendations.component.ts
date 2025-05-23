import { Component, Input } from '@angular/core';
import { SuggestionPackage } from '../../../models/domains/suggestion-package';
import { NgbCarouselModule } from '@ng-bootstrap/ng-bootstrap';
@Component({
  selector: 'app-recommendations',
  imports: [NgbCarouselModule],
  templateUrl: './recommendations.component.html',
  styleUrl: './recommendations.component.css'
})
export class RecommendationsComponent {
  @Input() item = new SuggestionPackage();
}
