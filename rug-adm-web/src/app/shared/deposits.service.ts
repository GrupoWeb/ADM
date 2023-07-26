import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import 'rxjs/Rx';
import { Observable } from "rxjs/Observable";
import { Subject } from "rxjs/Subject";

import { Deposit } from "./deposit.model";
import { Counter } from "./counter.model";
import { environment } from "../../environments/environment";
import { ResponseRs } from "./response.model";
import { StatsFilter } from "./stats-filter.model";
import { DepositStats } from "./deposit-stats.model";

@Injectable()
export class DepositsService {
  deposits: Deposit[] = [];
  depositsChanged = new Subject<Deposit[]>();

  constructor(private http: HttpClient) {}

  getDeposits() {

    return this.deposits.slice();
  }

  addDeposits(deposits: Deposit[]) {
    this.deposits.push(...deposits);
    this.depositsChanged.next(this.getDeposits());
  }

  updateDepositState(index: number, newDeposit: Deposit) {
    this.updateDataState(newDeposit.id, newDeposit).subscribe(
      data => {
        let updatedDeposit: Deposit = data;
        this.deposits.splice(index, 1);
        /*if (updatedDeposit.usada == 1) {
          // deposito aprobado
          this.deposits.splice(index, 1);
        } else {
          this.deposits[index] = updatedDeposit;
        }*/
        this.depositsChanged.next(this.deposits.slice());
      },
      err => console.error(err),
      () => console.log('Finalizada la actualizacion')
    );
  }

  fetchData(status: number, page: number, size: number, filtro: Deposit) {
    let param = (status != null ? '?status=' + status : '');
    if (page) {
      param += (param.length === 0 ? '?' : '&') + 'page=' + page + '&size=' + size;
    }
    param += (filtro != null && filtro.getFilter().length > 0) ? (param.length === 0 ? '?' : '&') + filtro.getFilter() : '';
    // console.log("parametro: ", param, " url ", environment.api_url);
    return this.http.get<ResponseRs>(environment.api_url + '/boletas' + param);
  }

  fetchSumData(filter: StatsFilter) {
    let param = 'fechaInicio=' + filter.fechaInicio + '&fechaFin=' + filter.fechaFin;
    return this.http.get<Counter[]>(environment.api_url + '/boletas/reporte?' + param);
  }
  
  fetchStatsData(filter: StatsFilter) {
    let param = filter.getFilter();
    return this.http.get<DepositStats[]>(environment.api_url + '/boletas/stats?' + param);
  }

  updateDataState(id: number, deposit: Deposit) {
    return this.http.put<Deposit>(environment.api_url + '/boletas/' + id + '/state', deposit);
  }
}
