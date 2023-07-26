import { TransactionPart } from "./transaction-part.model";
import { SpecialGood } from "./special-good.model";

export class TransactionPreview {
  	idTramite: number;
	idGarantia: number;
	fechaInscripcion: string;
	fechaUltAsiento: string;
	fechaAsiento: string;
	tipoAsiento: string;
	vigencia: number;
	descbienes: string;
	contrato: string;
	otrosTerminosR: string;
	aBoolean: boolean;
	aBooleanNoGaraOt: boolean;
	aPrioridad: boolean;
	aRegistro: boolean;
	instrumento: string;
	otrosterminos: string;
	otroscontrato: string;
	otrosgarantia: string;
	deudorTOs: TransactionPart[];
	acreedorTOs: TransactionPart[];
	bienesEspTOs: SpecialGood[];
	otorganteTOs: TransactionPart[];
	textos: string[];
}
