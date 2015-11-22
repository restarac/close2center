package controllers

import play.api.libs.json._
import play.api.cache._
import play.api.mvc._
import javax.inject.Inject
import closeness.core.vertex.Graph
import closeness.core.vertex.Edge

class Application @Inject() (cache: CacheApi) extends Controller {

  val graph: Graph[String] = cache.getOrElse("graph")(new Graph[String]().add("1", "6").add("6", "3").add("1", "2"))

  def showGraph = Action {
    Ok(Json.toJson(Json.obj(
      "nodes" -> Json.arr(graph.nodes.map { x =>
        Json.obj("name" -> x.value)
      }),
      "edges" -> Json.arr(graph.edges.map { x =>
        Json.obj(
          "nodeIn" -> x.nodeIn.value,
          "nodeOut" -> x.nodeOut.value)
      }))))
  }

  def showNodes = Action {
    Ok(Json.toJson(Json.obj(
      "nodes" -> Json.arr(graph.nodes.map { x =>
        Json.obj("name" -> x.value)
      }))))
  }

  def showEdges = Action {
    Ok(Json.toJson(Json.obj(
      "edges" -> Json.arr(graph.edges.map { x =>
        Json.obj(
          "fromNode" -> x.nodeIn.value,
          "toNode" -> x.nodeOut.value)
      }))))
  }

  //  def saveEdges = Action(BodyParsers.parse.json) { request =>
  //    val placeResult = request.body.validate[Edge[String]]
  //    placeResult.fold(
  //      errors => {
  //        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))
  //      },
  //      place => {
  //        Place.save(place)
  //        Ok(Json.obj("status" -> "OK", "message" -> ("Place '" + place.name + "' saved.")))
  //      })
  //  }

}
