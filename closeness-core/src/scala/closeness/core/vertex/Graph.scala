
package closeness.core.vertex

import scala.collection.immutable.BitSet

//There isnt any node alone...
class Graph[V <: Number](nodes: Set[Node[V]], edges: Set[Edge[V]]) {

  def add(v1: V, v2: V): Graph[V] = {
    val n2 = Node(v2)
    val n1 = Node(v1)
    new Graph(nodes + (n1, n2), edges + Edge(n1, n2));
  }
  
  def closeness(v1: V): Integer = {
    val n1 = Node(v1)
    1
  }
  
  lazy val outgoingEdges: Map[Node[V], Set[Node[V]]] = edges.groupBy(_.v1).mapValues(s => s.map(_.v2))
}

//outgoingEdges = o value é a key para o proximo path...
//Calcula de cada um verificando se o valor anterior é o menor, caso não seja, substitui-lo.
// testar todos os caminhos...



// BitSet
case class Node[+T](value: T)
case class Edge[+T](v1: Node[T], v2: Node[T])
