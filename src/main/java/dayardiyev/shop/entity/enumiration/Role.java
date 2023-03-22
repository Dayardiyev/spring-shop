package dayardiyev.shop.entity.enumiration;

public enum Role {
    ADMIN("admin", "Администратор"),
    MODERATOR("moderator", "Модератор"),
    USER("user", "Пользователь");
    private final String serviceName;

    Role(String serviceName, String displayName) {
       this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}
