
package closeness.core.vertex

import scala.collection.immutable.BitSet
import scala.annotation.tailrec

//There isnt any node alone...
class Graph[V <: Number](nodes: Set[Node[V]], edges: Set[Edge[V]]) {

  def add(v1: V, v2: V): Graph[V] = {
    val n2 = Node(v2)
    val n1 = Node(v1)
    new Graph(nodes + (n1, n2), edges + Edge(n1, n2));
  }

  def closeness(v1: V): Float = {
    val n1 = Node(v1)
    1
  }
}

protected class ShortestPath[T](edges: Set[Edge[T]]) {

  lazy val outgoingEdges = edges.groupBy(_.v1)mapValues(s => s.map(_.v2))
  lazy val incomingEdges = edges.groupBy(_.v2)mapValues(s => s.map(_.v1))
  lazy val nodesLinkedOtherNodes: Map[Node[T], Set[Node[T]]] = outgoingEdges ++ incomingEdges.map{ 
    case (k,v) => 
      k -> (outgoingEdges.getOrElse(k, Set.empty) ++ v) 
  }
  
  def from(origin: T): ShortestPathCalculator = {
    val originNode = Node(origin)
    new ShortestPathCalculator(originNode)
  }

  class ShortestPathCalculator(originNode: Node[T]) {
    def to(to: T): Integer = {
      discover(originNode, Node(to), Set.empty)
    }
    
    private def discover(origin: Node[T], to: Node[T], edgesAlreadyPassed: Set[Node[T]]): Integer = {
      val originEdges = nodesLinkedOtherNodes.get(origin)
      val edgesNotVerified: Option[Set[Node[T]]] = originEdges.map { x => x diff edgesAlreadyPassed }
      if (edgesNotVerified.isEmpty) 0
      if (edgesNotVerified.get.contains(to)) 1
      else edgesNotVerified.get.toList.map { x => discover(x, to, edgesAlreadyPassed+x) }.foldLeft(0)(_+_)
    }
  }

}

case class Node[+T](value: T)
case class Edge[+T](v1: Node[T], v2: Node[T]) {
  def this(v1: T, v2: T) {
    this(Node(v1), Node(v2))
  }
}
