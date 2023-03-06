package com.vigil.service

import com.typesafe.scalalogging.LazyLogging
import com.vigil.dal.dao.UserDao
import com.vigil.models.User
import com.vigil.tapir.EndpointError

import scala.concurrent.{ExecutionContext, Future}

class UserService(userDao: UserDao)(implicit ec: ExecutionContext) extends LazyLogging with ServiceCommon {

  def createUser(user: User): Future[Either[EndpointError, User]] = {

    userDao
      .findByEmail(user.email)
      .flatMap {
        case Some(_) =>
          val error: Either[EndpointError, User] = Left(EndpointError("User already exists"))
          Future.successful(error)
        case None =>
          userDao
            .create(user)
            .map(Right(_))
      }
      .recoverWith(handleExceptions("creating an user"))
  }

  def getUser(userId: Long): Future[Either[EndpointError, User]] = {
    userDao
      .findById(userId)
      .flatMap {
        case None =>
          val error: Either[EndpointError, User] = Left(EndpointError("User does not exist"))
          Future.successful(error)
        case Some(user) =>
          Future.successful(Right(user))
      }
      .recoverWith(handleExceptions("retrieving an user"))
  }

  def getAllUsers(): Future[Either[EndpointError, Seq[User]]] = {
    userDao
      .findAll()
      .flatMap(l => Future.successful(Right(l)))
      .recoverWith(handleExceptions("retrieving all users"))
  }

}
