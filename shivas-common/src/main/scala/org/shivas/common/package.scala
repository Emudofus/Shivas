package org.shivas

import java.util.concurrent.Callable

/**
 * @author Blackrush
 */
package object common {
  implicit class RunnableConverter(lambda: => Unit) extends Runnable {
    def run() = lambda
  }

  implicit class RunnableConverter2(lambda: () => Unit) extends Runnable {
    def run() = lambda
  }

  implicit class CallableConverter[V](lamba: => V) extends Callable[V] {
    def call(): V = lamba
  }

  implicit class CallableConverter2[V](lambda: () => V) extends Callable[V] {
    def call(): V = lambda()
  }
}
