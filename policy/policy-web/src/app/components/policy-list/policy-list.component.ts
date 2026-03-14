import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PolicyService } from '../../services/policy.service';
import { Policy } from '../../models/policy.model';

@Component({
    selector: 'app-policy-list',
    standalone: true,
    imports: [CommonModule, RouterModule],
    templateUrl: './policy-list.component.html',
    styleUrl: './policy-list.component.css'
})
export class PolicyListComponent implements OnInit {
    policies: Policy[] = [];

    constructor(private policyService: PolicyService) { }

    ngOnInit(): void {
        this.policyService.getPolicies().subscribe(data => {
            this.policies = data;
        });
    }
}
