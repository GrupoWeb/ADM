import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import 'rxjs/Rx';
import { Observable } from "rxjs/Observable";
import { Subject } from "rxjs/Subject";

import { Menu } from "./menus.model";
import { Counter } from "./counter.model";
import { environment } from "../../environments/environment";
import { ResponseRs } from "./response.model";
import { StatsFilter } from "./stats-filter.model";
import { Rol } from "./roles.model";

@Injectable()
export class MenusService {

  constructor(private http: HttpClient) { }
  fetchData(page: number, size: number) {
    let param = "";
    if (page) {
      param += (param.length === 0 ? '?' : '&') + 'page=' + page + '&size=' + size;    
    } 
    
    return this.http.get<ResponseRs>(environment.api_url + '/menus'+ param  );
  }
  fetchDataR() {
    return this.http.get<ResponseRs>(environment.api_url + '/roles'  );
  }
  fetchDataF(page: number, size: number,filtro: Menu) {
    let param = "";
    if (page) {
      param += (param.length === 0 ? '?' : '&') + 'page=' + page + '&size=' + size;    
    } 
    param += (filtro != null && filtro.getFilter().length > 0) ? (param.length === 0 ? '?' : '&') + filtro.getFilter() : '';
    
    return this.http.get<ResponseRs>(environment.api_url + '/menus'+ param  );
  }
  fetchDataC(filtro: Menu) {
    let param = "";
    param += (filtro != null && filtro.getFilter().length > 0) ? (param.length === 0 ? '?' : '&') + filtro.getFilter() : '';
    
 
    const formData = new FormData();
    formData.append('nombreMenu', filtro.nomMenu);
    formData.append('nombreSubMenu', filtro.nomSubMenu);
    formData.append('rol', filtro.rol);
    formData.append('link', filtro.link);
    
    return this.http.post<ResponseRs>(environment.api_url + '/menus/grabar' ,formData);
  }
  fetchDataCR(filtro: Rol) {
    let param = "";
    param += (filtro != null && filtro.getFilter().length > 0) ? (param.length === 0 ? '?' : '&') + filtro.getFilter() : '';
    
  
    const formData = new FormData();
    formData.append('nombre', filtro.nomRol);
    formData.append('tipo', filtro.tipoRol);
    
    
    return this.http.post<ResponseRs>(environment.api_url + '/roles/grabar' ,formData);
  }
  fetchDataMD(filtrom: Menu) {
    let param = "";
    param += (filtrom != null && filtrom.getFilter().length > 0) ? (param.length === 0 ? '?' : '&') + filtrom.getFilter() : '';
    var escabecera = filtrom.escabecera;
    const formData = new FormData();
    if(filtrom.escabecera==1){
      formData.append('escabecera',escabecera.toString()) ;
      formData.append('nomMenu',filtrom.nomMenu) ;
    }
    formData.append('menuId',filtrom.menuId.toString()) ;
    formData.append('nomSubMenu',filtrom.nomSubMenu) ;
    formData.append('link',filtrom.link) ;
    
    
    
    
    return this.http.post<ResponseRs>(environment.api_url + '/menus/borrar' ,formData);
  }
  fetchDataRD(filtro: Rol) {
    let param = "";
    param += (filtro != null && filtro.getFilter().length > 0) ? (param.length === 0 ? '?' : '&') + filtro.getFilter() : '';
    
    const formData = new FormData();
    
    formData.append('rolId',filtro.rolId.toString()) ;
    formData.append('nomRol',filtro.nomRol) ;
    
    
    return this.http.post<ResponseRs>(environment.api_url + '/roles/borrar' ,formData);
  }
}
