import { Component, OnInit, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { MaterializeAction } from 'angular2-materialize';
import { Country } from '../shared/country.model';
import { CountriesService } from '../shared/countries.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { PERSONA_SOLICITANTE, PERSONA_DEUDOR, PERSONA_ACREEDOR, TRAMITE_INSCRIPCION, PERSONA_JURIDICA, RESULTADO_EXITOSO } from '../shared/constants';
import { ExternalUser } from '../shared/external-user.model';
import { Guarantee } from '../shared/guarantee.model';
import { Transaction } from '../shared/transaction.model';
import { Subscription } from 'rxjs';
import { GuaranteesService } from '../shared/guarantees.service';
import { FileUploader } from 'ng2-file-upload';
import { environment } from '../../environments/environment';

declare var Materialize:any;

@Component({
  selector: 'app-inscripcion',
  templateUrl: './inscripcion.component.html',
  styleUrls: ['./inscripcion.component.css']
})
export class InscripcionComponent implements OnInit {
  @ViewChild('boletaFile')
  boletaFile: ElementRef;
  uploader = new FileUploader({
    itemAlias: 'document'
  });
  public loading = false;
  modalActions = new EventEmitter<string|MaterializeAction>();
  garantia: Guarantee;
  solicitantes: ExternalUser[];
  deudores: ExternalUser[];
  acreedores: ExternalUser[];
  countries: Country[];
  personForm: FormGroup;
  personFormTitle: string;
  personFormType: number;
  personFormEdit: boolean;
  personFormIndex: number;
  guaranteeForm: FormGroup;
  transactionSubscription: Subscription;

  constructor(private countriesService: CountriesService,
    private guaranteesService: GuaranteesService) { }

  ngOnInit() {
    this.countriesService.fetchData("AC").subscribe(
      data => {
        this.countries = data.data;
      }
    );
    this.transactionSubscription = this.guaranteesService.transactionSaved.subscribe(
      data => {
        if (data != RESULTADO_EXITOSO) {
          // mostrar error de garantia o tramite existente
        } else {
          this.solicitantes = [];
          this.deudores = [];
          this.acreedores = [];
          this.personForm.reset();
          this.guaranteeForm.reset();
          this.uploader.clearQueue();
        }
      }
    );
    this.solicitantes = [];
    this.deudores = [];
    this.acreedores = [];
    this.initPersonForm(null);
    this.initGuaranteeForm();
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

  private initGuaranteeForm() {
    this.guaranteeForm = new FormGroup({
      'numGarantia': new FormControl(null, Validators.required),
      'numTramite': new FormControl(null, Validators.required),
      'fechaCreacion': new FormControl(null, Validators.required),
      'descGarantia': new FormControl(null, Validators.required),
      'instrumentoPublico': new FormControl(null, Validators.required),
      'otrosTerminos': new FormControl(null, Validators.required)
    });
  }

  onAddSolicitanteClicked() {
    this.personFormTitle = "Solicitante";
    this.personFormType = PERSONA_SOLICITANTE;
    this.personFormEdit = false;
    this.modalActions.emit({action:"modal", params:["open"]});
  }

  onAddDeudorClicked() {
    this.personFormTitle = "Deudor";
    this.personFormType = PERSONA_DEUDOR;
    this.personFormEdit = false;
    this.modalActions.emit({action:"modal", params:["open"]});
  }

  onAddAcreedorClicked() {
    this.personFormTitle = "Acreedor";
    this.personFormType = PERSONA_ACREEDOR;
    this.personFormEdit = false;
    this.modalActions.emit({action:"modal", params:["open"]});
  }

  onCancelClicked() {
    this.modalActions.emit({action:"modal",params:['close']});
    this.personForm.reset();
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

    switch(this.personFormType) {
      case PERSONA_SOLICITANTE:
        if (this.personFormEdit) {
          this.solicitantes[this.personFormIndex] = persona;
        } else {
          this.solicitantes.push(persona);
        }
        break;
      case PERSONA_DEUDOR:
        this.deudores.push(persona);
        break;
      case PERSONA_ACREEDOR:
        this.acreedores.push(persona);
        break;
    }
    this.onCancelClicked();
  }

  onSaveGarantiaSubmit() {
    let transaction = new Transaction;
    let guarantee = new Guarantee;
    guarantee.numGarantia = this.guaranteeForm.value.numGarantia;
    guarantee.idGarantia = guarantee.numGarantia;
    guarantee.fechaInscr = this.guaranteeForm.value.fechaCreacion;
    guarantee.descGarantia = this.guaranteeForm.value.descGarantia;
    guarantee.otrosTerminosGarantia = this.guaranteeForm.value.otrosTerminos;
    guarantee.instrumentoPublico = this.guaranteeForm.value.instrumentoPublico;
    transaction.solicitante = this.solicitantes[0];
    transaction.deudores = this.deudores;
    transaction.acreedores = this.acreedores;
    transaction.guarantee = guarantee;
    transaction.idTramite = this.guaranteeForm.value.numTramite;
    transaction.idTramiteTemp = transaction.idTramite;
    transaction.fechaCreacion = guarantee.fechaInscr;
    transaction.idTipoTramite = TRAMITE_INSCRIPCION;
    this.guaranteesService.create(transaction, this.uploader);
  }

  onFileChanged() {
    if (this.uploader.queue.length > 1) {
      let firstItem = this.uploader.queue[0];
      this.uploader.removeFromQueue(firstItem);
    }
  }

  onEditSolicitanteClicked(solicitante: ExternalUser, index: number) {
    this.initPersonForm(solicitante);
    Materialize.updateTextFields();
    this.personFormTitle = "Solicitante";
    this.personFormType = PERSONA_SOLICITANTE;
    this.personFormEdit = true;
    this.personFormIndex = index;
    this.modalActions.emit({action:"modal", params:["open"]});
  }

  onRemoveSolicitanteClicked(index: number) {
    this.solicitantes.splice(index, 1);
  }

  onEditDeudorClicked(deudor: ExternalUser, index: number) {
    this.initPersonForm(deudor);
    Materialize.updateTextFields();
    this.personFormTitle = "Deudor";
    this.personFormType = PERSONA_DEUDOR;
    this.personFormEdit = true;
    this.personFormIndex = index;
    this.modalActions.emit({action:"modal", params:["open"]});
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
    this.modalActions.emit({action:"modal", params:["open"]});
  }

  onRemoveAcreedorClicked(index: number) {
    this.acreedores.splice(index, 1);
  }

  onRemoveBoletaClicked() {
    this.uploader.clearQueue();
  }

  disabledPerson() {
    return !this.personForm.valid ||
      (this.personFormType != PERSONA_SOLICITANTE &&
        !this.personForm.value.address ||
        (this.personForm.value.personType == PERSONA_JURIDICA &&
          (!this.personForm.value.legalInscription || !this.personForm.value.representativeInfo)));
  }

  disabledGuarantee() {
    return this.solicitantes.length == 0 ||
      this.deudores.length == 0 ||
      this.acreedores.length == 0 ||
      !this.guaranteeForm.valid;
  }
}
