# Analyze Java Projects With JavaParser

This repository contains a simple analyzer for Java project using JavaParser and JavaSymbolSolver.

## How to run

You can run the project with the following custom settings in `application.properties`:

* `PARENT_PATH`: The location of the project to be analyzed (parent directory)
    * `${user.dir}/workspace` (default)
* `TARGET_PROJECT`: The name of the project to be analyzed (target directory)
* `EXTRACTED_FILE_TYPE`: The file type for storing results
    * csv (default)
    * excel
* `SYMBOL_SOURCE_PATH`: When you want to read an already analyzed result file (only for csv format)

The results are saved in `${PARENT_PATH}/result/${TARGET_PROJECT}`.

## Description

The relationship between each reference and definition is established through
`FULL_QUALIFIED_NAME_ID`. (For primitive types, the value -1 is assigned.)
If the `FULL_QUALIFIED_NAME_ID` does not exist, it indicates that the symbol is defined in an
external dependency. (To be updated with dependencies jar)