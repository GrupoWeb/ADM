import { Subject } from "rxjs/Subject";

export class LoadingService {
  loadingChanged = new Subject<boolean>();

  constructor() {}

  changeLoading(isLoading: boolean) {
    this.loadingChanged.next(isLoading);
  }
}
