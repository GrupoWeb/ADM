import { Component, OnInit } from '@angular/core';
import { GuaranteesService } from '../shared/guarantees.service';
import { Subscription } from 'rxjs';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { LoadingService } from '../shared/loading.service';
import { Guarantee } from '../shared/guarantee.model';
import { ExternalUsersService } from '../shared/external-users.service';
import { ExternalUser } from '../shared/external-user.model';
import { UsersService } from '../shared/users.service';

declare var Materialize:any;

@Component({
  selector: 'app-vinculacion',
  templateUrl: './vinculacion.component.html',
  styleUrls: ['./vinculacion.component.css']
})
export class VinculacionComponent implements OnInit {
  public loading = false;
  garantiaHttpSubscription: Subscription;
  solicitanteHttpSubscription: Subscription;
  vincularHttpSubscription: Subscription;
  searchGuaranteeForm: FormGroup;
  searchUserForm: FormGroup;
  vincularForm: FormGroup;
  garantia: Guarantee;
  solicitante: ExternalUser;

  constructor(private guaranteesService: GuaranteesService,
    private externalUsersService: ExternalUsersService,
    private usersService: UsersService,
    private loadingService: LoadingService) { }

  ngOnInit() {
    this.initGuaranteeForm();
    this.initUserForm();
    this.initVincularForm();
  }

  private initGuaranteeForm() {
    this.searchGuaranteeForm = new FormGroup({
      'garantia': new FormControl(null, Validators.required)
    });
  }

  private initUserForm() {
    this.searchUserForm = new FormGroup({
      'solicitante': new FormControl(null, Validators.required)
    });
  }

  private initVincularForm() {
    this.vincularForm = new FormGroup({
      'motivo': new FormControl(null, Validators.required)
    });
  }

  onSearchGuaranteeSubmit() {
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    let garantia = this.searchGuaranteeForm.value.garantia;
    this.garantiaHttpSubscription = this.guaranteesService.fetchById(garantia).subscribe(
      data => {
        this.garantia = data.guarantee;
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }

  onSearchUserSubmit() {
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    let solicitante = this.searchUserForm.value.solicitante;
    this.solicitanteHttpSubscription = this.externalUsersService.fetchByEmail(solicitante).subscribe(
      data => {
        this.solicitante = data;
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }

  onVincularSubmit() {
    let idGarantia = this.garantia.idGarantia;
    let vinculacion = {
      solicitante: this.solicitante.personaId,
      causa: this.vincularForm.value.motivo,
      usuario: this.usersService.authenticatedUser.usuarioId
    }
    if (vinculacion.solicitante == this.garantia.solicitante.personaId) {
      Materialize.toast('La garantía no se puede vincular al usuario actual.', 4000);
      return false;
    }
    this.vincularHttpSubscription = this.guaranteesService.vincular(idGarantia, vinculacion).subscribe(
      res => {
        if (res.data) {
          this.onCancelClicked();
          Materialize.toast('La vinculación fue realizada exitosamente.', 4000);
        } else {
          Materialize.toast('La vinculación no se pudo realizar. Por favor verifique.', 4000);
        }
      },
      err => console.error(err),
      () => {}
    );
  }

  onCancelClicked() {
    this.garantia = null;
    this.solicitante = null;
    this.searchGuaranteeForm.reset();
    this.searchUserForm.reset();
    this.vincularForm.reset();
  }

  disabledVincular() {
    return !this.garantia || !this.solicitante || !this.vincularForm.value.motivo;
  }
}
