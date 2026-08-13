package com.zombachu.stick

interface BuilderScope<in E : Environment, S>

interface CommandScope<in E : Environment, S> : BuilderScope<E, S>
