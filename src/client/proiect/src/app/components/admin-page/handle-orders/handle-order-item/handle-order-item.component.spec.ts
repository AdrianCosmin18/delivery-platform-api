import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HandleOrderItemComponent } from './handle-order-item.component';

describe('HandleOrderItemComponent', () => {
  let component: HandleOrderItemComponent;
  let fixture: ComponentFixture<HandleOrderItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HandleOrderItemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HandleOrderItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
