package com.vigil.dal.dao

import com.vigil.dal.Connection
import com.vigil.models.User
import slick.jdbc.PostgresProfile.profile.api._

import scala.concurrent.Future

class UserDao(cn: Connection) {

  def create(user: User): Future[User] = {
    val insertQuery =
      BaseDao.userTable returning BaseDao.userTable.map(_.id) into ((item, id) => item.copy(id = id))
    val action = insertQuery += User(user.id, user.name, user.email)
    cn.db.run(action)
  }

  def findByEmail(email: String): Future[Option[User]] = {
    val query = BaseDao.userTable.filter(_.email === email).result.headOption
    cn.db.run(query)
  }

  def findById(userId: Long): Future[Option[User]] = {
    cn.db.run(BaseDao.userTable.filter(_.id === userId).result.headOption)
  }

  def findAll(): Future[Seq[User]] = cn.db.run(BaseDao.userTable.result)
}
