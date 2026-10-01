package com.zombachu.stick.paper

import com.zombachu.stick.Command

interface PaperCommand<S : Any> : Command<PaperEnvironment, S>
