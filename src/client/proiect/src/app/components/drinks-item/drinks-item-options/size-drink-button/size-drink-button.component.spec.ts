import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SizeDrinkButtonComponent } from './size-drink-button.component';

describe('SizeDrinkButtonComponent', () => {
  let component: SizeDrinkButtonComponent;
  let fixture: ComponentFixture<SizeDrinkButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SizeDrinkButtonComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SizeDrinkButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
