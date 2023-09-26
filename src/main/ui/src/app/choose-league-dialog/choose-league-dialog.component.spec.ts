import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChooseLeagueDialogComponent } from './choose-league-dialog.component';

describe('ChooseLeagueDialogComponent', () => {
  let component: ChooseLeagueDialogComponent;
  let fixture: ComponentFixture<ChooseLeagueDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChooseLeagueDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChooseLeagueDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
