import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoryOrderItemDetailsComponent } from './history-order-item-details.component';

describe('HistoryOrderItemDetailsComponent', () => {
  let component: HistoryOrderItemDetailsComponent;
  let fixture: ComponentFixture<HistoryOrderItemDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HistoryOrderItemDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HistoryOrderItemDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
