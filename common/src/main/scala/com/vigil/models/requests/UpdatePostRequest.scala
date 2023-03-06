package com.vigil.models.requests

import java.time.OffsetDateTime

final case class UpdatePostRequest(title: String, createdAt: OffsetDateTime)
