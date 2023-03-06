package com.vigil.tapir

import io.circe.generic.auto._
import sttp.model.StatusCode
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.{oneOfVariantExactMatcher, EndpointOutput}

trait ErrorStatusMapping {

  def internalServerErrorEndpointErrorStatusMapping(
      description: String = "An unexpected error has occurred."
  ): EndpointOutput.OneOfVariant[EndpointError] =
    singleInstanceEndpointErrorStatusMapping(
      statusCode = StatusCode.InternalServerError,
      instance = EndpointError(description = description),
      description = description,
    )

  def badRequestEndpointErrorStatusMapping(
      description: String = "Bad request."
  ): EndpointOutput.OneOfVariant[EndpointError] =
    singleInstanceEndpointErrorStatusMapping(
      statusCode = StatusCode.BadRequest,
      instance = EndpointError(description = description),
      description = description,
    )

  def notFoundEndpointErrorStatusMapping(
      description: String = "User does not exist"
  ): EndpointOutput.OneOfVariant[EndpointError] =
    singleInstanceEndpointErrorStatusMapping(
      statusCode = StatusCode.NotFound,
      instance = EndpointError(description = description),
      description = description,
    )

  def userAlreadyExistsErrorStatusMapping(
      description: String = "User already exists"
  ): EndpointOutput.OneOfVariant[EndpointError] =
    singleInstanceEndpointErrorStatusMapping(
      statusCode = StatusCode.Conflict,
      instance = EndpointError(description = description),
      description = description,
    )

  def userDoesNotExistErrorStatusMapping(
      description: String = "User does not exist"
  ): EndpointOutput.OneOfVariant[EndpointError] =
    singleInstanceEndpointErrorStatusMapping(
      statusCode = StatusCode.NotFound,
      instance = EndpointError(description = description),
      description = description,
    )

  def postDoesNotExistErrorStatusMapping(
      description: String = "Post does not exist"
  ): EndpointOutput.OneOfVariant[EndpointError] =
    singleInstanceEndpointErrorStatusMapping(
      statusCode = StatusCode.NotFound,
      instance = EndpointError(description = description),
      description = description,
    )

  def singleInstanceEndpointErrorStatusMapping(
      statusCode: StatusCode,
      instance: EndpointError,
      description: String,
  ): EndpointOutput.OneOfVariant[EndpointError] = {
    val output = jsonBody[EndpointError].description(description).example(instance)
    oneOfVariantExactMatcher(statusCode, output)(instance)
  }

}
