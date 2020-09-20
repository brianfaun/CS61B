# Targets that don't correspond to files, but are to be treated as commands.
.PHONY: default check clean output

CPATH = "..:$(CLASSPATH):;..;$(CLASSPATH)"

default: check

check: 
	@echo "Testing correct inputs..."
	@CLASSPATH=$(CPATH) bash test-correct correct/*.in
	@echo
	@echo "Testing erroneous inputs..."
	@CLASSPATH=$(CPATH) bash test-error error/*.in

# 'make clean' will clean up stuff you can reconstruct.
clean:
	$(RM) *~ OUT* ERR*
