import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit {
  isList!: number;
  isMenu: boolean = false;
  isSearch: boolean = false;
  constructor(private _router: Router){}
  ngOnInit(): void {
    
  }
  redirect(){
    this._router.navigate(['app/register-student'])
  }
  // var sideBar = document.getElementById("mobile-nav");
  // var openSidebar = document.getElementById("openSideBar");
  // var closeSidebar = document.getElementById("closeSideBar");
  // sideBar.style.transform = "translateX(-260px)";
  
  // function sidebarHandler(flag) {
  //     if (flag) {
  //         sideBar.style.transform = "translateX(0px)";
  //         openSidebar.classList.add("hidden");
  //         closeSidebar.classList.remove("hidden");
  //     } else {
  //         sideBar.style.transform = "translateX(-260px)";
  //         closeSidebar.classList.add("hidden");
  //         openSidebar.classList.remove("hidden");
  //     }
  // }

 
   

}
