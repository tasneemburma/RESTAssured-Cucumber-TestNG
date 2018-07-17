package stepdefinition;



import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import cucumber.api.Scenario;
import io.restassured.specification.RequestSpecification;

import HelperServices.GETMoviesApiHelper;

public class GETMovies {
		
		
		private static String ENDPOINT_GET_Movies = "https://splunk.mocklab.io/movies";
		private Response response;
		private RequestSpecification httpRequest ;
		public static JsonPath jsonPathEvaluator;
		private static int sizeoflist=-1;
		private int countval=0;
		private Scenario scenario;
		
		
		@Before
		public void beforetest(Scenario scenario) {
			
			this.scenario=scenario;
		}
		
		@Given("^the header Accepts Content type \"([^\"]*)\"$")
		public void the_header_Accepts_Content_type(String contenttypeaccepted) throws Throwable {
		   
			
			httpRequest= RestAssured.given().header("Accept",contenttypeaccepted);
			
		}
		
		@When("^user sends a get entire movies list request to the server$")
		public void user_sends_a_get_entire_movies_list_request_to_the_server() throws Throwable {
		    
			response=httpRequest.when().get(ENDPOINT_GET_Movies);
		}
		
		@Then("^returned response code is (\\d+)$")
		public void returned_response_code_is(int statusCode) throws Throwable {
		   
			int statuscode=response.getStatusCode();
	    	//System.out.println(statuscode);
	    	scenario.write("Status code returned is : "+statuscode);
	    	response.then().statusCode(statusCode);
	    	
			
		}

		@Then("^the Content type is \"([^\"]*)\"$")
		public void the_Content_type_is(String contenttypereturned) throws Throwable {
			response.then().contentType(contenttypereturned);
		}
		
		@Given("^Movies and its information exists with a queried movie name \"([^\"]*)\"$")
		public void movies_and_its_information_exists_with_a_queried_movie_name(String moviename) throws Throwable {
			httpRequest.param("q", moviename);
		}

		@Then("^verfy the number of records returned are \"([^\"]*)\"$")
		public void verfy_the_number_of_records_returned_are(String moviessizelist) throws Throwable {
			sizeoflist=response.then().extract().path("results.size()");
			//System.out.println("Real size of list"+ sizeoflist);
			scenario.write("Real size of list"+ sizeoflist);
			if(sizeoflist>-1) {
				scenario.write("The size of the movies list is "+moviessizelist);
				response.then().assertThat().body("results.size()", equalTo(Integer.parseInt(moviessizelist)));
				
			}  
			else {
				scenario.write("Incorrect movie query passed");
			}
		}
		
		@Given("^Movies and its information exists with a queried movie name \"([^\"]*)\" and limit the records count to (\\d+)$")
		public void movies_and_its_information_exists_with_a_queried_movie_name_and_limit_the_records_count_to(String moviename, int count) throws Throwable {
		    
			httpRequest.param("q", moviename).param("count", count);
			countval=count;
		}

		@Then("^validate total number of movies records returned$")
		public void validate_total_number_of_movies_records_returned() throws Throwable {
			
			   response.then().extract();
			   jsonPathEvaluator = response.jsonPath();
			   int size=jsonPathEvaluator.getInt("results.size()");
			   Boolean res=GETMoviesApiHelper.iscountcorrect(size,countval,sizeoflist);
			   //scenario.write("size got :"+size);
			   //scenario.write("count provided :"+countval);
			   //scenario.write("is real size saved :"+sizeoflist);
			   if(!res) {
				   scenario.write("Movie records returned does not match the count query value passed");
			   }
			   else {
				   scenario.write("Correct number of movie records are returned");

			   }
			   assertTrue(res);
		}

		@Then("^the JSON response retrieved should have unique value for the attribute poster_path$")
		public void the_JSON_response_retrieved_should_have_unique_value_for_the_attribute_poster_path() throws Throwable {
			List<String> poster_path = jsonPathEvaluator.get("results.poster_path");
			Boolean isunique=GETMoviesApiHelper.checkDuplicateUsingSet(poster_path);
			if(!isunique) {
				int countofdupliatemovies=0;
				Set<String>listofduplicateslinks=GETMoviesApiHelper.duplicateslist(poster_path);
				scenario.write("poster_path duplicate list of url below:");
				for (String s : listofduplicateslinks) {
					countofdupliatemovies+=1;
				    scenario.write(s);
				}
				scenario.write("Number of duplicate movies in the list are "+countofdupliatemovies);
			}
			else {
				scenario.write("All poster path links are unique");
				}
			assertTrue(isunique);
		}

		@Then("^in the JSON response retrieved should have atmost (\\d+) movies in the list whose sum of genre_ids is greater than (\\d+)$")
		public void in_the_JSON_response_retrieved_should_have_atmost_movies_in_the_list_whose_sum_of_genre_ids_is_greater_than(int arg1, int arg2) throws Throwable {
			List<List<Integer>> list_genre_ids = jsonPathEvaluator.get("results.genre_ids");
			int actualcountofmovies=GETMoviesApiHelper.countofmovies(list_genre_ids);
			Boolean iscorrectcount=GETMoviesApiHelper.verifyiscountmovies(actualcountofmovies);
			//System.out.println(iscorrectcount);
			
			//System.out.println("Actual Count of movies"+actualcountofmovies);
			scenario.write("Actual Count of movies is : "+actualcountofmovies);
			
			assertTrue(iscorrectcount);
		}

		@Then("^the JSON response retrieved should have atleast (\\d+) movie in the list whose title has a palindrome in it$")
		public void the_JSON_response_retrieved_should_have_atleast_movie_in_the_list_whose_title_has_a_palindrome_in_it(int arg1) throws Throwable {
			List<String> list_title = jsonPathEvaluator.get("results.title");
			boolean havepalindromewordmovies=GETMoviesApiHelper.verifypalindromemoviescount(list_title);
			scenario.write("It is "+havepalindromewordmovies + " that we have atleast 1 movie title having a plaindrome word in it");
			assertTrue(havepalindromewordmovies);
		}

		@Then("^the JSON response retrieved should have atleast (\\d+) movies in the list whose title contains the title of another movie$")
		public void the_JSON_response_retrieved_should_have_atleast_movies_in_the_list_whose_title_contains_the_title_of_another_movie(int arg1) throws Throwable {
			List<String> list_title = jsonPathEvaluator.get("results.title");
			boolean verifycontainstitlemoviescount=GETMoviesApiHelper.verifycontainstitlemoviescount(list_title);
			scenario.write("It is "+verifycontainstitlemoviescount + " that we have atleast 2 movie titles having title of another movie from the list in it");
			assertTrue(verifycontainstitlemoviescount);
		}

		@Then("^the JSON response retrieved should be in a correct sorted order$")
		public void the_JSON_response_retrieved_should_be_in_a_correct_sorted_order() throws Throwable {
			List<Integer> ids = jsonPathEvaluator.get("results.id");
			List<List<Integer>> list_genre_ids = jsonPathEvaluator.get("results.genre_ids");
			boolean ressortingorder=GETMoviesApiHelper.verifysort(list_genre_ids,ids);
			scenario.write("It is "+ressortingorder + " that we have correct sorting order");
			assertTrue(ressortingorder);
		}

		@Then("^the JSON response retrieved should have all valid poster path urls\\.$")
		public void the_JSON_response_retrieved_should_have_all_valid_poster_path_urls() throws Throwable {
			List<Object> poster_path = jsonPathEvaluator.get("results.poster_path");
		    boolean isposterpathurlvalid=GETMoviesApiHelper.isString(poster_path);
		    if(!isposterpathurlvalid) {
		    	scenario.write("This is poster path url is invalid");
		    }
		    else {
		    	scenario.write("This poster path url is valid-String or null");
		    }
		    assertTrue(isposterpathurlvalid);
		}





		
}
