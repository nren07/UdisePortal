import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthModule } from './auth/auth.module';
import { LayoutComponent } from './layout/layout.component';
import {  } from '@coreui/angular';
import { AvatarModule } from 'primeng/avatar';
import { AvatarGroupModule } from 'primeng/avatargroup';


@NgModule({
  declarations: [
    AppComponent,
    LayoutComponent,

    
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule, AuthModule,
    AvatarModule,AvatarGroupModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
