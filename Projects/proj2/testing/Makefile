# This makefile is defined to give you the following targets:
#
#    default: Same as check
#    check: For each file, F.in, use F.in as input to "java $(MAIN_CLASS)" and
#          compare the output to the contents of the file names F.out.
#          Report discrepencies.
#    clean: Remove all the .class files produced by java compilation, 
#          all Emacs backup files, and testing output files.
#
# You can use this file without understanding most of it, of course, but
# I strongly recommend that you try to figure it out, and where you cannot,
# that you ask questions.  The Lab Reader contains documentation.

SHELL = /bin/bash

# If your python3 implementation has a different name (like just 'python'),
# simply run
#      make PYTHON=python check
# No need to change this file.
PYTHON = python3

# Flags to Java interpreter: check assertions
JFLAGS = -ea

# A non-standard classpath that works Linux, Mac, and Windows.
# We include everything in CLASSPATH, plus the proj2 directory itself
# (from which one should be able to find the amazons classes).
CPATH = "..:$(CLASSPATH):;..;$(CLASSPATH)"

TESTER_FLAGS =

TESTS := *-1.in

.PHONY: default check clean std

# First, and therefore default, target.
default:
	"$(MAKE)" -C .. 
	"$(MAKE)" check

# The "CLASSPATH=..." clause in front of the command below temporarily 
# changes the location in which Java searches for classes to be the directory
# that contains your project.
check:
	@echo "Testing application loa.Main..."
	@CLASSPATH=$(CPATH) "$(PYTHON)" tester.py $(TESTER_FLAGS) $(TESTS)

# 'make clean' will clean up stuff you can reconstruct.
clean:
	$(RM) -r *~ OUTPUT* *.log *.out __pycache__
