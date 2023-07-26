import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExternalUsersStatsComponent } from './external-users-stats.component';

describe('ExternalUsersStatsComponent', () => {
  let component: ExternalUsersStatsComponent;
  let fixture: ComponentFixture<ExternalUsersStatsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExternalUsersStatsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExternalUsersStatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
