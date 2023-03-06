package com.vigil.routes

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

trait TestCommon extends AnyWordSpec with Matchers with ScalatestRouteTest
