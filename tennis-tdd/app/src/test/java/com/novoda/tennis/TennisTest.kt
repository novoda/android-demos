package com.novoda.tennis

import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

class TennisTest {

    private val roger = Player("Roger")
    private val rafa = Player("Rafa")
    private val game = Game(roger, rafa)

    @Test
    fun `initial score should be love all`() {
        val score = game.score()

        assertThat(score).isEqualTo("Love - Love")
    }
}
