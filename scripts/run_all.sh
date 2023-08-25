GATEWAY_PORT=8099
GATEWAY_URL=http://127.0.0.1:$GATEWAY_PORT

echo "rolling out the gateway"
kubectl rollout status -n openfaas deploy/gateway 
echo "proxying the gateway to port $GATEWAY_PORT"
kubectl port-forward -n openfaas svc/gateway $GATEWAY_PORT:8080 & 
FWD_PID=$!
echo "waiting for the proxy to be ready"
sleep 3 #waiting for the port forwarding
PASSWORD=$(kubectl get secret -n openfaas basic-auth -o jsonpath="{.data.basic-auth-password}" | base64 --decode; echo)
echo -n $PASSWORD | faas-cli -g $GATEWAY_URL login --username admin --password-stdin 
echo "removing the previous function if possible"
faas-cli -g $GATEWAY_URL remove -f ../functions/java-build-run.yml
cd ../java-code-runner
echo "building java package"
../scripts/run_with_dots mvn clean package 
cd ..
echo "copying java runner to dockerfile location"
cp java-code-runner/java-code-runner-cli/target/*-runner.jar functions/java-build-run/runner.jar 
echo "bulding the image"
cd ./functions
faas-cli build -f java-build-run.yml  
echo "pushing the image to the docker hub"
faas-cli push -f .java-build-run.yml 
echo "pushing the function to openfaas"
faas-cli -g $GATEWAY_URL deploy -f java-build-run.yml 
echo "killing gateway proxy"
kill -9 $FWD_PID
echo "all done"
