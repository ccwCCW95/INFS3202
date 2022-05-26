package com.ccw.project.controller;

import com.ccw.project.entities.*;
import com.ccw.project.service.LoginService;
import com.ccw.project.service.MainService;
import com.ccw.project.service.ThumbnailService;
import com.ccw.project.system.OnlineControl;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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

    @Autowired
    ThumbnailService thumbnailService;

    @RequestMapping("/ccw/main/showQuestion")
    @ResponseBody
    public Map<String, Object> showQuestion(@RequestParam("questionId") int questionId, HttpSession session){
        // Get the specific question
        Questions question = mainService.getQuestion(questionId);

        // Update the views number of specific question
        int oldViews = question.getViews();
        int newViews = oldViews + 1;

        mainService.updateQusViews(questionId, newViews);

        Map<String,Object> map = new HashMap<>();

        if (question != null){
            // get the author name
            String authorName = mainService.getAuthorName(question.getAuthor());

            map.put("errMeg","0");
            map.put("questiontitle", question.getTitle());
            map.put("questionauthor", authorName);
            map.put("questioncontent", question.getDescription());
            map.put("questionviews", newViews);

            if(mainService.checkIfPin(question, ((User)session.getAttribute("User")).getId())){
                map.put("pinvalue", "1");
            }else {
                map.put("pinvalue", "0");
            }

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

//            session.setAttribute("questions", mainService.getQuestions());

            // Get the new question list
            List<Questions> returnQuestions = mainService.getQuestions();

            List<Questions> newList = mainService.getAfPinQuestions(returnQuestions, authorId);

            session.setAttribute("questions", newList);
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

        return "main::questionlist";
    }

    @RequestMapping("/ccw/main/pinqus")
    public String pinQuestion(@RequestParam("questionId") int questionId, HttpSession session){
        // get the user id
        User user = (User)session.getAttribute("User");
        int userId = user.getId();

        // Get the info for this specific question
        Questions questions = mainService.getQuestion(questionId);

        String pinIDs = questions.getPinId();

        if(pinIDs == null){
            pinIDs = "";
        }

        String[] temp1 = pinIDs.split(",");

        boolean flag = false;

        for(int i = 0; i <= temp1.length - 1; i++){
            if (temp1[i].equals(userId)){
                flag = true;
            }
        }

        if (!flag){
            mainService.updatePinids(questionId, pinIDs + "," + userId);
        }

        // Get the new question list
        List<Questions> returnQuestions = mainService.getQuestions();

        List<Questions> newList = mainService.getAfPinQuestions(returnQuestions, userId);

        session.setAttribute("questions", newList);

        return "main::questionlist";
    }

    @RequestMapping("/ccw/main/nopinqus")
    public String noPinQuestion(@RequestParam("questionId") int questionId, HttpSession session){
        // get the user id
        User user = (User)session.getAttribute("User");
        int userId = user.getId();

        // Get the info for this specific question
        Questions questions = mainService.getQuestion(questionId);

        String pinIDs = questions.getPinId();

        String[] temp1 = pinIDs.split(",");

        String newPinIds = "";

        for(int i = 0; i <= temp1.length - 1; i++){
            if((!temp1[i].equals(String.valueOf(userId)) && (!"".equals(temp1[i])))){
                newPinIds += ("," + temp1[i]);
            }
        }

        mainService.updatePinids(questionId, newPinIds);


        // Get the new question list
        List<Questions> returnQuestions = mainService.getQuestions();

        List<Questions> newList = mainService.getAfPinQuestions(returnQuestions, userId);

        session.setAttribute("questions", newList);

        return "main::questionlist";
    }

    @RequestMapping("/ccw/main/image")
    public String image(Model model, HttpSession session){

        // Get the user image from database
        User user = (User) session.getAttribute("User");

        User newUser = loginService.getUserInfor(user.getUsername());

        String img = newUser.getImage();

        model.addAttribute("filename", "/images/rotPhoto/" + img);

        return "image";
    }

    @Value("${file.upload.path}")
    private String filePath;

    @RequestMapping("/ccw/main/upload")
    public String upload(@RequestParam("file") MultipartFile file, @RequestParam("resize") String resize, Model model, HttpSession session) {

        String filename = file.getOriginalFilename();

        String path = filePath + "rotPhoto/";

        File filepath = new File(path, filename);

        if (!filepath.getParentFile().exists()) {
            filepath.getParentFile().mkdirs();
        }

        // Check if the image should be resize
        if ("resize".equals(resize)){
            thumbnailService.changeScale(file, 0.3, path + File.separator + filename);
        }else {
            try {
                file.transferTo(new File(path + File.separator + filename));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Store the file name to database
        User user = (User) session.getAttribute("User");
        user.setImage(filename);

        mainService.updateImg(user);

        session.setAttribute("User", user);

        model.addAttribute("filename", "/images/rotPhoto/" + filename);
        return "image";
    }

    @RequestMapping("/ccw/main/imagegallery")
    public String imageGallery(Model model, HttpSession session){

        List<String> returnList = new ArrayList<String>();

        // Get the gallery image from database
        User user = (User) session.getAttribute("User");
        int userId = user.getId();

        Gallery gallery = mainService.getUserGallery(userId);

        if(gallery == null){
            return "imagegallery";
        }

        if (gallery.getImg1() != null && gallery.getImg1() != ""){
            returnList.add("/images/rotPhoto/" + gallery.getImg1());
        }

        if (gallery.getImg2() != null && gallery.getImg2() != ""){
            returnList.add("/images/rotPhoto/" + gallery.getImg2());
        }

        if (gallery.getImg3() != null && gallery.getImg3() != ""){
            returnList.add("/images/rotPhoto/" + gallery.getImg3());
        }

        model.addAttribute("gallerylist", returnList);

        return "imagegallery";
    }

    @RequestMapping("/ccw/main/uploadgallery")
    public String uploadGallery(MultipartFile[] files, Model model, HttpSession session) {

        String path = filePath + "rotPhoto/";

        User user = (User) session.getAttribute("User");
        int userId = user.getId();

        List<String> filenames = new ArrayList<String>();
        List<String> returnList = new ArrayList<String>();

        for (MultipartFile file : files) {

            String filename = file.getOriginalFilename();

            File filepath = new File(path, filename);

            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            }

            thumbnailService.changeSize(file, 200, 100, path + File.separator + filename);

            filenames.add(filename);
        }

        int listSize = filenames.size();
        Gallery newGallery = new Gallery();
        newGallery.setUserid(userId);

        if (listSize == 1){
            newGallery.setImg1(filenames.get(0));
            returnList.add("/images/rotPhoto/" + filenames.get(0));

        }else if(listSize == 2){
            newGallery.setImg1(filenames.get(0));
            returnList.add("/images/rotPhoto/" + filenames.get(0));
            newGallery.setImg2(filenames.get(1));
            returnList.add("/images/rotPhoto/" + filenames.get(1));
        }else {
            newGallery.setImg1(filenames.get(0));
            returnList.add("/images/rotPhoto/" + filenames.get(0));
            newGallery.setImg2(filenames.get(1));
            returnList.add("/images/rotPhoto/" + filenames.get(1));
            newGallery.setImg3(filenames.get(2));
            returnList.add("/images/rotPhoto/" + filenames.get(2));
        }

        // Store the file name to database
        if (mainService.checkUserinGallery(userId)){
            // user is exist in gallery, use update
            mainService.updateGallery(newGallery);
        }else {
            // insert
            mainService.insertGallery(newGallery);
        }

        model.addAttribute("gallerylist", returnList);

        return "imagegallery";

    }

    @RequestMapping("/ccw/main/imagegallerydrag")
    public String imageGalleryDrag(Model model, HttpSession session){

        List<String> returnList = new ArrayList<String>();

        // Get the gallery image from database
        User user = (User) session.getAttribute("User");
        int userId = user.getId();

        Gallery gallery = mainService.getUserGallery(userId);

        if(gallery == null){
            return "imagedrag";
        }

        if (gallery.getImg1() != null && gallery.getImg1() != ""){
            returnList.add("/images/rotPhoto/" + gallery.getImg1());
        }

        if (gallery.getImg2() != null && gallery.getImg2() != ""){
            returnList.add("/images/rotPhoto/" + gallery.getImg2());
        }

        if (gallery.getImg3() != null && gallery.getImg3() != ""){
            returnList.add("/images/rotPhoto/" + gallery.getImg3());
        }

        model.addAttribute("gallerylist", returnList);

        return "imagedrag";
    }

    @RequestMapping("/ccw/main/uploadgallerydrag")
    @ResponseBody
    public Map<String,Object> uploadGalleryDrag(MultipartFile[] files, Model model, HttpSession session) {

        String path = filePath + "rotPhoto/";

        User user = (User) session.getAttribute("User");
        int userId = user.getId();

        List<String> filenames = new ArrayList<String>();
        List<String> returnList = new ArrayList<String>();

        for (MultipartFile file : files) {

            String filename = file.getOriginalFilename();

            File filepath = new File(path, filename);

            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            }

            thumbnailService.changeSize(file, 200, 100, path + File.separator + filename);

            filenames.add(filename);
        }

        int listSize = filenames.size();
        Gallery newGallery = new Gallery();
        newGallery.setUserid(userId);

        if (listSize == 1){
            newGallery.setImg1(filenames.get(0));
            returnList.add("/images/rotPhoto/" + filenames.get(0));

        }else if(listSize == 2){
            newGallery.setImg1(filenames.get(0));
            returnList.add("/images/rotPhoto/" + filenames.get(0));
            newGallery.setImg2(filenames.get(1));
            returnList.add("/images/rotPhoto/" + filenames.get(1));
        }else {
            newGallery.setImg1(filenames.get(0));
            returnList.add("/images/rotPhoto/" + filenames.get(0));
            newGallery.setImg2(filenames.get(1));
            returnList.add("/images/rotPhoto/" + filenames.get(1));
            newGallery.setImg3(filenames.get(2));
            returnList.add("/images/rotPhoto/" + filenames.get(2));
        }

        // Store the file name to database
        if (mainService.checkUserinGallery(userId)){
            // user is exist in gallery, use update
            mainService.updateGallery(newGallery);
        }else {
            // insert
            mainService.insertGallery(newGallery);
        }

        Map<String,Object> map = new HashMap<>();

        return map;

    }

    @RequestMapping("/ccw/main/usermanagement")
    public String userManagement(Model model){

        // Get all the users in the database except manager
        List<User> users = mainService.getAllUsers();

        model.addAttribute("allusers", users);

        return "usermanagement";
    }

    @RequestMapping("/ccw/main/deleteuser")
    public String deleteUser(@RequestParam("userId") int userId, Model model){

        // delete the user
        mainService.deleteUser(userId);

        // Get all the users in the database except manager
        List<User> users = mainService.getAllUsers();

        model.addAttribute("allusers", users);


        return "usermanagement::users";
    }

}
