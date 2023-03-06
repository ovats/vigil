package com.vigil.routes.data

import com.vigil.models.requests.CreateCommentRequest
import com.vigil.models.{Comment, Post, User}
import io.circe._
import io.circe.parser._

import java.time.OffsetDateTime

object PostRoutesTestData {

  val userId: Long = 123L
  val user: User   = User(Some(userId), "John", "john@mail.com")

  val postId: Long = 456L
  val post: Post =
    Post(
      id = Some(postId),
      userId = userId,
      title = "this is a title",
      createdAt = OffsetDateTime.parse("2019-02-03T18:20:28.661Z"),
    )
  val wrongPostJsonStr: String =
    """
      | {
      |    "userId": 123,
      |    "titlexyz": "some title",
      |    "createdAt": "2019-02-03T18:20:28.661Z"
      | }
      |""".stripMargin
  val wrongPostRequest: Json = parse(wrongPostJsonStr).getOrElse(Json.Null)

  val commentId = 333L
  val commentRequest: CreateCommentRequest =
    CreateCommentRequest(userId = userId, text = "this is a comment", createdAt = "2019-02-03T18:20:28.661Z")
  val comment: Comment =
    Comment(
      id = commentId,
      userId = userId,
      postId = postId,
      text = commentRequest.text,
      createdAt = OffsetDateTime.parse(commentRequest.createdAt),
    )
  val wrongCommentJsonStr: String =
    """
      | {
      |    "userId": 123,
      |    "textxxx": "some title",
      |    "createdAt": "2019-02-03T18:20:28.661Z"
      | }
      |""".stripMargin
  val wrongCommentRequest: Json = parse(wrongCommentJsonStr).getOrElse(Json.Null)

  // format: off
  val post1: Post = Post(id = Some(1L), userId = 123, title = "Post #1", createdAt = OffsetDateTime.parse("2019-02-03T10:00:00Z"))
  val post2: Post = Post(id = Some(2L), userId = 124, title = "Post #2", createdAt = OffsetDateTime.parse("2019-02-04T10:00:00Z"))
  val post3: Post = Post(id = Some(3L), userId = 125, title = "Post #1", createdAt = OffsetDateTime.parse("2019-02-05T10:00:00Z"))
  val comm1: Comment = Comment(1L, userId = 123L, postId = 1L, "Comment for post #1 (1)", OffsetDateTime.parse("2019-02-06T10:00:00Z"))
  val comm2: Comment = Comment(2L, userId = 123L, postId = 1L, "Comment for post #1 (2)", OffsetDateTime.parse("2019-02-07T10:00:00Z"))
  val comm3: Comment = Comment(3L, userId = 123L, postId = 1L, "Comment for post #1 (3)", OffsetDateTime.parse("2019-02-08T10:00:00Z"))
  val comm4: Comment = Comment(4L, userId = 124L, postId = 2L, "Comment for post #2 (4)", OffsetDateTime.parse("2019-02-09T10:00:00Z"))
  val listPostAndComments: List[(Post, Comment)] = List((post1, comm1), (post1, comm2), (post1, comm3), (post2, comm4))
  // format: on

}
