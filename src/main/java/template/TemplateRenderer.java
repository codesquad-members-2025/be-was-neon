package template;

import model.User;

public interface TemplateRenderer {
    byte[] render(User user, byte[] template);
} 