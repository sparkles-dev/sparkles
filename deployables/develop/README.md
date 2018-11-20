Web Server and Load Balancer
============================

A local development server running on a `*.sparkles.docker` domain.

## Run on a Docker Machine

A docker machine named `sparkles` must be created.
Use the [`scripts/docker/machine-create.sh`](../../scripts/docker/machine-create.sh) script.

```bash
$ docker-machine start sparkles
$ docker-machine env sparkles
```

When using [dockness](https://github.com/bamarni/dockness),
the machine will be assigned the DNS hostname `sparkles.docker`.
At this point, you can `ping sparkles.docker`.

If working w/o dockness, you set up an `/etc/host` entry manually.


## Run on localhost

Set an `/etc/host` entry forwarding hostnames of the `*.sparkles.docker` domain to the local machine.


## Start the Web Server

Build the `sparkles/proxy-gen` image first.
Then, deploy and start the web server by deploying the compose file:

```bash
$ ./tools/docker/proxy-gen/build.sh
$ ./startup.sh
```

The web server is based on [jwilder/nginx-proxy](https://github.com/jwilder/nginx-proxy).
It will listen for other services to start and host them under a sub-domain of `*.sparkles.docker`.


## Adding services

Now that the web server is running, you are able to deploy other services to the environment.
Go create and implement your service, then add a compose file for your service(s).

You need to adhere to two conventions:
 - declare the default network as external and use the `sparkles` network,
 - use environment variables `VIRTUAL_HOST` and `VIRTUAL_PORT` to make your service public accessible, and
 - expose a port from the container.

Here is an example with a `docker run` command:

```bash
docker run \
  -e VIRTUAL_HOST=stuff.sparkles.docker \
  -e VIRTUAL_PORT=7000 \
  --network sparkles \
  --expose 7000 \
  -d sparkles/stuff
```

Here is another example for the Ghost CMS with a `docker-compose.yml` file:

```yml
version: '2'
services:
  cms:
    image: ghost:alpine
    environment:
      - VIRTUAL_HOST=cms.sparkles.docker
      - VIRTUAL_PORT=2368
networks:
  default:
    external:
      name: sparkles
```

For more complex configuration examples please refert to [Jason Wilder's blog post on the nginx reverse proxy](http://jasonwilder.com/blog/2014/03/25/automated-nginx-reverse-proxy-for-docker/).


## Creating SSL Certificates

How to re-create the self-signed wildcard certificates:

```bash
## Creates a private key
$ sudo openssl genrsa -out $DOMAIN.key 2048
## Creates a signing request
$ sudo openssl req -new \
  -key $DOMAIN.key \
  -out $DOMAIN.csr
## Signs the request with they key, issues a certificate
$ sudo openssl x509 -req -days 365 \
  -in $DOMAIN.csr \
  -signkey $DOMAIN.key  \
  -out $DOMAIN.crt
```

Read also https://serversforhackers.com/c/self-signed-ssl-certificates
