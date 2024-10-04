import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-register-student',
  templateUrl: './register-student.component.html',
  styleUrls: ['./register-student.component.scss']
})
export class RegisterStudentComponent implements OnInit {

  userForm!: FormGroup;
  ngOnInit() { }

  constructor(private fb: FormBuilder) {
    this.userForm = this.fb.group({
      StudentName: ['', Validators.required],
      email: ['', Validators.required],
      PhoneNumber: ['', [Validators.required,]],
      StudentId: ['', [Validators.required,]],
      courses: ['', Validators.required]
    });
  }


  onSubmit() {
    if (this.userForm.valid) {
      console.log(this.userForm.value);
    }
  }

  reset() {
    
      this.userForm.reset();
    }
  }
