package com.vigil.dal.dao

import com.vigil.dal.Connection
import com.vigil.models.Comment
import slick.jdbc.PostgresProfile.profile.api._

import scala.concurrent.Future

class CommentDao(cn: Connection) {

  def create(comment: Comment): Future[Comment] = {
    val insertQuery =
      BaseDao.commentTable returning BaseDao.commentTable.map(_.id) into ((item, id) => item.copy(id = id))
    val action = insertQuery += Comment(
            0,
            userId = comment.userId,
            postId = comment.postId,
            text = comment.text,
            createdAt = comment.createdAt,
          )
    cn.db.run(action)
  }

  def findAll(): Future[Seq[Comment]] = cn.db.run(BaseDao.commentTable.result)

}
