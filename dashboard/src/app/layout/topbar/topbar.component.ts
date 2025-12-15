import { Component, inject } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { LayoutService } from '../../core/services/layout.service';
import {InputGroup} from 'primeng/inputgroup';

@Component({
  selector: 'app-topbar',
  standalone: true,
  imports: [ButtonModule, InputGroup],
  templateUrl: './topbar.component.html',
  styleUrl: './topbar.component.scss'
})
export class TopbarComponent {
  layoutService = inject(LayoutService);
}
