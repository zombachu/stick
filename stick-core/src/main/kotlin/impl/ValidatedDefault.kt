package com.zombachu.stick.impl

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.element.Validator

class ValidatedDefault<S, T : Any>(
    val value: ContextualValue<S, T>,
    override val validate: (S) -> Boolean,
): Validator<S>
