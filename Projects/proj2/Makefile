# This makefile is defined to give you the following targets:
#
#    default: The default target: Compiles the program in package loa.
#    style: Run our style checker on the project source files.  Requires that
#           the source files compile.
#    check: Compiles the db61b package, if needed, and then performs the
#           tests described in testing/Makefile.
#    clean: Remove regeneratable files (such as .class files) produced by
#           other targets and Emacs backup files.
#
# In other words, type 'make' to compile everything; 'make check' to 
# compile and test everything, and 'make clean' to clean things up.
# 
# You can use this file without understanding most of it, of course, but
# I strongly recommend that you try to figure it out, and where you cannot,
# that you ask questions.  The Lab Reader contains documentation.

# Name of package containing main procedure 
PACKAGE = loa

STYLEPROG = style61b

# Targets that don't correspond to files, but are to be treated as commands.
.PHONY: default check integration unit clean style jar

default:
	"$(MAKE)" -C $(PACKAGE) default

check: integration unit

integration: default
	"$(MAKE)" -C testing check

unit: default
	"$(MAKE)" -C loa unit

style:
	"$(MAKE)" -C $(PACKAGE) STYLEPROG=$(STYLEPROG) style

# 'make clean' will clean up stuff you can reconstruct.
clean:
	$(RM) *~
	"$(MAKE)" -C $(PACKAGE) clean
	"$(MAKE)" -C testing clean


