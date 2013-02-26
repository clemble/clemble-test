Scenario: Simplest possible scenario

Given added anotherTest
And anotherTest add short 32 to context
When anotherTest validated
Then anotherTest validation fails
And anotherTest context size 3

Given added test
When test validated
Then test validation fails
And test context size 2