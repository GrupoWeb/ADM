import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReinicioSaldoComponent } from './reinicio-saldo.component';

describe('ReinicioSaldoComponent', () => {
  let component: ReinicioSaldoComponent;
  let fixture: ComponentFixture<ReinicioSaldoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReinicioSaldoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReinicioSaldoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
