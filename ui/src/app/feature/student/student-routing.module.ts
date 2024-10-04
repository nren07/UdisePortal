import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ViewCoursesComponent } from './view-courses/view-courses.component';

const routes: Routes = [
  {
    path: 'view-courses',
    component: ViewCoursesComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class StudentRoutingModule { }
