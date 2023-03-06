package com.vigil.dal.tables

import Common._
import com.vigil.models.Comment
import slick.driver.PostgresDriver.api._

import java.time.OffsetDateTime

class CommentTable(tag: Tag) extends Table[Comment](tag, Some(vigilSchema), commentTableName) {
  def id         = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def userId     = column[Long]("user_id")
  def postId     = column[Long]("post_id")
  def text       = column[String]("text")
  def createdAt  = column[OffsetDateTime]("created_at")
  override def * = (id, userId, postId, text, createdAt) <> (Comment.tupled, Comment.unapply)
}
