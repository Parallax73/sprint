import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { TabsModule } from 'primeng/tabs';


export interface SecurityAlert {
  id: string;
  severity: 'Critical' | 'High' | 'Medium' | 'Low';
  timestamp: string;
  ruleName: string;
  sourceIp: string;
  country?: string;
  target: string;
  status: 'New' | 'Investigating' | 'Resolved';
  rawLog?: string;
}

@Component({
  selector: 'app-alert-detail',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    SelectModule,
    TabsModule
  ],
  templateUrl: './alert-detail.html',
  styleUrl: './alert-detail.scss'
})
export class AlertDetail {

  @Input() alert!: SecurityAlert;
  @Output() onClose = new EventEmitter<void>();

  statusOptions = [
    { label: 'New', value: 'New' },
    { label: 'Investigating', value: 'Investigating' },
    { label: 'Resolved', value: 'Resolved' }
  ];

  blockIp() {
    console.log(`[SOAR Trigger] Blocking IP: ${this.alert.sourceIp}`);

  }

  saveAndClose() {
    console.log(`[Update] Alert ${this.alert.id} status set to ${this.alert.status}`);
    this.onClose.emit();
  }
}
