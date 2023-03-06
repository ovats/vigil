package com.vigil.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.LazyLogging
import com.vigil.service.UserService
import com.vigil.tapir.UserEndpoints.{createUserEndpoint, getAllUserEndpoint, getUserEndpoint}
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.concurrent.ExecutionContext

class UserRoutes(userService: UserService)(implicit ec: ExecutionContext) extends LazyLogging {

  private val createUserRoute: Route = AkkaHttpServerInterpreter().toRoute(
    createUserEndpoint.serverLogic(userRequest => userService.createUser(userRequest))
  )
  private val getUserRoute: Route =
    AkkaHttpServerInterpreter().toRoute(getUserEndpoint.serverLogic(userId => userService.getUser(userId)))
  private val getAllUsersRoute: Route =
    AkkaHttpServerInterpreter().toRoute(getAllUserEndpoint.serverLogic(_ => userService.getAllUsers()))

  val routes: Route = createUserRoute ~ getUserRoute ~ getAllUsersRoute

}
