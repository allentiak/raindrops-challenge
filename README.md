# Coding Challenge

Leandro Doctors' solution to the "Raindrops" Coding Challenge


## Solution

### Usage

Run the project directly, via `:main-opts` (`-m allentiak.challenges.raindrops.cli`):

    $ clojure -M:run-m

Run the project's tests:

    $ clojure -T:build test

Run the project's CI pipeline and build an uberjar:

    $ clojure -T:build ci

This will produce an updated `pom.xml` file with synchronized dependencies inside the `META-INF`
directory inside `target/classes` and the uberjar in `target`. You can update the version (and SCM tag)
information in generated `pom.xml` by updating `build.clj`.

If you don't want the `pom.xml` file in your project, you can remove it. The `ci` task will
still generate a minimal `pom.xml` as part of the `uber` task, unless you remove `version`
from `build.clj`.

Run that uberjar:

    $ java -jar target/net.clojars.allentiak/raindrops-0.1.0-SNAPSHOT.jar


### Hacking

Start a REPL from the terminal:

    % clojure -M:repl

Run tests on save:

    % clojure -X:watch-test


### License

Copyright © 2024 Leandro Doctors

This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

You should have received a copy of the GNU Affero General Public License along with this program.
If not, see <http://www.gnu.org/licenses/>.

#### Additional Permission to convey with Clojure (under GNU AGPL version 3 section 7)

If you modify this Program, or any covered work, by linking or combining it with Clojure (or a modified version of that library), containing parts covered by the terms of the Eclipse Public License (EPL), the licensors of this Program grant you additional permission to convey the resulting work.
Corresponding Source for a non-source form of such a combination shall include the source code for the parts of Clojure used as well as that of the covered work.

#### Warranty Disclaimer

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
See the GNU Affero General Public License for more details.


## Problem Statement

### Raindrops

*Remember that day when you were listening to those raindrops making different sounds? We want to make it rain through numbers.*

## Mission

For the numbers from one to 24, we want to print ...

   - "pling" for any time the number is divisible by "2"
   - "plang" for any time the number is divisible by "3"
   - "plong" for any time the number is divisible by "5"
   - "tshäng" for any time the number is divisible by "17"
   - "blob" if the number is not divisible by any of those primes

The number of divisions is also important:

- If the number contains one of those prime factors two times, the sound is louder ("`PLING`" instead of "`pling`").
- If the number contains one of those prime factors three times, the sound is even louder ("`*PLING*`" instead of "`pling`").
- If the number contains one of those prime factors four times, the sound is the louder followed by the normal sound (16 = "`*PLING*` `pling`")
- 128 would so be "`*PLING*`, `*PLING*`, `pling`"
- and so on

### Possible roadmap

- one day, we might want to have a generator that generates a constant stream of raindrops for an ongoing stream of numbers (increasing or random)
- one other day, there might be more sounds ("pläng", "rattle", "rongle")
- even another day, it might rain so hard, that even "`*PLING*`" is not enough. "`*P*L*I*N*G*`or even louder sounds might occur.

### Example:

```
1 = blob
2 = pling
3 = plang
4 = PLING
5 = plong
6 = pling, plang
7 = blob
8 = *PLING*
9 = PLANG
10 = pling, plong
11 = blob
12 = PLING, plang
13 = blob
14 = pling
15 = plang, plong
16 = *PLING*, pling
17 = tshäng
18 = pling, PLANG
19 = blob
20 = PLING, plong
21 = plang
22 = pling
23 = blob
24 = *PLING*, plang
(...)
80 = *PLING*, pling, plong
81 = *PLANG*, plang
82 = pling
83 = blob
84 = PLING, plang
85 = plong, tshäng
86 = pling
87 = plang
88 = *PLING*
89 = blob
90 = pling, PLANG, plong
91 = blob
92 = PLING
93 = plang
94 = pling
95 = plong
96 = *PLING*, PLING, plang ;; this one is wrong: it should be "pling, *PLING*, pling, plang"
(...)
111 = plang
112 = *PLING*, pling
113 = blob
(...)
119 = tshäng
120 = *PLING*, plang, plong
121 = blob
122 = pling
123 = plang
124 = PLING
125 = *PLONG*
126 = pling, PLANG
127 = blob
128 = *PLING*, *PLING*, pling
129 = plang
130 = pling, plong
```
