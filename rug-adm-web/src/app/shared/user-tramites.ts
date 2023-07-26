import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { UserModel } from "./userModel";


@Injectable()
export class UserTramites {

    private httpOptions = {
        headers: new HttpHeaders({
          'content-type':'application/json; charset=iso-8859-1'
        })
      };

    user: UserModel[] = [];

    constructor(private http: HttpClient){}

    getTramites(isUsuario: string){
        return this.http.post<any>(environment.api_oracle + '/reporte-tramites', this.jsonUser(isUsuario), this.httpOptions);
    }

    jsonUser(idUsuario){
        const json = { codigo : idUsuario }
        return JSON.stringify(json)
    }
}