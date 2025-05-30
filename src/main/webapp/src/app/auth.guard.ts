import { inject } from '@angular/core';
import { CanActivateFn, mapToCanActivate, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const token = sessionStorage.getItem('token');
  const router = inject(Router)
  if (token) {
    return true;
  }

  router.navigate(['/login']);

  return false;
};
