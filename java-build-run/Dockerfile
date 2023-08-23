FROM ghcr.io/openfaas/classic-watchdog:0.2.1 as watchdog

FROM alpine:3.18

RUN mkdir -p /home/app

COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog

# Add non root user
RUN addgroup -S app && adduser app -S -G app
RUN chown app /home/app

WORKDIR /home/app


RUN apk add openjdk17-jdk curl

RUN curl https://maven.miage.dev/snapshots/fr/pantheonsorbonne/ufr27/miage/java-code-runner/0.0.1-SNAPSHOT/java-code-runner-0.0.1-20230823.100813-3-runner.jar  -o runner.jar

USER app

# Populate example here - i.e. "cat", "sha512sum" or "node index.js"
ENV fprocess="java -jar runner.jar"
# Set to true to see request in function logs
ENV write_debug="true"

EXPOSE 8082

HEALTHCHECK --interval=3s CMD [ -e /tmp/.lock ] || exit 1


CMD ["fwatchdog"]
