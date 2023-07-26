import { Component, OnInit, EventEmitter } from '@angular/core';
import { Subscription } from 'rxjs';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ExternalUsersService } from '../shared/external-users.service';
import { LoadingService } from '../shared/loading.service';
import { SolrDoc } from '../shared/solr-doc.model';
import { Part } from '../shared/part.model';
import { MaterializeAction } from 'angular2-materialize';
import { GuaranteeTransactionPart } from '../shared/guarantee-transaction-part.model';

declare var Materialize:any;

@Component({
  selector: 'app-external-users-search',
  templateUrl: './external-users-search.component.html',
  styleUrls: ['./external-users-search.component.css']
})
export class ExternalUsersSearchComponent implements OnInit {
  public loading = false;
  httpSubscription: Subscription;
  searchUserForm: FormGroup;
  docs: SolrDoc[];
  noResults: boolean;
  modalActions = new EventEmitter<string|MaterializeAction>();
  parts: GuaranteeTransactionPart[];
  modalDoc: SolrDoc;

  constructor(private externalUsersService: ExternalUsersService,
    private loadingService: LoadingService) { }

  ngOnInit() {
    this.initUserForm();
  }

  private initUserForm() {
    this.searchUserForm = new FormGroup({
      'query': new FormControl(null, Validators.required)
    });
  }

  onSearchUserSubmit() {
    let query = this.searchUserForm.value.query;
    if (!query) {
      Materialize.toast('Debe ingresar un criterio de búsqueda.', 4000);
      return false;
    }
    this.docs = null;
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.externalUsersService.search(query).subscribe(
      res => {
        this.noResults = true;
        if (res.total > 0) {
          this.docs = res.data;
          this.noResults = false;
        }
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }

  onViewTransactionsClicked(doc: SolrDoc) {
    // obtener tramites del usuario
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.externalUsersService.fetchPartData(doc.id).subscribe(
      res => {
        this.modalDoc = doc;
        this.parts = res;
        this.modalActions.emit({action:"modal",params:['open']});
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }

  closeModal() {
    this.modalActions.emit({action:"modal",params:['close']});
    this.modalDoc = new SolrDoc;
  }
}
