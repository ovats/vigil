package com.vigil.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route.seal
import com.vigil.dal.dao.{CommentDao, PostDao, UserDao}
import com.vigil.models.{Comment, Post}
import com.vigil.routes.data.PostRoutesTestData._
import com.vigil.service.PostService
import com.vigil.tapir.EndpointError
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import org.mockito.MockitoSugar.{mock, when}

import java.time.OffsetDateTime
import scala.concurrent.Future

class PostRoutesSpec extends TestCommon with FailFastCirceSupport {

  private val postDaoMock    = mock[PostDao]
  private val userDaoMock    = mock[UserDao]
  private val commentDaoMock = mock[CommentDao]
  private val postService    = new PostService(postDaoMock, userDaoMock, commentDaoMock)
  private val routes         = new PostRoutes(postService).routes

  "POST /posts" should {

    "return 200 Ok when creating a new post" in {
      when(postDaoMock.create(post)).thenReturn(Future.successful(post))
      when(userDaoMock.findById(post.userId)).thenReturn(Future.successful(Some(user)))

      Post("/posts", post) ~> seal(routes) ~> check {
        status shouldBe StatusCodes.OK
        responseAs[Post] shouldBe post
      }
    }

    "return 400 Bad Request when sending invalid data in the request" in {
      Post("/posts", wrongPostRequest) ~> seal(routes) ~> check {
        status shouldBe StatusCodes.BadRequest
      }
    }

    "return 404 Not Found when sending an id of an user that does not exist" in {
      when(userDaoMock.findById(post.userId)).thenReturn(Future.successful(None))
      Post("/posts", post) ~> seal(routes) ~> check {
        status shouldBe StatusCodes.NotFound
        val response = responseAs[EndpointError]
        response.description shouldBe "User does not exist"
      }
    }

    "return 500 Internal Server Error when post service throws an exception" in {
      when(userDaoMock.findById(post.userId)).thenReturn(Future.successful(Some(user)))
      when(postDaoMock.create(post)).thenReturn(Future.failed(new RuntimeException()))
      Post("/posts", post) ~> seal(routes) ~> check {
        status shouldBe StatusCodes.InternalServerError
        val response = responseAs[EndpointError]
        response.description shouldBe "An unexpected error has occurred."
      }
    }

  }

  "PATCH /posts/postId={postId}" should {

    "return 200 Ok with the list of all posts" in {
      val newPost = post.copy(title = "new title")
      when(postDaoMock.findById(postId)).thenReturn(Future.successful(Some(post)))
      when(postDaoMock.update(postId, newPost)).thenReturn(Future.successful(1))
      Patch(s"/posts/$postId", newPost) ~> seal(routes) ~> check {
        status shouldBe StatusCodes.OK
        responseAs[Post] shouldBe newPost
      }
    }

    "return 404 Not Found if the post does not exist" in {
      when(postDaoMock.findById(postId)).thenReturn(Future.successful(None))
      Patch(s"/posts/$postId", post) ~> seal(routes) ~> check {
        status shouldBe StatusCodes.NotFound
        val response = responseAs[EndpointError]
        response.description shouldBe "Post does not exist"
      }
    }

    "return 500 Internal Server Error when user service throws an exception" in {
      when(postDaoMock.findById(postId)).thenReturn(Future.failed(new RuntimeException))
      Patch(s"/posts/$postId", post) ~> seal(routes) ~> check {
        status shouldBe StatusCodes.InternalServerError
        val response = responseAs[EndpointError]
        response.description shouldBe "An unexpected error has occurred."
      }
    }

  }

  "GET /posts?userId={userId}" should {

    "return 200 Ok with the list of all posts" in {
      val post1 = post.copy(id = Some(1L))
      val post2 = post.copy(id = Some(2L))
      val post3 = post.copy(id = Some(3L))

      when(postDaoMock.findAll(None)).thenReturn(Future.successful(List(post1, post2, post3)))

      Get("/posts") ~> seal(routes) ~> check {
        status shouldBe StatusCodes.OK
        val result = responseAs[List[Post]]
        result should contain allOf (post1, post2, post3)
        result.size shouldBe 3
      }
    }

    "return 500 Internal Server Error when user service throws an exception" in {
      when(postDaoMock.findAll(None)).thenReturn(Future.failed(new RuntimeException))
      Get("/posts") ~> seal(routes) ~> check {
        status shouldBe StatusCodes.InternalServerError
        val response = responseAs[EndpointError]
        response.description shouldBe "An unexpected error has occurred."
      }
    }
  }

  "POST /posts/{postId}/comment" should {

    "return 200 Ok when creating a new comment in a post" in {
      when(userDaoMock.findById(userId)).thenReturn(Future.successful(Some(user)))
      when(postDaoMock.findById(postId)).thenReturn(Future.successful(Some(post)))
      when(commentDaoMock.create(comment.copy(id = 0))).thenReturn(Future.successful(comment))

      Post(s"/posts/$postId/comment", commentRequest) ~> seal(routes) ~> check {
        status shouldBe StatusCodes.OK
        responseAs[Comment] shouldBe comment
      }
    }

    "return 400 Bad Request when sending invalid data in the request" in {
      Post(s"/posts/$postId/comment", wrongCommentRequest) ~> seal(routes) ~> check {
        status shouldBe StatusCodes.BadRequest
      }
    }

    "return 404 Not Found when the post id does not exist" in {
      when(userDaoMock.findById(userId)).thenReturn(Future.successful(Some(user)))
      when(postDaoMock.findById(postId)).thenReturn(Future.successful(None))
      Post(s"/posts/$postId/comment", commentRequest) ~> seal(routes) ~> check {
        status shouldBe StatusCodes.NotFound
        val response = responseAs[EndpointError]
        response.description shouldBe "Post does not exist"
      }
    }

    "return 404 Not Found when the user id does not exist" in {
      when(userDaoMock.findById(userId)).thenReturn(Future.successful(None))

      Post(s"/posts/$postId/comment", commentRequest) ~> seal(routes) ~> check {
        status shouldBe StatusCodes.NotFound
        val response = responseAs[EndpointError]
        response.description shouldBe "User does not exist"
      }
    }

  }

  "GET /comments?userId={userId}" should {

    "return 200 Ok with the list of all comments" in {
      when(postDaoMock.findAllPostWithComments(None)).thenReturn(Future.successful(listPostAndComments))
      Get("/comments") ~> seal(routes) ~> check {
        status shouldBe StatusCodes.OK
        val result = responseAs[List[Comment]]
        result should contain allOf (comm1, comm2, comm3, comm4)
        result.size shouldBe 4
      }
    }

    "return 200 Ok with the list of all posts with comments, created by a given user" in {
      when(postDaoMock.findAllPostWithComments(Some(post1.userId)))
        .thenReturn(Future.successful(listPostAndComments.filter(_._1.userId == post1.userId)))
      Get(s"/comments?userId=${post1.userId}") ~> seal(routes) ~> check {
        status shouldBe StatusCodes.OK
        val result = responseAs[List[Comment]]
        result should contain allOf (comm1, comm2, comm3)
        result.size shouldBe 3
      }
    }

  }
}
