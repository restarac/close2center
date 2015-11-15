
package closeness.core.vertex

import scala.collection.immutable.BitSet
import scala.annotation.tailrec
import scala.collection.immutable.Set
import scala.collection.immutable.Map
import scala.io.Source

//There isnt any node alone...
class Graph[V](nodes: Set[Node[V]], edges: Set[Edge[V]]) {
  lazy val closeness = new ClosenessCentrality(edges)

  def this() { this(Set.empty[Node[V]], Set.empty[Edge[V]]) }
  def countNodes(): Int = { nodes.size }
  def countEdges(): Int = { edges.size }

  def fromFile(fileName: String): Graph[String] = {

    def addLine(graph: Graph[String], line: String): Graph[String] = {
      val values = line.split(" ")(_)
      graph.add(values(0), values(1))
    }

    val f = Source.fromFile(fileName)
    val graphFromFile = f.getLines().foldLeft(new Graph[String]()) { case (acc, value) => addLine(acc, value) }
    f.close()
    graphFromFile
  }

  def add(in: V, out: V): Graph[V] = {
    val nodeIn = Node(out)
    val nodeOut = Node(in)
    new Graph(nodes + (nodeIn, nodeOut), edges + Edge(nodeIn, nodeOut));
  }

  def closenessRanking(): Map[Node[V], Double] = {
    val nodesList = nodes.toList
    val closenessResult: List[Double] = nodesList.map { node => 1.0./(closeness.from(node.value)) }
    nodesList zip closenessResult toMap
    //http://objdig.ufrj.br/60/teses/coppe_m/LeandroQuintanilhaDeFreitas.pdf
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
    val originNode = Node(origin)
    this.from(originNode)
  }

  def from(originNode: Node[T]): Int = {
    def discover(from: Node[T], deepLevel: Int, edgesAlreadyPassed: Set[Node[T]]): Int = {
      val edgesNotVerified: Set[Node[T]] = nodesLinkedOtherNodes getOrElse (from, Set()) diff edgesAlreadyPassed;
      if (edgesNotVerified.isEmpty) 0
      else {
        val edgesUpdated = edgesAlreadyPassed ++ edgesNotVerified
        val nextLevel = deepLevel + 1
        val total = edgesNotVerified.foldLeft(0) {
            case (acc, value) => acc + discover(value, nextLevel, edgesUpdated)
          }
        (edgesNotVerified.size * deepLevel) + total
      }
    }
    discover(originNode, 1, Set(originNode))
  }

}

case class Node[+T](value: T)
case class Edge[+T](nodeIn: Node[T], nodeOut: Node[T]) {
  def this(in: T, out: T) {
    this(Node(in), Node(out))
  }
}
