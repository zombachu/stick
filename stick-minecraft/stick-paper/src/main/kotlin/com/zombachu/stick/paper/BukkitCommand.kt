package com.zombachu.stick.paper

import com.zombachu.stick.Command

interface BukkitCommand<S : Any> : Command<BukkitEnvironment, S>
