import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { SelectModule } from 'primeng/select'; // Renamed from DropdownModule
import { ToggleSwitchModule } from 'primeng/toggleswitch'; // Renamed from InputSwitchModule
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea'; // Renamed from InputTextareaModule

interface Rule {
  id: string;
  name: string;
  description: string;
  severity: 'Critical' | 'High' | 'Medium' | 'Low';
  isActive: boolean;
  logic: string;
}

@Component({
  selector: 'app-rules',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    ButtonModule,
    DialogModule,
    SelectModule,        // Updated
    ToggleSwitchModule,  // Updated
    InputTextModule,
    TextareaModule       // Updated
  ],
  templateUrl: './rules.html',
  styleUrl: './rules.scss'
})
export class Rules {

  displayCreateDialog: boolean = false;
  isValidating: boolean = false;

  severities = [
    { label: 'Critical', value: 'Critical' },
    { label: 'High', value: 'High' },
    { label: 'Medium', value: 'Medium' },
    { label: 'Low', value: 'Low' }
  ];

  newRule: Partial<Rule> = {
    severity: 'Medium',
    logic: ''
  };

  rules: Rule[] = [
    {
      id: 'R-101',
      name: 'SSH Brute Force',
      description: 'Detects 5+ failed SSH login attempts within 1 minute from same IP',
      severity: 'High',
      isActive: true,
      logic: "eventType == 'SSH_FAIL' && count(sourceIp, '1m') > 5"
    },
    {
      id: 'R-102',
      name: 'Root Login Attempt',
      description: 'Alerts when root user successfully logs in via SSH',
      severity: 'Critical',
      isActive: true,
      logic: "eventType == 'SSH_SUCCESS' && username == 'root'"
    },
    {
      id: 'R-103',
      name: 'Suspicious Outbound Traffic',
      description: 'High volume of data transfer to non-whitelisted countries',
      severity: 'Medium',
      isActive: false,
      logic: "bytesOut > 5000000 && !geo.isWhitelisted(destinationIp)"
    },
    {
      id: 'R-104',
      name: 'New Admin User Created',
      description: 'Tracks creation of users with administrative privileges',
      severity: 'Low',
      isActive: true,
      logic: "eventType == 'USER_CREATE' && role == 'ADMIN'"
    }
  ];

  openCreateDialog() {
    this.newRule = { severity: 'Medium', logic: '' };
    this.displayCreateDialog = true;
  }

  toggleRule(rule: Rule) {
    console.log(`Rule ${rule.name} is now ${rule.isActive ? 'Active' : 'Disabled'}`);
  }

  validateSyntax() {
    this.isValidating = true;
    setTimeout(() => {
      this.isValidating = false;
      alert('Syntax is valid!');
    }, 1000);
  }

  saveRule() {
    this.displayCreateDialog = false;
    console.log('Rule Saved:', this.newRule);
  }
}
