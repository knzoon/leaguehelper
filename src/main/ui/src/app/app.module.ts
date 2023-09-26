import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import {ButtonModule} from 'primeng/button';
import {ChartModule} from 'primeng/chart';
import {CheckboxModule} from 'primeng/checkbox';
import {DialogModule} from 'primeng/dialog';
import {DropdownModule} from 'primeng/dropdown';
import {InputTextModule} from 'primeng/inputtext';
import {ListboxModule} from 'primeng/listbox';
import {AutoCompleteModule} from 'primeng/autocomplete';
import {InputNumberModule} from 'primeng/inputnumber';

import { AppComponent } from './app.component';
import { ChooseLeagueDialogComponent } from './choose-league-dialog/choose-league-dialog.component';
import { SaveLeagueDialogComponent } from './save-league-dialog/save-league-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    ChooseLeagueDialogComponent,
    SaveLeagueDialogComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    ButtonModule,
    ChartModule,
    CheckboxModule,
    DialogModule,
    DropdownModule,
    InputTextModule,
    ListboxModule,
    AutoCompleteModule,
    InputNumberModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
