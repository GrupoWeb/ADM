export class Usuario {

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
  public idUsuario_b : String;
 

  getFilter() {

    let filter = '';

    if (this.textobusqueda && this.textobusqueda.length > 0) {

      filter += 'textobusqueda=' + this.textobusqueda + '&';
    }
    if (this.idUsuario_b && this.idUsuario_b.length > 0) {

      filter += 'idUsuario_b=' + this.idUsuario_b + '&';
    }
    filter = filter.substr(0, filter.length -1);
    return filter;

  }

}

