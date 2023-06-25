import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {map, Observable, Subscription} from 'rxjs';
import {CustomerService} from "../services/customer.service";
import {Store} from "@ngrx/store";
import * as fromApp from "../store/app.reducer";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  private auth$!: Observable<{ loggedIn: boolean; }>;

  constructor(
    private router: Router,
    private store: Store<fromApp.AppState>
  ) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> {
    this.auth$ = this.store.select("auth");
    return this.auth$.pipe(
      map((val: any) => {
        if (val.loggedIn) {
          return true;
        } else {
          this.router.navigate(['/mainPage'], {queryParams: {returnUrl: state.url}});
          return false;
        }
      })
    )
  }

}
