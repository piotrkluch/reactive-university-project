#Reactive-university-project

### Example of akka usage can be found:

####Java:

```
app/akka/demo/HelloAkkaJava.java
test/akka/demo/HelloAkkaTest.java
```

####Scala:
```
app/akka/demo/HelloAkkaScala.scala
test/akka/demo/HelloAkkaSpec.scala
```


@Piotr Kluch to check if docker deployment is correct please wrap up project into docker container and run (inside docker container):

```
sbt compile
sbt test
```

## Cluster installation

### Using docker

1. Clone the repository with:

```
git clone https://github.com/szymonlyszkowski/reactive-university-project.git
```

2. Enter newly created directory and run the container (build from within the container):

```
cd ./reactive-university-project
docker-compose build
```

3. Run the containers with docker-compose:

```
docker-compose up
```


