import { ExternalUser } from "../shared/external-user.model";

export class Guarantee {
  public idGarantia: number;
  public descGarantia: string;
  public tipoContrato: string;
  public original: string;
  public otrosTerminos: string;
  public esPrioritaria: string;
  public fechaFinGar: Date;
  public fechaInscr: Date;
  public fechaReg: Date;
  public folioMercantil: string;
  public garantiaStatus: string;
  public persona: ExternalUser;
  public idTipoGarantia: number;
  public instrumentoPublico: string;
  public mesesGarantia: number;
  public numGarantia: number;
  public otrosTerminosGarantia: string;
  public statusReg: string;
  public tiposBienesMuebles: string;
  public txtRegistros: string;
  public valorBienes: number;
  public vigencia: number;
  public solicitante: ExternalUser;
  public deudores: ExternalUser[];
  public acreedores: ExternalUser[];
  
  /*getFilter() {
    let filter = '';
    if (this.agencia && this.agencia.length > 0) {
      filter += 'agencia=' + this.agencia + '&';
    }
    if (this.numero && this.numero.length > 0) {
      filter += 'numero=' + this.numero + '&';
    }
    if (this.persona && this.persona.name && this.persona.name.length > 0) {
      filter += 'persona=' + this.persona.name + '&';
    }
    if (this.tipoPago && this.tipoPago.length > 0) {
      filter += 'tipoPago=' + this.tipoPago + '&';
    }
    filter = filter.substr(0, filter.length -1);

    return filter;
  }*/
}
