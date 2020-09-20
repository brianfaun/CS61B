
STYLEPROG = style61b

# All Java sources.
SRCS = $(wildcard *.java)

CLASSES = $(SRCS:.java=.class)

JFLAGS = -g

.PHONY: default style clean

default: $(CLASSES)

style:
	$(STYLEPROG) $(SRCS)

clean:
	$(RM) *.class *~ sentinel

check: default
	@echo Warning: ONLY sample tests defined.
	java WeirdListTest
	java WeirdListClientTest

$(CLASSES): sentinel

sentinel: $(SRCS)
	javac $(JFLAGS) $(SRCS)
	touch $@
