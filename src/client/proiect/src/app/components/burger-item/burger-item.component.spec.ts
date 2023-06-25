import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BurgerItemComponent } from './burger-item.component';

describe('BurgerItemComponent', () => {
  let component: BurgerItemComponent;
  let fixture: ComponentFixture<BurgerItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BurgerItemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BurgerItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
