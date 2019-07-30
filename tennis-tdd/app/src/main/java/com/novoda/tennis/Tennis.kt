package com.novoda.tennis

class Game(private val playerOne: Player, private val playerTwo: Player) {

    fun score(): String {
        return ""
    }
}

data class Player(private val name: String)
