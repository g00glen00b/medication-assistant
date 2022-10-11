import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LoginPageComponent} from './pages/login-page/login-page.component';
import {AuthenticationRoutingModule} from "./authentication-routing.module";
import {SharedModule} from "../shared/shared.module";
import { LoginFormComponent } from './components/login-form/login-form.component';
import {ReactiveFormsModule} from "@angular/forms";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import { SignupPageComponent } from './pages/signup-page/signup-page.component';
import { SignupFormComponent } from './components/signup-form/signup-form.component';
import { UpdateProfilePageComponent } from './pages/update-profile-page/update-profile-page.component';
import { UpdateProfileFormComponent } from './components/update-profile-form/update-profile-form.component';
import { UpdateCredentialsFormComponent } from './components/update-credentials-form/update-credentials-form.component';
import {MatSelectModule} from "@angular/material/select";
import {MatCardModule} from "@angular/material/card";


@NgModule({
  declarations: [
    LoginPageComponent,
    LoginFormComponent,
    SignupPageComponent,
    SignupFormComponent,
    UpdateProfilePageComponent,
    UpdateProfileFormComponent,
    UpdateCredentialsFormComponent
  ],
    imports: [
        CommonModule,
        SharedModule,
        AuthenticationRoutingModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatSelectModule,
        MatCardModule
    ]
})
export class AuthenticationModule { }
