export class Change {
	public cambioId: number;
	public usuarioId: number;
	public usuarioSolicitante: string;
	public sistema: string;
	public version: string;
	public descripcion: string;
	public estado: number;
	public fechaRegistro: string;
	public fechaInicio: string;
	public fechaFin: string;
	public impacto: number;
	public observaciones: string;
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

		filter = filter.substr(0, filter.length - 1);

		return filter;
	}
}