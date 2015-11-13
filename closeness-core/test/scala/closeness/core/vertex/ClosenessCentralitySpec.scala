package closeness.core.vertex

class ClosenessCentralitySpec extends UnitSpec{
  "The shortest path " should " shows the equality path score" in {
    val edges: Set[Edge[Int]] = Set(new Edge(1, 2),new Edge(2, 3),new Edge(1, 4),new Edge(4, 3))
    val closeness = new ClosenessCentrality(edges)
    
    closeness.from(1) should be (6)
  }
  
  it should " show score without score the path after the objective node" in {
    val edges: Set[Edge[Int]] = Set(new Edge(1, 2),new Edge(2, 3),new Edge(1, 4),new Edge(4, 3),new Edge(3, 5),new Edge(3, 10))
    val closeness = new ClosenessCentrality(edges)
    
    closeness.from(3) should be (8)
  }
  
  it should " show score betwen 2 diferents paths" in {
    val edges: Set[Edge[Int]] = Set(new Edge(1, 2),new Edge(2, 3),new Edge(3, 4),new Edge(4, 5),new Edge(1, 3),new Edge(3, 6),new Edge(6, 5))
    val closeness = new ClosenessCentrality(edges)
    
    closeness.from(2) should be (3)
  }
  
  it should " show score betwen 1 unresolved path" in {
    val edges: Set[Edge[Int]] = Set(new Edge(1, 2),new Edge(2, 3),new Edge(3, 4),new Edge(4, 5),new Edge(1, 3),new Edge(3, 6))
    val closeness = new ClosenessCentrality(edges)
    
    closeness.from(1) should be (6)
  }
}