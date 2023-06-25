import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SaucePageComponent } from './sauce-page.component';

describe('SaucePageComponent', () => {
  let component: SaucePageComponent;
  let fixture: ComponentFixture<SaucePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SaucePageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SaucePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
