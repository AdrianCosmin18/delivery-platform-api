import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoryOrderItemComponent } from './history-order-item.component';

describe('HistoryOrderItemComponent', () => {
  let component: HistoryOrderItemComponent;
  let fixture: ComponentFixture<HistoryOrderItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HistoryOrderItemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HistoryOrderItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
