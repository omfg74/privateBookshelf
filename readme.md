# Private Book Shelf

# Simple file storage to keep my e-book in order

## 1. Deploy

### 1.1 To deploy application this way it needs to be Docker installed

#### 1.1.1 building deployment images
then run docker ```docker build -t rooki-builder .``` from deploy/builder directory  
then run docker ```docker build -t rooki-runtime .``` from deploy/runtime directory
 
then create runtime directory with file run.sh 
it must contain text like ```!#/bin/bash \n java -jar -Denv.var=value <other values> book-shelf.jar```

example
```#!bin/bash```

```java -jar  -Dapp.book.file_path=/tmp/books book-shelf.jar```

#### 1.1.2 compiling project
To run builder execute ```docker run -d --rm -v <source_dir>:/source -v <runtime_dir>:/runtime  rooki-builder :latest``` 
example  ``` docker run -d --rm -v $(pwd):/source -v /tmp/runtime:/runtime  rooki-builder ```

#### 1.1.3 running compiled project