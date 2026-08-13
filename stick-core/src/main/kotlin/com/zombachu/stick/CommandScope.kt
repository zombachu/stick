package com.zombachu.stick

interface CommandScope<in E : Environment, S> : BuilderScope<E, S>
