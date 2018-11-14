# testcontainer

A docker container to run tests on Circle CI.

## For Developers

Install Docker on your machine in order to build/pull/push this image.

```bash
$ docker login
```

Publish a new version:

```bash
$ tools/docker/testcontainer/publish.sh [tag eg. 0.2.3]
```   
