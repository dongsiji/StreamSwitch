# How to build docker image

1. Build flink with maven in the flink folder with `mvn clean package -DskipTests -Dcheckstyle.skip`.
2. Compress the files generated in the `build-target` folder with `tar -czvf flink.tgz <path to folder>`.
3. Move the `flink.tgz` file to the `1.8.1` folder.
6. Build the docker image with `docker build . -t <tag-name>` in the `1.8.1` folder.

