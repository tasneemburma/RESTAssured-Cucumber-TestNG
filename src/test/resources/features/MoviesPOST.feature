#Author: t.burmawala@yahoo.com
@APITesting
Feature: Testing POST requests and response for /movies API endpoint

  @APITesting
  Scenario Outline: User submits a  movie name and its description
    Given the header Content type format is "<CONTENT_TYPE>"
    And user adds the movie name "<moviename>" and its description "<description>"
    When user post the request using the endpoint
    Then returned response code from POST request is <statuscode>
    And validate the respons body

    Examples: 
      | statuscode | CONTENT_TYPE                      | moviename | description              |
      #valid data
      |        200 | application/json                  | superman  | the best movie ever made |
      #incorrect Content type
      |        404 | application/x-www-form-urlencoded | superman  | the best movie ever made |
      #Incorrect Payload
      |        400 | application/json                  |       123 | ['hfgfh','jghgj']        |

  @APITesting
  Scenario Outline: User submits binary file in the request
    Given the header Content type format is "<CONTENT_TYPE>"
    And user adds binary file
    When user post the request using the endpoint
    Then returned response code from POST request is <statuscode>
    And validate the respons body

    Examples: 
      | statuscode | CONTENT_TYPE     |
      |        500 |  multipart/mixed |

  @APITesting
  Scenario Outline: User submits multipart form data in the request
    Given the header Content type format is "<CONTENT_TYPE>"
    And user sends the multipart form request
    When user post the request using the endpoint
    Then returned response code from POST request is <statuscode>

    Examples: 
      | statuscode | CONTENT_TYPE    |
      |        500 | multipart/mixed |
