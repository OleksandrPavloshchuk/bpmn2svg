# BPMN2SVG

A single-purpose command-line tool that transforms valid BPMN 2.0 XML into static SVG for viewing.

Command line call Format : `java -jar bpmn2svg.jar <{input file name} >{output file name}`

## Non-goals

This tool deliberately does **not**:

- edit BPMN diagrams
- validate BPMN beyond basic structural sanity checks
- perform automatic layout or routing
- provide a graphical user interface
- act as a BPMN engine or execution environment

## Design philosophy

This project follows the Unix philosophy:

- do one thing
- do it well
- read from stdin, write to stdout
- compose with other tools

## Assumptions

- Input BPMN XML is syntactically and semantically valid
- BPMN validation is expected to be handled by external tools (e.g. Camunda)
- Diagram layout is predefined in the BPMN file

## Typical usage

```bash

camunda-validate process.bpmn \
  && java -jar bpmn2svg.jar < process.bpmn > process.svg \
  && xdg-open process.svg
```

## When NOT to use this tool

- If you need to edit BPMN diagrams interactively
- If you expect automatic layout of arbitrary diagrams
- If you need BPMN execution or simulation
- If you want a web-based diagram editor

## Feature requests

Feature requests are welcome **only if** they:

- strengthen the core transformation (BPMN → SVG)
- do not introduce additional operating modes
- do not require interactive state or UI
- keep the tool usable in Unix pipelines

Requests outside this scope will be closed as out-of-scope.

## Stability

The command-line interface and SVG output format are intended to be stable.
Breaking changes will be avoided unless strictly necessary.

This project intentionally prefers composability over feature completeness.

