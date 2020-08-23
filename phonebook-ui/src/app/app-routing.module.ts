import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RegistrationComponent} from "./registration/registration.component";
import {ActivateEmailComponent} from "./activate-email/activate-email.component";
import {ActivationComponent} from "./activation/activation.component";
import {ForgotPasswordComponent} from "./forgot-password/forgot-password.component";
import {PasswordRecoveryComponent} from "./password-recovery/password-recovery.component";
import {LoginComponent} from "./login/login.component";
import {WelcomePageComponent} from "./pages/welcome-page/welcome-page.component";

const routes: Routes = [
  {path: '', redirectTo: 'user/home', pathMatch: 'full'},
  {path: 'user/login', component: LoginComponent},
  {path: 'user/registration', component: RegistrationComponent},
  {path: 'user/activate-email', component: ActivateEmailComponent},
  {path: 'user/activation/:token', component: ActivationComponent},
  {path: 'user/forgot-password', component: ForgotPasswordComponent},
  {path: 'user/password-recovery/:token', component: PasswordRecoveryComponent},
  {path: 'user/home', component: WelcomePageComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
