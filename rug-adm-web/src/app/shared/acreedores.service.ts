import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import 'rxjs/Rx';
import { Observable } from "rxjs/Observable";
import { Subject } from "rxjs/Subject";

import { Acreedor } from "./acreedores.model";
import { Counter } from "./counter.model";
import { environment } from "../../environments/environment";
import { ResponseRs } from "./response.model";
import { StatsFilter } from "./stats-filter.model";
//import { DepositStats } from "./deposit-stats.model";

@Injectable()
export class AcreedoresService {
  acreedores: Acreedor[] = [];
  depositsChanged = new Subject<Acreedor[]>();
  constructor(private http: HttpClient) {}
  getDeposits() {

    return this.acreedores.slice();
  }

  addDeposits(deposits: Acreedor[]) {
    this.acreedores.push(...deposits);
    this.depositsChanged.next(this.getDeposits());
  }

  
  fetchData(status: number, page: number, size: number, filtro: Acreedor) {
    let param = (status != null ? '?status=' + status : '');
    if (page) {
      param += (param.length === 0 ? '?' : '&') + 'page=' + page + '&size=' + size;
    }
    console.log("FILTRO:"+filtro);
    console.log(filtro.getFilter().length)
    param += (filtro != null && filtro.getFilter().length > 0) ? (param.length === 0 ? '?' : '&') + filtro.getFilter() : '';
    // console.log("parametro: ", param, " url ", environment.api_url);
    console.log(environment.api_url + '/acreedores' + param);
    return this.http.get<ResponseRs>(environment.api_url + '/acreedores' + param );
  }
  fetchData_b(status: number, page: number, size: number, filtro: Acreedor) {
    let param = (status != null ? '?status=' + status : '');
    if (page) {
      param += (param.length === 0 ? '?' : '&') + 'page=' + page + '&size=' + size ;
    }
    console.log("FILTRO:"+filtro);
    console.log(filtro.getFilter().length)
    param += (filtro != null && filtro.getFilter().length > 0) ? (param.length === 0 ? '?' : '&') + filtro.getFilter() : '';
    // console.log("parametro: ", param, " url ", environment.api_url);
    console.log(environment.api_url + '/acreedores' + param);
    return this.http.get<ResponseRs>(environment.api_url + '/acreedores' + param );
  }
  

  fetchSumData(filter: StatsFilter) {
    let param = 'fechaInicio=' + filter.fechaInicio + '&fechaFin=' + filter.fechaFin;
    return this.http.get<Counter[]>(environment.api_url + '/boletas/reporte?' + param);
  }
  
  /*fetchStatsData(filter: StatsFilter) {
    let param = filter.getFilter();
    return this.http.get<DepositStats[]>(environment.api_url + '/boletas/stats?' + param);
  }*/

  updateDataState(id: number, deposit: Acreedor) {
    return this.http.put<Acreedor>(environment.api_url + '/boletas/' + id + '/state', deposit);
  }

}
