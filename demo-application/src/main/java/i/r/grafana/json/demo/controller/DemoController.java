package i.r.grafana.json.demo.controller;

import i.r.grafana.json.demo.controller.dto.PingResponse;
import i.r.grafana.json.demo.service.ExternalStubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/external/")
public class DemoController {

    private final ExternalStubService externalService;

    @PostMapping("save")
    public PingResponse save(@RequestParam("objects") List<String> objects) {
        externalService.save(objects);
        return new PingResponse(0L, "success operation");
    }

    @PostMapping("delete")
    public PingResponse delete(@RequestParam("objects") List<String> objects) {
        externalService.delete(objects);
        return new PingResponse(0L, "success operation");
    }

    @PostMapping("fetch")
    public PingResponse fetch(@RequestParam("objects") List<String> objects) {
        externalService.fetch(objects);
        return new PingResponse(0L, "success operation");
    }
}
