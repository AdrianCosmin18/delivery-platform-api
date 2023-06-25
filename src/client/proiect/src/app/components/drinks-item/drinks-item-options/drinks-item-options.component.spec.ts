import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrinksItemOptionsComponent } from './drinks-item-options.component';

describe('DrinksItemOptionsComponent', () => {
  let component: DrinksItemOptionsComponent;
  let fixture: ComponentFixture<DrinksItemOptionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DrinksItemOptionsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DrinksItemOptionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
