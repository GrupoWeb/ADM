import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import 'rxjs/Rx';
import { Observable } from "rxjs/Observable";
import { Subject } from "rxjs/Subject";

import { Mail } from "./mail.model";
import { environment } from "../../environments/environment";
import { ResponseRs } from "./response.model";

@Injectable()
export class MailsService {
  mails: Mail[] = [];
  mailsChanged = new Subject<Mail[]>();

  constructor(private http: HttpClient) {}

  getMails() {
    return this.mails.slice();
  }

  addMails(mails: Mail[]) {
    this.mails.push(...mails);
    this.mailsChanged.next(this.getMails());
  }

  updateMailState(index: number, newMail: Mail) {
    this.updateDataState(newMail.idMail, newMail.idStatusMail).subscribe(
      data => {
        let updatedMail: Mail = data;
        this.mails[index] = updatedMail;
        this.mailsChanged.next(this.mails.slice());
      },
      err => console.error(err),
      () => console.log('Finalizada la actualizacion')
    );
  }

  forwardMail(index: number, idMail: number) {
    this.forwardDataMail(idMail).subscribe(
      data => {
        this.mails.splice(index, 1);
        this.mailsChanged.next(this.mails.slice());
      },
      err => console.error(err),
      () => console.log('Finalizada la actualizacion')
    );
  }

  fetchData(idPersona: number, todos: boolean) {
    let param: string = '';
    if(todos) {
      param = '?todos=true'
    }
    return this.http.get<Mail[]>(environment.api_url + '/secu-usuarios/' + idPersona + '/mails' + param);
  }

  forwardDataMail(idMail: number) {
    return this.http.post<string>(environment.api_url + '/mail/' + idMail + '/send', {responseType: 'text'});
  }

  updateDataState(id: number, status: number) {
    return this.http.put<Mail>(environment.api_url + '/mail/' + id + '/state', status);
  }
}
