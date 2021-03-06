
package closeness.core.vertex

import scala.annotation.migration
import scala.collection.Map
import scala.collection.Set
import scala.io.Source

case class Ranking[+T](node: T, position: Double)
case class Node[+T](value: T)
case class Edge[+T](nodeIn: Node[T], nodeOut: Node[T]) {
  def this(in: T, out: T) {
    this(Node(in), Node(out))
  }
}

class Graph[V](val nodes: Set[Node[V]], val edges: Set[Edge[V]]) {
  lazy val closeness = new ClosenessCentrality(edges)

  def this() { this(Set.empty[Node[V]], Set.empty[Edge[V]]) }

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

  val decrescent = (r1: Ranking[Node[V]], r2: Ranking[Node[V]]) => (r1.position > r2.position)

  def par(): Graph[V] = {
    new Graph(nodes, edges) {
      override def closenessRanking(): List[Ranking[Node[V]]] = {
        nodes.par.map { node => Ranking(node, 1.0./(closeness from node)) }.toList.sortWith(decrescent)
      }
    }
  }
  
  def closenessRanking(): List[Ranking[Node[V]]] = {
    rankingNodes
  }
  lazy val rankingNodes = nodes.map { node => Ranking(node, 1.0./(closeness from node)) }.toList.sortWith(decrescent)
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
    discover(originNode, 1, Set(originNode))
  }

  private def discover(from: Node[T], deepLevel: Int, edgesAlreadyPassed: Set[Node[T]]): Int = {
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
}
