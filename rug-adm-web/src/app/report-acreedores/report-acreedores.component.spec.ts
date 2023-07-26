import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportAcreedoresComponent } from './report-acreedores.component';

describe('ReportAcreedoresComponent', () => {
  let component: ReportAcreedoresComponent;
  let fixture: ComponentFixture<ReportAcreedoresComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReportAcreedoresComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportAcreedoresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
