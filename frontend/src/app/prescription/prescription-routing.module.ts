import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {AuthenticationGuard} from "../authentication/services/authentication.guard";
import {PrescriptionPageComponent} from "./pages/prescription-page/prescription-page.component";

const routes: Routes = [
  {path: '', component: PrescriptionPageComponent, canActivate: [AuthenticationGuard]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PrescriptionRoutingModule { }