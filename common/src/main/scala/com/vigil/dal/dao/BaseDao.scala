package com.vigil.dal.dao

import com.vigil.dal.tables.{CommentTable, PostTable, UserTable}
import slick.lifted.TableQuery

object BaseDao {
  val userTable    = TableQuery[UserTable]
  val postTable    = TableQuery[PostTable]
  val commentTable = TableQuery[CommentTable]
}
