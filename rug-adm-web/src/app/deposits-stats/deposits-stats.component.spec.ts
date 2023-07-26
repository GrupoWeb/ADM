import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DepositsStatsComponent } from './deposits-stats.component';

describe('DepositsStatsComponent', () => {
  let component: DepositsStatsComponent;
  let fixture: ComponentFixture<DepositsStatsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DepositsStatsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DepositsStatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
