import { Component, OnInit, EventEmitter, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Guarantee } from '../shared/guarantee.model';
import { GuaranteesService } from '../shared/guarantees.service';
import { LoadingService } from '../shared/loading.service';
import { MaterializeAction } from 'angular2-materialize';
import { TransactionPreview } from '../shared/transaction-preview.model';
import * as moment from 'moment';
import { Person } from '../shared/person.model';
import { TransactionPart } from '../shared/transaction-part.model';
import { Transaction } from '../shared/transaction.model';
import { ExternalUser } from '../shared/external-user.model';
import { CountriesService } from '../shared/countries.service';
import { Country } from '../shared/country.model';
import { PERSONA_DEUDOR, PERSONA_ACREEDOR, PERSONA_SOLICITANTE, PERSONA_JURIDICA } from '../shared/constants';
import { environment } from '../../environments/environment';
import { SwalComponent } from '@toverux/ngx-sweetalert2';
import { Router } from '@angular/router';

declare var Materialize: any;

@Component({
  selector: 'app-correcciones',
  templateUrl: './correcciones.component.html',
  styleUrls: ['./correcciones.component.css']
})
export class CorreccionesComponent implements OnInit {
  public loading = false;
  garantiaHttpSubscription: Subscription;
  searchGuaranteeForm: FormGroup;
  tramites: Transaction[];
  garantia: Guarantee;
  modalActions = new EventEmitter<string | MaterializeAction>();
  modalTransaction: TransactionPreview;
  transaction: Transaction;
  guarantee: Guarantee;
  deudores: ExternalUser[];
  acreedores: ExternalUser[];
  countries: Country[];
  personForm: FormGroup;
  personFormTitle: string;
  personFormType: number;
  personFormEdit: boolean;
  personFormIndex: number;
  guaranteeForm: FormGroup;
  fechaInscripcionEdit: boolean;
  descGarantiaEdit: boolean;
  ContratoEdit: boolean;
  representanteInfo: boolean;
  instrumentoPublicoEdit: boolean;
  otrosTerminosGarantiaEdit: boolean;
  controlCambios: string[];
  modificarPersonas: boolean = false;
  transactionSubscription: Subscription;
  baseUrl: string = environment.base_url;
  @ViewChild('swalSearchGuarantee') private swalSearchGuarantee: SwalComponent;
  @ViewChild('swalNoChanges') private swalNoChanges: SwalComponent;

  constructor(private countriesService: CountriesService,
    private guaranteesService: GuaranteesService,
    private loadingService: LoadingService,
    private router: Router) { }

  ngOnInit() {
    this.countriesService.fetchData("AC").subscribe(
      data => {
        this.countries = data.data;
      }
    );
    this.transactionSubscription = this.guaranteesService.transactionSaved.subscribe(
      data => {
        this.resetAll();
      }
    );
    this.initSearchGuaranteeForm();
    this.initPersonForm(null);
    this.initGuaranteeForm(null);
  }

  private initSearchGuaranteeForm() {
    this.searchGuaranteeForm = new FormGroup({
      'garantia': new FormControl(null, Validators.required)
    });
  }

  private initPersonForm(person: ExternalUser) {
    this.personForm = new FormGroup({
      'personType': new FormControl(person ? person.personType : null, Validators.required),
      'nationality': new FormControl(person ? person.nationality : null, Validators.required),
      'name': new FormControl(person ? person.name : null, Validators.required),
      'docId': new FormControl(person ? person.docId : null, Validators.required),
      'nit': new FormControl(person ? person.nit : null, Validators.required),
      'email': new FormControl(person ? person.email : null, Validators.required),
      'address': new FormControl(person ? person.address : null),
      'legalInscription': new FormControl(person ? person.legalInscription : null),
      'representativeInfo': new FormControl(person ? person.representativeInfo : null)
    });
  }

  private initGuaranteeForm(transaction: Transaction) {
    this.guaranteeForm = new FormGroup({
      'numGarantia': new FormControl(transaction ? transaction.guarantee.idGarantia : null, Validators.required),
      'numTramite': new FormControl(transaction ? transaction.idTramite : null, Validators.required),
      'fechaCreacion': new FormControl(transaction ? transaction.fechaCreacion : null, Validators.required),
      'descGarantia': new FormControl(transaction ? transaction.guarantee.descGarantia : null, Validators.required),
      'desContrato': new FormControl(transaction ? transaction.guarantee.tipoContrato : null, Validators.required),
      'otrosTerminosR': new FormControl(transaction ? transaction.guarantee.otrosTerminos : null, Validators.required),
      'instrumentoPublico': new FormControl(transaction ? transaction.guarantee.instrumentoPublico : null, Validators.required),
      'otrosTerminos': new FormControl(transaction ? transaction.guarantee.otrosTerminosGarantia : null, Validators.required)
    });
  }

  onSearchGuaranteeSubmit() {
    let garantia = this.searchGuaranteeForm.value.garantia;
    if (garantia) {
      this.loading = true;
      this.loadingService.changeLoading(this.loading);
      this.garantiaHttpSubscription = this.guaranteesService.fetchTransactionsData(garantia).subscribe(
        data => {
          this.tramites = data.data;
          console.log("data tramite", this.tramites);
          this.controlCambios = [];
          this.modalTransaction = null;
        },
        err => console.error(err),
        () => {
          this.loading = false;
          this.loadingService.changeLoading(this.loading);
        }
      );
    } else {
      this.swalSearchGuarantee.show();
    }
  }

  onViewPartsClicked(index: number) {
    this.transaction = this.tramites[index];
    this.garantia = this.tramites[index].guarantee;
    this.deudores = this.garantia.deudores;
    this.acreedores = this.garantia.acreedores;
    this.initGuaranteeForm(this.tramites[index]);

    this.descGarantiaEdit = false;
    this.ContratoEdit = false;
    this.representanteInfo = false;
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.modalTransaction = new TransactionPreview;
    this.modalTransaction.idGarantia = this.garantia.idGarantia;
    this.modalTransaction.fechaInscripcion = moment(this.tramites[index].fechaCreacion, moment.defaultFormatUtc).format('DD/MM/YYYY');
    this.modalTransaction.descbienes = this.garantia.descGarantia;
    this.modalTransaction.contrato = this.garantia.tipoContrato;
    this.modalTransaction.otrosTerminosR = this.garantia.otrosTerminos;

    this.modalTransaction.instrumento = this.garantia.instrumentoPublico;
    this.modalTransaction.otrosgarantia = this.garantia.otrosTerminosGarantia;
    this.modalTransaction.vigencia = this.garantia.mesesGarantia / 12;

    this.loading = false;
    this.loadingService.changeLoading(this.loading);
  }

  onAddDeudorClicked() {
    this.personFormTitle = "Deudor";
    this.personFormType = PERSONA_DEUDOR;
    this.personFormEdit = false;
    this.modalActions.emit({ action: "modal", params: ["open"] });
  }

  onAddAcreedorClicked() {
    this.personFormTitle = "Acreedor";
    this.personFormType = PERSONA_ACREEDOR;
    this.personFormEdit = false;
    this.modalActions.emit({ action: "modal", params: ["open"] });
  }

  onEditDeudorClicked(deudor: ExternalUser, index: number) {
    this.initPersonForm(deudor);
    Materialize.updateTextFields();
    this.personFormTitle = "Deudor";
    this.personFormType = PERSONA_DEUDOR;
    this.personFormEdit = true;
    this.personFormIndex = index;
    this.modalActions.emit({ action: "modal", params: ["open"] });
  }

  onRemoveDeudorClicked(index: number) {
    this.deudores.splice(index, 1);
  }

  onEditAcreedorClicked(acreedor: ExternalUser, index: number) {
    this.initPersonForm(acreedor);
    Materialize.updateTextFields();
    this.personFormTitle = "Acreedor";
    this.personFormType = PERSONA_ACREEDOR;
    this.personFormEdit = true;
    this.personFormIndex = index;
    this.modalActions.emit({ action: "modal", params: ["open"] });
  }

  onRemoveAcreedorClicked(index: number) {
    this.acreedores.splice(index, 1);
  }

  onSavePersonSubmit() {
    let persona = new ExternalUser;
    persona.personType = this.personForm.value.personType;
    persona.nationality = this.personForm.value.nationality;
    persona.name = this.personForm.value.name;
    persona.docId = this.personForm.value.docId;
    persona.nit = this.personForm.value.nit;
    persona.email = this.personForm.value.email;
    persona.parentEmail = persona.email;
    persona.address = this.personForm.value.address;
    persona.legalInscription = this.personForm.value.legalInscription;
    persona.representativeInfo = this.personForm.value.representativeInfo;

    switch (this.personFormType) {
      case PERSONA_DEUDOR:
        this.deudores.push(persona);
        break;
      case PERSONA_ACREEDOR:
        this.acreedores.push(persona);
        break;
    }
    this.onCancelClicked();
  }

  onCancelClicked() {
    this.modalActions.emit({ action: "modal", params: ['close'] });
    this.personForm.reset();
  }

  closeModal() {
    this.modalActions.emit({ action: "modal", params: ['close'] });
  }

  disabledPerson() {
    return !this.personForm.valid ||
      (this.personFormType != PERSONA_SOLICITANTE &&
        !this.personForm.value.address ||
        (this.personForm.value.personType == PERSONA_JURIDICA &&
          (!this.personForm.value.legalInscription || !this.personForm.value.representativeInfo)));
  }

  onEditFechaInscripcionClicked() {
    this.fechaInscripcionEdit = true;
  }

  onSaveFechaInscripcionClicked() {
    this.fechaInscripcionEdit = false;
    this.modalTransaction.fechaInscripcion = this.guaranteeForm.value.fechaCreacion;
    if (this.controlCambios.indexOf("fechaInscripcion") < 0) {
      this.controlCambios.push("fechaInscripcion");
    }
  }

  onCancelFechaInscripcionClicked() {
    this.fechaInscripcionEdit = false;
  }

  onEditDescGarantiaClicked() {
    this.descGarantiaEdit = true;
  }

  onEditContratoClicked() {
    this.ContratoEdit = true;
  }

  onEditRepresentanteClicked() {
    this.representanteInfo = true;
  }

  onSaveDescGarantiaClicked() {
    this.descGarantiaEdit = false;
    // this.ContratoEdit= false;
    this.modalTransaction.descbienes = this.guaranteeForm.value.descGarantia;
    if (this.controlCambios.indexOf("descGarantia") < 0) {
      this.controlCambios.push("descGarantia");
    }
  }

  onSaveContratoClicked() {
    // this.descGarantiaEdit = false;
    this.ContratoEdit = false;
    this.modalTransaction.contrato = this.guaranteeForm.value.desContrato;
    if (this.controlCambios.indexOf("desContrato") < 0) {
      this.controlCambios.push("desContrato");
    }
  }

  onSaveRepresentanteClicked() {
    // this.descGarantiaEdit = false;
    this.representanteInfo = false;
    this.modalTransaction.otrosTerminosR = this.guaranteeForm.value.otrosTerminosR;
    if (this.controlCambios.indexOf("otrosTerminos") < 0) {
      this.controlCambios.push("otrosTerminos");
    }
  }

  onCancelDescGarantiaClicked() {
    this.descGarantiaEdit = false;
  }

  onCancelContratoClicked() {
    this.ContratoEdit = false;
  }

  onCancelRepresentanteClicked() {
    this.representanteInfo = false;
  }

  onEditInstrumentoPublicoClicked() {
    this.instrumentoPublicoEdit = true;
  }

  onSaveInstrumentoPublicoClicked() {
    this.instrumentoPublicoEdit = false;
    this.modalTransaction.instrumento = this.guaranteeForm.value.instrumentoPublico;
    if (this.controlCambios.indexOf("instrumentoPublico") < 0) {
      this.controlCambios.push("instrumentoPublico");
    }
  }

  onCancelInstrumentoPublicoClicked() {
    this.instrumentoPublicoEdit = false;
  }

  onEditOtrosTerminosGarantiaClicked() {
    this.otrosTerminosGarantiaEdit = true;
  }

  onSaveOtrosTerminosGarantiaClicked() {
    this.otrosTerminosGarantiaEdit = false;
    this.modalTransaction.otrosgarantia = this.guaranteeForm.value.otrosTerminos;
    if (this.controlCambios.indexOf("otrosTerminosGarantia") < 0) {
      this.controlCambios.push("otrosTerminosGarantia");
    }
  }

  onCancelOtrosTerminosGarantiaClicked() {
    this.otrosTerminosGarantiaEdit = false;
  }

  onSaveGarantiaSubmit() {
    if (this.controlCambios.length > 0) {
      this.garantia.fechaInscr = this.guaranteeForm.value.fechaCreacion;
      this.garantia.descGarantia = this.guaranteeForm.value.descGarantia;
      this.garantia.tipoContrato = this.guaranteeForm.value.desContrato;
      this.garantia.otrosTerminos = this.guaranteeForm.value.otrosTerminosR;
      this.garantia.otrosTerminosGarantia = this.guaranteeForm.value.otrosTerminos;
      this.garantia.instrumentoPublico = this.guaranteeForm.value.instrumentoPublico;
      this.transaction.deudores = this.deudores;
      this.transaction.acreedores = this.acreedores;
      this.transaction.guarantee = this.garantia;
      // this.transaction.idTramite = this.guaranteeForm.value.numTramite;
      this.transaction.fechaCreacion = this.garantia.fechaInscr;
      this.transaction.controlCambios = this.controlCambios;
      this.guaranteesService.update(this.transaction);
      console.log(this.transaction);
    } else {
      // mostrar que no hay cambios
      this.swalNoChanges.show();
    }
  }

  onNewSearchClicked() {
    this.resetAll();
  }

  resetAll() {
    this.searchGuaranteeForm.reset();
    this.personForm.reset();
    this.guaranteeForm.reset();
    this.garantia = null;
    this.tramites = null;
    this.deudores = [];
    this.acreedores = [];
    this.modalTransaction = null;
  }

  onGeneratePDFClicked(index: number) {
    const transaction = this.tramites[index];
    const pdfUrl = this.baseUrl + '/rugboletap?tramite=' + transaction.idTramiteTemp + '&garantia=' + transaction.guarantee.idGarantia + '&generar=true';
    console.log(pdfUrl);
    window.open(pdfUrl, "_blank");
  }
}
