package com.zombachu.stick.impl

import com.zombachu.stick.Environment

interface CommandScope<in E : Environment, S> : BuilderScope<E, S>
