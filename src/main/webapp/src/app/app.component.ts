import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { PromptComponent } from './modules/movies/prompt/prompt.component';
import { AuthComponent } from './modules/auth/auth.component';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { FooterComponent } from './shared/footer/footer.component';
import { RecommendationsComponent } from './modules/movies/recommendations/recommendations.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavbarComponent, FooterComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'webapp';
}
