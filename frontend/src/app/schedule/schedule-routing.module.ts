import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {AuthenticationGuard} from "../authentication/services/authentication.guard";
import {SchedulePageComponent} from "./pages/schedule-page/schedule-page.component";
import {CreateSchedulePageComponent} from "./pages/create-schedule-page/create-schedule-page.component";
import {UpdateSchedulePageComponent} from "./pages/update-schedule-page/update-schedule-page.component";

const routes: Routes = [
    {path: '', component: SchedulePageComponent, canActivate: [AuthenticationGuard]},
    {path: 'create', component: CreateSchedulePageComponent, canActivate: [AuthenticationGuard]},
    {path: 'update/:id', component: UpdateSchedulePageComponent, canActivate: [AuthenticationGuard]},
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ScheduleRoutingModule { }