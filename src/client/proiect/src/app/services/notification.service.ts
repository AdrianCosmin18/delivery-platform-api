import { Injectable } from '@angular/core';
import {MessageService} from "primeng/api";


@Injectable({
  providedIn: 'root',
})
export class NotificationService {

  constructor(private messageService : MessageService) { }

  onSuccess(key: string, summary: string): void{
    this.messageService.add({key: key,severity: Severity.SUCCESS, summary: summary});
  }

  onInfo(key: string, summary: string, detail: string): void {
    this.messageService.add({key: key, severity: Severity.INFO, summary: summary, detail: detail});
  }

  onWarning(summary: string, detail: string): void {
    this.messageService.add({severity: Severity.WARNING, summary: summary, detail: detail});
  }

  onError(key: string, summary: string): void {
    console.log(summary);
    this.messageService.add({key: key, severity: Severity.ERROR, summary: summary});
  }
}

enum Severity { SUCCESS = 'success', INFO = 'info', WARNING = 'warn', ERROR = 'error'}
