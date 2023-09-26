import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SaveLeagueDialogComponent } from './save-league-dialog.component';

describe('SaveLeagueDialogComponent', () => {
  let component: SaveLeagueDialogComponent;
  let fixture: ComponentFixture<SaveLeagueDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SaveLeagueDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SaveLeagueDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
