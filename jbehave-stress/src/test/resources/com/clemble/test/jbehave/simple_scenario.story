Scenario: Simplest possible scenario

Given contextSizeOfOne add short 16 to context
Then contextSizeOfOne context size 1
And contextSizeOfOne short equals 16


Given contextSizeOfTwo add short 17 to context
And contextSizeOfTwo add int 324 to context
Then contextSizeOfTwo context size 2
And contextSizeOfTwo short equals 17
And contextSizeOfTwo int equals 324
And contextSizeOfOne context size 1

Given contextSizeOfThree add short 18 to context
And contextSizeOfThree add int 234 to context
And contextSizeOfThree add long 64 to context
Then contextSizeOfThree context size 3
And contextSizeOfThree int value equals 234
And contextSizeOfThree short equals 18
And contextSizeOfThree long equals 64
And contextSizeOfTwo context size 2
And contextSizeOfTwo int value equals 324
And contextSizeOfOne context size 1

Given contextSizeOfFour add short 16 to context
And contextSizeOfFour add int 32 to context
And contextSizeOfFour add byte 8 to context
And contextSizeOfFour add long 64 to context
Then contextSizeOfFour context size 4
And contextSizeOfThree context size 3
And contextSizeOfTwo context size 2
And contextSizeOfOne context size 1

Given contextSizeOfFive add short 16 to context
And contextSizeOfFive add int 32 to context
And contextSizeOfFive add byte 8 to context
And contextSizeOfFive add char A to context
And contextSizeOfFive add long 64 to context
Then contextSizeOfFive context size 5
And contextSizeOfFour context size 4
And contextSizeOfThree context size 3
And contextSizeOfTwo context size 2
And contextSizeOfOne context size 1

Given contextSizeOfSix add short 16 to context
And contextSizeOfSix add int 32 to context
And contextSizeOfSix add byte 8 to context
And contextSizeOfSix add char A to context
And contextSizeOfSix add long 64 to context
And contextSizeOfSix add string someString to context
Then contextSizeOfSix context size 6
And contextSizeOfFive context size 5
And contextSizeOfFour context size 4
And contextSizeOfThree context size 3
And contextSizeOfTwo context size 2
And contextSizeOfOne context size 1