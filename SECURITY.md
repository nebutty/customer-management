
# Security Documentation

## Authentication

The application uses Keycloak for user authentication
and OAuth2 access tokens.

Spring Security validates JWT access tokens before
allowing access to protected endpoints.

## Authorization Rules

| Endpoint | Access Rule |
|---|---|
| GET /api/hello | Public |
| GET /api/greeting | Public |
| GET /api/demo-error | Public |
| GET /actuator/health | Public |
| GET /api/customers | CUSTOMER_USER or CUSTOMER_ADMIN |
| GET /api/customers/{id} | CUSTOMER_USER or CUSTOMER_ADMIN |
| POST /api/customers | CUSTOMER_USER or CUSTOMER_ADMIN |
| DELETE /api/customers/{id} | CUSTOMER_ADMIN |
| Other endpoints | Authentication required |

## Unauthorized and Forbidden Requests

- HTTP 401: Missing, invalid, or expired access token.
- HTTP 403: Valid authentication but insufficient permissions.

## Password and Secret Handling

Keycloak manages user credentials.
The application validates access tokens rather than
handling users' login passwords directly.

Production credentials and client secrets must not
be committed to source control or written to logs.

## CSRF and CORS

CSRF protection is disabled for the current stateless
bearer-token API configuration.

CORS must be configured if a browser-based client
accesses the API from a different origin.

## Testing

Security tests should verify public access,
unauthenticated access, role restrictions, and
successful requests from authorized users.
