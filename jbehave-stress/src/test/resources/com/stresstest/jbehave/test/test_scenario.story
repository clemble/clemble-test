Scenario: Simplest possible scenario

Given contextSizeOfOne add short 16 to context
Then contextSizeOfOne context size 1

Given contextSizeOfTwo add short 16 to context
And contextSizeOfTwo add int 32 to context
Then contextSizeOfTwo context size 2

Given contextSizeOfThree add short 16 to context
And contextSizeOfThree add int 32 to context
And contextSizeOfThree add long 64 to context
Then contextSizeOfThree context size 3

