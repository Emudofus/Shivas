package org.shivas.common

import com.google.common.base.Preconditions.checkState
import annotation.tailrec
import collection.mutable

/**
 * @author Blackrush
 */
trait Tree[K, V] {
  type Child = Tree[K, V]

  val key: K
  val value: Option[V]
  val parent: Option[Tree[K, V]]

  protected def getChild(key: K): Option[Child]

  def isRoot: Boolean
  def root: Tree[K, V]
  def path: Seq[K]

  def get(key: K): Option[V]
  def get(keys: Seq[K]): Option[V]

  def apply(key: K) = get(key)
  def apply(keys: Seq[K]) = get(keys)
}

/**
 * @author Blackrush
 */
trait MutableTree[K, V] extends Tree[K, V] {
  override type Child = MutableTree[K, V]

  def put(key: K, value: Option[V])
  def put(keys: Seq[K], value: Option[V])

  def remove(key: K): Option[V]
  def remove(keys: Seq[K]): Option[V]

  def put(key: K, value: V) = put(key, Option(value))
  def put(keys: Seq[K], value: V) = put(keys, Option(value))

  def update(key: K, value: Option[V]) = put(key, value)
  def update(key: K, value: V) = put(key, value)
  def update(keys: Seq[K], value: Option[V]) = put(keys, value)
  def update(keys: Seq[K], value: V) = put(keys, value)
}

/**
 * @author Blackrush
 */
trait BaseTree[K, V] extends Tree[K, V] {
  protected val children = mutable.Map.empty[K, Child]
  protected def getChild(key: K) = children.get(key)

  def get(key: K) = children.get(key).flatMap(_.value)

  def get(keys: Seq[K]) = {
    val it = keys.iterator

    def rec(current: Tree[K, V]): Tree[K, V] =
      if (!it.hasNext)
        current
      else
        current.getChild(it.next()) match {
          case Some(child) => rec(child)
          case None => current
        }

    rec(this).value
  }

  def path = if (isRoot) Seq.empty else parent.get.path :+ key
}

/**
 * @author Blackrush
 */
trait BaseMutableTree[K, V] extends BaseTree[K, V] with MutableTree[K, V] {
  protected def createChild(key: K, value: Option[V]): Child

  def put(key: K, value: Option[V]) = children.put(key, createChild(key, value))

  def put(keys: Seq[K], value: Option[V]) = {
    val it = keys.iterator
    checkState(it.hasNext, "keys.length must be greater than or equal 1")

    @tailrec
    def rec(current: MutableTree[K, V]) {
      val key = it.next()

      current.getChild(key) match {
        case Some(child) => rec(child)
        case None => current.put(key, value)
      }
    }

    rec(this)
  }

  def remove(key: K) = children.remove(key).flatMap(_.value)

  def remove(keys: Seq[K]) = {
    val it = keys.iterator

    @tailrec
    def rec(current: MutableTree[K, V]): Option[V] = {
      val key = it.next()

      if (!it.hasNext)
        current.remove(key)
      else
        current.getChild(key) match {
          case Some(child) => rec(child)
          case None => None
        }
    }

    rec(this)
  }
}

/**
 * @author Blackrush
 */
class RootTree[K, V] extends BaseTree[K, V] {
  val parent = None
  def isRoot = true
  def root = this

  val key = null
  val value = None
}

/**
 * @author Blackrush
 */
class TreeImpl[K, V](val key: K, val value: Option[V], _parent: Tree[K, V]) extends BaseTree[K, V] {
  val parent = Some(_parent)
  def isRoot = false
  def root = _parent.root
}

/**
 * @author Blackrush
 */
class MutableRootTree[K, V]
  extends RootTree[K, V]
  with BaseMutableTree[K, V]
{
  protected def createChild(key: K, value: Option[V]) = new MutableTreeImpl(key, value, this)
}

/**
 * @author Blackrush
 */
class MutableTreeImpl[K, V](key: K, value: Option[V], _parent: Tree[K, V])
  extends TreeImpl[K, V](key, value, _parent)
  with BaseMutableTree[K, V]
{
  protected def createChild(key: K, value: Option[V]) = new MutableTreeImpl(key, value, this)
}

/**
 * @author Blackrush
 */
class TreeBuilder[K, V] extends mutable.Builder[(Seq[K], Option[V]), Tree[K, V]] {
  private val builder = MutableTree.empty[K, V]

  def +=(elem: (Seq[K], Option[V])) = {
    builder(elem._1) = elem._2
    this
  }

  def clear() {}

  def result() = builder // TODO return a true immutable tree
}

/**
 * @author Blackrush
 */
object Tree {
  def builder[K, V]: mutable.Builder[(Seq[K], Option[V]), Tree[K, V]] = new TreeBuilder[K, V]
}

/**
 * @author Blackrush
 */
object MutableTree {
  def empty[K, V] = new MutableRootTree[K, V]

  def build[K, V](builder: (MutableTree[K, V]) => Unit) = {
    val tree = empty[K, V]
    builder(tree)
    tree
  }
}
