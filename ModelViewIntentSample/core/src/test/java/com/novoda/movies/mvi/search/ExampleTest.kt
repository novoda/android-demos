package com.novoda.movies.mvi.search

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


internal class ExampleTest {

    @Test
    fun `should assert sth`() {
        val foo = mock<Foo>()
        whenever(foo.bar()).thenReturn("bar2")

        assertThat(foo.bar()).isEqualTo("bar2")
    }

    class Foo {
        fun bar(): String = "bar"
    }
}
