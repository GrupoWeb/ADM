import { HttpClient } from "@angular/common/http";
import { ExternalUser } from "./external-user.model";

import { environment } from "../../environments/environment";
import { Injectable } from "@angular/core";
import { Subject } from "rxjs/Subject";

import { ResponseRs } from "./response.model";
import { Counter } from "./counter.model";
import { StatsFilter } from "./stats-filter.model";
import { ExternalUserStats } from "./external-user-stats.model";
import { Part } from "./part.model";
import { GuaranteeTransactionPart } from "./guarantee-transaction-part.model";

@Injectable()
export class ExternalUsersService {
  users: ExternalUser[] = [];
  usersChanged = new Subject<ExternalUser[]>();

  constructor(private http: HttpClient) {
  }

  getUsers() {
    return this.users.slice();
  }

  addUsers(users: ExternalUser[]) {
    this.users.push(...users);
    this.usersChanged.next(this.getUsers());
  }

  reinicioSaldo(code: string){
    return this.http.get<any>(environment.api_url + '/reinicio/saldo/' + code);
  }


  updateUserState(index: number, newUser: ExternalUser) {
    this.updateDataState(newUser.personaId, newUser).subscribe(
      data => {
        let updatedUser: ExternalUser = data;
        this.users.splice(index, 1);
        /*if (updatedUser.status === 'IN') {
          // cuenta aprobada
          this.users.splice(index, 1);
        } else {
          // cuenta rechazada
          this.users[index] = updatedUser;
        }*/
        this.usersChanged.next(this.getUsers());
        // let mensaje = newUser.status == 'A' ? 'activado' : (newUser.status == 'I' ? 'inactivado' : 'eliminado');
        // this.userOperationEnded.next('El usuario ' + updatedUser.name + ' fue ' + mensaje + ' exitosamente.');
      },
      err => console.error(err),
      () => console.log('Finalizada la actualizacion')
    );
  }

  fetchData(state: string, page: number, size: number, filtro: ExternalUser) {
    let param = (state === 'ALL' ? '' : (state === 'ALLNM' ? '?migracion=false' : (state === 'ALLCE' ? '?emailError=true' : '?state=' + state)));
    if (page) {
      param += (param.length === 0 ? '?' : '&') + 'page=' + page + '&size=' + size;
    }
    param += (filtro != null && filtro.getFilter().length > 0) ? (param.length === 0 ? '?' : '&') + filtro.getFilter() : '';
    let  resultado = this.http.get<ResponseRs>(environment.api_url + '/secu-usuarios' + param);
    console.log("URL: ", environment.api_url  + '/secu-usuarios' + param)
    return this.http.get<ResponseRs>(environment.api_url + '/secu-usuarios' + param);
  }

  fetchCountData(filter: StatsFilter) {
    let param = 'fechaInicio=' + filter.fechaInicio + '&fechaFin=' + filter.fechaFin;
    param += filter.im ? '&im=' + filter.im : '';
    return this.http.get<Counter[]>(environment.api_url + '/secu-usuarios/reporte?' + param);
  }

  fetchStatsData(filter: StatsFilter) {
    let param = filter.getFilter();
    return this.http.get<ExternalUserStats[]>(environment.api_url + '/secu-usuarios/stats?' + param);
  }

  fetchByEmail(email: string) {
    return this.http.get<ExternalUser>(environment.api_url + '/secu-usuarios/find?email=' + email);
  }

  search(query: string) {
    return this.http.get<ResponseRs>(environment.api_url + '/secu-usuarios/search?q=' + query);
  }

  fetchPartData(idPersona: number) {
    return this.http.get<GuaranteeTransactionPart[]>(environment.api_url + '/secu-usuarios/' + idPersona + '/partes');
  }

  fetchSaldoData(idPersona: number) {
    return this.http.get(environment.api_url + '/secu-usuarios/' + idPersona + '/saldo');
  }

  updateDataState(id: number, user: ExternalUser) {
    return this.http.put<ExternalUser>(environment.api_url + '/secu-usuarios/' + id + '/state', user);
  }

  modificarMigrado(vinculacion: any) {
    return this.http.put<ResponseRs>(environment.api_url + '/secu-usuarios/' + vinculacion.solicitante + '/modmigrado', vinculacion);
  }

  modificarCorreo(vinculacion: any) {
    return this.http.put<ResponseRs>(environment.api_url + '/secu-usuarios/' + vinculacion.solicitante + '/modcorreo', vinculacion);
  }
}
