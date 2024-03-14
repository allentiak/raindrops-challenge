# Coding Challenge

## Raindrops

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

## Possible roadmap

- one day, we might want to have a generator that generates a constant stream of raindrops for an ongoing stream of numbers (increasing or random)
- one other day, there might be more sounds ("pläng", "rattle", "rongle")
- even another day, it might rain so hard, that even "`*PLING*`" is not enough. "`*P*L*I*N*G*`or even louder sounds might occur.

## Example:

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
96 = *PLING*, PLING, plang
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
