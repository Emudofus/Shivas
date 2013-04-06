package org.shivas.common

/**
 * @author Blackrush
 */
package object event {
  implicit class EventBusExtension(eventBus: EventBus) extends AnyVal {
    def !(event: EventInterface) = eventBus.dispatch(event)
    def ?(event: EventInterface) = eventBus.query(event)
    def +=(subscriber: Any) = eventBus.subscribe(subscriber)
    def -=(subscriber: Any) = eventBus.unsubscribe(subscriber)
  }
}
