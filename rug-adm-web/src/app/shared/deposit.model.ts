import { ExternalUser } from "../shared/external-user.model";

export class Deposit {
  public id: number;
  public agencia: string;
  public codigoTramite: number;
  public fechaHora: Date;
  public idGarantia: number;
  public idTransaccion: string;
  public identificador: string;
  public monto: number;
  public montoOtrosBancos : number;
  public numero: string;
  public resolucion: string;
  public serie: string;
  public usada: number;
  public tieneArchivo: boolean;
  public url: string;
  public tipoPago: string;
  public externalUser: ExternalUser;
  public cause: string;
  public fechaInicio: string;
  public fechaFin: string;

  getFilter() {
    let filter = '';
    if (this.agencia && this.agencia.length > 0) {
      filter += 'agencia=' + this.agencia + '&';
    }
    if (this.numero && this.numero.length > 0) {
      filter += 'numero=' + this.numero + '&';
    }
    if (this.externalUser && this.externalUser.name && this.externalUser.name.length > 0) {
      filter += 'externalUser=' + this.externalUser.name + '&';
    }
    if (this.tipoPago && this.tipoPago.length > 0) {
      filter += 'tipoPago=' + this.tipoPago + '&';
    }
    if (this.usada) {
      filter += 'status=' + this.usada + '&';
    }

    if (this.fechaInicio && this.fechaInicio.length > 0) {
      filter += 'fechaInicio=' + this.fechaInicio + '&';
      filter += 'fechaFin=' + this.fechaFin + '&';
    }

    filter = filter.substr(0, filter.length -1);

    return filter;
  }
}
