package io.github.winfeo.superpositiongame.rule.rule

import io.github.winfeo.superpositiongame.rule.model.RuleContext
import io.github.winfeo.superpositiongame.rule.model.ValidationResult

interface Rule {
    fun check(ruleContext: RuleContext): ValidationResult?
}
