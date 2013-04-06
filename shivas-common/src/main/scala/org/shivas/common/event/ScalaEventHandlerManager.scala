package org.shivas.common.event

import com.google.common.collect.Multimap
import org.shivas.common.threads.Future
import scala.collection.JavaConversions._
import org.shivas.common.Singleton

/**
 * @author Blackrush
 */
object ScalaEventHandlerManager extends EventHandlerManager with Singleton {
  type This = this.type
  type Handler = PartialFunction[Any, Any]

  def onRegistered(handlerClass: Class[_]) { }
  def onUnregistered(handlerClass: Class[_]) { }

  def dispatch(handlers: Multimap[Class[_], AnyRef], event: EventInterface, future: Future[AnyRef]) {
    for (o <- handlers.values() if !event.hasBeenStopped && o.isInstanceOf[Handler]) {
      val handler = o.asInstanceOf[Handler]
      
      if (handler.isDefinedAt(event)) handler.apply(event)
    }
  }
}
