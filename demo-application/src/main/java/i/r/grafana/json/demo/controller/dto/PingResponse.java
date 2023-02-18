package i.r.grafana.json.demo.controller.dto;

public class PingResponse {

    private final long code;

    private final String message;

    public PingResponse(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
