package com.zombachu.stick

sealed interface Position {

    sealed interface Anywhere : Leading, Optional

    sealed interface Leading : Last

    sealed interface Last : Position

    sealed interface Optional : LastOptional

    sealed interface LastOptional : Last
}
