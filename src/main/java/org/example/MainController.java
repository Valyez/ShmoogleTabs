package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @Autowired
    private Data data;

    @GetMapping("/main")
    public String main(Model model) {
        return "main";
    }




    @PostMapping("/main")
    public String main(@RequestParam(required = false) String address,
                       @RequestParam(required = false) String formula,
                       Model model) {
        try {
            data.getPage().setCellFormula(address, formula);
        } catch (RuntimeException e) {
            model.addAttribute("exception", e.getMessage());
            System.out.println(e);
        }
        return "main";
    }
}
