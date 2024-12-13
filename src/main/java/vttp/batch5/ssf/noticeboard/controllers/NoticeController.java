package vttp.batch5.ssf.noticeboard.controllers;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.print.attribute.standard.Media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.JsonObject;
import jakarta.validation.Valid;
import vttp.batch5.ssf.noticeboard.models.NoticeObj;
import vttp.batch5.ssf.noticeboard.services.NoticeService;

// Use this class to write your request handlers


@Controller
@RequestMapping
public class NoticeController {


    @Autowired
    private NoticeService service;




    @GetMapping("/")
    public String getLandingForm(
        Model model
    ){


        model.addAttribute("notice", new NoticeObj());

        return "notice";
    }


    @PostMapping("/notice")
    public String postForm(
        @Valid @ModelAttribute("notice") NoticeObj newNotice,
        BindingResult bindings,
        Model model
    ) throws ParseException {

        String dateGet =  newNotice.getPostDate();
        //validate before it goes parsing
        if(dateGet.isBlank()){

            FieldError err1 = new FieldError("notice", "postDate", "Post date cannot be blank");

            bindings.addError(err1);

            return "notice";

        }
        //check Future or present
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate enDate = LocalDate.parse(dateGet, formatter);
        LocalDate currdate = LocalDate.now();

        if (enDate.isBefore(currdate)) {

            FieldError err = new FieldError("notice", "postDate", "Post date should be today or in the future.");

            bindings.addError(err);
        }


        if(bindings.hasErrors()){

            return "notice";
        }


        System.out.println(newNotice);

        String reply = service.postToNoticeServer(newNotice);
        System.out.println(reply + "this is from controller");
        //conditional return of view from reply:
        String[] array = reply.split(",");

        if(array[0].equals("200 OK")){

            model.addAttribute("id", array[1]);
            return "view2";


        } else {

            model.addAttribute("message", array[1]);

            return "view3";
        }


    }

    @GetMapping(path="/status", produces="application/json")
    @ResponseBody
    public ResponseEntity<String> returnJsonObject(){

        String result = service.returnFromRepo();

        if(result.equals("connected")){

            return ResponseEntity.status(200)
                                .body("{}");

        } else {



            return ResponseEntity.status(503)
                                .body("{}");

        }

      

        






    }








}
