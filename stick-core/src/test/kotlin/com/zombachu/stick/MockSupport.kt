package com.zombachu.stick

import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.SuspendingParameter
import io.mockk.coVerify
import io.mockk.verify

internal fun <E : Environment, S> verifyResolves(parameter: Parameter.Size1<E, S, *>, exactly: Int) =
    verify(exactly = exactly) { context(any<Invocation<E, S>>()) { val ignored = parameter.resolve(any<String>()) } }

internal fun <E : Environment, S> verifyResolves(parameter: SuspendingParameter.Size1<E, S, *>, exactly: Int) =
    coVerify(exactly = exactly) { context(any<Invocation<E, S>>()) { val ignored = parameter.resolve(any<String>()) } }

internal fun <E : Environment, S> verifyResolves(parameter: Parameter.Bounded<E, S, *>, exactly: Int) =
    verify(exactly = exactly) {
        context(any<Invocation<E, S>>()) { val ignored = parameter.resolve(any<List<String>>()) }
    }
