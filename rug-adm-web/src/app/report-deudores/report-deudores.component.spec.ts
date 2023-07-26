import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportDeudoresComponent } from './report-deudores.component';

describe('ReportDeudoresComponent', () => {
  let component: ReportDeudoresComponent;
  let fixture: ComponentFixture<ReportDeudoresComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReportDeudoresComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportDeudoresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
