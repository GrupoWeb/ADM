import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import 'rxjs/Rx';
import { Observable } from "rxjs/Observable";
import { Subject } from "rxjs/Subject";

import { Garantias } from "./garantias.model";
import { Counter } from "./counter.model";
import { environment } from "../../environments/environment";
import { ResponseRs } from "./response.model";
import { StatsFilter } from "./stats-filter.model";
//import { DepositStats } from "./deposit-stats.model";

@Injectable()
export class GarantiasService {
  garantias: Garantias[] = [];
  depositsChanged = new Subject<Garantias[]>();
  constructor(private http: HttpClient) {}
  getDeposits() {

    return this.garantias.slice();
  }

  addDeposits(deposits: Garantias[]) {
    this.garantias.push(...deposits);
    this.depositsChanged.next(this.getDeposits());
  }

  
  fetchData(status: number, page: number, size: number, filtro: Garantias) {
    let param = (status != null ? '?status=' + status : '');
    if (page) {
      param += (param.length === 0 ? '?' : '&') + 'page=' + page + '&size=' + size;    
    }    
    param += (filtro != null && filtro.getFilter().length > 0) ? (param.length === 0 ? '?' : '&') + filtro.getFilter() : '';
    console.log(environment.api_url + '/garantia' + param +'&acreedor_b=0');
    return this.http.get<ResponseRs>(environment.api_url + '/garantia' + param +'&acreedor_b=0');
  }
  fetchData_acr(status: number, page: number, size: number, filtro: Garantias) {
    let param = (status != null ? '?status=' + status : '');
    if (page) {
      param += (param.length === 0 ? '?' : '&') + 'page=' + page + '&size=' + size;
    }
    
    param += (filtro != null && filtro.getFilter().length > 0) ? (param.length === 0 ? '?' : '&') + filtro.getFilter() : '';
    
    console.log(environment.api_url + '/garantia' + param +'&acreedor_b=1');
    return this.http.get<ResponseRs>(environment.api_url + '/garantia' + param +'&acreedor_b=1');
  }
  fetchData_all_acr(status: number, page: number, size: number, filtro: Garantias) {
    
    let param = (status != null ? '?status=' + status : '');
    if (page) {
      param += (param.length === 0 ? '?' : '&') + 'page=' + page + '&size=' + size;
    }   
    param += (filtro != null && filtro.getFilter().length > 0) ? (param.length === 0 ? '?' : '&') + filtro.getFilter() : '';
     
    console.log(environment.api_url + '/garantia/' + param +'&acreedor_b=0');
    return this.http.get<ResponseRs>(environment.api_url + '/garantia' + param +'&acreedor_b=0');
  }
  fetchSumData(filter: StatsFilter) {
    let param = 'fechaInicio=' + filter.fechaInicio + '&fechaFin=' + filter.fechaFin;
    return this.http.get<Counter[]>(environment.api_url + '/boletas/reporte?' + param);
  }
  
  /*fetchStatsData(filter: StatsFilter) {
    let param = filter.getFilter();
    return this.http.get<DepositStats[]>(environment.api_url + '/boletas/stats?' + param);
  }*/

  updateDataState(id: number, deposit: Garantias) {
    return this.http.put<Garantias>(environment.api_url + '/boletas/' + id + '/state', deposit);
  }

}
