package com.vigil.models

import java.time.OffsetDateTime

case class Comment(id: Long, userId: Long, postId: Long, text: String, createdAt: OffsetDateTime)
