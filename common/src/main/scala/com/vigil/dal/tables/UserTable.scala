package com.vigil.dal.tables

import Common._
import com.vigil.models.User
import slick.driver.PostgresDriver.api._

class UserTable(tag: Tag) extends Table[User](tag, Some(vigilSchema), userTableName) {

  def id         = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
  def name       = column[String]("name")
  def email      = column[String]("email")
  override def * = (id, name, email) <> (User.tupled, User.unapply)
}
