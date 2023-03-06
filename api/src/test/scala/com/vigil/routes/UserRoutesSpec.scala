package com.vigil.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route.seal
import com.vigil.dal.dao.UserDao
import com.vigil.models.User
import com.vigil.routes.data.UserRoutesTestData.{user, userId, wrongUserRequest}
import com.vigil.service.UserService
import com.vigil.tapir.EndpointError
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import org.mockito.MockitoSugar.{mock, when}

import scala.concurrent.Future

class UserRoutesSpec extends TestCommon with FailFastCirceSupport {

  private val userDaoMock = mock[UserDao]
  private val userService = new UserService(userDaoMock)
  private val routes      = new UserRoutes(userService).routes

  "POST /users" should {

    "return 200 Ok when adding a new user" in {
      when(userDaoMock.findByEmail(user.email)).thenReturn(Future.successful(None))
      when(userDaoMock.create(user)).thenReturn(Future.successful(user))

      Post("/users", user) ~> seal(routes) ~> check {
        status shouldBe StatusCodes.OK
        responseAs[User] shouldBe user
      }
    }

    "return 400 Bad Request when sending invalid data in the request" in {
      Post("/users", wrongUserRequest) ~> seal(routes) ~> check {
        status shouldBe StatusCodes.BadRequest
      }
    }

    "return 409 Conflict when the user (email) is already registered" in {
      when(userDaoMock.findByEmail(user.email)).thenReturn(Future.successful(Some(user)))

      Post("/users", user) ~> seal(routes) ~> check {
        status shouldBe StatusCodes.Conflict
        val response = responseAs[EndpointError]
        response.description shouldBe "User already exists"
      }
    }

    "return 500 Internal Server Error when user service throws an exception" in {
      when(userDaoMock.findByEmail(user.email)).thenReturn(Future.failed(new RuntimeException))
      Post("/users", user) ~> seal(routes) ~> check {
        status shouldBe StatusCodes.InternalServerError
        val response = responseAs[EndpointError]
        response.description shouldBe "An unexpected error has occurred."
      }
    }

  }

  "GET /users/{userId}" should {

    "return 200 Ok with the user data" in {
      when(userDaoMock.findById(userId)).thenReturn(Future.successful(Some(user)))

      Get(s"/users/$userId") ~> seal(routes) ~> check {
        status shouldBe StatusCodes.OK
        responseAs[User] shouldBe user
      }
    }

    "return 404 Not Found when the user does not exist" in {
      when(userDaoMock.findById(userId)).thenReturn(Future.successful(None))

      Get(s"/users/$userId") ~> seal(routes) ~> check {
        status shouldBe StatusCodes.NotFound
        val response = responseAs[EndpointError]
        response.description shouldBe "User does not exist"
      }
    }

    "return 500 Internal Server Error when user service throws an exception" in {
      when(userDaoMock.findById(userId)).thenReturn(Future.failed(new RuntimeException))
      Get(s"/users/$userId") ~> seal(routes) ~> check {
        status shouldBe StatusCodes.InternalServerError
        val response = responseAs[EndpointError]
        response.description shouldBe "An unexpected error has occurred."
      }
    }

  }

  "GET /users" should {

    "return 200 Ok with the list of users" in {
      val user1 = User(Some(1L), "Carl", "sagan@mail.com")
      val user2 = User(Some(2L), "Melanie", "melanie@mail.com")
      val user3 = User(Some(3L), "Albert", "einstein@mail.com")

      when(userDaoMock.findAll()).thenReturn(Future.successful(List(user1, user2, user3)))

      Get("/users") ~> seal(routes) ~> check {
        status shouldBe StatusCodes.OK
        responseAs[List[User]] should contain allOf (user1, user2, user3)
      }
    }

    "return 500 Internal Server Error when user service throws an exception" in {
      when(userDaoMock.findAll()).thenReturn(Future.failed(new RuntimeException))
      Get("/users") ~> seal(routes) ~> check {
        status shouldBe StatusCodes.InternalServerError
        val response = responseAs[EndpointError]
        response.description shouldBe "An unexpected error has occurred."
      }
    }

  }
}
