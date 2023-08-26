#!/bin/bash

GATEWAY_PORT=8099

OPENFAAS_URL="http://127.0.0.1:$GATEWAY_PORT"
echo "rolling out the gateway"
kubectl rollout status -n openfaas deploy/gateway 
echo "proxying the gateway to port $GATEWAY_PORT"
kubectl port-forward -n openfaas svc/gateway $GATEWAY_PORT:8080 & 
FWD_PID=$!
echo "waiting for the proxy to be ready"
sleep 3 #waiting for the port forwarding
PASSWORD=$(kubectl get secret -n openfaas basic-auth -o jsonpath="{.data.basic-auth-password}" | base64 --decode; echo)
echo -n $PASSWORD | faas-cli  -g $OPENFAAS_URL login --username admin --password-stdin 

echo "removing the previous function if possible"
faas-cli -g $OPENFAAS_URL remove -f ../functions/java-build-run.yml
cd ../java-code-runner
echo "building java package"
mvn clean package 
cd ..
echo "copying java runner to dockerfile location"
cp java-code-runner/java-code-runner-cli/target/*-runner.jar functions/java-build-run/runner.jar 
echo "bulding the image"
cd ./functions
while [ "No such function: java-build-run" != "$(faas-cli -g $OPENFAAS_URL describe java-build-run)" ]; do echo "waiting for function to be removed" && sleep 2; done
faas-cli -g $OPENFAAS_URL up -f java-build-run.yml  
kill -9 $FWD_PID
echo "all done"
