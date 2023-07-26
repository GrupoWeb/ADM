import { Injectable } from "@angular/core";
import { Subject } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";

@Injectable()
export class PentahoService {

    constructor(private http: HttpClient){
    }

    fetchReport(url: string) {
      // return this.http.post(url, {'output-target': 'table/html;page-mode=stream'});
      return this.http.get(url);
    }
}
