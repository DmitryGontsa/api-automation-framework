Meta:

Narrative:
As a user
I want to perform an action
So that I can achieve a business goal

Scenario: Verify user is able to create new Post

Given user creates new 'Post', using API:
| title              | author               | age |
| Hello From JBehave | Test Automation User | 20  |
Then new post should be created


Scenario: Verify user is able to update existing Post

Given user creates new 'Post', using API:
| title                                    | author         | age |
| Java Programming Language. JDK Version 7 | Gerbert Shield | 50  |
When user update existing post, using following data:
| title                                    | author         | age |
| Java Programming Language. JDK Version 8 | Gerbert Shield | 51  |
Then following post should be updated

Scenario: Verify user is able to filter all Posts, by filter data

Given user creates new 'Post', using API:
| title           | author           | age |
| Some Fake Title | Some Fake Author | 20  |
And user creates new 'Post', using API:
| title                                    | author         | age |
| Java Programming Language. JDK Version 8 | Gerbert Shield | 51  |
And user creates new 'Post', using API:
| title                                    | author         | age |
| Java Programming Language. JDK Version 8 | Gerbert Shield | 51  |
And user creates new 'Post', using API:
| title                                    | author         | age |
| Java Programming Language. JDK Version 8 | Gerbert Shield | 51  |
When user filters retrieved Posts by next filter params:
| title                                    | author         |
| Java Programming Language. JDK Version 8 | Gerbert Shield |
Then each filtered Post should contain such data only:
| title                                    | author         |
| Java Programming Language. JDK Version 8 | Gerbert Shield |