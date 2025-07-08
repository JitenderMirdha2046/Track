FROM ubuntu:latest
LABEL authors="jitender"

ENTRYPOINT ["top", "-b"]