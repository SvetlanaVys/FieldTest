package v1.user;

public class UserLoginResource {
        private String link;
        private String email;
        private String password;

        public UserLoginResource() {
        }

        public UserLoginResource(String link, String email, String password) {
            this.link = link;
            this.email = email;
            this.password = password;
        }

        public UserLoginResource(UserData data, String link) {
            this.link = link;
            this.email = data.email;
            this.password = data.password;
        }

        public String getLink() {
            return link;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
}
