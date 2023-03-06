package com.vigil.tapir

import com.vigil.models.User
import io.circe.generic.auto._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.{oneOf, path}

object UserEndpoints extends EndpointsCommon with ErrorStatusMapping {

  private val userRequestSample = User(id = None, "John Doe", "john.doe@mail.com")

  val createUserEndpoint: PublicEndpoint[User, EndpointError, User, Any] =
    usersBaseEndpoint
      .summary("Creates a new user")
      .description("Use this endpoint to create new users")
      .post
      .in(jsonBody[User].example(userRequestSample))
      .out(jsonBody[User])
      .errorOut(
        oneOf[EndpointError](
          internalServerErrorEndpointErrorStatusMapping(),
          badRequestEndpointErrorStatusMapping(),
          userAlreadyExistsErrorStatusMapping(),
        )
      )

  val getUserEndpoint: PublicEndpoint[Long, EndpointError, User, Any] = {
    usersBaseEndpoint
      .summary("Retrieves user details")
      .description("Use this endpoint to get the details for a given user")
      .get
      .in(path[Long]("userId"))
      .out(jsonBody[User])
      .errorOut(
        oneOf[EndpointError](
          internalServerErrorEndpointErrorStatusMapping(),
          notFoundEndpointErrorStatusMapping(),
        )
      )
  }

  val getAllUserEndpoint: PublicEndpoint[Unit, EndpointError, Seq[User], Any] =
    usersBaseEndpoint
      .summary("Retrieves all users")
      .description("Use this endpoint to retrieve details of all users in the application")
      .get
      .out(jsonBody[Seq[User]])
      .errorOut(
        oneOf[EndpointError](
          internalServerErrorEndpointErrorStatusMapping()
        )
      )

  val allEndpoints = List(createUserEndpoint, getUserEndpoint, getAllUserEndpoint)
}
