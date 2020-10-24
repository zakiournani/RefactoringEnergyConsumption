#!/bin/bash
name=$1
./RefactoringMiner -bt $name 1.13.0-beta v1.36.0  > "tag1.13-1.36_$name.JSON"
