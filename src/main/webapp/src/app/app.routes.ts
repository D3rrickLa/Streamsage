import { RouterModule, Routes } from '@angular/router';
import { PromptComponent } from './modules/movies/prompt/prompt.component';
import { LoginComponent } from './modules/auth/login/login.component';
import { RegisterComponent } from './modules/auth/register/register.component';
import { NgModule } from '@angular/core';
import { UsersComponent } from './modules/users/users.component';
import { authGuard } from './auth.guard';
import { ContactComponent } from './modules/contact/contact.component';

export const routes: Routes = [
    {path: '', component: PromptComponent},
    {path: 'login', component: LoginComponent},
    {path: 'register', component: RegisterComponent},
    {path: 'account', component: UsersComponent, canActivate: [authGuard]},
    {path: 'contact', component: ContactComponent}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}