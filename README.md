# WebCalc

A Server/Client Web based command line calculator.

## Getting Started

Create a Java Project. 
Import the 3 source WebCalc files.
Import the 2 Expression Packages.
Edit/Run WebCalcTesting file.

### Prerequisities

JRE 1.5 Compliant
Java 6+

## Command Line Usage

From Command Line:
  Expression string -> Returns Answer String
  
All numbers are converted to Double type. Returned answer is a Double.
Variables MUST start with a Letter and contain only digits and characters, no special characters.
Variables are evaluated as 0.0 if they are not instantiated first.

Negative Exponenets without enclosing ()'s have an issue. Example '5^-.333'.

```
10^2+5<ret>
105.0

10^(2+5)/8<ret>
1250000.0

10^(2+A)/8<ret>  #A has not been assigned a value yet. So it is assumed as 0.0
12.5

A=5<ret>         #Create a variable A. Set its value to 5.
5

10^(2+A)/8<ret>  #A set to 5 in previous step.
1250000.0

var2b=1/9<ret>
0.1111111111111111

var2b=var2b*3<ret>
0.3333333333333333

-1045*A+A^(var2b*A^var2b*5)<ret>
8693.834814762566
```
