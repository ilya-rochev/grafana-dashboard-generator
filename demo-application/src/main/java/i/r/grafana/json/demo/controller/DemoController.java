package i.r.grafana.json.demo.controller;

import i.r.grafana.json.demo.controller.dto.PingResponse;
import i.r.grafana.json.demo.service.ExternalCounterServiceStub;
import i.r.grafana.json.demo.service.ExternalTimerServiceStub;
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

    private final ExternalTimerServiceStub externalTimerService;
    private final ExternalCounterServiceStub externalCounterService;

    @PostMapping("save")
    public PingResponse save(@RequestParam("objects") List<String> objects) {
        externalTimerService.save();
        externalCounterService.save(objects);
        return new PingResponse(0L, "success operation");
    }

    @PostMapping("delete")
    public PingResponse delete(@RequestParam("objects") List<String> objects) {
        externalTimerService.delete();
        externalCounterService.delete(objects);
        return new PingResponse(0L, "success operation");
    }

    @PostMapping("fetch")
    public PingResponse fetch(@RequestParam("objects") List<String> objects) {
        externalTimerService.fetch();
        externalCounterService.fetch(objects);
        return new PingResponse(0L, "success operation");
    }
}
