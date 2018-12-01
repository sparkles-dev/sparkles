Pub-Sub w/ Topics
=================

Downstream subscribes at upstream:

```http
POST /replication/subscription
Content-Type: application/json

{ "topic": "foobar", "notifyUrl": "http://localhost:7001/replication/notification" }

HTTP/1.1 201 Created
Content-Type: application/json

{ "id": "123", "topic": "foobar", "subscriptionUrl": "http://localhost:7002/replication/subscription/123" }
```

Upstream notifies downstream about changes:

```http
POST /replication/notification
Content-Type: application/json

{ "id": "123", "topic": "foobar", "subscriptionUrl": "http://localhost:7002/replication/subscription/123" }

HTTP/1.1 204 No Content
```

Downstream fetches data from upstream:

```http
GET /replication/subscription/123

HTTP/1.1 200 Ok
Content-Type: application/json

{ "payload": { "best": "data in town" } }
```
