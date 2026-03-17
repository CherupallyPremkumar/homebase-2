export enum Effect {
    ALLOW = 'ALLOW',
    DENY = 'DENY',
    ABSTAIN = 'ABSTAIN'
}

export interface Rule {
    id: string;
    name: string;
    expression: string;
    effect: Effect;
    priority: number;
    active: boolean;
}

export interface Policy {
    id: string;
    name: string;
    targetModule?: string;
    description: string;
    active: boolean;
    defaultEffect: Effect;
    rules: Rule[];
}

export interface Decision {
    id: string;
    policyId: string;
    targetModule?: string;
    subjectId?: string;
    resource?: string;
    action?: string;
    effect: Effect;
    reasons: string;
    timestamp?: string;
    metadata?: Record<string, string>;
}

export interface FactDefinition {
    id: string;
    module: string;
    entityName: string;
    displayName: string;
    attribute: string;
    dataType: string;
}
