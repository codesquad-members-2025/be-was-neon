package webserver.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.common.HttpSession;
import webserver.http.exception.HttpException;
import webserver.model.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static webserver.http.common.HttpConstants.LF;
import static webserver.http.response.HttpStatusCode.INTERNAL_SERVER_ERROR;

public class TemplateEngine {

    private static final Logger logger = LoggerFactory.getLogger(TemplateEngine.class);
    private static final String TEMPLATE_DIR = "templates/";
    private static final String SESSION = "session";
    private static final String NOT = "!";

    // {{#if expr}} ... {{/if}}
    private static final Pattern IF_BLOCK =
            Pattern.compile("\\{\\{#if\\s+(.+?)}}([\\s\\S]*?)\\{\\{/if}}");

    // {{ expr }}
    private static final Pattern PLACEHOLDER =
            Pattern.compile("\\{\\{\\s*(.+?)\\s*}}");

    private final String viewName;
    private final Model model;
    private final HttpSession session;

    public TemplateEngine(ModelAndView mav, HttpSession session) {
        this.viewName = mav.getViewName();
        this.model = mav.getModel();
        this.session = session;
    }

    public String render() {
        logger.debug("Rendering template {}", viewName);
        String tpl = loadTemplate(viewName + ".html");
        tpl = processIfBlocks(tpl);

        return processPlaceholders(tpl);
    }

    // 클래스패스(resources/templates/)에서 한 줄씩 읽어 옴
    private String loadTemplate(String filename) {
        InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream(TEMPLATE_DIR + filename);

        if (is == null) {
            throw new HttpException(INTERNAL_SERVER_ERROR);
        }
        try (var rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line).append(LF);
            }

            return sb.toString();
        } catch (IOException e) {
            throw new HttpException(INTERNAL_SERVER_ERROR);
        }
    }

    // {{#if expr}} … {{/if}} 처리
    private String processIfBlocks(String src) {
        Matcher m = IF_BLOCK.matcher(src);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            String expr = m.group(1).trim(); // e.g. "!session.user" or "user"
            String body = m.group(2);
            Object val = evaluate(expr);
            boolean keep = val instanceof Boolean
                    ? (Boolean) val
                    : (val != null && truthy(val));
            m.appendReplacement(sb, Matcher.quoteReplacement(keep ? body : ""));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    // {{ expr }} 치환
    private String processPlaceholders(String src) {
        Matcher m = PLACEHOLDER.matcher(src);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            String expr = m.group(1).trim(); // e.g. "user.name"
            Object val = evaluate(expr);
            String rep = val == null ? "" : val.toString();
            m.appendReplacement(sb, Matcher.quoteReplacement(rep));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * • !expr 부정을 지원
     * • 그 외엔 resolveExpression 결과를 Boolean/Null 판정
     */
    private Object evaluate(String expr) {
        boolean negate = expr.startsWith(NOT);
        if (negate) expr = expr.substring(1).trim();

        Object raw = resolveExpression(expr);
        if (negate) {
            // 부정: raw 가 truthy 이면 false, 아니면 true
            return !(raw instanceof Boolean
                    ? (Boolean) raw
                    : (raw != null && truthy(raw)));
        }
        return raw;
    }

    private Object resolveExpression(String expr) {
        String[] parts = expr.split("\\.");
        Object cur;

        // 첫 번째 토큰
        if (SESSION.equals(parts[0])) {
            cur = session;
        } else {
            cur = model.getAttribute(parts[0]);
        }

        // 나머지 토큰들
        for (int i = 1; cur != null && i < parts.length; i++) {
            String prop = parts[i];

            if (cur instanceof HttpSession hs) {
                // 세션 객체라면 getAttribute 로 꺼낸다
                cur = hs.getAttribute(prop);
            } else {
                // 일반 객체 → getter 리플렉션
                cur = invokeGetter(cur, prop);
            }
        }

        return cur;
    }

    // JavaBean getter 호출 (getXxx or isXxx)
    private Object invokeGetter(Object obj, String prop) {
        String cap = prop.substring(0, 1).toUpperCase() + prop.substring(1);
        String[] methods = {"get" + cap, "is" + cap};
        for (String name : methods) {
            try {
                Method m = obj.getClass().getMethod(name);
                return m.invoke(obj);
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                throw new HttpException(INTERNAL_SERVER_ERROR);
            }
        }

        return null;
    }

    private boolean truthy(Object o) {
        if (o instanceof String s) {
            return !s.isBlank();
        }

        return true;
    }

}
