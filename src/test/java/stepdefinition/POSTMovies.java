package stepdefinition;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.StringWriter;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;

public class POSTMovies {
	
	private static String ENDPOINT_POST_Movies = "https://splunk.mocklab.io/movies";
	private Response response;
	private RequestSpecification httpRequest ;
	private Scenario scenario;
	
	@Before
	public void beforetest(Scenario scenario) {
		
		this.scenario=scenario;
	}
	
	@Given("^the header Content type format is \"([^\"]*)\"$")
	public void the_header_Content_type_format_is(String contenttype) throws Throwable {
		httpRequest= RestAssured.given().header("Content-Type",contenttype);
	}
	
	@Given("^user adds the movie name \"([^\"]*)\" and its description \"([^\"]*)\"$")
	public void user_adds_the_movie_name_and_its_description(String moviename, String description) throws Throwable {
		JSONObject requestParams = new JSONObject();

		requestParams.put("name",moviename);
		requestParams.put("description",description);
	    StringWriter out = new StringWriter();
	    requestParams.writeJSONString(out);
	      
	    String jsonText = out.toString();
	    //System.out.print(jsonText);
	    httpRequest.body(requestParams.toJSONString());
	}


	@When("^user post the request using the endpoint$")
	public void user_post_the_request_using_the_endpoint() throws Throwable {
		response = httpRequest.post(ENDPOINT_POST_Movies);
	}

	
	@Then("^returned response code from POST request is (\\d+)$")
	public void returned_response_code_from_POST_request_is(int statusCode) throws Throwable {
		int statuscode=response.getStatusCode();
    	//System.out.println(statuscode);
    	scenario.write("Status code returned is : "+statuscode);
    	response.then().statusCode(statusCode);	
	}

	@Then("^validate the respons body$")
	public void validate_the_respons_body() throws Throwable {
	   String body=response.getBody().asString();
	   //System.out.println(body);
	   scenario.write(body);
	}


	@Given("^user adds binary file$")
	public void user_adds_binary_file() throws Throwable {
		final byte[] bytes = IOUtils.toByteArray(getClass().getResourceAsStream("/testimg.jpeg"));
		httpRequest.multiPart("file", "myFile", bytes);
	
	}


	
	@Given("^user sends the multipart form request$")
	public void user_sends_the_multipart_form_request() throws Throwable {
		
		httpRequest.multiPart("name", "superman").multiPart("description", "the best movie ever made");
	}
			


}
