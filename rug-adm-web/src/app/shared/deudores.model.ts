export class Deudor {

  public idPersona: number;
  public rfc: string;
  public nombrePersona: string;
  public perJuridica: string;
  public idNacionalidad: number;
  public nomNacionalidad: string;
  public email: string;
  public idDomicilio: number;
  public direccion : string;
  public cGarantias: number;
  public cVigentes: number;
  public cnVigentes: number;
  public cCanceladas: number;
  public textobusqueda : string;
  public idDeudor_b : String;
  public dusuarios : String;
  getFilter() {

    let filter = '';

    if (this.textobusqueda && this.textobusqueda.length > 0) {

      filter += 'textobusqueda=' + this.textobusqueda + '&';
    }
    if (this.idDeudor_b && this.idDeudor_b.length > 0) {

      filter += 'idDeudor_b=' + this.idDeudor_b + '&';
    }
    if (this.dusuarios && this.dusuarios.length > 0) {

      filter += 'dusuarios=' + this.dusuarios + '&';
    }
    filter = filter.substr(0, filter.length -1);
    return filter;

  }

}

