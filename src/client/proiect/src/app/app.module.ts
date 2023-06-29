import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { MainPageComponent } from './components/pages/main-page/main-page.component';
import { HeaderComponent } from './components/pages/header/header.component';
import {MessageService, PrimeIcons, ConfirmationService} from "primeng/api";
import {ConfirmDialogModule} from 'primeng/confirmdialog';
import {ButtonModule} from "primeng/button";
import { BurgerComponent } from './components/pages/burger/burger.component';
import { BurgerItemComponent } from './components/burger-item/burger-item.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {RouterModule, Routes} from "@angular/router";
import { FooterComponent } from './components/pages/footer/footer.component';
import { FriesPageComponent } from './components/pages/fries-page/fries-page.component';
import { FriesItemComponent } from './components/fries-item/fries-item.component';
import { DrinksItemComponent } from './components/drinks-item/drinks-item.component';
import { DrinksPageComponent } from './components/pages/drinks-page/drinks-page.component';
import { CartComponent } from './components/pages/cart/cart.component';
import {environment} from "../environments/environment";
import { LoginComponent } from './components/pages/login/login.component';
import { RegisterComponent } from './components/pages/register/register.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { CartItemComponent } from './components/cart-item/cart-item.component';
import {ToastModule} from "primeng/toast";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {FieldsetModule} from "primeng/fieldset";
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {CardModule} from "primeng/card";
import {BadgeModule} from "primeng/badge";
import {TooltipModule} from "primeng/tooltip";
import { BurgerItemOptionsComponent } from './components/burger-item/burger-item-options/burger-item-options.component';
import {AccordionModule} from "primeng/accordion";
import { IngredientComponent } from './components/burger-item/burger-item-options/ingredient/ingredient.component';
import { FriesItemOptionsComponent } from './components/fries-item/fries-item-options/fries-item-options.component';
import {SelectButtonModule} from "primeng/selectbutton";
import {StoreModule} from "@ngrx/store";
import {StoreDevtoolsModule} from "@ngrx/store-devtools";
import {StoreRouterConnectingModule} from "@ngrx/router-store";
import {EffectsModule} from "@ngrx/effects";
import * as fromApp from './store/app.reducer';
import {RippleModule} from "primeng/ripple";
import { DrinksItemOptionsComponent } from './components/drinks-item/drinks-item-options/drinks-item-options.component';
import { SaucePageComponent } from './components/pages/sauce-page/sauce-page.component';
import { SauceItemComponent } from './components/sauce-item/sauce-item.component';
import {DropdownModule} from "primeng/dropdown";
import {MenuModule} from "primeng/menu";
import {OverlayPanelModule} from "primeng/overlaypanel";
import {DividerModule} from "primeng/divider";
import { SizeDrinkButtonComponent } from './components/drinks-item/drinks-item-options/size-drink-button/size-drink-button.component';
import { PersonalDataComponent } from './components/pages/header/personal-data/personal-data.component';
import {InputTextModule} from "primeng/inputtext";
import {AuthEffects} from "./store/auth/auth.effects";
import { AddressComponent } from './components/pages/header/address/address.component';
import {AuthInterceptor} from "./interceptor/auth.interceptor";
import {OrderListModule} from "primeng/orderlist";
import {PanelModule} from "primeng/panel";
import { AddressItemComponent } from './components/pages/header/address/address-item/address-item.component';
import { AddressUpdateFormComponent } from './components/pages/header/address/address-update-form/address-update-form.component';
import {localStorageSync} from "ngrx-store-localstorage";
import {CheckboxModule} from "primeng/checkbox";
import { CardPageComponent } from './components/pages/header/card-page/card-page.component';
import { CardItemComponent } from './components/pages/header/card-page/card-item/card-item.component';
import { CardFormComponent } from './components/pages/header/card-page/card-form/card-form.component';
import {InputMaskModule} from "primeng/inputmask";
import { PlaceOrderComponent } from './components/pages/place-order/place-order.component';
import {InputTextareaModule} from "primeng/inputtextarea";
import { CustomTipsComponent } from './components/pages/place-order/custom-tips/custom-tips.component';
import { HistoryOrdersComponent } from './components/pages/header/history-oreders/history-orders.component';
import { HistoryOrderItemComponent } from './components/pages/header/history-oreders/history-order-item/history-order-item.component';
import { HistoryOrderItemDetailsComponent } from './components/pages/header/history-oreders/history-order-item/history-order-item-details/history-order-item-details.component';
import { StatusOrderComponent } from './components/pages/header/history-oreders/history-order-item/history-order-item-details/status-order/status-order.component';
import { MyPreferencesComponent } from './components/pages/main-page/my-preferences/my-preferences.component';
import {MegaMenuModule} from "primeng/megamenu";
import {TabViewModule} from "primeng/tabview";
import {MultiSelectModule} from "primeng/multiselect";
import { IntoleranceFoodComponent } from './components/pages/main-page/intolerance-food/intolerance-food.component';
import { AdminPageComponent } from './components/admin-page/admin-page.component';
import { HandleOrdersComponent } from './components/admin-page/handle-orders/handle-orders.component';
import { HandleOrderItemComponent } from './components/admin-page/handle-orders/handle-order-item/handle-order-item.component';
import { SelectCourierComponent } from './components/admin-page/handle-orders/handle-order-item/select-courier/select-courier.component';
import {CalendarModule} from "primeng/calendar";
import { StatisticsComponent } from './components/admin-page/statistics/statistics.component';
import {ChartModule} from "primeng/chart";
import { ChangePasswordComponent } from './components/pages/header/change-password/change-password.component';
import {PasswordModule} from "primeng/password";
import {AuthGuard} from "./guards/auth.guard";
import {AdminGuard} from "./guards/admin.guard";
import { InfoCourierComponent } from './components/pages/header/history-oreders/history-order-item/history-order-item-details/info-courier/info-courier.component';
import {VirtualScrollerModule} from "primeng/virtualscroller";
import { LoadingDialogComponent } from './components/loading-dialog/loading-dialog.component';
const appRoutes: Routes = [
  {path: '', redirectTo: "/mainPage", pathMatch: "full"},
  {path: 'login', component: LoginComponent},
  {path: "mainPage", component: MainPageComponent},
  {path: "register", component: RegisterComponent},
  {path: "burgers", component: BurgerComponent},
  {path: "fries", component: FriesPageComponent},
  {path: "drinks", component: DrinksPageComponent},
  {path: "sauces", component: SaucePageComponent},
  {path: "placeOrder", component: PlaceOrderComponent, canActivate: [AuthGuard]},
  {path: "historyOrders", component: HistoryOrdersComponent, canActivate: [AuthGuard]},
  {path: "historyOrders/:id", component: HistoryOrderItemDetailsComponent,canActivate: [AuthGuard]},
  {path: "myPreferences", component: MyPreferencesComponent},
  {path: "intolerance", component: IntoleranceFoodComponent},
  {path: "adminPage", component: AdminPageComponent, canActivate: [AuthGuard, AdminGuard]},
  {path: "adminPage/handleOrders", component: HandleOrdersComponent, canActivate: [AuthGuard, AdminGuard]},
  {path: "adminPage/statistics", component: StatisticsComponent, canActivate: [AuthGuard, AdminGuard]},
  {path: '**', redirectTo: "/mainPage", pathMatch: "full"},
]

@NgModule({
  declarations: [
    AppComponent,
    MainPageComponent,
    HeaderComponent,
    BurgerComponent,
    BurgerItemComponent,
    FooterComponent,
    FriesPageComponent,
    FriesItemComponent,
    DrinksItemComponent,
    DrinksPageComponent,
    CartComponent,
    LoginComponent,
    RegisterComponent,
    CartItemComponent,
    BurgerItemOptionsComponent,
    IngredientComponent,
    FriesItemOptionsComponent,
    DrinksItemOptionsComponent,
    SaucePageComponent,
    SauceItemComponent,
    SizeDrinkButtonComponent,
    PersonalDataComponent,
    AddressComponent,
    AddressItemComponent,
    AddressUpdateFormComponent,
    CardPageComponent,
    CardItemComponent,
    CardFormComponent,
    PlaceOrderComponent,
    CustomTipsComponent,
    HistoryOrdersComponent,
    HistoryOrderItemComponent,
    HistoryOrderItemDetailsComponent,
    StatusOrderComponent,
    MyPreferencesComponent,
    IntoleranceFoodComponent,
    AdminPageComponent,
    HandleOrdersComponent,
    HandleOrderItemComponent,
    SelectCourierComponent,
    StatisticsComponent,
    ChangePasswordComponent,
    InfoCourierComponent,
    LoadingDialogComponent,
  ],
  imports: [
    BrowserModule,
    ButtonModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot(appRoutes),
    ReactiveFormsModule,
    ToastModule,
    BrowserAnimationsModule,
    FieldsetModule,
    CardModule,
    BadgeModule,
    StoreModule.forRoot(fromApp.appReducer, {
      metaReducers: [localStorageSyncReducer, localStorageSyncReducerCart]
    }),
    TooltipModule,
    FieldsetModule,
    AccordionModule,
    SelectButtonModule,
    StoreDevtoolsModule.instrument({logOnly: environment.production}),
    StoreRouterConnectingModule.forRoot(),
    RippleModule,
    DropdownModule,
    MenuModule,
    OverlayPanelModule,
    DividerModule,
    InputTextModule,
    EffectsModule.forRoot(([AuthEffects])),
    PanelModule,
    OrderListModule,
    CheckboxModule,
    ConfirmDialogModule,
    InputMaskModule,
    InputTextareaModule,
    MegaMenuModule,
    TabViewModule,
    MultiSelectModule,
    CalendarModule,
    ChartModule,
    PasswordModule,
    VirtualScrollerModule,
  ],
  providers: [
    DialogService,
    MessageService,
    ConfirmationService,
    DynamicDialogConfig,
    DynamicDialogRef,
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}
  ],
  bootstrap: [AppComponent],
  exports: [
    BurgerItemComponent,
    FriesItemComponent,
    HeaderComponent,
  ]
})
export class AppModule { }

export function localStorageSyncReducer(reducer: any) {
  return localStorageSync({
    keys: ['auth', 'settings'], // Specify the state slices to synchronize with local storage
    rehydrate: true,
  })(reducer);
}

export function localStorageSyncReducerCart(reducer: any){
  return localStorageSync({
    keys: ['items', 'settings'],
    rehydrate: true
  })(reducer);
}
