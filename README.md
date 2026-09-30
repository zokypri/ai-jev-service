# ai-jev-service

POC: REST API that sends state and question instructions to Jev (TypeSafe AI, `POST /v1/systemone`) and returns the typed answer.

## Run

```bash
export JEV_API_KEY=<key from the TypeSafe console>
mvn spring-boot:run
```

## Try it

With the application running, open Swagger UI at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).
The OpenAPI specification is available at [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs).
The API has separate endpoints for choice (`/api/v1/choices`), noul (`/api/v1/nouls`), and score (`/api/v1/scores`) questions, plus `/api/v1/evaluations` to submit a Jev-style `questions` map containing any combination of all three types.

```bash
curl -X POST localhost:8080/api/v1/choices -H 'Content-Type: application/json' -d '{
  "state": {
    "customer_tier": "enterprise",
    "ticket": "Help! My payouts have been failing for 3 days."
  },
  "instructions": "Which team should handle this?",
  "criteria": {
    "billing": "Payments, invoicing, refunds",
    "technical": "Bugs, outages, integrations",
    "sales": "Pricing, upgrades, new accounts"
  }
}'
```
