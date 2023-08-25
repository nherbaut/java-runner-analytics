kubectl rollout status -n openfaas deploy/gateway
kubectl port-forward -n openfaas svc/gateway 8090:8080&
