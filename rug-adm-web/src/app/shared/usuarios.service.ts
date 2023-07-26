import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import 'rxjs/Rx';
import { Observable } from "rxjs/Observable";
import { Subject } from "rxjs/Subject";

import { Usuario } from "./usuarios.model";
import { Counter } from "./counter.model";
import { environment } from "../../environments/environment";
import { ResponseRs } from "./response.model";
import { StatsFilter } from "./stats-filter.model";
import { DepositStats } from "./deposit-stats.model";

@Injectable()
export class UsuariosService {
  usuarios: Usuario[] = [];
  depositsChanged = new Subject<Usuario[]>();

  constructor(private http: HttpClient) {}

  getDeposits() {

    return this.usuarios.slice();
  }

  addDeposits(deposits: Usuario[]) {
    this.usuarios.push(...deposits);
    this.depositsChanged.next(this.getDeposits());
  }

  updateDepositState(index: number, newDeposit: Usuario) {
    this.updateDataState(newDeposit.idPersona, newDeposit).subscribe(
      data => {
        let updatedDeposit: Usuario = data;
        this.usuarios.splice(index, 1);
        /*if (updatedDeposit.usada == 1) {
          // deposito aprobado
          this.deposits.splice(index, 1);
        } else {
          this.deposits[index] = updatedDeposit;
        }*/
        this.depositsChanged.next(this.usuarios.slice());
      },
      err => console.error(err),
      () => console.log('Finalizada la actualizacion')
    );
  }

  fetchData(status: number, page: number, size: number, filtro: Usuario) {
    let param = (status != null ? '?status=' + status : '');
    if (page) {
      param += (param.length === 0 ? '?' : '&') + 'page=' + page + '&size=' + size;
    }
    param += (filtro != null && filtro.getFilter().length > 0) ? (param.length === 0 ? '?' : '&') + filtro.getFilter() : '';
    console.log(environment.api_url + '/usuario' + param);
    return this.http.get<ResponseRs>(environment.api_url + '/usuario' + param);
  }
  fetchData_b(status: number, page: number, size: number, filtro: Usuario) {
    let param = (status != null ? '?status=' + status : '');
    if (page) {
      param += (param.length === 0 ? '?' : '&') + 'page=' + page + '&size=' + size;
    }
    param += (filtro != null && filtro.getFilter().length > 0) ? (param.length === 0 ? '?' : '&') + filtro.getFilter() : '';
    console.log(environment.api_url + '/usuario' + param);
    return this.http.get<ResponseRs>(environment.api_url + '/usuario' + param);
  }
  fetchSumData(filter: StatsFilter) {
    let param = 'fechaInicio=' + filter.fechaInicio + '&fechaFin=' + filter.fechaFin;
    return this.http.get<Counter[]>(environment.api_url + '/usuario/reporte?' + param);
  }
  
  fetchStatsData(filter: StatsFilter) {
    let param = filter.getFilter();
    return this.http.get<DepositStats[]>(environment.api_url + '/usuario/stats?' + param);
  }

  updateDataState(id: number, deposit: Usuario) {
    return this.http.put<Usuario>(environment.api_url + '/usuario/' + id + '/state', deposit);
  }
}
