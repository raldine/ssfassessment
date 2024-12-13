package vttp.batch5.ssf.noticeboard.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Component
public class NoticeObj {

    private String id;

    @NotNull(message="Title cannot be left blank.")
    @NotEmpty(message="Title cannot be left blank")
    @Size(min=3, max=128, message="The title must be between 2 and 128 characters.")
    private String title;

    @NotEmpty(message = "Cannot be left blank")
    @Email(message = "Must be a valid email")
    private String poster;

    private String postDate;

 

    @Size(min=1, message = "Must include at least one category")
    private List<String> categories;

    @NotNull(message="Post content cannot be left blank.")
    @NotEmpty(message="Post content cannot be left blank")
    private String text;



    public JsonObject fromNoticeToJson() throws ParseException{

        JsonObjectBuilder builder = Json.createObjectBuilder();

        builder = builder
                .add("title", this.title)
                .add("poster", this.poster)
                // .add("postDate", this.postDate)
                .add("text", this.text);


        //for categories
        JsonArrayBuilder aBuilder = Json.createArrayBuilder();
        for(String s : this.categories)
        {
            aBuilder = aBuilder
                        .add(s);

        }
        JsonArray catArray = aBuilder.build();

        //add array to main obj
        builder = builder
                .add("categories", catArray);

        //for date to long
        String dateString = this.postDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateString);
        long postDateInMili = date.getTime();

        //add date in long to main obj
        builder = builder
                .add("postDate", postDateInMili);

        JsonObject finalObj = builder.build();

        System.out.println("check if json make correctly: " + finalObj.toString());

        return finalObj;
    }









    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getPoster() {
        return poster;
    }
    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPostDate() {
        return postDate;
    }
    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }
    public List<String> getCategories() {
        return categories;
    }
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "NoticeObj [id=" + id + ", title=" + title + ", poster=" + poster + ", postDate=" + postDate
                + ", categories=" + categories + ", text=" + text + "]";
    }


    




    
}
