package com.vigil.tapir

import com.vigil.models.requests.{CreateCommentRequest, UpdatePostRequest}
import com.vigil.models.{Comment, Post}
import io.circe.generic.auto._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.{oneOf, path, query}

import java.time.OffsetDateTime

object PostEndpoints extends EndpointsCommon with ErrorStatusMapping {

  private val createPostExample =
    Post(id = None, userId = 123L, "title of the post", OffsetDateTime.parse("2019-02-03T18:20:28.661Z"))
  private val createCommentExample =
    CreateCommentRequest(userId = 123L, text = "comment for the post", "2020-04-06T15:21:45.135Z")
  private val updatePostExample =
    UpdatePostRequest(title = "title edited", createdAt = OffsetDateTime.parse("2019-02-03T18:20:28.661Z"))

  val createPostEndpoint: PublicEndpoint[Post, EndpointError, Post, Any] =
    postsBaseEndpoint
      .summary("Creates a new post")
      .description("Use this endpoint to create a new post")
      .post
      .in(jsonBody[Post].example(createPostExample))
      .out(jsonBody[Post])
      .errorOut(
        oneOf[EndpointError](
          internalServerErrorEndpointErrorStatusMapping(),
          badRequestEndpointErrorStatusMapping(),
          userDoesNotExistErrorStatusMapping(),
        )
      )

  val updatePostEndpoint: PublicEndpoint[(Long, UpdatePostRequest), EndpointError, Post, Any] =
    postsBaseEndpoint
      .summary("Updates a post")
      .description("Updates a post (title, creation date/time) given its id")
      .patch
      .in(path[Long]("postId"))
      .in(jsonBody[UpdatePostRequest].example(updatePostExample))
      .out(jsonBody[Post])
      .errorOut(
        oneOf[EndpointError](
          internalServerErrorEndpointErrorStatusMapping(),
          badRequestEndpointErrorStatusMapping(),
          postDoesNotExistErrorStatusMapping(),
        )
      )

  val getAllPostsEndpoint: PublicEndpoint[Option[Long], EndpointError, Seq[Post], Any] =
    postsBaseEndpoint
      .summary("Retrieve all posts")
      .description(
        "Use this endpoint to get all the posts of all users (order by date descending). Use with care, data is not paginated and it will return all posts."
      )
      .get
      .in(query[Option[Long]]("userId").description("For filtering posts created by a given user id"))
      .out(jsonBody[Seq[Post]])
      .errorOut(
        oneOf[EndpointError](
          internalServerErrorEndpointErrorStatusMapping(),
          badRequestEndpointErrorStatusMapping(),
        )
      )

  val createPostCommentEndpoint: PublicEndpoint[(Long, CreateCommentRequest), EndpointError, Comment, Any] = {
    postsBaseEndpoint
      .summary("Add a comment to a post")
      .description("Use this endpoint to add comment from users to a post")
      .post
      .in(path[Long]("postId"))
      .in("comment")
      .in(jsonBody[CreateCommentRequest].example(createCommentExample))
      .out(jsonBody[Comment])
      .errorOut(
        oneOf[EndpointError](
          internalServerErrorEndpointErrorStatusMapping(),
          badRequestEndpointErrorStatusMapping(),
          userDoesNotExistErrorStatusMapping(),
          postDoesNotExistErrorStatusMapping(),
        )
      )
  }

  val getPostCommentsEndpoint: PublicEndpoint[Option[Long], EndpointError, Seq[Comment], Any] =
    commentsBaseEndpoint
      .summary("Retrieve post with comments")
      .description("Use this endpoint to retrieve post with comments")
      .get
      .in(query[Option[Long]]("userId").description("For filtering posts created by a given user id"))
      .out(jsonBody[Seq[Comment]])
      .errorOut(
        oneOf[EndpointError](
          internalServerErrorEndpointErrorStatusMapping(),
          badRequestEndpointErrorStatusMapping(),
          userDoesNotExistErrorStatusMapping(),
        )
      )

  val allEndpoints = List(
    createPostEndpoint,
    updatePostEndpoint,
    getAllPostsEndpoint,
    createPostCommentEndpoint,
    getPostCommentsEndpoint,
  )
}
