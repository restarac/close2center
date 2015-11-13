
package closeness.core.vertex

import scala.collection.immutable.BitSet
import scala.annotation.tailrec
import scala.collection.immutable.Set

//There isnt any node alone...
class Graph[V <: Number](nodes: Set[Node[V]], edges: Set[Edge[V]]) {

  def add(in: V, out: V): Graph[V] = {
    val nodeIn = Node(out)
    val nodeOut = Node(in)
    new Graph(nodes + (nodeIn, nodeOut), edges + Edge(nodeIn, nodeOut));
  }
  lazy val closenessCentrality = new ClosenessCentrality(edges)

  def closeness(nodeId: V): Float = {
    //http://objdig.ufrj.br/60/teses/coppe_m/LeandroQuintanilhaDeFreitas.pdf
    1 / closenessCentrality.from(nodeId)
  }
}

protected class ClosenessCentrality[T](edges: Set[Edge[T]]) {

  lazy val outgoingEdges = edges.groupBy(_.nodeIn) mapValues (s => s.map(_.nodeOut))
  lazy val incomingEdges = edges.groupBy(_.nodeOut) mapValues (s => s.map(_.nodeIn))
  lazy val nodesLinkedOtherNodes: Map[Node[T], Set[Node[T]]] = (outgoingEdges ++ incomingEdges).map {
    case (k, v) =>
      k -> (outgoingEdges.getOrElse(k, Set.empty) ++ v)
  }

  def from(origin: T): Int = {
    
    def discover(from: Node[T], deepLevel: Int, edgesAlreadyPassed: Set[Node[T]]): Int = {
      val edgesNotVerified: List[Node[T]] = nodesLinkedOtherNodes getOrElse (from, Set()) diff edgesAlreadyPassed toList;
      if (edgesNotVerified.isEmpty) 0
      else {
        val total = edgesNotVerified
          .view
          .foldLeft(0) {
            case (acc, value) => acc + discover(value, deepLevel + 1, edgesAlreadyPassed ++ edgesNotVerified)
          }
        (edgesNotVerified.size * deepLevel) + total
      }
    }
    
    val originNode = Node(origin)
    discover(originNode, 1, Set(originNode))
  }

}

case class Node[+T](value: T)
case class Edge[+T](nodeIn: Node[T], nodeOut: Node[T]) {
  def this(in: T, out: T) {
    this(Node(in), Node(out))
  }
}
