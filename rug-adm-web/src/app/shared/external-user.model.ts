import { Attachment } from "./attachment.model";

export class ExternalUser {
	public personaId: number;
	public name: string;
	public email: string;
	public docId: string;
	public nit: string;
	public status: string;
	public registered: Date;
	public registryCode: string;
	public files: Attachment[];
	public cause: string;
	public correosNoEnviados: number;
	public correosError: number;
	public respuesta: string;
	public nationality: number;
	public personType: string;
	public securityQuestion: string;
	public securityAnswer: string;
	public password: string;
	public parentEmail: string;
	public isJudicial: string;
	public groupId: number;
	public address: string;
	public legalInscription: string;
	public representativeInfo: string;

	getFilter() {
		let filter = '';
		if (this.name && this.name.length > 0) {
			filter += 'name=' + this.name + '&';
		}
		if (this.email && this.email.length > 0) {
			filter += 'email=' + this.email + '&';
		}
		if (this.docId && this.docId.length > 0) {
			filter += 'docId=' + this.docId + '&';
		}
		if (this.nit && this.nit.length > 0) {
			filter += 'nit=' + this.nit + '&';
		}
		if (this.status && this.status.length > 0) {
			filter += 'state=' + this.status + '&';
		}
		filter = filter.substr(0, filter.length -1);

		return filter;
	}
}
