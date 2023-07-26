import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ExternalUser } from '../shared/external-user.model';
import { ExternalUsersService } from '../shared/external-users.service';
import { UsersService } from '../shared/users.service';
import { LoadingService } from '../shared/loading.service';

declare var Materialize:any;

@Component({
  selector: 'app-modificar-migrado',
  templateUrl: './modificar-migrado.component.html',
  styleUrls: ['./modificar-migrado.component.css']
})
export class ModificarMigradoComponent implements OnInit {
  public loading = false;
  solicitanteHttpSubscription: Subscription;
  modificarHttpSubscription: Subscription;
  searchUserForm: FormGroup;
  modificarForm: FormGroup;
  solicitante: ExternalUser;

  constructor(private externalUsersService: ExternalUsersService,
    private usersService: UsersService,
    private loadingService: LoadingService) { }

  ngOnInit() {
    this.initUserForm();
    this.initModificarForm();
  }

  private initUserForm() {
    this.searchUserForm = new FormGroup({
      'solicitante': new FormControl(null, Validators.required)
    });
  }

  private initModificarForm() {
    this.modificarForm = new FormGroup({
      'motivo': new FormControl(null, Validators.required)
    });
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

  onModificarSubmit() {
    let vinculacion = {
      solicitante: this.solicitante.personaId,
      causa: this.modificarForm.value.motivo,
      usuario: this.usersService.authenticatedUser.usuarioId
    }
    this.modificarHttpSubscription = this.externalUsersService.modificarMigrado(vinculacion).subscribe(
      res => {
        if (res.data) {
          this.onCancelClicked();
          Materialize.toast('La modificación fue realizada exitosamente.', 4000);
        } else {
          Materialize.toast('La modificación no se pudo realizar. Por favor verifique.', 4000);
        }
      },
      err => console.error(err),
      () => {}
    );
  }

  onCancelClicked() {
    this.solicitante = null;
    this.searchUserForm.reset();
    this.modificarForm.reset();
  }

  disabledModificar() {
    return !this.solicitante || !this.modificarForm.value.motivo;
  }
}
