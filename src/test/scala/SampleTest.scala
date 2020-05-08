import org.scalatest.{FunSpec, Matchers}

class SampleTest extends FunSpec with Matchers {
  it("sample") {
    val a = 3
    a shouldBe 3
  }
}
