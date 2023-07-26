import { HttpClient } from "@angular/common/http";

import { environment } from "../../environments/environment";
import { Injectable } from "@angular/core";
import { Subject } from "rxjs/Subject";

import { Counter } from "./counter.model";
import { GuaranteeStats } from "./guarantee-stats.model";
import { Transaction } from "./transaction.model";
import { StatsFilter } from "./stats-filter.model";
import { ResponseRs } from "./response.model";
import { Part } from "./part.model";
import { TransactionPreview } from "./transaction-preview.model";
import { User } from "./user.model";
import { FileUploader } from "ng2-file-upload";
import { RESULTADO_EXITOSO } from "./constants";
import { Observable } from "rxjs";
import { tokenNotExpired } from 'angular2-jwt';

@Injectable()
export class GuaranteesService {
  transactions: Transaction[] = [];
  transactionSaved: Subject<number> = new Subject<number>();
  authenticatedUser: User;

  constructor(private http: HttpClient) {
    this.authenticatedUser = JSON.parse(localStorage.getItem('currentUser'));
    if (this.authenticatedUser != null && !tokenNotExpired(null, this.authenticatedUser.token)) {
      this.authenticatedUser = null;
    }
  }

  getGuarantees() {
    return this.transactions.slice();
  }

  create(transaction: Transaction, uploader: FileUploader) {
    this.saveData(transaction).subscribe(
      (response) => {
        if (response.data == RESULTADO_EXITOSO) {
          if (uploader.queue.length > 0) {
            uploader.onBeforeUploadItem = (fileItem: any) => {
              fileItem.withCredentials = false;
            };
            uploader.setOptions({
              url: environment.api_url + '/garantias/' + transaction.guarantee.idGarantia + '/upload',
            });
            uploader.onCompleteAll = () => {
              this.transactionSaved.next(RESULTADO_EXITOSO);
            };
            uploader.uploadAll();
          } else {
            this.transactionSaved.next(RESULTADO_EXITOSO);
          }
        } else {
          this.transactionSaved.next(response.data);
        }
      }
    );
  }

  update(transaction: Transaction) {
    this.updateData(transaction).subscribe(
      (response) => {
        this.transactionSaved.next(RESULTADO_EXITOSO);

      }
    );
  }

  fetchCountData(filter: StatsFilter) {
    let param = 'fechaInicio=' + filter.fechaInicio + '&fechaFin=' + filter.fechaFin;
    param += filter.im ? '&im=' + filter.im : '';
    return this.http.get<Counter[]>(environment.api_url + '/garantias/reporte?' + param);
  }

  fetchCountTypeData(filter: StatsFilter) {
    let param = 'fechaInicio=' + filter.fechaInicio + '&fechaFin=' + filter.fechaFin;
    param += filter.im ? '&im=' + filter.im : '';
    return this.http.get<Counter[]>(environment.api_url + '/garantias/stats-tp?' + param);
  }

  fetchStatsData(filter: StatsFilter) {
    let param = filter.getFilter();
    return this.http.get<GuaranteeStats[]>(environment.api_url + '/garantias/stats?' + param);
  }

  signatureFile(idTramite: number, idGarantia: number) {
    // return this.http.post<ResponseRs>(environment.api_url + '/firma/signature/', this.jsonSignatureSend(idTramite, idGarantia, this.authenticatedUser.email));
    return this.http.get<ResponseRs>(environment.api_url + '/firma/signature/' + idTramite + '/'+ idGarantia + '/' + this.authenticatedUser.email);
  }

  checkSignature(name: string){
    return this.http.get(environment.signature_api + '/check/'+ name )
  }

  downloadFiles(name: string): Observable<Blob> {
    return this.http.get(environment.signature_api + '/file/' + name, { responseType: 'blob'})
  }

  fetchData(page: number, size: number, filtro: Transaction, fechaInicio: String, fechaFin: String) {
    console.log(filtro);
    let param = '';
    if (page) {
      param += '?page=' + page + '&size=' + size;
    }
    if (fechaInicio) {
      param += (param.length === 0 ? '?' : '&') + 'fechaInicio=' + fechaInicio + '&fechaFin=' + fechaFin;
    }
    param += (filtro != null && filtro.getFilter().length > 0) ? (param.length === 0 ? '?' : '&') + filtro.getFilter() : '';
    return this.http.get<ResponseRs>(environment.api_url + '/garantias' + param);
  }

  fetchTypeData(filter: StatsFilter) {
    let param = 'fechaInicio=' + filter.fechaInicio + '&fechaFin=' + filter.fechaFin;
    param += filter.im ? '&im=' + filter.im : '';
    return this.http.get<Counter[]>(environment.api_url + '/garantias/reporte-tp?' + param);
  }

  fetchPartData(idTramite: number) {
    return this.http.get<Part[]>(environment.api_url + '/garantias/' + idTramite + '/partes');
  }

  fetchDetailData(idTramite: number, idGarantia: number) {
    return this.http.get<TransactionPreview>(environment.api_url + '/garantias/' + idTramite + '/detalle?garantia=' + idGarantia);
  }

  fetchById(idGarantia: number) {
    return this.http.get<any>(environment.api_url + '/garantias/' + idGarantia);
  }

  vincular(idGarantia: number, vinculacion: any) {
    return this.http.put<ResponseRs>(environment.api_url + '/garantias/' + idGarantia + '/vincular', vinculacion);
  }

  fetchQueryData(page: number, size: number, fechaInicio: String, fechaFin: String) {
    let param = '';
    if (page) {
      param += '?page=' + page + '&size=' + size;
    }
    if (fechaInicio) {
      param = 'fechaInicio=' + fechaInicio + '&fechaFin=' + fechaFin;
    }
    return this.http.get<ResponseRs>(environment.api_url + '/garantias/consultas?' + param);
  }

  fetchVinculacionData(page: number, size: number, fechaInicio: String, fechaFin: String, garantia:String) {
    let param = '';
    if (page) {
      param += '?page=' + page + '&size=' + size;
    }
    if (fechaInicio) {
      param = 'fechaInicio=' + fechaInicio + '&fechaFin=' + fechaFin;
    }
    if (garantia)
    {
      param += '&garantia=' + garantia;
    }


    return this.http.get<ResponseRs>(environment.api_url + '/garantias/vinculaciones?' + param);
  }

  fetchTransactionsData(idGarantia: number) {
    console.log("URL ",environment.api_url , " id garantia" , idGarantia);
    return this.http.get<ResponseRs>(environment.api_url + '/garantias/' + idGarantia + '/tramites');
  }

  saveData(transaction: Transaction) {
    return this.http.post<ResponseRs>(environment.api_url + '/garantias', transaction);
  }

  updateData(transaction: Transaction) {
    return this.http.put(environment.api_url + '/garantias', transaction);
  }

  search(query: string) {
    return this.http.get<ResponseRs>(environment.api_url + '/garantias/search?q=' + query);
  }

  jsonSignature(name, flag){
    if(flag == 'check'){
      const json = { "garantia" : name }
      return JSON.stringify(json)
    }
  }

  jsonSignatureSend(tramite, garantia, email){
    const json = { tramite: tramite, garantia: garantia, email: email }
    return JSON.stringify(json)
  }
}
