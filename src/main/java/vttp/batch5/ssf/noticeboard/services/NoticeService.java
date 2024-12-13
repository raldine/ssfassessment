package vttp.batch5.ssf.noticeboard.services;

import java.io.StringReader;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.ssf.noticeboard.models.NoticeObj;
import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;

@Service
public class NoticeService {

	// TODO: Task 3
	// You can change the signature of this method by adding any number of
	// parameters
	// and return any type

	@Value("${publish.host.name}")
	private String hostName;

	@Autowired
	private NoticeRepository repo;

	public String postToNoticeServer(NoticeObj receivedNotice) throws ParseException {

		String reply = "did not connect to server";

		/////////

		JsonObject toPost = receivedNotice.fromNoticeToJson();

		System.out.println("service side >>" + toPost.toString());

		String postURL = UriComponentsBuilder
				.fromUriString(hostName)
				.path("/notice")
				.toUriString();

		System.out.println(postURL);

		RequestEntity<String> req = RequestEntity
				.post(postURL)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(toPost.toString(), String.class);

		RestTemplate template = new RestTemplate();
		try {
			ResponseEntity<String> resp = template.exchange(req, String.class);

			String receivedPayload = resp.getBody();

			JsonReader reader = Json.createReader(new StringReader(receivedPayload));
			JsonObject replyPayload = reader.readObject();
			System.out.println("received: " + replyPayload.toString());

			HttpStatusCode statusCode = resp.getStatusCode();
			String stat = statusCode.toString();

			String result = evalReply(stat, replyPayload);

			reply = result;

		} catch (HttpClientErrorException ex) {

			HttpStatusCode statusCode = ex.getStatusCode();
			String stat = statusCode.toString();

			String errorPayload = ex.getResponseBodyAsString();
			JsonReader reader = Json.createReader(new StringReader(errorPayload));
			JsonObject errorPayloadObj = reader.readObject();


			String result = evalReply(stat, errorPayloadObj);

			reply = result;
			System.out.println("from request: " + ex.getMessage());
			

			
		}

		return reply;

	}

	public String evalReply(String status, JsonObject reply) {

		String id = "-";
		String message = "-";

		String result = "error";

		if (reply.containsKey("id") && status.equals("200 OK")) {

			id = reply.getString("id");

			// insert to repo
			repo.insertNotices(reply);

		} else if (reply.containsKey("message") && !status.equals("200 OK")) {
			message = reply.getString("message");
		}

		long timeStamp = reply.getJsonNumber("timestamp").longValue();

		if (status.equals("200 OK")) {
			result = status + "," + id + "," + timeStamp;
		} else {

			result = status + "," + message + "," + timeStamp;
		}

		System.out.println("received reply was : " + result);

		// to pass to controller

		return result;

	}

	public String returnFromRepo() {

		String result = repo.getRandKey();

		return result;
	}

}
