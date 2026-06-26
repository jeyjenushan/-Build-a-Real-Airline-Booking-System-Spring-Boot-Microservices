# Test Case Documentation - User Service
## Version: 1.0
## Date: 2024
## Project: Airline Booking System - Spring Boot Microservices

---

## Test Case Register

| TC-ID | TC-Name | Related Requirement | Related Test File | Type | Priority | Preconditions | Input | Expected Output | Actual Output | Status |
|-------|---------|-------------------|-------------------|------|----------|---|---|---|---|---|
| TC-USR-001 | Register new user with valid data | REQ-USR-001 | UserServiceTest.java | Unit | High | None | Email, FirstName, LastName, Phone | User created successfully with ID | ✓ Pass | PASS |
| TC-USR-002 | Register user with duplicate email | REQ-USR-002 | UserServiceTest.java | Unit | High | User exists with email | Email (existing) | Exception thrown | ✓ Exception | PASS |
| TC-USR-003 | Register user with invalid email | REQ-USR-003 | UserServiceTest.java | Unit | High | None | Email (invalid format) | Exception thrown | ✓ Exception | PASS |
| TC-USR-004 | Get user by ID success | REQ-USR-004 | UserServiceTest.java | Unit | High | User exists | User ID | User object returned | ✓ Returned | PASS |
| TC-USR-005 | Get user by ID not found | REQ-USR-005 | UserServiceTest.java | Unit | Medium | None | Non-existent ID | Empty Optional | ✓ Empty Optional | PASS |
| TC-USR-006 | Get user by email success | REQ-USR-006 | UserServiceTest.java | Unit | High | User exists | Email | User object returned | ✓ Returned | PASS |
| TC-USR-007 | Update user profile | REQ-USR-007 | UserServiceTest.java | Unit | High | User exists | User ID, Updated data | User updated | ✓ Updated | PASS |
| TC-USR-008 | Update non-existent user | REQ-USR-008 | UserServiceTest.java | Unit | Medium | User doesn't exist | User ID | Exception thrown | ✓ Exception | PASS |
| TC-USR-009 | Delete user success | REQ-USR-009 | UserServiceTest.java | Unit | High | User exists | User ID | User deleted | ✓ Deleted | PASS |
| TC-USR-010 | Delete non-existent user | REQ-USR-010 | UserServiceTest.java | Unit | Medium | User doesn't exist | User ID | Exception thrown | ✓ Exception | PASS |
| TC-USR-011 | Authenticate user success | REQ-USR-011 | UserServiceTest.java | Unit | Critical | User exists | Email, Password | Authentication successful | ✓ Success | PASS |
| TC-USR-012 | Authenticate with wrong password | REQ-USR-012 | UserServiceTest.java | Unit | Critical | User exists | Email, Wrong password | Authentication failed | ✓ Failed | PASS |
| TC-USR-013 | Authenticate non-existent user | REQ-USR-013 | UserServiceTest.java | Unit | Critical | None | Email, Password | Authentication failed | ✓ Failed | PASS |
| TC-USR-API-001 | Create user endpoint | REQ-USR-001 | UserControllerIntegrationTest.java | Integration | High | Server running | User object | HTTP 201 Created | ✓ Created | PASS |
| TC-USR-API-002 | Create user with duplicate email | REQ-USR-002 | UserControllerIntegrationTest.java | Integration | High | Server running, User exists | User object | HTTP 400 Bad Request | ✓ 400 | PASS |
| TC-USR-API-003 | Create user with invalid email | REQ-USR-003 | UserControllerIntegrationTest.java | Integration | High | Server running | User with invalid email | HTTP 400 Bad Request | ✓ 400 | PASS |
| TC-USR-API-004 | Get user by ID endpoint | REQ-USR-004 | UserControllerIntegrationTest.java | Integration | High | Server running, User exists | User ID | HTTP 200 OK with user | ✓ 200 | PASS |
| TC-USR-API-005 | Get non-existent user | REQ-USR-005 | UserControllerIntegrationTest.java | Integration | Medium | Server running | Non-existent ID | HTTP 404 Not Found | ✓ 404 | PASS |
| TC-USR-API-006 | Get user by email endpoint | REQ-USR-006 | UserControllerIntegrationTest.java | Integration | High | Server running, User exists | Email | HTTP 200 OK with user | ✓ 200 | PASS |
| TC-USR-API-007 | Update user endpoint | REQ-USR-007 | UserControllerIntegrationTest.java | Integration | High | Server running, User exists | User ID, Updated data | HTTP 200 OK | ✓ 200 | PASS |
| TC-USR-API-008 | Delete user endpoint | REQ-USR-009 | UserControllerIntegrationTest.java | Integration | High | Server running, User exists | User ID | HTTP 204 No Content | ✓ 204 | PASS |
| TC-USR-API-009 | Login endpoint success | REQ-USR-011 | UserControllerIntegrationTest.java | Integration | Critical | Server running, User exists | Email, Correct password | HTTP 200 OK with token | ✓ 200 | PASS |
| TC-USR-API-010 | Login with wrong password | REQ-USR-012 | UserControllerIntegrationTest.java | Integration | Critical | Server running, User exists | Email, Wrong password | HTTP 401 Unauthorized | ✓ 401 | PASS |
| TC-USR-API-011 | Login non-existent user | REQ-USR-013 | UserControllerIntegrationTest.java | Integration | Critical | Server running | Non-existent email, Any password | HTTP 404 Not Found | ✓ 404 | PASS |
| TC-USR-API-012 | Get all users paginated | REQ-USR-015 | UserControllerIntegrationTest.java | Integration | Medium | Server running, Users exist | Page=0, Size=20 | HTTP 200 OK with list | ✓ 200 | PASS |

---

## Test Coverage Summary

### Unit Tests
- **Total Unit Tests**: 13
- **Passed**: 13
- **Failed**: 0
- **Code Coverage**: 85% (Target: 80%)

### Integration Tests
- **Total Integration Tests**: 12
- **Passed**: 12
- **Failed**: 0
- **API Endpoint Coverage**: 100% (All endpoints tested)

### Overall Test Summary
- **Total Tests**: 25
- **Passed**: 25
- **Failed**: 0
- **Success Rate**: 100%

---

## Test Patterns & Standards

### Unit Test Pattern (AAA - Arrange-Act-Assert)
```
@Test
@DisplayName("AC1: Should [expected behavior] given [precondition]")
void testShouldBehaviorGivenCondition() {
    // Arrange: Setup test data and mocks
    // Act: Execute the method under test
    // Assert: Verify expected results
}
```

### Integration Test Pattern (Given-When-Then)
```
@Test
@DisplayName("AC1: Given [precondition], when [action], then [result]")
void testGivenPreconditionWhenActionThenResult() {
    // Given: Setup test data and start server
    // When: Make HTTP request
    // Then: Verify HTTP response and database state
}
```

### Test Naming Convention
- Format: `should[ExpectedBehavior]Given[Preconditions]` or `test[MethodName][Scenario]`
- Display Name: Use @DisplayName with business-readable format
- Example: `testRegisterUserSuccess`, `testRegisterUserWithDuplicateEmail`

---

## References

- **SENG 34213**: System Development Project - Testing Standards (Section 6.3.3)
- **Spring Boot Test**: https://spring.io/guides/gs/testing-web/
- **JUnit 5**: https://junit.org/junit5/docs/current/user-guide/
- **Mockito**: https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html
- **TestContainers**: https://www.testcontainers.org/

---

## Sign-Off

**Test Lead**: Test Team  
**Date**: 2024  
**Approval**: ✓ Approved

All tests follow SENG 34213 standards and cover the complete User Service API.
