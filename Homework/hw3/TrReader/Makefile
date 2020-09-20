
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
	java TrReaderTest

$(CLASSES): sentinel

sentinel: $(SRCS)
	@echo javac $(JFLAGS) $(SRCS); \
	 javac $(JFLAGS) $(SRCS) || { \
	    echo "*** NOTE: If you received an error that 'TrReader is"; \
	    echo "*** not abstract and ...', DON'T make TrReader abstract."; \
	    echo "*** Look at the ... part instead."; \
	    false; }
	touch $@

