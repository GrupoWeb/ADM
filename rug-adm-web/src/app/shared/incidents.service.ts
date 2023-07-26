import { Injectable } from "@angular/core";
import { Incident } from "./incident.model";
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";
import { Subject } from "rxjs";
import { ResponseRs } from "./response.model";
import { Attachment } from "./attachment.model";

@Injectable()
export class IncidentsService {
    incidents: Incident[] = [];
    incidentsChanged = new Subject<Incident[]>();
    attachments: Attachment[] = [];
    attachmentsChanged = new Subject<Attachment[]>();

    constructor(private http: HttpClient){

    }

    getIncidents() {
        return this.incidents.slice();
    }

    getAttachments() {
        return this.attachments.slice();
    }

    addIncident(incident: Incident) {
        this.saveData(incident).subscribe(
            (response) => {
                let savedIncident: Incident = response;
                this.incidents.push(savedIncident);
                this.incidentsChanged.next(this.getIncidents());
            },
            err => {
                console.log(err);
            }
        );
    }

    addAttachment(attachment: Attachment, attachments: Attachment[]) {
        this.attachments = attachments;
        this.saveDataAtt(attachment).subscribe(
            (response) => {
                let savedAttachment: Attachment = response;
                this.attachments.push(savedAttachment);
                this.attachmentsChanged.next(this.getAttachments());
            },
            err => {
                console.log(err);
            }
        );
    }

    addIncidents(incidents: Incident[]) {
        this.incidents.push(...incidents);
        this.incidentsChanged.next(this.getIncidents());
    }

    updateIncidents(index: number, newIncident: Incident) {
        this.updateData(newIncident.incidenteId, newIncident).subscribe(
            (response) => {
                let updatedIncident: Incident = response;
                this.incidents[index] = updatedIncident;
                this.incidentsChanged.next(this.getIncidents());
            }
        );
    }

    fetchData(page: number, size: number, filtro: Incident, fechaInicio: String, fechaFin: String) {
        let param = '';
        if (page) {
            param += '?page=' + page + '&size=' + size;
        }
        if (fechaInicio) {
            param += (param.length === 0 ? '?' : '&') + 'fechaInicio=' + fechaInicio + '&fechaFin=' + fechaFin;
        }
        param += (filtro != null && filtro.getFilter().length > 0) ? (param.length === 0 ? '?' : '&') + filtro.getFilter() : '';
        return this.http.get<ResponseRs>(environment.api_url + '/incidentes' + param)
    }

    fetchDataAtt(incidenteId : number) {
        return this.http.get<ResponseRs>(environment.api_url + '/adjuntos?incidenteId=' + incidenteId)
    }

    saveData(incident: Incident){
        return this.http.post<Incident>(environment.api_url + '/incidentes/',incident);
    }

    saveDataAtt(attachment: Attachment){
        return this.http.post<Attachment>(environment.api_url + '/adjuntos/',attachment.adjunto);
    }

    updateData(id: number, incident: Incident){
        return this.http.put<Incident>(environment.api_url + '/incidentes/' + id,incident);
    }
}