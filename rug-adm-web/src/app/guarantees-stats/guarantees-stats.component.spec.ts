import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GuaranteesStatsComponent } from './guarantees-stats.component';

describe('GuaranteesStatsComponent', () => {
  let component: GuaranteesStatsComponent;
  let fixture: ComponentFixture<GuaranteesStatsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GuaranteesStatsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GuaranteesStatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
