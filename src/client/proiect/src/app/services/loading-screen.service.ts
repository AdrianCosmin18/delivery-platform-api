import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LoadingScreenService {

  private loading = false;

  setLoading(loading: boolean): void {
    this.loading = loading;
  }

  public getLoading(): boolean {
    return this.loading;
  }
}
