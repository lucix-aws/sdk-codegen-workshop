#!/bin/zsh -e

# 1. build our code generator
cd codegen
./gradlew publishToMavenLocal

# 2. run our smithy build
cd ..
cd sdk-build-project
smithy build

# 3. copy our generated client source out into a directory where it can access
# the runtime
cd ..
cp sdk-build-project/build/smithy/source/ts-client-codegen/client.ts client.ts
