.DEFAULT_GOAL := all

clean:
	rm -f java-build-run/runner.jar
all: clean
	sh run_all.sh
