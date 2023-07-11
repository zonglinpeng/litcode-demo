#!/usr/bin/env bash

set -o pipefail
set -o errexit
set -o xtrace

# USAGE: APP_VERSION=<> script.sh
# ENABLE_PUSH=y to enable push after build

# Check if docker has been downloaded
if ! [ -x "$(command -v docker)" ]; then
    printf "%s\n" 'Error: docker is not installed.' >&2
    exit 1
fi

# Check if git has been downloaded
if ! [ -x "$(command -v git)" ]; then
    printf "%s\n" 'Error: git is not installed.' >&2
    exit 1
fi

COMMIT_SHA="$(git rev-parse --short=8 HEAD)"
APP_VERSION="${APP_VERSION:-$COMMIT_SHA}"


PROJECT_ROOT=$(git rev-parse --show-toplevel)
APP_DOCKER_PATH="bamboovir/litcode:$APP_VERSION"
APP_SNAPSHOT_DOCKER_PATH="bamboovir/litcode:$COMMIT_SHA"

DOCKER_FILE_PATH="${PROJECT_ROOT}/docker/litcode/Dockerfile"

# DockerCmd Cmd Array Sequence constructor
DockerCmd=("buildx")
DockerCmd=("build")
DockerCmd+=("-t")
DockerCmd+=("$APP_DOCKER_PATH")
DockerCmd+=("--build-arg")
DockerCmd+=("APP_VERSION=$APP_VERSION")
DockerCmd+=("-f")
DockerCmd+=("$DOCKER_FILE_PATH")
#DockerCmd+=("--no-cache")
DockerCmd+=("$PROJECT_ROOT")

docker "${DockerCmd[@]}"


ENABLE_PUSH="${ENABLE_PUSH:-n}"

if [[ "$ENABLE_PUSH" == "y" ]]; then
    docker tag "$APP_DOCKER_PATH" "$APP_SNAPSHOT_DOCKER_PATH"
    docker push "$APP_DOCKER_PATH"
    docker push "$APP_SNAPSHOT_DOCKER_PATH"
fi
