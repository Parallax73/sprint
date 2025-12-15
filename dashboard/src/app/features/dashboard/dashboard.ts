import { Component } from '@angular/core';

interface DashCard {
  title: string;
  value: string | number;
  label?: string;
  isCritical?: boolean;
}

interface LogEntry {
  time: string;
  event: string;
  ip: string;
}

interface Attacker {
  ip: string;
  count: number;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {

  kpiCards: DashCard[] = [
    { title: 'Total Alerts (24h)', value: '1,240' },
    { title: 'Critical Threats', value: 5, isCritical: true },
    { title: 'Active Rules', value: '12 / 15' },
    { title: 'System Status', value: '20ms', label: 'Ingest Latency' }
  ];

  threatFeed: LogEntry[] = [
    { time: '14:20:01', event: 'SSH_FAIL', ip: '192.168.1.105' },
    { time: '14:20:05', event: 'SQL_INJECT_ATTEMPT', ip: '45.33.22.11' },
    { time: '14:20:12', event: 'XSS_DETECTED', ip: '10.0.0.5' },
    { time: '14:20:15', event: 'SSH_FAIL', ip: '192.168.1.105' },
    { time: '14:20:18', event: 'PORT_SCAN', ip: '203.0.113.42' },
    { time: '14:20:22', event: 'RDP_AUTH_FAIL', ip: '88.12.3.99' }
  ];

  topAttackers: Attacker[] = [
    { ip: '192.168.1.105', count: 450 },
    { ip: '45.33.22.11', count: 320 },
    { ip: '203.0.113.42', count: 150 },
    { ip: '10.0.0.5', count: 85 },
    { ip: '172.16.0.23', count: 40 }
  ];

}
