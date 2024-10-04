import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterStudentComponent } from './register-student/register-student.component';
import { AddCoursesComponent } from './add-courses/add-courses.component';

const routes: Routes = [
  {
    path: 'register-student', 
    component: RegisterStudentComponent
  },
  {
    path: 'add-courses',
    component: AddCoursesComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LearningRoutingModule { }
