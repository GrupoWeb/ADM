import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportUsuariosComponent } from './report-usuarios.component';

describe('ReportUsuariosComponent', () => {
  let component: ReportUsuariosComponent;
  let fixture: ComponentFixture<ReportUsuariosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReportUsuariosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportUsuariosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
