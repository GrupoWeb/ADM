import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import 'rxjs/Rx';
import { Observable } from "rxjs/Observable";
import { Subject } from "rxjs/Subject";

import { Deudor } from "./deudores.model";
import { Counter } from "./counter.model";
import { environment } from "../../environments/environment";
import { ResponseRs } from "./response.model";
import { StatsFilter } from "./stats-filter.model";
//import { DepositStats } from "./deposit-stats.model";

@Injectable()
export class DeudoresService {
  deudores: Deudor[] = [];
  depositsChanged = new Subject<Deudor[]>();
  constructor(private http: HttpClient) {}
  getDeposits() {

    return this.deudores.slice();
  }

  addDeposits(deposits: Deudor[]) {
    this.deudores.push(...deposits);
    this.depositsChanged.next(this.getDeposits());
  }

  
  fetchData(status: number, page: number, size: number, filtro: Deudor) {
    let param = (status != null ? '?status=' + status : '');
    if (page) {
      param += (param.length === 0 ? '?' : '&') + 'page=' + page + '&size=' + size;
    }
    
    param += (filtro != null && filtro.getFilter().length > 0) ? (param.length === 0 ? '?' : '&') + filtro.getFilter() : '';
    
    console.log(environment.api_url + '/deudores' + param);
    return this.http.get<ResponseRs>(environment.api_url + '/deudores' + param+ '&unico=0');
  }

  fetchData_b(status: number, page: number, size: number, filtro: Deudor) {
    let param = (status != null ? '?status=' + status : '');
    if (page) {
      param += (param.length === 0 ? '?' : '&') + 'page=' + page + '&size=' + size;
    }
    
    param += (filtro != null && filtro.getFilter().length > 0) ? (param.length === 0 ? '?' : '&') + filtro.getFilter() : '';
    
    console.log(environment.api_url + '/deudores' + param);
    return this.http.get<ResponseRs>(environment.api_url + '/deudores' + param);
  }

  fetchSumData(filter: StatsFilter) {
    let param = 'fechaInicio=' + filter.fechaInicio + '&fechaFin=' + filter.fechaFin;
    return this.http.get<Counter[]>(environment.api_url + '/boletas/reporte?' + param);
  }
  
  /*fetchStatsData(filter: StatsFilter) {
    let param = filter.getFilter();
    return this.http.get<DepositStats[]>(environment.api_url + '/boletas/stats?' + param);
  }*/

  updateDataState(id: number, deposit: Deudor) {
    return this.http.put<Deudor>(environment.api_url + '/boletas/' + id + '/state', deposit);
  }

}
