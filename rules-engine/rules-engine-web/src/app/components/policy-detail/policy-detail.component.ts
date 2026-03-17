import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { PolicyService } from '../../services/policy.service';
import { Policy, Rule } from '../../models/policy.model';
import { RuleEditorComponent } from '../rule-editor/rule-editor.component';

@Component({
    selector: 'app-policy-detail',
    standalone: true,
    imports: [CommonModule, RouterModule, FormsModule, RuleEditorComponent],
    templateUrl: './policy-detail.component.html',
    styleUrl: './policy-detail.component.css'
})
export class PolicyDetailComponent implements OnInit {
    policy: Policy | null = null;
    editingRule: Rule | null = null;
    modules: string[] = ['Catalog', 'Cart', 'Inventory', 'Shipping', 'Payment', 'Order', 'User', 'Promo'];

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private policyService: PolicyService
    ) { }

    ngOnInit(): void {
        const id = this.route.snapshot.paramMap.get('id');
        if (id && id !== 'new') {
            this.policyService.getPolicies().subscribe({
                next: (policies) => {
                    this.policy = policies.find(p => p.id === id) || null;
                },
                error: (err) => console.error('Error fetching policy', err)
            });
        } else {
            // It's a new policy (either id is 'new' or null from /policies/new route)
            this.policy = {
                id: '',
                name: '',
                targetModule: '',
                description: '',
                active: true,
                defaultEffect: 'DENY' as any,
                rules: []
            };
        }
    }

    addRule(): void {
        this.editingRule = {
            id: '',
            name: '',
            expression: '',
            effect: 'ALLOW' as any,
            priority: (this.policy?.rules.length || 0) + 1,
            active: true
        };
    }

    editRule(rule: Rule): void {
        this.editingRule = { ...rule };
    }

    saveRule(rule: Rule): void {
        if (!this.policy) return;

        const index = this.policy.rules.findIndex(r => r.id === rule.id);
        if (index >= 0) {
            this.policy.rules[index] = rule;
        } else {
            rule.id = 'temp_' + Math.random().toString(36).substr(2, 9);
            this.policy.rules.push(rule);
        }
        this.editingRule = null;
    }

    deleteRule(rule: Rule): void {
        if (!this.policy) return;
        this.policy.rules = this.policy.rules.filter(r => r !== rule);
    }

    savePolicy(): void {
        if (!this.policy) return;

        const obs = this.policy.id 
            ? this.policyService.updatePolicy(this.policy.id, this.policy)
            : this.policyService.createPolicy(this.policy);

        obs.subscribe({
            next: () => {
                this.router.navigate(['/policies']);
            },
            error: (err) => {
                console.error('Error saving policy', err);
            }
        });
    }

    cancelEdit(): void {
        this.editingRule = null;
    }
}
