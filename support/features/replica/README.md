Replica
=======

> A JSON-powered, SQLite-backed data store.


## APIs

### Collection API

Creates a new collection.

Implementation:
 - `INSERT INTO COLLECTIONS VALUES (:name)`
 - `CREATE TABLE DOCUMMENTS_<name> <...>`

```http request
POST collection
{ "name": "foo" }

201 Created
Location: collection/:name
```


Check if collection exists:

```http request
HEAD collection/:name

200 Ok
```

### Document API

Create a new document in a given collection:

```http request
POST collection/:name
{ "blablubb": "123", "foo": "bar", "visible": false }

201 Created
Location: collection/:name/:id

{
  "_id": "<uuid>",
  "_meta": {
    "version": "<uuid>",
    "lastModified": "<iso string>"
  },
  "blablubb": "123",
  "foo": "bar",
  "visible": false
}
```

Implementation:

- `INSERT INTO DOCUMENTS_<name> VALUES(..., <clob>)`

For now, simple binary storage of JSON document.

### Query API

Create index:

```http request
POST collection/:name/query/:byIndex

{ "dataType": "string", "jsonPointer": "/name" }

204 No Content
```

- `ALTER TABLE DOCUMENTS_<name> ADD COLUMN INDEX_byName`
- `SELECT * FROM DOCUMENTS_<name>`: for each: read json, read jsonPointer, update column INDEX_byName


```http request
GET collection/:name/query/:byIndex?{value}

200 Ok

[ { .. }, { .. }]
```

- `SELECT * FROM DOCUMENTS_<name> WHERE INDEX_:byIndex := value`



### Change Stream API

Change Table:

Initial Document:
```json
{ "name": "John Doe" }
```

Final Document:

| ID   | DOCUMENT_ID | DATE       | VERSION_BEFORE | VERSION_AFTER | PATCH                      | PATCH_REVERSE              |
|------|-------------|------------|----------------|---------------|----------------------------|----------------------------|
| 1100 | 123         | 2019-08-01 | 1100           | 2200          | [/* json patch forward */] | [/* json patch reverse */] |
| 7744 | 123         | 2019-08-02 | 2200           | 3300          | [/* json patch forward */] | [/* json patch reverse */] |
| 8866 | 123         | 2019-08-03 | 3300           | 4400          | [/* json patch forward */] | [/* json patch reverse */] |



Get changes to a document:

```http request
GET collection/:name/:id?changes
Last-Modified: 2019-08-02

200 Ok
[
  { "from": "2200", "to": "3300", "patch$": [ ], "document$": {} }
  { "from": "3300", "to": "4400", "patch$": [ ], "document$": {} }
]
```

Subscribe to changes of a document:

```http request
POST changes

{ "collection": "<name">, "documentIds": [ /*..*/, /*..*/ ], "url": "<webhook url>" } 

201 Created
Location: changes/:id
```

Unsubscribe:

```http request
DELETE changes/:id

204 No Content
```


On Changes:

```http request
POST <webhook-url>

{ "documentIds": [ /*..*/, /*..*/ ] }

204 No Content
```

