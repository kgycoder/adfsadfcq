#!/bin/sh
# Gradle start-up script for POSIX
APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`
APP_HOME=`pwd -P`
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

# Detect OS
case `uname` in
  Darwin* ) JAVA_EXE=java ;;
  CYGWIN* ) JAVA_EXE=java ;;
  MINGW* ) JAVA_EXE=java ;;
  * ) JAVA_EXE=java ;;
esac

exec "$JAVA_EXE" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
