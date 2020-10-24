#!/bin/bash
name=$1
./RefactoringMiner -a $name master > "$name.JSON"
