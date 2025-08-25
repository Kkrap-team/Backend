package com.kkrap.Controller;

import com.kkrap.Controller.Spec.FoldersLinksAPISpec;
import io.micrometer.core.annotation.Timed;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@Timed(value = "http.controller", extraTags = {"controller","FoldersLinks"})
@RequestMapping("/folderlist")
public class FoldersLinksController implements FoldersLinksAPISpec {


}
