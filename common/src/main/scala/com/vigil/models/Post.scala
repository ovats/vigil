package com.vigil.models

import java.time.OffsetDateTime

case class Post(id: Option[Long], userId: Long, title: String, createdAt: OffsetDateTime)
