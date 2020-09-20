Version 1.0 6/30/2019.

This directory contains a skeleton for CS61B Project 0.

Although some of what's in here might seem mysterious to you, try to
understand what it's all for.  Don't be afraid to ask us about it.

GENERAL NOTE:  EVERYTHING in these files is changeable as you see fit, unless
   otherwise noted.  Your task is to conform to the spec, period.  While
   we have suggested, with these files, certain points about the design, we
   don't require any of it.

CONTENTS:
---------

README                 This file.  You need not turn this in.

Makefile               For those of you using the standard Unix tool chain
                       rather than Eclipse, this file provides for some
                       simple compilation/testing control.  See the
                       comments in it for the targets provided.  Those of
                       you using Eclipse will need it on the instructional
                       machines (the commands 'make' and 'make check' must
                       work correctly on your submission, regardless of
                       which editor or IDE you used).

signpost/

    Makefile           A Makefile for compiling, style checking, testing, and
                       cleaning up files in this directory.

    Main.java          Entry point to program. This handles program options
                       and sets up the necessary objects to play one or more
                       games.

    Model.java         This class describes game state: the positions of
                       centers and barriers. It contains the logic for
                       adding and removing barriers, testing that constraints
                       are satisfied, and testing for a solution.

    Place.java         Encapsulates a position on the puzzle board.

    Controller.java    Contains the logic for controlling a game: responding
                       to commands by updating the model as apppropriate.

    CommandSource.java An interface that for classes that provide commands
                       to the Controller.

    GUISource.java     A CommandSource that fetches inputs from a user's
                       mouse actions.

    PuzzleSource.java  An interface to classes that can provide puzzles.

    TestSource.java    A CommandSource and PuzzleSource that fetches
                       commands and puzzles from the standard input.

    PuzzleGenerator.java
                       A kind of PuzzleSource that randomly generates
                       puzzles.

    View.java          Interface to classes that can display a Model.

    GUI.java           A View that represents the graphical display of the
                       game state and receives mouse actions from the user.

    BoardWidget.java   Used by GUI to represent the grid of squares and tiles.

    Utils.java         A collection of static utility methods.

    UnitTests.java     Main unit-test file.  This dispatches to other *Tests
                       classes for unit testing.

    ModelTests.java    Unit tests for class Model.

    PuzzleGeneratorTests.java
                       Unit tests for class PuzzleGenerator.


testing/                 Directory with integration test machinery.

    tester.py          A Python 3 script for testing your program.  It makes
                       use of the --testing option in your program to feed
                       in known moves.
    testing.py         A module that supports testing in general.
    *.in               Test scripts accepted by the tester.
    *.out              The outputs (logged by BoardLogger) expected for each
                       test.
