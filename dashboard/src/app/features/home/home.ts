import { Component } from '@angular/core';
import {RouterLink} from '@angular/router';

interface HubCard {
  title: string;
  description: string;
  link?: string;
}

@Component({
  selector: 'app-home',
  templateUrl: './home.html',
  imports: [
    RouterLink
  ],
  styleUrls: ['./home.scss']
})
export class Home {

  cards: HubCard[] = [
    {
      title: 'Dashboard',
      description: 'View system health and performance metrics.',
      link: '/dashboard'
    },
    {
      title: 'Alerts',
      description: 'View active security alerts, triage incidents, and investigate potential threats.',
      link: '/alerts'
    },
    {
      title: 'Detection Rules',
      description: 'Manage detection logic, create new rules, and tune existing parameters.',
      link: '/detection-rules'
    },
    {
      title: 'Audit Logs',
      description: 'Review system activity, track access logs, and monitor compliance events.',
      link: '/audit-logs'
    },
    {
      title: 'Settings',
      description: 'Configure system preferences.',
      link: '/settings'
    },
    {
      title: 'Users',
      description: 'Add, Remove, Update users, Manage roles and View user accounts.',
      link: '/users'
    }
  ];

}
