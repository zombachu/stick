package com.zombachu.stick.velocity

import com.zombachu.stick.Command

interface VelocityCommand<S : Any> : Command<VelocityEnvironment, S>
