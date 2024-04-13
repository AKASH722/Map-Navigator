package co.akash.mapnavigator.controller;

import co.akash.mapnavigator.service.MapNavigatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ViewsController {

    private final MapNavigatorService mapNavigatorService;

    @GetMapping("/")
    public String search(Model model) {
        model.addAttribute("suggestions", mapNavigatorService.getAllStations());
        return "index";
    }

    @GetMapping("/add-route")
    public String addRoute(Model model) {
        model.addAttribute("suggestions", mapNavigatorService.getAllStations());
        return "add-route";
    }
}
