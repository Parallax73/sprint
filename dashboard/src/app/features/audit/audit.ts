import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';

interface AuditLog {
  id: string;
  timestamp: string;
  actor: string;
  action: 'RULE_UPDATE' | 'LOGIN_BLOCK' | 'ALERT_RESOLVE' | 'USER_LOGIN' | 'SYSTEM_CONFIG';
  details: string;
  ip: string;
}

@Component({
  selector: 'app-audit',
  standalone: true,
  imports: [
    CommonModule,
    TableModule,
    ButtonModule,
    InputTextModule
  ],
  templateUrl: './audit.html',
  styleUrl: './audit.scss'
})
export class Audit {

  logs: AuditLog[] = [
    {
      id: 'AUD-9001',
      timestamp: '2023-10-27 15:45:22',
      actor: 'sprint_admin',
      action: 'RULE_UPDATE',
      details: 'Changed severity of rule "SSH Brute Force" to CRITICAL',
      ip: '10.50.1.100'
    },
    {
      id: 'AUD-9002',
      timestamp: '2023-10-27 15:30:10',
      actor: 'j_smith',
      action: 'ALERT_RESOLVE',
      details: 'Marked alert AL-1024 as Resolved (False Positive)',
      ip: '10.50.1.105'
    },
    {
      id: 'AUD-9003',
      timestamp: '2023-10-27 15:15:00',
      actor: 'system_bot',
      action: 'LOGIN_BLOCK',
      details: 'Auto-blocked IP 192.168.1.55 after 10 failed attempts',
      ip: '127.0.0.1'
    },
    {
      id: 'AUD-9004',
      timestamp: '2023-10-27 14:00:55',
      actor: 'm_doe',
      action: 'SYSTEM_CONFIG',
      details: 'Updated retention policy to 90 days',
      ip: '10.50.1.102'
    },
    {
      id: 'AUD-9005',
      timestamp: '2023-10-27 09:30:12',
      actor: 'sprint_admin',
      action: 'USER_LOGIN',
      details: 'Successful login via MFA',
      ip: '10.50.1.100'
    },
    {
      id: 'AUD-9006',
      timestamp: '2023-10-27 09:28:00',
      actor: 'k_jones',
      action: 'RULE_UPDATE',
      details: 'Disabled rule "Legacy FTP Check"',
      ip: '10.50.1.108'
    }
  ];

}
