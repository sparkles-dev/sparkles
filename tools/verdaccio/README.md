Verdaccio
=========

> Local npm registry for temporary builds and more complex workflows.

- Username: `sparkles`
- Password: `lights`

Startup:

```bash
$ node_modules/.bin/verdaccio --config tools/verdaccio/config.yml 
```

Login:

```bash
$ npm login --registry http://localhost:4873
```

Generated token in `~/.npmrc`:

```
//localhost:4873/:_authToken="6rL2ZE1godDdP2vspyvsEw=="
```

Publish package to registry:

```bash
$ cd <path-to-project>/dist
$ npm publish --registry http://localhost:4873
```

When it fails, `publishConfig` property in package.json must be changed.


### Further Reading

- https://github.com/verdaccio/verdaccio-memory#requirements
