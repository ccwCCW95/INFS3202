package com.ccw.project.controller;

import com.ccw.project.entities.Commentors;
import com.ccw.project.entities.Comments;
import com.ccw.project.entities.Questions;
import com.ccw.project.entities.User;
import com.ccw.project.service.LoginService;
import com.ccw.project.service.MainService;
import com.ccw.project.system.OnlineControl;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MainController {

    @Autowired
    private MainService mainService;

    @Autowired
    private LoginService loginService;

    @Autowired
    DefaultKaptcha defaultKaptcha;

    @RequestMapping("/ccw/main/showQuestion")
    @ResponseBody
    public Map<String, Object> showQuestion(@RequestParam("questionId") int questionId, HttpSession session){
        // Get the specific question
        Questions question = mainService.getQuestion(questionId);

        Map<String,Object> map = new HashMap<>();

        if (question != null){
            // get the author name
            String authorName = mainService.getAuthorName(question.getAuthor());

            map.put("errMeg","0");
            map.put("questiontitle", question.getTitle());
            map.put("questionauthor", authorName);
            map.put("questioncontent", question.getDescription());

        }else {
            map.put("errMeg","1");
        }

        return map;
    }

    @RequestMapping("/ccw/main/showComents")
    public String showComments(@RequestParam("questionId") int questionId, Model model){
        // Get the comments corresponding to this question
        List<Comments> comments = mainService.getComments(questionId);

        List<Commentors> returnCommentors = new ArrayList<Commentors>();

        if (comments.size() != 0){
            for(Comments comment : comments){
                Commentors commentors = new Commentors(comment.getId(), comment.getqId(), comment.getcId(),
                        mainService.getAuthorName(comment.getcId()), comment.getTime(), comment.getRating(), comment.getContent());

                returnCommentors.add(commentors);
            }
        }

        model.addAttribute("commentors", returnCommentors);

        return "main::commentors";
    }

    @RequestMapping("/ccw/main/postquestion")
    public String postQuestion(Model model){
        model.addAttribute("postquestion", new Questions());
        return "postquestion";
    }

    @PostMapping("/ccw/main/submitquestion")
    public String subQuest(Model model, HttpSession session, Questions questions){

        // get the author id
        User user = (User)session.getAttribute("User");
        int authorId = user.getId();

        questions.setAuthor(authorId);

        SimpleDateFormat myFmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        questions.setTime(date);

        int result = mainService.insertQuestion(questions);

        if (result == 1){

            session.setAttribute("questions", mainService.getQuestions());
            return "main";

        }else {
            model.addAttribute("postquestion", new Questions());
            return "postquestion";
        }
    }

    @RequestMapping("/ccw/main/addcomments")
    public String addComments(@RequestParam("qId") int questionId, @RequestParam("content") String content, Model model, HttpSession session){
        // get the author id
        User user = (User)session.getAttribute("User");
        int authorId = user.getId();

        // Build the new comment object
        Comments comments = new Comments();
        comments.setContent(content);
        comments.setcId(authorId);
        comments.setqId(questionId);
        comments.setTime(new Date());

        int result = mainService.insertComments(comments);

        List<Comments> returncComments = mainService.getComments(questionId);

        List<Commentors> returnCommentors = new ArrayList<Commentors>();

        if (returncComments.size() != 0){
            for(Comments comment : returncComments){
                Commentors commentors = new Commentors(comment.getId(), comment.getqId(), comment.getcId(),
                        mainService.getAuthorName(comment.getcId()), comment.getTime(), comment.getRating(), comment.getContent());

                returnCommentors.add(commentors);
            }
        }

        model.addAttribute("commentors", returnCommentors);

        return "main::commentors";
    }


    @RequestMapping("/ccw/main/addrating")
    public String addRating(@RequestParam("commentId") int commentId, @RequestParam("previousRating") int previousRating, @RequestParam("questionId") int questionId, Model model){
        Comments comments = new Comments();
        comments.setId(commentId);
        comments.setRating(previousRating + 1);

        int result = mainService.updateRating(comments);

        List<Comments> returncComments = mainService.getComments(questionId);

        List<Commentors> returnCommentors = new ArrayList<Commentors>();

        if (returncComments.size() != 0){
            for(Comments comment : returncComments){
                Commentors commentors = new Commentors(comment.getId(), comment.getqId(), comment.getcId(),
                        mainService.getAuthorName(comment.getcId()), comment.getTime(), comment.getRating(), comment.getContent());

                returnCommentors.add(commentors);
            }
        }

        model.addAttribute("commentors", returnCommentors);

        return "main::commentors";
    }

    @RequestMapping("/ccw/main/profile")
    public String profile(HttpSession session, Model model){
        // Get the user information from session
        User user = (User) session.getAttribute("User");

        User userAll = loginService.getUserInfor(user.getUsername());

        model.addAttribute("user", userAll);
        return "profile";
    }

    @PostMapping("/ccw/main/submitprofile")
    public String subPro(Model model, HttpSession session, User user){

        int result = mainService.updateUser(user);


        User userAll = loginService.getUserInfor(user.getUsername());

        session.setAttribute("User", userAll);

        model.addAttribute("user", userAll);

        if (result != 1){
            model.addAttribute("emailFlag", "Email update error!");
        }else {
            model.addAttribute("emailFlag", "Email update success!");
        }

        return "profile";

    }

    @RequestMapping("/ccw/main/onlinecount")
    @ResponseBody
    public Map<String, Object> number(HttpServletRequest request, HttpServletResponse response){

        int num = OnlineControl.getActiveUserSum();

        Map<String,Object> map = new HashMap<>();

        map.put("errMeg","0");
        map.put("count", num);

        return map;
    }

    @RequestMapping("/ccw/main/getkaptcha")
    public void defaultKaptcha(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();

        try {
            // Create ver code
            String createText = defaultKaptcha.createText();
            request.getSession().setAttribute("rightCode", createText);

            // Create ver image
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Create image bytes stream
        byte[] captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    @RequestMapping("/ccw/main/verifykaptcha")
    @ResponseBody
    public Map<String, Object> verifyKaptcha(@RequestParam("verCode") String verCode, HttpServletRequest request,
                                                         HttpServletResponse response) {

        Map<String,Object> map = new HashMap<>();

        String rightCode = (String) request.getSession().getAttribute("rightCode");

        if (!rightCode.equals(verCode)) {
            map.put("errMeg","1");
        } else {
            map.put("errMeg","0");
        }
        return map;
    }

    @RequestMapping("/ccw/main/searchlikely")
    public String searchLikely(@RequestParam("keyword") String keyword, HttpSession session){

        Questions questions = new Questions();
        questions.setTitle(keyword);

        List<Questions> returnQuestions = mainService.getLikelyQuestions(questions);

        session.setAttribute("questions", returnQuestions);

//        model.addAttribute("returnQuestions", returnQuestions);

        return "main::questionlist";
    }

}
