package closeness.core.vertex

import scala.collection.immutable.BitSet

class GraphSpec extends UnitSpec {
  "A Simple Graph " should "return a new graph when add a vertex" in {
    val graph = new Graph[Int]()
    graph.add(1, 2) should not be equal(graph)
  }

  it should " ranking the vertex added before" in {
    val graph = new Graph[Int]().add(1, 2).add(1, 3)
    val ranking: List[Ranking[Node[Int]]] = graph.closenessRanking
    
    ranking(0).position should be (0.5)
    ranking(1).position should be (0.3333333333333333)
  }
  
  "A Graph from file" should " load all vertex" in {
    val graph = new Graph[Int]()
    val graphFromFile = graph.fromFile("test/resource/edges.txt")
    graphFromFile.edges.size should be (945)
    graphFromFile.nodes.size should be (100)
  }
}