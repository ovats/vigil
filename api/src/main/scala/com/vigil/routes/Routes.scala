package com.vigil.routes

import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import com.vigil.dal.Connection
import com.vigil.service.{PostService, UserService}
import buildInfoArticleApi.BuildInfo
import com.vigil.dal.dao.{CommentDao, PostDao, UserDao}
import com.vigil.tapir.{PostEndpoints, UserEndpoints}

import scala.concurrent.{ExecutionContext, Future}
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

class Routes(cn: Connection)(implicit ec: ExecutionContext) {

  // Data access layer
  protected val postDao    = new PostDao(cn)
  protected val userDao    = new UserDao(cn)
  protected val commentDao = new CommentDao(cn)

  // Service
  protected val postService = new PostService(postDao, userDao, commentDao)
  protected val userService = new UserService(userDao)

  // Api routes
  protected val userRoutes = new UserRoutes(userService).routes
  protected val postRoutes = new PostRoutes(postService).routes

  // Documentation routes (tapir)
  private val endpoints = UserEndpoints.allEndpoints ++ PostEndpoints.allEndpoints
  private val swaggerEndpoints =
    SwaggerInterpreter().fromEndpoints[Future](endpoints, s"${BuildInfo.name}-vigil-code-challenge", BuildInfo.version)
  private val swaggerRoute = AkkaHttpServerInterpreter().toRoute(swaggerEndpoints)

  val routes: Route = userRoutes ~ postRoutes ~ swaggerRoute
}
