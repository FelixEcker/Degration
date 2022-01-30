# Degration
A Brainfuck inspired Esolang.

## Overview
### Command Set
Degration's Command set is identical to that of Brainfuck, with the symbols
differing in order to distinguish each other.

* [$01] `P+` Increment Cell Pointer
* [$02] `P-` Decrement Cell Pointer
* [$03] `V+` Increment Cell Value
* [$04] `V-` Decrement Cell Value
* [$05] `[` Open Loop
* [$06] `]` Close Loop
* [$07] `*` Output Cell Value (ASCII)
* [$08] `?` Get Input (Written to tape starting at Cell Pointer)
* [$09] `!` Exit

### Looping
Loops are opened using the `[` command and closed using the `]` command,
the closing bracket should be prepended by a 1 Byte number. If the value
of the cell which is selected at the closing bracket is the same as this number,
the loop terminates.

### Degration
The Name giver of this Esolang, "Degration", functions like this:
Every 8th Instruction execution, 1/16 of the tape's values get degraded
randomly, this can also affect the programs instruction, as they are located
on the tape aswell.

## License
Please view the LICENSE file.

## Links
* [Esolang Wiki Entry](https://esolangs.org/wiki/Degration)