package com.finsafe.dto;

public class AuthDtos {

    public static class LoginRequest {
        private String email;
        private String password;

        public LoginRequest() {}
        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class RegisterRequest {
        private String fullName;
        private String email;
        private String password;
        private Integer age;
        private String occupation;
        private String employmentStatus;

        public RegisterRequest() {}

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        public String getOccupation() { return occupation; }
        public void setOccupation(String occupation) { this.occupation = occupation; }
        public String getEmploymentStatus() { return employmentStatus; }
        public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }
    }

    public static class AuthResponse {
        private String token;
        private Long userId;
        private String email;
        private String fullName;

        public AuthResponse() {}
        public AuthResponse(String token, Long userId, String email, String fullName) {
            this.token = token;
            this.userId = userId;
            this.email = email;
            this.fullName = fullName;
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private String token;
            private Long userId;
            private String email;
            private String fullName;

            public Builder token(String token) { this.token = token; return this; }
            public Builder userId(Long userId) { this.userId = userId; return this; }
            public Builder email(String email) { this.email = email; return this; }
            public Builder fullName(String fullName) { this.fullName = fullName; return this; }
            public AuthResponse build() { return new AuthResponse(token, userId, email, fullName); }
        }
    }
}
