import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {AvailabilityPageComponent} from "./pages/availability-page/availability-page.component";
import {AuthenticationGuard} from "../authentication/services/authentication.guard";
import {CreateAvailabilityPageComponent} from "./pages/create-availability-page/create-availability-page.component";
import {UpdateAvailabilityPageComponent} from "./pages/update-availability-page/update-availability-page.component";

const routes: Routes = [
    {path: '', component: AvailabilityPageComponent, canActivate: [AuthenticationGuard]},
    {path: 'create', component: CreateAvailabilityPageComponent, canActivate: [AuthenticationGuard]},
    {path: 'update/:id', component: UpdateAvailabilityPageComponent, canActivate: [AuthenticationGuard]},
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AvailabilityRoutingModule { }