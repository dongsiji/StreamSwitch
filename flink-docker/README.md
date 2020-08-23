# How to build docker image

1. Build flink with maven in the flink folder with `mvn clean package -DskipTests -Dcheckstyle.skip`.
2. Compress the files generated in the `build-target` folder with `tar -czvf flink.tgz <path to folder>`.
3. Move the `flink.tgz` file to this folder.

Now from here there are two ways of generating the docker image.

First way, through github.

4. git add and push the files to github.
5. Change the Dockerfile to use the correct branch so that it fetches the updated `flink.tgz`.
6. Build the docker image with `docker build . -t <tag-name>` in the `1.8.1` folder.

Second way, through locally copying. It will increase the image size, so it is not really recommended but it saves effort through hosting the compressed build file.

4. Change the Dockerfile to add `COPY ../flink.tgz /` to the line right after setting the work directory of flink.
5. Remove the part of the Dockerfile which is pulling the flink.tgz from github.
6. Build the docker image with `docker build . -t <tag-name>` in the `1.8.1` folder.

