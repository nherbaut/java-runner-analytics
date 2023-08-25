GATEWAY_PORT=8099
GATEWAY_URL=http://127.0.0.1:$GATEWAY_PORT
echo "rolling out the gateway"
kubectl rollout status -n openfaas deploy/gateway > /dev/null
echo "proxying the gateway to port $GATEWAY_PORT"
kubectl port-forward -n openfaas svc/gateway $GATEWAY_PORT:8080 & > /dev/null
FWD_PID=$!
echo "waiting for the proxy to be ready"
sleep 3 #waiting for the port forwarding
PASSWORD=$(kubectl get secret -n openfaas basic-auth -o jsonpath="{.data.basic-auth-password}" | base64 --decode; echo)
echo -n $PASSWORD | faas-cli -g $GATEWAY_URL login --username admin --password-stdin > /dev/null
echo "removing the previous function if possible"
faas-cli -g $GATEWAY_URL remove -f java-build-run.yml
cd java-code-runner
echo "building java package"
../run_with_dots mvn clean package > /dev/null
cd ..
echo "copying java runner to dockerfile location"
cp java-code-runner/java-code-runner-cli/target/*-runner.jar java-build-run/runner.jar > /dev/null
echo "bulding the image"
faas-cli build -f java-build-run.yml  > /dev/null
echo "pushing the image to the docker hub"
faas-cli push -f java-build-run.yml > /dev/null
echo "pushing the function to openfaas"
faas-cli -g $GATEWAY_URL deploy -f java-build-run.yml > /dev/null
echo "killing gateway proxy"
kill -9 $FWD_PID
echo "all done"