# BCPlan

BCPlan is a bounded contingent planner that uses the Bounded AO\* (BAO\*) search algorithm to find optimal strong solutions via belief space search.

BCPlan is implemented in Java and depends on [JOpt Simple]([https://pholser.github.io/jopt-simple/).

It has been tested on Ubuntu 18.04.1 (64-bit) and macOS 10.13.6.

## Usage

```
$ java -jar bcp.jar --help
Usage: java -jar bcp.jar -o <FILE> -f <FILE> [-k <NUM>] | -p <FILE> [-k <NUM>]
Example: java -jar bcp.jar -o domain.cpddl -f problem.cpddl -k 1

Option                 Description                           
------                 -----------                           
-f, --fact <FILE>      Path to fact (problem) <FILE> in CPDDL
-h, --help             Display usage information and exit    
-k, --bound <NUM>      Value of k bound; <NUM> can be non-   
                         negative integer or 'infinity'      
                         (default: infinity)                 
-o, --operator <FILE>  Path to operator (domain) <FILE> in   
                         CPDDL                               
-p, --path <FILE>      Path to combined operator and fact    
                         <FILE> in CPDDL                     
-v, --version          Display version information and exit
```

## CPDDL

Contingent PDDL (CPDDL) is a PDDL-like language for contingent planning problems similar to [NuPDDL](http://mbp.fbk.eu/NuPDDL.html) and [PO-PPDDL](http://users.cecs.anu.edu.au/~ssanner/IPPC_2011).  The grammar of CPDDL in extended Backus-Naur form (EBNF) is as follows:

```
letter                    = 'a' | 'b' | 'c' | 'd' | 'e' | 'f' | 'g' | 'h' | 'i' | 'j' | 'k' | 'l' | 'm' | 'n' | 'o' | 'p' | 'q' | 'r' | 's' | 't' | 'u' | 'v' | 'w' | 'x' | 'y' | 'z' | 'A' | 'B' | 'C' | 'D' | 'E' | 'F' | 'G' | 'H' | 'I' | 'J' | 'K' | 'L' | 'M' | 'N' | 'O' | 'P' | 'Q' | 'R' | 'S' | 'T' | 'U' | 'V' | 'W' | 'X' | 'Y' | 'Z' ;
digit                     = '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' ;
character                 = letter | digit | '_' | '-' ;
name                      = letter , { character } ;

atom                      = name ;
constant                  = 'true' | 'false' ;
literal                   = atom | '(' , 'not' , atom , ')' ;
term                      = constant | literal ;
formula                   = term | '(' , 'and' , term , term , { term } , ')' ;

constant-effect           = 'null' ;
literal-effect            = atom | '(' , 'not' , atom , ')' ;
unconditional-effect      = constant-effect | literal-effect ;
term-effect               = unconditional-effect | (' , 'when' , formula , unconditional-effect , ')' ;
deterministic-effect      = term-effect | '(' , 'and' , term-effect , term-effect , { term-effect } , ')' ;
effect                    = deterministic-effect | '(' , 'oneof' , deterministic-effect , deterministic-effect , { deterministic-effect } , ')' ;

constant-observation      = 'noop' ;
literal-observation       = atom | '(' , 'not' , atom , ')' ;
unconditional-observation = constant-observation | literal-observation ;
term-observation          = unconditional-observation | (' , 'when' , formula , unconditional-observation , ')' ;
deterministic-observation = term-observation | '(' , 'and' , term-observation , term-observation , { term-observation } , ')' ;
observation               = deterministic-observation | '(' , 'oneof' , deterministic-observation , deterministic-observation , { deterministic-observation } , ')' ;

predicates                = '(' , ':predicates' , atom , { atom } , ')' ;
action                    = '(' , ':action' , name , [ ':precondition' , formula ] , ':effect' , effect , ')' | '(' , ':action' , name , [ ':precondition' , formula ] , ':observation' , observation , ')' | '(' , ':action' , name , [ ':precondition' , formula ] , ':effect' , effect , ':observation' , observation , ')' ;
domain                    = '(' , 'define' , '(' , 'domain' , name ')' , [ predicates ] , action , { action } , ')' ;

deterministic-init        = atom | '(' , 'and' , atom , atom , { atom } , ')' ;
nondeterministic-init     = '(' , 'oneof' , deterministic-init , deterministic-init , { deterministic-init } , ')' ;
formula-init              = deterministic-init | nondeterministic-init ;

init                      = '(' , ':init' , formula-init , ')' ;
goal                      = '(' , ':goal' , formula , ')' ;
problem                   = '(' , 'define' , '(' , 'problem' , name , ')' , '(' , ':domain' , name , ')' , init , goal , ')' ;
```
