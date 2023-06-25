import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntoleranceFoodComponent } from './intolerance-food.component';

describe('IntoleranceFoodComponent', () => {
  let component: IntoleranceFoodComponent;
  let fixture: ComponentFixture<IntoleranceFoodComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntoleranceFoodComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IntoleranceFoodComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
