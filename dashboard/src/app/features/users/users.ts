import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { FormsModule } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
// Assuming you will create a detail component similar to AlertDetail,
// or you can replace the dialog content with a form directly.
// import { UserDetail } from '../user-detail/user-detail';

export interface User {
  id: string;
  username: string;
  email: string;
  status: 'Active' | 'Inactive' | 'Pending'; // Added for UI parity with badges
  lastLogin?: string;
}

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [
    CommonModule,
    ButtonModule,
    FormsModule,
    DialogModule
    // UserDetail
  ],
  templateUrl: './users.html',
  styleUrl: './users.scss'
})
export class Users {

  displayDetailDialog: boolean = false;
  selectedUser: User | null = null;

  // Mock data based on your UUID generation strategy
  users: User[] = [
    {
      id: 'f47ac10b-58cc-4372-a567-0e02b2c3d479',
      username: 'jdoe',
      email: 'jdoe@example.com',
      status: 'Active',
      lastLogin: '2023-10-27 14:23:01'
    },
    {
      id: 'a12bc34d-90ef-1234-b567-8f90a1b2c3d4',
      username: 'asmith',
      email: 'alice.smith@company.net',
      status: 'Inactive',
      lastLogin: '2023-09-15 09:30:00'
    },
    {
      id: 'c56de78f-12gh-5678-i901-2j3k4l5m6n7o',
      username: 'dev_ops_master',
      email: 'admin@infrastructure.io',
      status: 'Active',
      lastLogin: '2023-10-27 11:05:12'
    },
    {
      id: 'e90fg12h-34ij-9012-k345-6l7m8n9o0p1q',
      username: 'guest_user_01',
      email: 'temp.guest@partner.org',
      status: 'Pending',
      lastLogin: '-'
    }
  ];

  cycleStatus(user: User, event: Event) {
    event.stopPropagation();
    if (user.status === 'Active') user.status = 'Inactive';
    else if (user.status === 'Inactive') user.status = 'Active';
    // Simple toggle logic
  }

  openDetail(user: User) {
    this.selectedUser = user;
    this.displayDetailDialog = true;
  }

  createNew() {
    this.selectedUser = { id: '', username: '', email: '', status: 'Pending' }; // Reset/New
    this.displayDetailDialog = true;
  }

  onDialogClose() {
    this.displayDetailDialog = false;
    this.selectedUser = null;
  }
}
