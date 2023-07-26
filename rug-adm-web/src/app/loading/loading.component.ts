import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'skeleton-loading',
  templateUrl: './loading.component.html',
  styleUrls: ['./loading.component.css']
})
export class LoadingComponent implements OnInit {
  @Input()
  state: boolean = false;

  constructor() { }

  ngOnInit() {
  }

}
