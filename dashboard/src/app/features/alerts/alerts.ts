import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { FormsModule } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import {AlertDetail, SecurityAlert} from '../alert-detail/alert-detail';



@Component({
  selector: 'app-alert',
  standalone: true,
  imports: [
    CommonModule,
    ButtonModule,
    FormsModule,
    DialogModule,
    AlertDetail
  ],
  templateUrl: './alerts.html',
  styleUrl: './alerts.scss'
})
export class Alert {

  displayDetailDialog: boolean = false;
  selectedAlert: SecurityAlert | null = null;

  alerts: SecurityAlert[] = [
    {
      id: 'AL-1024',
      severity: 'Critical',
      timestamp: '2023-10-27 14:23:01',
      ruleName: 'SSH Brute Force',
      sourceIp: '192.168.1.105',
      country: 'CN',
      target: 'srv-db-01',
      status: 'New',
      rawLog: JSON.stringify({
        event_type: "SSH_FAIL",
        src_ip: "192.168.1.105",
        dst_host: "srv-db-01",
        user: "root",
        attempt_count: 56,
        timestamp: "2023-10-27T14:23:01Z"
      }, null, 2)
    },
    {
      id: 'AL-1025',
      severity: 'High',
      timestamp: '2023-10-27 14:20:15',
      ruleName: 'SQL Injection Attempt',
      sourceIp: '45.33.22.11',
      country: 'RU',
      target: 'web-front-02',
      status: 'Investigating',
      rawLog: JSON.stringify({
        event_type: "HTTP_REQUEST",
        src_ip: "45.33.22.11",
        url: "/login?user=' OR 1=1--",
        method: "POST",
        user_agent: "Mozilla/5.0 (compatible; EvilBot/1.0)",
        timestamp: "2023-10-27T14:20:15Z"
      }, null, 2)
    },
    {
      id: 'AL-1026',
      severity: 'Medium',
      timestamp: '2023-10-27 13:45:00',
      ruleName: 'Port Scan Detected',
      sourceIp: '203.0.113.42',
      country: 'US',
      target: 'gateway-01',
      status: 'Resolved',
      rawLog: JSON.stringify({
        event_type: "NET_FLOW",
        src_ip: "203.0.113.42",
        ports_scanned: [22, 80, 443, 8080, 3306],
        flag: "SYN",
        timestamp: "2023-10-27T13:45:00Z"
      }, null, 2)
    },
    {
      id: 'AL-1027',
      severity: 'Low',
      timestamp: '2023-10-27 12:10:33',
      ruleName: 'Failed Login (User)',
      sourceIp: '10.0.0.55',
      country: 'DE',
      target: 'wkstn-hr-04',
      status: 'New',
      rawLog: JSON.stringify({
        event_type: "WIN_LOGON_FAIL",
        src_ip: "10.0.0.55",
        user: "jdoe",
        reason: "bad_password",
        timestamp: "2023-10-27T12:10:33Z"
      }, null, 2)
    },
    {
      id: 'AL-1028',
      severity: 'Critical',
      timestamp: '2023-10-27 11:05:12',
      ruleName: 'Malware Callback',
      sourceIp: '172.16.0.100',
      country: 'Unknown',
      target: 'srv-file-01',
      status: 'New',
      rawLog: JSON.stringify({
        event_type: "DNS_QUERY",
        domain: "malicious-c2.xyz",
        src_ip: "172.16.0.100",
        query_type: "A",
        timestamp: "2023-10-27T11:05:12Z"
      }, null, 2)
    }
  ];

  cycleStatus(alert: SecurityAlert, event: Event) {
    event.stopPropagation();
    if (alert.status === 'New') alert.status = 'Investigating';
    else if (alert.status === 'Investigating') alert.status = 'Resolved';
    else alert.status = 'New';
  }

  openDetail(alert: SecurityAlert) {
    this.selectedAlert = alert;
    this.displayDetailDialog = true;
  }

  onDialogClose() {
    this.displayDetailDialog = false;
    this.selectedAlert = null;
  }
}
