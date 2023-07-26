import { Injectable } from "@angular/core";
import { Term } from "./term.model";
import { Subject } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { ResponseRs } from "./response.model";
import { environment } from "../../environments/environment";

@Injectable()
export class TermsService {
  terms: Term[] = [];
  termsChanged = new Subject<Term[]>();

  constructor(private http: HttpClient) {}

  getTerms() {
    return this.terms.slice();
  }

  addTerm(term: Term) {
    this.saveData(term).subscribe(
      (response) => {
        let savedTerm: Term = response;
        this.terms.push(savedTerm);
        this.termsChanged.next(this.getTerms());
      },
      err => console.error(err)
    );
  }

  addTerms(terms: Term[]) {
    this.terms.push(...terms);
  }

  updateTerm(index: number, newTerm: Term) {
    this.updateData(newTerm).subscribe(
      (response) => {
        let updatedTerm: Term = response;
        this.terms[index] = updatedTerm;
        this.termsChanged.next(this.getTerms());
      }
    );
  }

  fetchData(page: number, size: number, incluirSinClasificar: boolean) {
    let param = '';
    if (page) {
      param += '?page=' + page + '&size=' + size;
    }
    if (incluirSinClasificar) {
      param += (page ? '&' : '?') + 'sin-clasificar=' + incluirSinClasificar;
    }
    return this.http.get<ResponseRs>(environment.api_url + '/terminos' + param);
  }

  saveData(term: Term) {
    return this.http.post<Term>(environment.api_url + '/terminos', term);
  }

  updateData(term: Term) {
    return this.http.put<Term>(environment.api_url + '/terminos/' + term.clasificacionTerminoId, term);
  }
}
