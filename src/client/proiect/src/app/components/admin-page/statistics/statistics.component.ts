import { Component, OnInit } from '@angular/core';
import {OrderItemService} from "../../../services/order-item.service";
import {LoadingScreenService} from "../../../services/loading-screen.service";

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {
  public mapItems = new Map<string, number>();
  public baseData: any;
  public horizontalOptions: any;

  constructor(private orderItemService: OrderItemService, private loadingScreenService: LoadingScreenService) { }

  ngOnInit(): void {
    this.loadingScreenService.setLoading(true);
    this.getMapProducts();
  }

  getMapProducts(){
    this.orderItemService.getStatisticsProducts().subscribe({
      next: response => {
        const keys = Object.keys(response);
        for (const key of keys) {
          this.mapItems.set(key, response[key]);
        }

        this.initData();
        this.initOptions();
        this.loadingScreenService.setLoading(false);
      },
      error: err => {
        alert(err.error.message);
      }
    });
  }

  initData(){

    let lbl: string[] = [];
    let val: number[] = [];
    for(let [key, value] of this.mapItems.entries()){
      lbl.push(key);
      val.push(value);
    }
    let labels = lbl;
    let dataSet = [
      {
        label: 'Produse vândute',
        backgroundColor: '#42A5F5',
        data: val
      }
    ];

    this.baseData = {
      labels: labels,
      datasets: dataSet
    }
  }

  initOptions() {

    this.horizontalOptions = {
      indexAxis: 'y',
      plugins: {
        legend: {
          labels: {
            color: '#495057'
          }
        }
      },
      scales: {
        x: {
          ticks: {
            color: '#495057'
          },
          grid: {
            color: '#ebedef'
          }
        },
        y: {
          ticks: {
            color: '#495057'
          },
          grid: {
            color: '#ebedef'
          }
        }
      }
    };
  }

}
