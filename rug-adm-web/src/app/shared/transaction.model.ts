import { ExternalUser } from "./external-user.model";
import { Guarantee } from "./guarantee.model";
import { bienLista } from "./bienLista.model";

export class Transaction {
  public idTramite: number;
  public bCargaMasiva: number;
  public fechaCreacion: Date;
  public idStatusTramite: number;
  public idTramiteTemp: number;
  public statusReg: string;
  public idTipoTramite: number;
  public descripcion: string;
  public precio: number;
  public vigencia: number;
  public solicitante: ExternalUser;
  public guarantee: Guarantee;
  public bienLista: bienLista;
  public deudores: ExternalUser[];
  public acreedores: ExternalUser[];
  public controlCambios: string[];

  getFilter() {
    
    let filter = '';
    if (this.solicitante && this.solicitante.name && this.solicitante.name.length > 0) {
      filter += 'nombre=' + this.solicitante.name + '&';
    }
    if (this.guarantee && this.guarantee.idGarantia) {
      filter += 'numero=' + this.guarantee.idGarantia + '&';
    }
    filter = filter.substr(0, filter.length -1);

    return filter;
  }
}
