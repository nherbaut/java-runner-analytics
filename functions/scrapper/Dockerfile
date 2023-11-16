FROM ghcr.io/openfaas/classic-watchdog:0.2.1 as watchdog

FROM alpine:3

RUN mkdir -p /home/app

COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog

# Add non root user
RUN addgroup -S app && adduser app -S -G app
RUN chown app /home/app

WORKDIR /home/app

RUN apk add --no-cache chromium chromium-chromedriver gcc musl-dev python3 py3-pip xvfb python3-dev
COPY ./requirements.txt /tmp
RUN pip3 install -r /tmp/requirements.txt
RUN mkdir -p /opt/robots

WORKDIR /opt/robots

COPY robot.py /home/app/robot.py
USER app

# Populate example here - i.e. "cat", "sha512sum" or "node index.js"
ENV fprocess="python3 /home/app/robot.py"
# Set to true to see request in function logs
ENV write_debug="true"

EXPOSE 8082

HEALTHCHECK --interval=3s CMD [ -e /tmp/.lock ] || exit 1


CMD ["fwatchdog"]

