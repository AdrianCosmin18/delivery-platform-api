import { Component, OnInit } from '@angular/core';
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {User} from "../../../../interfaces/user";
import {CustomerService} from "../../../../services/customer.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Constants, ErrorMessages} from "../../../../constants/constants";
import {MessageService} from "primeng/api";
import {Observable} from "rxjs";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../../store/app.reducer";
import {NotificationService} from "../../../../services/notification.service";
import * as Actions from "../../../../store/auth/auth.actions";
import {Router} from "@angular/router";
import {LoadingScreenService} from "../../../../services/loading-screen.service";

@Component({
  selector: 'app-personal-data',
  templateUrl: './personal-data.component.html',
  styleUrls: ['./personal-data.component.css'],
  providers: [MessageService]
})
export class PersonalDataComponent implements OnInit {
  public user!: User;
  public form!: FormGroup;
  private auth$!: Observable<{ email: string; firstName: string; loggedIn: boolean }>;
  private email!: string;

  constructor(
    private dialogService: DialogService,
    private config: DynamicDialogConfig,
    public ref: DynamicDialogRef,
    private userService: CustomerService,
    private formBuilder: FormBuilder,
    private messageService: MessageService,
    private store: Store<fromApp.AppState>,
    private notificationService: NotificationService,
    private router:Router,
    private loadingScreenService: LoadingScreenService
  ) { }

  ngOnInit(): void {
    this.initForm();
    this.getCurrentUser();
  }

  getCurrentUser(){

    this.auth$ = this.store.select("auth");
    this.auth$.subscribe(value => {
      this.email = value.email;

      this.userService.getCustomerByEmail(this.email).subscribe({
        next: value => {
          this.user = value;
          this.putUserInForm();
        }
      })
    })
  }

  initForm(){
    this.form = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern('07\\d{8}')]],
    });
  }

  putUserInForm(){
    this.form.setValue({
      firstName: this.user.firstName,
      lastName: this.user.lastName,
      email: this.user.email,
      phone: this.user.phone
    })
  }

  updateUser(){

    let user: User = {
      firstName: this.form.get("firstName")?.value,
      lastName: this.form.get("lastName")?.value,
      email: this.form.get("email")?.value,
      phone: this.form.get("phone")?.value,
      password: ''
    }

    this.loadingScreenService.setLoading(true);
    // @ts-ignore
    this.userService.updateCustomerByEmail(this.email, user).subscribe({
      next: () => {
        this.loadingScreenService.setLoading(false);
        // this.messageService.add({severity:'success', summary:'Datele au fost salvate'});
        this.store.dispatch(new Actions.Logout());
        this.notificationService
          .onInfo("modifyUserInfo", "Datele au fost salvate cu succes", "Autentifică-te pentru a putea intra în contul tău");
        // this.ngOnInit();
        this.router.navigate(['/mainPage']);
        this.ref.close();
      },
      error: err => {
        this.loadingScreenService.setLoading(false);
        if(err === ErrorMessages.USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION){
          this.notificationService.onError("modifyUserInfo", 'Există deja un cont cu acest mail');
        }else if (err === ErrorMessages.USER_ALREADY_EXISTS_PHONE_EXCEPTION){
          this.notificationService.onError("modifyUserInfo", "Există deja un cont cu acest număr de telefon")
        }
      }
    })
  }

}
