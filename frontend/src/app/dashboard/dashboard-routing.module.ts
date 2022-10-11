import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {AuthenticationGuard} from "../authentication/services/authentication.guard";
import {DashboardPageComponent} from "./pages/dashboard-page/dashboard-page.component";

const routes: Routes = [
  {path: '', component: DashboardPageComponent, canActivate: [AuthenticationGuard]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule { }