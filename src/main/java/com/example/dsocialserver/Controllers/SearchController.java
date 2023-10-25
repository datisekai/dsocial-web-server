/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Controllers;

import com.example.dsocialserver.Services.GroupService;
import com.example.dsocialserver.Services.PostService;
import com.example.dsocialserver.Services.UserService;
import com.example.dsocialserver.Utils.JwtTokenProvider;
import static com.example.dsocialserver.Utils.ParseJSon.ParseJSon;
import com.example.dsocialserver.Utils.StatusUntilIndex;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author haidu
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private GroupService groupService;

    @GetMapping("/user")
    public ResponseEntity searchPeople(@RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "limit", defaultValue = "10") String limit,
            @RequestParam(value = "q", defaultValue = "") String q) {
        try {
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
//        ----------------------------------

            Map<String, Object> gr = userService.getPeopleList(Integer.parseInt(page) - 1, Integer.parseInt(limit), Integer.parseInt(userId), q);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("data", gr.get("data"));
            responseData.put("pagination", gr.get("pagination"));
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @GetMapping("/group")
    public ResponseEntity searchGroup(@RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "limit", defaultValue = "10") String limit,
            @RequestParam(value = "q", defaultValue = "") String q) {
        try {
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
//        ----------------------------------

            Map<String, Object> gr = groupService.getSearchGroupList(Integer.parseInt(page) - 1, Integer.parseInt(limit), q);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("data", gr.get("data"));
            responseData.put("pagination", gr.get("pagination"));
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }

    @GetMapping("/post")
    public ResponseEntity searchPost(@RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "limit", defaultValue = "10") String limit,
            @RequestParam(value = "q", defaultValue = "") String q) {
        try {
            String userId = JwtTokenProvider.getIDByBearer(authorizationHeader).getSubject();
//        ----------------------------------

            Map<String, Object> gr = postService.getPostListByHtml(Integer.parseInt(page) - 1, Integer.parseInt(limit), q);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("data", gr.get("data"));
            responseData.put("pagination", gr.get("pagination"));
            return ResponseEntity.status(HttpStatus.OK).body(ParseJSon(responseData));
        } catch (NumberFormatException e) {
            return StatusUntilIndex.showInternal(e);
        }
    }
}
