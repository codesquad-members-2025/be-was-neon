package template;

import model.User;

/**
 * 템플릿 렌더링을 위한 인터페이스입니다.
 * 이 인터페이스를 구현하는 클래스는 특정 템플릿을 사용자 정보에 따라 렌더링하는 로직을 제공합니다.
 */
public interface TemplateRenderer {
    /**
     * 주어진 템플릿을 사용자 정보에 따라 렌더링합니다.
     *
     * @param user 렌더링에 사용될 사용자 정보. 로그인하지 않은 경우 null이 전달됩니다.
     * @param template 렌더링할 템플릿의 바이트 배열
     * @return 렌더링된 결과의 바이트 배열
     */
    byte[] render(User user, byte[] template);
} 