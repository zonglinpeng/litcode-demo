FROM gradle:7.0.0-jdk11 as backend_builder
WORKDIR /project
COPY . /project/
RUN gradle assemble --no-daemon

FROM gradle:7.0.0-jdk11 as frontend_builder
WORKDIR /project
COPY . /project/
RUN mkdir assets

FROM openjdk:11-jre-slim

ARG APP_VERSION
ENV APP_FILE "litcode-${APP_VERSION}-fat.jar"
ENV APP_HOME /usr/app

EXPOSE 8080

WORKDIR $APP_HOME
COPY --from=backend_builder /project/build/libs/*-fat.jar $APP_HOME/$APP_FILE
COPY --from=frontend_builder /project/assets $APP_HOME/assets
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $APP_FILE"]
