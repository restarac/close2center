package closeness.core.vertex

import scala.collection.immutable.BitSet

class GraphSpec extends UnitSpec {
  "A Simple Graph " should "return a new graph when add a vertex" in {
    val graph = new Graph[Int]()
    graph.add(1, 2) should not be equal(graph)
  }

  it should " ranking the vertex added before" in {
    val graph = new Graph[Int]().add(1, 2).add(1, 3)
    val ranking: Map[Node[Int], Double] = graph.closenessRanking
    
    ranking.get(Node(1)) should be (Some(0.5))
    ranking.get(Node(2)) should be (Some(0.3333333333333333))
    ranking.get(Node(3)) should be (Some(0.3333333333333333))
  }
  
  "A Graph from file" should " load all vertex" in {
    val graph = new Graph[Int]()
    val graphFromFile = graph.fromFile("test/resource/edges.txt")
    graphFromFile.countEdges should be (945)
    graphFromFile.countNodes should be (100)
  }
  
  it should " ranking nodes" in {
    val graphFromFile = new Graph().fromFile("test/resource/edges.txt");
    //WORKING WITH 1 THREAD TOO SLOW>>>
    val ranking: Map[Node[String], Double] = graphFromFile.closenessRanking();
    ranking.get(Node("5")) should be (Some(0.15555))
  }
}