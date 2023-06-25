import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrinksItemComponent } from './drinks-item.component';

describe('DrinksItemComponent', () => {
  let component: DrinksItemComponent;
  let fixture: ComponentFixture<DrinksItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DrinksItemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DrinksItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
