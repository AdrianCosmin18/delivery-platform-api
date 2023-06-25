import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {map, Observable} from 'rxjs';
import {Store} from "@ngrx/store";
import * as fromApp from "../store/app.reducer";
import {Roles} from "../constants/constants";

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  private auth$!: Observable<{ role: string; }>;

  constructor(
    private router: Router,
    private store: Store<fromApp.AppState>
  ) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    this.auth$ = this.store.select("auth");
    return this.auth$.pipe(
      map((val: any) => {
        if (val.role === Roles.ROLE_ADMIN) {
          return true;
        } else {
          this.router.navigate(['/mainPage'], {queryParams: {returnUrl: state.url}});
          return false;
        }
      })
    )
  }

}
