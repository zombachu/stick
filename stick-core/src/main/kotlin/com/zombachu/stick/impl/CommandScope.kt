package com.zombachu.stick.impl

import com.zombachu.stick.Environment

interface CommandScope<E : Environment, S> : BuilderScope<E, S>
