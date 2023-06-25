import {Component, OnDestroy, OnInit} from '@angular/core';
import {CustomerService} from "../../../services/customer.service";
import {User} from "../../../interfaces/user";
import {Constants} from "../../../constants/constants";
import {Observable} from "rxjs";
import * as Actions from "../../../store/auth/auth.actions";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../store/app.reducer";
import {Router} from "@angular/router";


@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {
  public user!: User;
  public email!: string | null;

  constructor() { }

  ngOnInit(): void {

    const itemList: any[] = [];
    localStorage.setItem(Constants.ITEM_LIST, JSON.stringify(itemList));

    const quantity = 0;
    localStorage.setItem(Constants.QUANTITY, String(quantity));
  }

}
