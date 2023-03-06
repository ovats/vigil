package com.vigil.service

import com.typesafe.scalalogging.LazyLogging
import com.vigil.dal.dao.{CommentDao, PostDao, UserDao}
import com.vigil.models.requests.{CreateCommentRequest, UpdatePostRequest}
import com.vigil.models.{Comment, Post}
import com.vigil.tapir.EndpointError

import java.time.OffsetDateTime
import scala.concurrent.{ExecutionContext, Future}

class PostService(postDao: PostDao, userDao: UserDao, commentDao: CommentDao)(implicit ec: ExecutionContext)
    extends LazyLogging
    with ServiceCommon {

  def createPost(post: Post): Future[Either[EndpointError, Post]] = {
    userDao.findById(post.userId).flatMap {
      case None =>
        val error: Either[EndpointError, Post] = Left(EndpointError("User does not exist"))
        Future.successful(error)
      case Some(_) =>
        postDao
          .create(post)
          .map(Right(_))
          .recoverWith(handleExceptions("creating a post"))
    }
  }

  def updatePost(postId: Long, details: UpdatePostRequest): Future[Either[EndpointError, Post]] = {
    postDao
      .findById(postId)
      .flatMap {
        case None =>
          val error: Either[EndpointError, Post] = Left(EndpointError("Post does not exist"))
          Future.successful(error)
        case Some(oldPost) =>
          val newPost =
            Post(id = Some(postId), userId = oldPost.userId, title = details.title, createdAt = details.createdAt)
          postDao
            .update(postId, newPost)
            .map(_ => Right(newPost): Either[EndpointError, Post])

      }
      .recoverWith(handleExceptions("updating a post"))
  }

  def getAllPosts(maybeUserId: Option[Long]): Future[Either[EndpointError, Seq[Post]]] = {
    postDao
      .findAll(maybeUserId)
      .flatMap(l => Future.successful(Right(l)))
      .recoverWith(handleExceptions("retrieving all posts"))
  }

  def createComment(postId: Long, commentRequest: CreateCommentRequest): Future[Either[EndpointError, Comment]] = {
    val checks = for {
      user <- userDao.findById(commentRequest.userId)
      post <- postDao.findById(postId)
    } yield (user, post)

    checks.flatMap {
      case (None, _) =>
        val error: Either[EndpointError, Comment] = Left(EndpointError("User does not exist"))
        Future.successful(error)
      case (_, None) =>
        val error: Either[EndpointError, Comment] = Left(EndpointError("Post does not exist"))
        Future.successful(error)
      case (Some(user), Some(post)) =>
        val newComment = Comment(
          0L,
          user.id.getOrElse(0),
          post.id.getOrElse(0),
          commentRequest.text,
          OffsetDateTime.parse(commentRequest.createdAt),
        )
        commentDao
          .create(newComment)
          .map(Right(_))
          .recoverWith(handleExceptions("adding a comment to a post"))
    }
  }

  def getAllComments(maybeUserId: Option[Long]): Future[Either[EndpointError, Seq[Comment]]] = {
    postDao
      .findAllPostWithComments(maybeUserId)
      .map(row => row.map(_._2))
      .map(Right(_))
      .recoverWith(handleExceptions("retrieving post and comments"))
  }

}
