import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { GuaranteesService } from '../shared/guarantees.service';
import { LoadingService } from '../shared/loading.service';
import { Guarantee } from '../shared/guarantee.model';

declare var Materialize:any;

@Component({
  selector: 'app-asset-search',
  templateUrl: './asset-search.component.html',
  styleUrls: ['./asset-search.component.css']
})
export class AssetSearchComponent implements OnInit {
  public loading = false;
  httpSubscription: Subscription;
  searchAssetForm: FormGroup;
  noResults: boolean;
  guarantees: Guarantee[];

  constructor(private guaranteesService: GuaranteesService,
    private loadingService: LoadingService) { }

  ngOnInit() {
    this.initAssetForm();
  }

  private initAssetForm() {
    this.searchAssetForm = new FormGroup({
      'query': new FormControl(null, Validators.required)
    });
  }

  onSearchAssetSubmit() {
    let query = this.searchAssetForm.value.query;
    if (!query) {
      Materialize.toast('Debe ingresar el identificador del bien.', 4000);
      return false;
    }
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.guaranteesService.search(query).subscribe(
      res => {
        this.noResults = true;
        if (res.data.length > 0) {
          this.guarantees = res.data;
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
}
