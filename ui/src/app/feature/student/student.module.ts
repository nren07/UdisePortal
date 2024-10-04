import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AvatarModule } from 'primeng/avatar';
import { StudentRoutingModule } from './student-routing.module';
import { ViewCoursesComponent } from './view-courses/view-courses.component';
import { AvatarGroupModule } from 'primeng/avatargroup';

@NgModule({
  declarations: [
    ViewCoursesComponent
  ],
  imports: [
    CommonModule,
    StudentRoutingModule,
    AvatarModule,AvatarGroupModule
  ]
})
export class StudentModule { }
