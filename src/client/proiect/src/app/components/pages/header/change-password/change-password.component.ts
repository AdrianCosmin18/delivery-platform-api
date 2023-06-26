import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NotificationService} from "../../../../services/notification.service";
import {Observable, Subscription} from "rxjs";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../../store/app.reducer";
import {CustomerService} from "../../../../services/customer.service";
import {DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {LoadingScreenService} from "../../../../services/loading-screen.service";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit, OnDestroy {
  public form!: FormGroup;
  public email: string = '';
  private auth$!: Observable<{ email: string }>;
  private subscriptions: Subscription= new Subscription();


  actualPassword: string = '';
  newPassword: string= '';

  constructor(
    private formBuilder: FormBuilder,
    private notificationService: NotificationService,
    private store: Store<fromApp.AppState>,
    private customerService: CustomerService,
    public config: DynamicDialogConfig,
    public ref: DynamicDialogRef,
    private loadingScreenService: LoadingScreenService
  ) { }

  ngOnInit(): void {
    this.auth$ = this.store.select("auth");
    this.subscriptions = this.auth$.subscribe(value => {
      this.email = value.email;
    });

    this.form = this.formBuilder.group({
      actualPassword: ["", [Validators.required, Validators.minLength(6)]],
      newPassword: ['', [Validators.required, Validators.minLength(6), Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$')]]
    });
  }

  changePassword(){

    const actualPassword = this.form.get("actualPassword")?.value;
    const newPassword = this.form.get("newPassword")?.value;

    if(actualPassword === newPassword){

      this.notificationService.onError('modifyPassword', 'Parola nouă nu poate fi identică cu cea veche');
    }else{
      this.loadingScreenService.setLoading(true);
      this.customerService.changePassword(this.email, actualPassword, newPassword).subscribe({
        next: () => {
          this.loadingScreenService.setLoading(false);
          this.notificationService.onSuccess('modifyPassword', 'Parolă modificată');
          this.ref.close();
        }, error: err => {
          this.loadingScreenService.setLoading(false);
          this.notificationService.onError('modifyPassword', 'Parola curentă este incorectă');
        }
      })
    }
  }

  cancelUpdatePassword(){
    this.ref.close();
  }


  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

}
