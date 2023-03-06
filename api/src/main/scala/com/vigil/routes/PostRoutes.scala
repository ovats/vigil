package com.vigil.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.LazyLogging
import com.vigil.service.PostService
import com.vigil.tapir.PostEndpoints._
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.concurrent.ExecutionContext

class PostRoutes(postService: PostService)(implicit ec: ExecutionContext) extends LazyLogging {

  private val createPostRoute = AkkaHttpServerInterpreter().toRoute(
    createPostEndpoint.serverLogic(postRequest => postService.createPost(postRequest))
  )

  private val updatePostRoute = AkkaHttpServerInterpreter().toRoute(
    updatePostEndpoint.serverLogic { parameters =>
      val (postId, updateRequest) = parameters
      postService.updatePost(postId, updateRequest)
    }
  )

  private val getAllPostsRoute = AkkaHttpServerInterpreter().toRoute(
    getAllPostsEndpoint.serverLogic(maybeUserId => postService.getAllPosts(maybeUserId))
  )

  private val createCommentRoute = AkkaHttpServerInterpreter().toRoute(
    createPostCommentEndpoint.serverLogic { parameters =>
      val (postId, commentRequest) = parameters
      postService.createComment(postId, commentRequest)
    }
  )

  private val getAllPostsWithCommentsRoute = AkkaHttpServerInterpreter().toRoute(
    getPostCommentsEndpoint.serverLogic(maybeUserId => postService.getAllComments(maybeUserId))
  )

  val routes: Route =
    createPostRoute ~ updatePostRoute ~ getAllPostsRoute ~ createCommentRoute ~ getAllPostsWithCommentsRoute
}
