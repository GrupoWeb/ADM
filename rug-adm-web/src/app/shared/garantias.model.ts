export class Garantias {

    public idGarantia: number;
    public movs: number;
    public inscr: number;
    public cert: number;
    public modif: number;
    public cancelado: number;
    public ejec: number;
    public prorr: number;
    public fInscr: Date;
    public idTramite: number;
    public dTramite: String;
    public vigencia: number;
    public dGarantia: String;
    public gStatus: String;
    public solicitante: String;
    public sOriginal: String;
    public idDeudor: String;
    public nDeudor: String;
    public idAcreedor: String;
    public nAcreedor: String;
    public idUsuario: String;
    public nUsuario: String;
    public textobusqueda: String;
    public fechaInicio : String;
    public fechaFin : String;
   
  
    getFilter() {
  
      let filter = '';
  
      if (this.textobusqueda && this.textobusqueda.length > 0) {
  
        filter += 'textobusqueda=' + this.textobusqueda + '&';
      }
      if (this.idDeudor && this.idDeudor.length > 0) {
  
        filter += 'idDeudor=' + this.idDeudor + '&';
      }
      if (this.idGarantia && this.idGarantia > 0) {
        
        filter += 'idGarantia=' + this.idGarantia + '&';
      }
      if (this.fechaInicio && this.fechaInicio.length > 0) {

        filter += 'fechaInicio=' + this.fechaInicio + '&';
        filter += 'fechaFin=' + this.fechaFin + '&';
      }
      if (this.idAcreedor && this.idAcreedor.length > 0) {
  
        filter += 'idAcreedor=' + this.idAcreedor + '&';
      }
      if (this.idUsuario && this.idUsuario.length > 0) {
  
        filter += 'idUsuario=' + this.idUsuario + '&';
      }
      if (this.nDeudor && this.nDeudor.length > 0) {
  
        filter += 'nDeudor=' + this.nDeudor + '&';
      }
      if (this.nAcreedor && this.nAcreedor.length > 0) {
  
        filter += 'nAcreedor=' + this.nAcreedor + '&';
      }
      filter = filter.substr(0, filter.length -1);
      return filter;
  
    }
  
  }
  
  