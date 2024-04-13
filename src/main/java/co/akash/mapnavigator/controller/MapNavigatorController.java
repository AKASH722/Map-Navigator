package co.akash.mapnavigator.controller;

import co.akash.mapnavigator.dto.RouteRequest;
import co.akash.mapnavigator.dto.common.CommonResponse;
import co.akash.mapnavigator.service.MapNavigatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MapNavigatorController {
    private final MapNavigatorService mapNavigatorService;

    @GetMapping("/search")
    public ResponseEntity<?> search(
        @RequestParam(name = "source") String source,
        @RequestParam(name = "destination") String destination
    ) {
        CommonResponse<?> commonResponse = mapNavigatorService.getShortestRoute(source, destination);
        if (commonResponse.getHasException()) {
            return ResponseEntity.internalServerError().body(commonResponse.getErrorResponse());
        }
        if (!commonResponse.getIsSuccess()) {
            return ResponseEntity.badRequest().body(commonResponse.getErrorResponse());
        }
        return ResponseEntity.ok().body(commonResponse.getResult());
    }

    @PostMapping("/add-route")
    public ResponseEntity<?> addRoute(@RequestBody RouteRequest routeRequest) {
        CommonResponse<?> commonResponse = mapNavigatorService.addRoute(routeRequest);
        if (commonResponse.getHasException()) {
            return ResponseEntity.internalServerError().body(commonResponse.getErrorResponse());
        }
        if (!commonResponse.getIsSuccess()) {
            return ResponseEntity.badRequest().body(commonResponse.getErrorResponse());
        }
        return ResponseEntity.ok().body(commonResponse.getResult());
    }
}
