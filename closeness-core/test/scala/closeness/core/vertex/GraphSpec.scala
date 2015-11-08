package closeness.core.vertex

import scala.collection.immutable.BitSet

class GraphSpec extends UnitSpec{
    "A Graph" should "add the vertex" in {
    val graph = new Graph(Set.empty[Node[Integer]],Set.empty[Edge[Integer]])
    graph.add(1, 2).add(1, 3)
  }

//  it should "throw NoSuchElementException if an empty stack is popped" in {
//    val emptyGraph = new Stack[Int]
//    a [NoSuchElementException] should be thrownBy {
//      emptyGraph.pop()
//    } 
//  }
}