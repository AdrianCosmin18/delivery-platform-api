import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SauceItemComponent } from './sauce-item.component';

describe('SauceItemComponent', () => {
  let component: SauceItemComponent;
  let fixture: ComponentFixture<SauceItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SauceItemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SauceItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
