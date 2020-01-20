## Building docker image

```shell script
docker build --tag exercism-kotlin-test-runner .
```

## Using docker image

```shell script
docker run -v (pwd)/examples:/examples exercism-kotlin-test-runner slug /examples/full /examples/out
```

## Running docker image interactively

```shell script
docker run -v (pwd)/examples:/examples --entrypoint "/bin/bash" -it exercism-kotlin-test-runner

/opt/test-runner/bin/run.sh slug /examples/full /examples/out
```
