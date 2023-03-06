package com.vigil.tapir

import sttp.tapir.{endpoint, Endpoint}

trait EndpointsCommon {

  type PublicEndpoint[INPUT, ERROR_OUTPUT, OUTPUT, -R] = Endpoint[Unit, INPUT, ERROR_OUTPUT, OUTPUT, R]

  protected val usersBaseEndpoint = endpoint
    .in("users")
    .tag("users")

  protected val postsBaseEndpoint = endpoint
    .in("posts")
    .tag("posts")

  protected val commentsBaseEndpoint = endpoint
    .in("comments")
    .tag("posts")

}
