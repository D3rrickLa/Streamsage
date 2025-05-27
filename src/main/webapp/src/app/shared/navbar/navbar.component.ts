import { Component } from '@angular/core';
import { AuthService } from '../../modules/auth/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, NgbDropdownModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  constructor(public authService: AuthService, private router: Router) {}

  logout() {
    this.authService.logout()
    this.router.navigate(["/"])
  }

  goToSettings() {
    this.router.navigate(['/account'])
  }
}
