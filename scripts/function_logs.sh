kubectl port-forward -n openfaas svc/gateway 8070:8080&
PID=$!
sleep 5
PASSWORD=$(kubectl get secret -n openfaas basic-auth -o jsonpath="{.data.basic-auth-password}" | base64 --decode; echo)
echo $PASSWORD
echo -n $PASSWORD | faas-cli login --username admin --password-stdin
faas-cli -g http://127.0.0.1:8070 logs java-build-run
kill -9 $PID

