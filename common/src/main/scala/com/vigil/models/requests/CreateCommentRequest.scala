package com.vigil.models.requests

final case class CreateCommentRequest(userId: Long, text: String, createdAt: String)
