#	make            # Defaults to the first target: default
#	make default    # Same as make compile.
#	make compile    # Compiles Java code in this directory.
#	make style      # Runs style checks (only on instructional machines)
#	make check      # Compile if needed and test using NFATests.
#	make clean      # Remove extraneous or rebuildable files

# Define $(SRCS) to be a list of Java files.
SRCS = NFA.java NFATests.java

# The targets following .PHONY aren't actually files; they serve as commands.
.PHONY: compile default check clean style

# By default, compile all sources
default: compile

# To compile all source files, just bring the file NFATests.class up to date.
compile: NFATests.class

# Run our style checks.
style:
	style61b $(SRCS)

# Test the compiled program, after first making sure it is up-to-date.
check: NFATests.class
	java -ea NFATests

# Remove extraneous or reconstructable files.
clean:
	rm -f *.class *~

NFATests.class: $(SRCS) 
	javac -g $(SRCS)
