import { Injectable } from "@angular/core";
import { Change } from "./changed.model";
import { Subject } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";
import { ResponseRs } from "./response.model";

@Injectable()
export class ChangeService {
    changes: Change[] = [];
    changesChanged = new Subject<Change[]>();

    constructor(private http: HttpClient){        
    }

    getChanges() {
        return this.changes.slice();
    }

    addChange(change: Change){
        this.saveData(change).subscribe(
            (response) => {
                let savedChange: Change = response;
                this.changes.push(savedChange);
                this.changesChanged.next(this.getChanges());
            },
            err => {
                console.log(err);
            }
        );
    }

    addChanges(changes: Change[]){
        this.changes.push(...changes);
        this.changesChanged.next(this.getChanges());
    }

    updateChange(index: number, newChange: Change){
        this.updateData(newChange.cambioId, newChange).subscribe(
            (response) => {
                let updatedChange: Change = response;
                this.changes[index] = updatedChange;
                this.changesChanged.next(this.getChanges());
            }
        );
    }

    fetchData(page: number, size: number, filtro: Change, fechaInicio: String, fechaFin: String) {
        let param = '';
        if (page) {
            param += '?page=' + page + '&size=' + size;
        }
        if (fechaInicio) {
            param += (param.length === 0 ? '?' : '&') + 'fechaInicio=' + fechaInicio + '&fechaFin=' + fechaFin;
        }
        param += (filtro != null && filtro.getFilter().length > 0) ? (param.length === 0 ? '?' : '&') + filtro.getFilter() : '';
        return this.http.get<ResponseRs>(environment.api_url + '/cambios' + param);
    }

    saveData(change: Change) {
        return this.http.post<Change>(environment.api_url + "/cambios/", change);
    }

    updateData(id: number, change: Change) {
        return this.http.put<Change>(environment.api_url + "/cambios/" + id, change);
    }
}