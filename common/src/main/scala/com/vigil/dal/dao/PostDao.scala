package com.vigil.dal.dao

import com.vigil.dal.Connection
import com.vigil.models.{Comment, Post}
import slick.jdbc.PostgresProfile.profile.api._

import scala.concurrent.Future

class PostDao(cn: Connection) {

  def create(post: Post): Future[Post] = {
    val insertQuery =
      BaseDao.postTable returning BaseDao.postTable.map(_.id) into ((item, id) => item.copy(id = id))
    val action = insertQuery += Post(post.id, userId = post.userId, title = post.title, createdAt = post.createdAt)
    cn.db.run(action)
  }

  def findById(postId: Long): Future[Option[Post]] = {
    cn.db.run(BaseDao.postTable.filter(_.id === postId).result.headOption)
  }

  def update(postId: Long, post: Post): Future[Int] = {
    val query = BaseDao.postTable
      .filter(_.id === postId)
      .map(post => (post.userId, post.title, post.createdAt))
      .update((post.userId, post.title, post.createdAt))
    cn.db.run(query)
  }

  def findAll(maybeUserId: Option[Long]): Future[Seq[Post]] =
    cn.db.run(
      BaseDao.postTable
        .filterOpt(maybeUserId)((row, value) => row.userId === value)
        .sortBy(_.createdAt.desc)
        .result
    )

  def findAllPostWithComments(maybeUserId: Option[Long]): Future[Seq[(Post, Comment)]] = {
    val query = BaseDao.postTable join BaseDao.commentTable on (_.id === _.postId)
    cn.db.run(
      query
        .filterOpt(maybeUserId)((row, value) => row._1.userId === value)
        .sortBy(m => (m._1.createdAt.desc, m._2.createdAt.desc))
        .result
    )
  }

}
