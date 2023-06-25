import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HandleOrdersComponent } from './handle-orders.component';

describe('HandleOrdersComponent', () => {
  let component: HandleOrdersComponent;
  let fixture: ComponentFixture<HandleOrdersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HandleOrdersComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HandleOrdersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
