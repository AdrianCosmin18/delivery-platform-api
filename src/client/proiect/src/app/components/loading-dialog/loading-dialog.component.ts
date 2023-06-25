import { Component, OnInit } from '@angular/core';
import {LoadingScreenService} from "../../services/loading-screen.service";

@Component({
  selector: 'app-loading-dialog',
  templateUrl: './loading-dialog.component.html',
  styleUrls: ['./loading-dialog.component.css']
})
export class LoadingDialogComponent {

  constructor(public loadingScreenService: LoadingScreenService) { }

}
