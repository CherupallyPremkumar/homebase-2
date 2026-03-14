import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Rule, FactDefinition } from '../../models/policy.model';
import { PolicyService } from '../../services/policy.service';

@Component({
    selector: 'app-rule-editor',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './rule-editor.component.html',
    styleUrl: './rule-editor.component.css'
})
export class RuleEditorComponent implements OnInit {
    @Input() rule!: Rule;
    @Output() save = new EventEmitter<Rule>();
    @Output() cancel = new EventEmitter<void>();

    facts: FactDefinition[] = [];
    selectedFact: string = '';
    selectedOperator: string = '==';
    enteredValue: string = '';

    operators = [
        { label: 'Equals', value: '==' },
        { label: 'Not Equals', value: '!=' },
        { label: 'Greater Than', value: '>' },
        { label: 'Less Than', value: '<' },
        { label: 'Greater or Equal', value: '>=' },
        { label: 'Less or Equal', value: '<=' }
    ];

    constructor(private policyService: PolicyService) {}

    ngOnInit(): void {
        this.policyService.getFacts().subscribe(data => {
            this.facts = data;
        });
        
        if (this.rule.expression) {
            const parts = this.rule.expression.split(' ');
            if (parts.length >= 3) {
                this.selectedFact = parts[0];
                this.selectedOperator = parts[1];
                let val = parts.slice(2).join(' ');
                if (val.startsWith("'") && val.endsWith("'")) {
                    val = val.substring(1, val.length - 1);
                }
                this.enteredValue = val;
            }
        }
    }

    getModules(): string[] {
        return [...new Set(this.facts.map(f => f.module))];
    }

    getFactsByModule(module: string): FactDefinition[] {
        return this.facts.filter(f => f.module === module);
    }

    onSave(): void {
        if (this.selectedFact) {
            let val = this.enteredValue;
            if (isNaN(Number(val)) && val !== 'true' && val !== 'false') {
                val = `'${val}'`;
            }
            this.rule.expression = `${this.selectedFact} ${this.selectedOperator} ${val}`;
        }
        this.save.emit(this.rule);
    }

    onCancel(): void {
        this.cancel.emit();
    }
}
