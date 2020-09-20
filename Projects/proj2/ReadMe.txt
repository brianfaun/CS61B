This directory contains a skeleton for CS61B Project 2.

Although some of what's in here might seem mysterious to you, try to 
understand what it's all for.  Don't be afraid to ask us about it.
The skeleton files are YOURS TO CHANGE AS YOU DESIRE OR THROW AWAY
ENTIRELY!!!

CONTENTS:

ReadMe			This file.
	
Makefile		A makefile that will compile your
			files and run tests.  You must turn in a Makefile,
			'make' must compile all your files, and 
			'make check' must perform all your tests.  
			Currently, this makefile is set up to do just 
			that with our skeleton files.  Be sure to keep 
			it up to date.

loa/			Directory containing the Lines of Action package.

    Makefile		A convenience Makefile so that you can issue 
			compilation commands from the game directory.

    Piece.java	 	An enumeration type describing the kinds of pieces.

    Board.java	        Represents a game board.  Contains much of the
			machinery for checking or generating possible moves.

    Square.java         Represents a position on a Board.

    Move.java		Represents a single move.

    Game.java           Controls play of the game.  Calls on Players to
                        provide moves, executes other commands,
                        and maintains a current Board.

    Player.java         Supertype representing common characteristics of
                        players.

    HumanPlayer.java	A kind of Player that reads moves from the standard
                        input (i.e., presumably from a human player).

    MachinePlayer.java  A kind of Player that chooses its moves automatically.

    Reporter.java       The supertype of "reporters", which announce errors,
                        moves, and other notes to the user.

    TextReporter.java   A type of Reporter that uses the standard output
                        (generally the terminal) for output.

    View.java           An interface for things that display the Board on
                        each move.

    NullView.java       A View that does nothing.

    Utils.java          Assorted utility functions for debugging messages and
                        error reporting.

    UnitTests.java      Class that coordinates unit testing of the loa package.

    BoardTest.java      Class containing unit tests of the Board class.

    HelpText.txt        Contains a brief description of the commands (intended
                        for printing when help requested).

The following are relevant to the extra-credit portion:

    GUI.java            A class that represents a graphical user interface
                        (GUI) for the Loa game.

    BoardWidget.java    Used by the GUI class to display the board.

    GUIPlayer.java      A type of manual Player that takes move from the GUI.

    About.html           
    Help.html           Files displayable by the GUI containing various
                        documentation.

testing/

    Makefile            Directions for testing.

    *-1.in
    *-2.in	        Test cases.  Each one is input to a testing script
                        for test-loa.  Where there is just XXX-1.in, test-loa
                        tests a single program.  Where there are both
                        XXX-1.in and XXX-2.in, the ...-1 file gives the input
                        script for one of the programs and ...-2 for the
                        other.

    *-1.std
    *-2.std		Correct output from the corresponding .in files,
                        containing dumps of the board and win messages.

    test-loa            A program that feeds a tesitng script into one or two
                        running Loa games and checks the output.

    tester.py           Runs test-loa on a given set of *.in files.

    testing.py          General testing support.