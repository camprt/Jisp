# Jisp
Welcome to my Lisp interpreter in Java, aka Jysp. The "y" comes from the name of the professor who assigned this project, who named the assignment "Yisp". In this file you will find all information necessary to navigate the project, understand the output, and run the interpreter yourself.

There are two different runner files for this project. Both must first be compiled via the command "javac *.java". To run the unit tests, type "java Test", which will print the results of the unit tests for this project in the form "[Test Name]: PASS/FAIL". 
(Note: the Parse Error test will print the error because there was no way to suppress the output without affecting regular runner operations. Please disregard this line.)
To run the repl, run the command "java Jysp" and input your Lisp code. The repl may be closed by typing Control-D.
To run a Yisp file, run the command "java Jysp [path to file]". This will work with either a .txt or .yisp extension.

The test files used for the unit tests can be found in the tests folder

A sample run of the repl can be found in the file sample_repl_run.txt. The output of the unit tests can be found in the file unit_test_output.txt.


Notes on syntax:

- The symbols for "true" and "false" are T and () respectively

- All arithmetic and logical expressions use the alphabetical names given in the Sprint descriptions, not their symbols. Additionally, only type-checking functions use question marks (i.e., nil?, list?, etc.). And & or do not.

- cond statements follow the form (cond ([condition 1] [block 1]) ([c2] [b2]) ... (T [bn])). That is, each condition-execution block pair should be enclosed in parentheses

- define statements follow the form (define [identifier] (0+ parameters) [execution block]). Parameters should always be enclosed in parentheses, even if there are only 0 or 1.

- When calling user defined functions, arguments should not be placed in their own list, but rather added on one after another following the function identifier. Ex: (list3 a b c)

- The repl input line does not allow scrolling, so it is suggested that you write your statements in a separate editor and copy-paste them into the repl to ensure that your parentheses match properly. Or, just write them in a separate file and run the file instead.


Created by Campbell Thompson for CS 403-001
