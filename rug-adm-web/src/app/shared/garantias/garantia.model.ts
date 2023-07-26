import { Subscription } from "rxjs";
import { Guarantee } from "../guarantee.model";
import { Transaction } from "../transaction.model";

export class garantia{
  public idGarantia: any;
  public guarantee: Guarantee;
  public filtro: Transaction;
  public WarrantyNumber: number;
  public guarantees:Transaction[];
  public loading: boolean;
  public httpsSubscription: Subscription;


}