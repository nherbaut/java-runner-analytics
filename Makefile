.DEFAULT_GOAL := all

clean:
	rm -f functions/java-build-run/runner.jar
all: clean
	cd scripts && sh run_all.sh 
scrapper: 
	cd scripts && sh run_scrapping.sh
