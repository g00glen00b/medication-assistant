import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {LoginPageComponent} from "./pages/login-page/login-page.component";
import {UnauthenticatedGuard} from "./services/unauthenticated.guard";
import {SignupPageComponent} from "./pages/signup-page/signup-page.component";
import {UpdateProfilePageComponent} from "./pages/update-profile-page/update-profile-page.component";
import {AuthenticationGuard} from "./services/authentication.guard";

const routes: Routes = [
    {path: 'login', component: LoginPageComponent, canActivate: [UnauthenticatedGuard]},
    {path: 'signup', component: SignupPageComponent, canActivate: [UnauthenticatedGuard]},
    {path: 'profile', component: UpdateProfilePageComponent, canActivate: [AuthenticationGuard]},
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AuthenticationRoutingModule { }