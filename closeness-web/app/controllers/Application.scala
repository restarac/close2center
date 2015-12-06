package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.cache._
import javax.inject.Inject
import closeness.core.vertex.Graph
import closeness.core.vertex.Edge
//import closeness.core.vertex.Node

class Application @Inject() (cache: CacheApi) extends Controller {

  case class EdgePresenter(fromNode: String, toNode: String)

  def graph: Graph[String] = cache.getOrElse("graph")(new Graph[String]().add("1", "6").add("6", "3").add("1", "2"))
  implicit val edgePresenterReads: Reads[EdgePresenter] = (
    (__ \ "fromNode").read[String](minLength[String](1)) and
    (__ \ "toNode").read[String](minLength[String](1)))(EdgePresenter.apply _)

  def showGraph = Action {
    Ok(Json.toJson(Json.obj(
      "nodes" -> graph.nodes.map { x =>
        Json.obj("name" -> x.value)
      },
      "edges" -> graph.edges.map { x =>
        Json.obj(
          "nodeIn" -> x.nodeIn.value,
          "nodeOut" -> x.nodeOut.value)
      })))
  }

  def showNodes = Action {
    Ok(Json.toJson(Json.obj(
      "nodes" -> graph.nodes.map { x =>
        Json.obj("name" -> x.value)
      })))
  }

  def showEdges = Action {
    Ok(Json.toJson(Json.obj(
      "edges" -> graph.edges.map { x =>
        Json.obj(
          "fromNode" -> x.nodeIn.value,
          "toNode" -> x.nodeOut.value)
      })))
  }

  def showRanking = Action {
    Ok(Json.toJson(Json.obj(
      "ranking" -> graph.closenessRanking().map { x =>
        Json.obj(
          "node" -> x.node.value,
          "value" -> x.position)
      })))
  }

  def saveEdges = Action(BodyParsers.parse.json) { request =>
    val placeResult = request.body.validate[EdgePresenter]
    placeResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      edge => {
        cache.set("graph", graph.add(edge.fromNode, edge.toNode))
        Ok(Json.obj("status" -> "OK", "message" -> ("Edge '" + edge + "' saved.")))
      })
  }
}
