import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Subbar } from './subbar';

describe('Subbar', () => {
  let component: Subbar;
  let fixture: ComponentFixture<Subbar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Subbar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Subbar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
