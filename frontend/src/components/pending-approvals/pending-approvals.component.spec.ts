import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PendingApprovalsComponent } from './pending-approvals.component';

describe('PendingApprovalsComponent', () => {
  let component: PendingApprovalsComponent;
  let fixture: ComponentFixture<PendingApprovalsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PendingApprovalsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PendingApprovalsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
