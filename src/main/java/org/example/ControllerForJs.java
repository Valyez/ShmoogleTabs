package org.example;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerForJs {
    @Autowired
    private Data data;

    @GetMapping("/json")
    public String json(Model model) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString;
        try {
            data.calculatePage();
            jsonString = mapper.writeValueAsString(data.getPage());
        } catch (Exception e) {
            System.out.println(e);
            return e.getMessage();
        }
        return jsonString;
    }
}


