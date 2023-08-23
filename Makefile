all:
	faas-cli build -f java-build-run.yml
	docker push nherbaut/java-build-run:latest
	faas-cli push -f java-build-run.yml
	faas-cli deploy -f java-build-run.yml 