export class Incident {
    public incidenteId: number;
    public asunto: string;
    public categoria: number;
    public descripcion: string;
    public diasPausa: number;
    public estado: number;
    public fechaCreacion: string;
    public fechaFin: string;
    public impacto: number;
    public modoIngreso: number;
    public prioridad: number;
    public resoulucion: string;
    public tipoSolicitud: number;
    public urgencia: number;
    public usuarioId: number;
    public usuarioSolicitante: string;
    public texto: string;

    getFilter() {
      let filter = '';
      if (this.texto && this.texto.length > 0) {
        filter += 'texto=' + this.texto + '&';
      }
      if (this.estado) {
        filter += 'estado=' + this.estado + '&';
      }
      if (this.impacto) {
        filter += 'impacto=' + this.impacto + '&';
      }
      //
      if (this.modoIngreso) {
        filter += 'modoIngreso=' + this.modoIngreso + '&';
      }
      if (this.tipoSolicitud) {
        filter += 'tipoSolicitud=' + this.tipoSolicitud + '&';
      }
      if (this.categoria) {
        filter += 'categoria=' + this.categoria + '&';
      }
      if (this.prioridad) {
        filter += 'prioridad=' + this.prioridad + '&';
      }
  
      filter = filter.substr(0, filter.length - 1);
  
      return filter;
    }
  }