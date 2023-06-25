import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CustomerService} from "../../../services/customer.service";
import {MessageService} from "primeng/api";
import {User} from "../../../interfaces/user";
import {AuthService} from "../../../services/auth.service";
import * as AuthAction from "../../../store/auth/auth.actions";
import * as fromApp from "../../../store/app.reducer";
import {Store} from "@ngrx/store";
import {Observable, Subscription} from "rxjs";
import {NotificationService} from "../../../services/notification.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [MessageService]
})
export class LoginComponent implements OnInit ,OnDestroy{
  public form!: FormGroup;
  private user!: User;
  public email: string = "";
  public password: string = "";
  private auth$!: Observable<{ loggedIn: boolean }>;

  private subscriptions: Subscription= new Subscription();


  constructor(private service: CustomerService,
              private router: Router,
              private formBuilder: FormBuilder,
              private messageService: MessageService,
              private authService: AuthService,
              private store: Store<fromApp.AppState>,
              private notificationService: NotificationService) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      email: ["", [Validators.required]],
      password: ["", [Validators.required, Validators.minLength(6)]]
      //Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$') -> trebuie adaugat
    })
  }

  goToRegister(): void{
    this.router.navigate(["/register"]);
  }

  login(){

    const emailForm = this.form.get("email")?.value;
    const passwordForm = this.form.get("password")?.value;

    this.store.dispatch(new AuthAction.LoginStart({email: emailForm, password: passwordForm}));
    this.auth$ = this.store.select("auth");
    this.subscriptions = this.auth$.subscribe(value => {
      if(value.loggedIn){
        this.form.reset();
        this.router.navigate(['/mainPage']);
        localStorage.removeItem("auth");
      }
    })


  }


  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  protected readonly fromApp = fromApp;
}
