import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LearningRoutingModule } from './learning-routing.module';
import { RegisterStudentComponent } from './register-student/register-student.component';
import { InputTextModule } from 'primeng/inputtext';
import { ReactiveFormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { AddCoursesComponent } from './add-courses/add-courses.component';

@NgModule({
  declarations: [
    RegisterStudentComponent,
    AddCoursesComponent
  ],
  imports: [
    CommonModule,
    LearningRoutingModule,
     ReactiveFormsModule,
     InputTextModule,
     CardModule
  ]
})
export class LearningModule { }
