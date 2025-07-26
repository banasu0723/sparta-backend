package hello.auth.dto;

public class ErrorResponse {
    public ErrorDetail error;

    public ErrorResponse() {}

    public ErrorResponse(String code, String message) {
        this.error = new ErrorDetail(code, message);
    }

    public ErrorDetail getError() { return error; }
    public void setError(ErrorDetail error) { this.error = error; }

    public static class ErrorDetail {
        private String code;
        private String message;

        public ErrorDetail() {}

        public ErrorDetail(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
