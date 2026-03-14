import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { PolicyService } from '../../services/policy.service';
import { Policy, Decision } from '../../models/policy.model';

@Component({
    selector: 'app-testing-sandbox',
    standalone: true,
    imports: [CommonModule, RouterModule, FormsModule],
    templateUrl: './testing-sandbox.html',
    styleUrl: './testing-sandbox.css'
})
export class TestingSandboxComponent implements OnInit {
    policies: Policy[] = [];
    selectedPolicyId: string = '';
    selectedModule: string = '';
    modules: string[] = ['Catalog', 'Cart', 'Inventory', 'Shipping', 'Payment', 'Order', 'User', 'Promo'];
    testPayload: string = '{\n  "cart": {\n    "total": 500\n  }\n}';
    result: Decision | null = null;
    error: string = '';

    constructor(private policyService: PolicyService) {}

    ngOnInit(): void {
        this.policyService.getPolicies().subscribe(data => {
            this.policies = data;
        });
    }

    evaluate(): void {
        this.error = '';
        this.result = null;

        if (!this.selectedPolicyId && !this.selectedModule) {
            this.error = 'Please select a policy or a module.';
            return;
        }

        let payloadObj;
        try {
            payloadObj = JSON.parse(this.testPayload);
        } catch (e) {
            this.error = 'Invalid JSON payload. Please fix format.';
            return;
        }

        this.policyService.evaluate(
            this.selectedPolicyId || null, 
            payloadObj, 
            this.selectedModule || undefined
        ).subscribe({
            next: (decision) => {
                this.result = decision;
            },
            error: (err) => {
                this.error = err.message || 'Error executing policy';
                console.error(err);
            }
        });
    }
}
