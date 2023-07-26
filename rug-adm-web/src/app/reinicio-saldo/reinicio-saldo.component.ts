import { Component, OnInit, EventEmitter } from '@angular/core';
import { Subscription } from 'rxjs';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { LoadingService } from '../shared/loading.service';
import { ExternalUsersService } from '../shared/external-users.service';
import { MaterializeAction } from 'angular2-materialize';

declare var Materialize:any;

@Component({
  selector: 'app-reinicio-saldo',
  templateUrl: './reinicio-saldo.component.html',
  styleUrls: ['./reinicio-saldo.component.css']
})
export class ReinicioSaldoComponent implements OnInit {
  public loading = false;
  searchUserForm: FormGroup;
  httpSubscription: Subscription;
  result: String
  flag = false

  constructor(private externalUsersService: ExternalUsersService, private loadingService: LoadingService) { }

  ngOnInit() {
    this.initUserForm();
  }

  onSearchUserSubmit(){
    const query =  this.searchUserForm.value.query
    if (!query) {
      Materialize.toast('Debe ingresar un criterio de búsqueda.', 4000);
      return false;
    }
    this.loading = true
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.externalUsersService.reinicioSaldo(query).subscribe(
      response => {
        this.result = response.data
        this.flag = true
        this.loading = false
        this.loadingService.changeLoading(this.loading);
      },
      err => {
        this.flag = false
      }
    )
    
    
    
  }

  private initUserForm() {
    this.searchUserForm = new FormGroup({
      'query': new FormControl(null, Validators.required)
    });
  }

}
