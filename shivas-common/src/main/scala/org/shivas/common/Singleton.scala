package org.shivas.common

/**
 * @author Blackrush
 */
trait Singleton {
  type This <: Singleton
  def instance = this.asInstanceOf[This]
  def getInstance() = instance
}
