import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PolicyService } from '../../services/policy.service';
import { FactDefinition } from '../../models/policy.model';

@Component({
    selector: 'app-fact-list',
    standalone: true,
    imports: [CommonModule, FormsModule],
    template: `
        <div class="page-header">
            <h1>Fact Metadata</h1>
            <p>Master registry of all fields available for rule engineering.</p>
        </div>

        <div class="card-grid" style="display: block;">
            <div class="search-bar" style="margin-bottom: 20px;">
                <input type="text" [(ngModel)]="searchTerm" placeholder="Search facts..." 
                       class="form-control" style="max-width: 400px; padding: 12px;">
            </div>

            <div *ngFor="let module of getModules()" class="module-section" style="margin-bottom: 30px;">
                <h2 style="border-bottom: 2px solid #eee; padding-bottom: 10px;">{{ module }} Module</h2>
                <div class="fact-grid" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px; margin-top: 15px;">
                    <div *ngFor="let fact of getFactsByModule(module)" class="card fact-card">
                        <div style="font-weight: bold; font-size: 1.1em; color: #2c3e50;">{{ fact.displayName }}</div>
                        <div style="font-family: monospace; color: #3498db; margin: 5px 0;">#{{ fact.attribute }}</div>
                        <div style="font-size: 0.9em; color: #7f8c8d;">
                            Entity: {{ fact.entityName }} | Type: {{ fact.dataType }}
                        </div>
                    </div>
                </div>
            </div>
            
            <div *ngIf="facts.length === 0" class="card" style="text-align: center; padding: 40px;">
                <p>No fact definitions found. Check if Metadata Registry is populated.</p>
            </div>
        </div>
    `,
    styles: [`
        .fact-card {
            border-left: 4px solid #3498db;
            transition: transform 0.2s;
        }
        .fact-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
    `]
})
export class FactListComponent implements OnInit {
    facts: FactDefinition[] = [];
    searchTerm: string = '';

    constructor(private policyService: PolicyService) {}

    ngOnInit(): void {
        this.policyService.getFacts().subscribe(data => {
            this.facts = data;
        });
    }

    getModules(): string[] {
        const filtered = this.getFilteredFacts();
        return [...new Set(filtered.map(f => f.module))];
    }

    getFactsByModule(module: string): FactDefinition[] {
        return this.getFilteredFacts().filter(f => f.module === module);
    }

    getFilteredFacts(): FactDefinition[] {
        if (!this.searchTerm) return this.facts;
        const term = this.searchTerm.toLowerCase();
        return this.facts.filter(f => 
            f.displayName.toLowerCase().includes(term) || 
            f.attribute.toLowerCase().includes(term) ||
            f.module.toLowerCase().includes(term)
        );
    }
}
