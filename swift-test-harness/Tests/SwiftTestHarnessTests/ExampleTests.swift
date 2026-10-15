#if canImport(Testing)
import Testing
import Rayon

@Suite("Rayon Swift Export Tests")
struct RayonExportTests {
    @Test("Rayon swift module imported cleanly")
    func testSwiftModuleLoads() throws {
        #expect(Bool(true), "Rayon swift module imported cleanly")
    }
}
#elseif canImport(XCTest)
import XCTest
import Rayon

final class RayonExportTests: XCTestCase {
    func testSwiftModuleLoads() throws {
        XCTAssertTrue(true, "Rayon swift module imported cleanly")
    }
}
#endif
