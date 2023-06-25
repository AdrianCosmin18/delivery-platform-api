import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoryOrdersComponent } from './history-orders.component';

describe('HistoryOredersComponent', () => {
  let component: HistoryOrdersComponent;
  let fixture: ComponentFixture<HistoryOrdersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HistoryOrdersComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HistoryOrdersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
