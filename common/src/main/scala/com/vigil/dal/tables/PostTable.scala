package com.vigil.dal.tables

import Common._
import com.vigil.models.Post
import slick.driver.PostgresDriver.api._

import java.time.OffsetDateTime

class PostTable(tag: Tag) extends Table[Post](tag, Some(vigilSchema), postTableName) {
  def id         = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
  def userId     = column[Long]("user_id")
  def title      = column[String]("title")
  def createdAt  = column[OffsetDateTime]("created_at")
  override def * = (id, userId, title, createdAt) <> (Post.tupled, Post.unapply)
}
