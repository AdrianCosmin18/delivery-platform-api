import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BurgerItemOptionsComponent } from './burger-item-options.component';

describe('BurgerItemOptionsComponent', () => {
  let component: BurgerItemOptionsComponent;
  let fixture: ComponentFixture<BurgerItemOptionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BurgerItemOptionsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BurgerItemOptionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
