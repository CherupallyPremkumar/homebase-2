import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PolicyService } from '../../services/policy.service';
import { Decision } from '../../models/policy.model';

@Component({
    selector: 'app-audit-log',
    standalone: true,
    imports: [CommonModule, RouterModule],
    templateUrl: './audit-log.html',
    styleUrl: './audit-log.css'
})
export class AuditLogComponent implements OnInit {
    decisions: Decision[] = [];

    constructor(private policyService: PolicyService) {}

    ngOnInit(): void {
        this.policyService.getDecisions().subscribe((data: Decision[]) => {
            this.decisions = data;
        });
    }
}
