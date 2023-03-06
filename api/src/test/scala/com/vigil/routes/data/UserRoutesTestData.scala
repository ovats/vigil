package com.vigil.routes.data

import com.vigil.models.User
import io.circe._
import io.circe.parser._

object UserRoutesTestData {

  val userId: Long = 123L
  val user: User   = User(Some(userId), "John", "john@mail.com")

  val wrongJsonStr: String =
    """
      | {"namexyz": "some name", "email": "name@domain.com"}
      |""".stripMargin
  val wrongUserRequest: Json = parse(wrongJsonStr).getOrElse(Json.Null)

}
