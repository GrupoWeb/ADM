export class Acreedor {

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
  public textobusqueda : String;
  public idAcreedor_b : String;
  public ausuarios : String;

  getFilter() {

    let filter = '';

    if (this.textobusqueda && this.textobusqueda.length > 0) {

      filter += 'textobusqueda=' + this.textobusqueda + '&';
    }
    if (this.idAcreedor_b && this.idAcreedor_b.length > 0) {

      filter += 'idAcreedor_b=' + this.idAcreedor_b + '&';
    }
    if (this.ausuarios && this.ausuarios.length > 0) {

      filter += 'ausuarios=' + this.ausuarios + '&';
    }
    filter = filter.substr(0, filter.length -1);
    return filter;

  }

}

