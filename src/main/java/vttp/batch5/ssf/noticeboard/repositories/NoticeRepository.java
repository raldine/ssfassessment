package vttp.batch5.ssf.noticeboard.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;


@Repository
public class NoticeRepository {

	// TODO: Task 4
	// You can change the signature of this method by adding any number of parameters
	// and return any type
	// 
	/*
	 * Write the redis-cli command that you use in this method in the comment. 
	 * For example if this method deletes a field from a hash, then write the following
	 * redis-cli command 
	 * 	hdel myhashmap a_key
	 *
	 *
	 */


	
	@Autowired @Qualifier("notice")
	private RedisTemplate<String, Object> template;



	//REDIS-CLI command = set key value || where (key = id) (value=JsonObject.toString())
	public void insertNotices(JsonObject replyPayload) {

		String key = replyPayload.getString("id");

		ValueOperations<String, Object> valueOps = template.opsForValue();
		valueOps.set(key, replyPayload.toString());
		System.err.println("success into repo");


	}

	//REDIS-CLI command = randomkey
	public String getRandKey(){

		String returnedString = "-";

		try{

			String check = template.randomKey();

			if(check!=null){
				returnedString ="connected";
			}



		} catch (Exception ex){

			String message = ex.getMessage();

			returnedString = message;
		}
		
		return returnedString;


	}


}
