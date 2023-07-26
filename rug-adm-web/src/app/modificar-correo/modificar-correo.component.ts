import { Component, OnInit } from '@angular/core';
import { ExternalUsersService } from '../shared/external-users.service';
import { LoadingService } from '../shared/loading.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { ExternalUser } from '../shared/external-user.model';
import { UsersService } from '../shared/users.service';

declare var Materialize:any;

@Component({
  selector: 'app-modificar-correo',
  templateUrl: './modificar-correo.component.html',
  styleUrls: ['./modificar-correo.component.css']
})
export class ModificarCorreoComponent implements OnInit {
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
      'nuevoCorreo': new FormControl(null, Validators.required)
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
    let modificacion = {
      solicitante: this.solicitante.personaId,
      solicitanteNuevo: this.modificarForm.value.nuevoCorreo,
      usuario: this.usersService.authenticatedUser.usuarioId
    }
    if (modificacion.solicitante == modificacion.solicitanteNuevo) {
      Materialize.toast('El nuevo correo no puede ser igual al actual.', 4000);
      return false;
    }
    this.modificarHttpSubscription = this.externalUsersService.modificarCorreo(modificacion).subscribe(
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
    return !this.solicitante || !this.modificarForm.value.nuevoCorreo;
  }
}
