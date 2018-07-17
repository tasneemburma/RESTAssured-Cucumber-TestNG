#Author: t.burmawala@yahoo.com
@APITesting
Feature: Testing GET requests and response for /movies API endpoint

  @APITesting
  Scenario Outline: User gets a list of all movies when sends GET request to /movies API endpoint
    Given the header Accepts Content type "<CONTENT_TYPE>"
    When user sends a get entire movies list request to the server
    Then returned response code is <statuscode>
    And the Content type is "<CONTENT_TYPE_RETURNED>"

    Examples: 
      | statuscode | CONTENT_TYPE     | CONTENT_TYPE_RETURNED |
      #/movies end point does not exist
      |        404 | application/json | text/plain            |

  @APITesting
  Scenario Outline: user gets a list of movies and its information by querying the Movie name
    Given the header Accepts Content type "<CONTENT_TYPE>"
    And Movies and its information exists with a queried movie name "<movie name query>"
    When user sends a get entire movies list request to the server
    Then returned response code is <statuscode>
    And the Content type is "<CONTENT_TYPE_RETURNED>"
    And verfy the number of records returned are "<movieslistsize>"

    Examples: 
      | movie name query | statuscode | CONTENT_TYPE     | movieslistsize | CONTENT_TYPE_RETURNED |
      #correct data
      | batman           |        200 | application/json |             16 | application/json      |
      #movie name does not exist in catalog
      | DDLJ             |        404 | application/json |              0 | text/plain            |
      #movie name uses invalid characters
      | A%$@             |        400 | application/json |                | text/plain            |
      
  @APITesting
  Scenario Outline: user gets a list of movies and its information by querying the Movie name and limiting the records by providing count
    Given the header Accepts Content type "<CONTENT_TYPE>"
    And Movies and its information exists with a queried movie name "<movie name query>" and limit the records count to <count>
    When user sends a get entire movies list request to the server
    Then returned response code is <statuscode>
    And the Content type is "<CONTENT_TYPE_RETURNED>"
    And validate total number of movies records returned

    Examples: 
      | movie name query | count | statuscode | CONTENT_TYPE     | CONTENT_TYPE_RETURNED |
      #count=2
      | batman           |     2 |        200 | application/json | application/json      |
      #count=0
      | batman           |     0 |        200 | application/json | application/json      |

  @APITesting
  Scenario Outline: Validate the poster_path attribute is unique in the list of movies retrieved in response.
    Given the header Accepts Content type "<CONTENT_TYPE>"
    And Movies and its information exists with a queried movie name "<movie name query>"
    When user sends a get entire movies list request to the server
    Then returned response code is <statuscode>
    And the Content type is "<CONTENT_TYPE_RETURNED>"
    And the JSON response retrieved should have unique value for the attribute poster_path

    Examples: 
      | movie name query | CONTENT_TYPE     | statuscode | CONTENT_TYPE_RETURNED |
      #SPL-003
      | batman           | application/json |        200 | application/json      |

  @APITesting
  Scenario Outline: Validate there are atmost 7 movies in the list whose sum of genre_ids is greater than 400
    Given the header Accepts Content type "<CONTENT_TYPE>"
    And Movies and its information exists with a queried movie name "<movie name query>"
    When user sends a get entire movies list request to the server
    Then returned response code is <statuscode>
    And the Content type is "<CONTENT_TYPE_RETURNED>"
    And in the JSON response retrieved should have atmost 7 movies in the list whose sum of genre_ids is greater than 400

    Examples: 
      | movie name query | CONTENT_TYPE     | statuscode | CONTENT_TYPE_RETURNED |
      #SPL-004
      | batman           | application/json |        200 | application/json      |

  @APITesting
  Scenario Outline: Validate there is atleast 1 movie in the list whose title has a plaindrome in it
    Given the header Accepts Content type "<CONTENT_TYPE>"
    And Movies and its information exists with a queried movie name "<movie name query>"
    When user sends a get entire movies list request to the server
    Then returned response code is <statuscode>
    And the Content type is "<CONTENT_TYPE_RETURNED>"
    And the JSON response retrieved should have atleast 1 movie in the list whose title has a palindrome in it

    Examples: 
      | movie name query | CONTENT_TYPE     | statuscode | CONTENT_TYPE_RETURNED |
      #SPL-005
      | batman           | application/json |        200 | application/json      |

  @APITesting
  Scenario Outline: Validate there are atleast 2 movies in the list whose title contains the title of another movie
    Given the header Accepts Content type "<CONTENT_TYPE>"
    And Movies and its information exists with a queried movie name "<movie name query>"
    When user sends a get entire movies list request to the server
    Then returned response code is <statuscode>
    And the Content type is "<CONTENT_TYPE_RETURNED>"
    And the JSON response retrieved should have atleast 2 movies in the list whose title contains the title of another movie

    Examples: 
      | movie name query | CONTENT_TYPE     | statuscode | CONTENT_TYPE_RETURNED |
      #SPL-006
      | batman           | application/json |        200 | application/json      |

  @APITesting
  Scenario Outline: Validate the sorting order of movies retrieved in response.
    Given the header Accepts Content type "<CONTENT_TYPE>"
    And Movies and its information exists with a queried movie name "<movie name query>"
    When user sends a get entire movies list request to the server
    Then returned response code is <statuscode>
    And the Content type is "<CONTENT_TYPE_RETURNED>"
    And the JSON response retrieved should be in a correct sorted order

    Examples: 
      | movie name query | CONTENT_TYPE     | statuscode | CONTENT_TYPE_RETURNED |
      #SPL-003
      | batman           | application/json |        200 | application/json      |

  @APITesting
  Scenario Outline: Validate poster path link is valid.
    Given the header Accepts Content type "<CONTENT_TYPE>"
    And Movies and its information exists with a queried movie name "<movie name query>"
    When user sends a get entire movies list request to the server
    Then returned response code is <statuscode>
    And the Content type is "<CONTENT_TYPE_RETURNED>"
    And the JSON response retrieved should have all valid poster path urls.

    Examples: 
      | movie name query | CONTENT_TYPE     | statuscode | CONTENT_TYPE_RETURNED |
      #SPL-002
      | batman           | application/json |        200 | application/json      |
  
