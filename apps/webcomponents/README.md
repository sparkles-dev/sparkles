WebComponents Demo
==================

```bash
$ yarn add @webcomponents/custom-elements
$ yarn add --dev ngx-build-plus@7
$ ng add @angular/elements 
```

```bash
$ ng build webcomponents-demo-element --prod
$ ng serve webcomponents-ng-app
```


#### Hello World w/o Externals 

```bash
$ ng build webcomponents-demo-element --prod
                                                                                 
Date: 2019-05-19T20:12:01.782Z
Hash: 842d9ca2034e081373a1
Time: 17267ms
chunk {0} es2015-polyfills.js (es2015-polyfills) 57.2 kB [entry] [rendered]
chunk {1} main.js (main) 6.07 kB [entry] [rendered]
```

```bash
$ ng serve webcomponents-ng-app --prod

Date: 2019-05-19T20:14:01.909Z
Hash: 8eaa9ab93bf9fb22da4e
Time: 98642ms
chunk {0} runtime.26209474bfa8dc87a77c.js, runtime.26209474bfa8dc87a77c.js.map (runtime) 1.46 kB [entry] [rendered]
chunk {1} es2015-polyfills.b52d94361daeae65b82a.js, es2015-polyfills.b52d94361daeae65b82a.js.map (es2015-polyfills) 56.5 kB [initial] [rendered]
chunk {2} main.4d3d5c1e1e8530605aab.js, main.4d3d5c1e1e8530605aab.js.map (main) 291 kB [initial] [rendered]
chunk {3} polyfills.2afdafbdf66f379aba7d.js, polyfills.2afdafbdf66f379aba7d.js.map (polyfills) 42.6 kB [initial] [rendered]
chunk {4} styles.8c83ae69e704b183f801.css, styles.8c83ae69e704b183f801.css.map (styles) 59 bytes [initial] [rendered]
chunk {scripts} scripts.659fb640ce9301474612.js, scripts.659fb640ce9301474612.js.map (scripts) 610 kB [entry] [rendered]
ℹ ｢wdm｣: Compiled successfully.
```


#### Hello World w/ Embeddeds

```bash
$ ng build webcomponents-demo-element --prod
                                                                                 
Date: 2019-05-19T20:04:45.758Z
Hash: 2093c7720f0bea16cd1a
Time: 32537ms
chunk {0} es2015-polyfills.js (es2015-polyfills) 57.2 kB [entry] [rendered]
chunk {1} main.js (main) 157 kB [entry] [rendered]
```

```bash
$ ng serve webcomponents-ng-app --prod
                                                                                                                      
Date: 2019-05-19T20:10:03.650Z
Hash: 8eaa9ab93bf9fb22da4e
Time: 104067ms
chunk {0} runtime.26209474bfa8dc87a77c.js, runtime.26209474bfa8dc87a77c.js.map (runtime) 1.46 kB [entry] [rendered]
chunk {1} es2015-polyfills.b52d94361daeae65b82a.js, es2015-polyfills.b52d94361daeae65b82a.js.map (es2015-polyfills) 56.5 kB [initial] [rendered]
chunk {2} main.4d3d5c1e1e8530605aab.js, main.4d3d5c1e1e8530605aab.js.map (main) 291 kB [initial] [rendered]
chunk {3} polyfills.2afdafbdf66f379aba7d.js, polyfills.2afdafbdf66f379aba7d.js.map (polyfills) 42.6 kB [initial] [rendered]
chunk {4} styles.8c83ae69e704b183f801.css, styles.8c83ae69e704b183f801.css.map (styles) 59 bytes [initial] [rendered]
chunk {scripts} scripts.ce52370d66b7fb9f19cd.js, scripts.ce52370d66b7fb9f19cd.js.map (scripts) 157 kB [entry] [rendered]
ℹ ｢wdm｣: Compiled successfully.
```

```json
  "scripts": [
    "node_modules/@webcomponents/custom-elements/src/native-shim.js",
    "node_modules/@webcomponents/custom-elements/externs/custom-elements.js",
    "dist/apps/webcomponents/demo-element/main.js"
  ]
```
