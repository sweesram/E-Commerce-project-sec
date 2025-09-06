package com.cart.ecom_proj.util;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class InputValidationUtil {

    // SQL Injection prevention patterns
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
        "(?i).*('|(\\-\\-)|(;)|(\\|)|(\\*)|(%)|(\\+)|(\\=)|(\\<)|(\\>)|(\\()|(\\))|(\\[)|(\\])|(\\{)|(\\})|(\\,)|(\\\\)|(\\/)|(\\?)|(\\&)|(\\^)|(\\$)|(\\#)|(\\@)|(\\!)|(\\~)|(\\`)).*"
    );

    // XSS prevention patterns
    private static final Pattern XSS_PATTERN = Pattern.compile(
        "(?i).*(<script|</script|javascript:|onload=|onerror=|onclick=|onmouseover=|onfocus=|onblur=|onchange=|onsubmit=|onreset=|onselect=|onkeydown=|onkeyup=|onkeypress=).*"
    );

    // Path traversal prevention
    private static final Pattern PATH_TRAVERSAL_PATTERN = Pattern.compile(
        "(?i).*(\\.\\.|%2e%2e|%2E%2E|%c0%ae%c0%ae|%c1%9c%c1%9c).*"
    );

    public boolean isInputSafe(String input) {
        if (input == null || input.trim().isEmpty()) {
            return true;
        }
        
        return !containsSQLInjection(input) && 
               !containsXSS(input) && 
               !containsPathTraversal(input);
    }

    public boolean containsSQLInjection(String input) {
        return SQL_INJECTION_PATTERN.matcher(input).matches();
    }

    public boolean containsXSS(String input) {
        return XSS_PATTERN.matcher(input).matches();
    }

    public boolean containsPathTraversal(String input) {
        return PATH_TRAVERSAL_PATTERN.matcher(input).matches();
    }

    public String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        
        // Remove potentially dangerous characters
        return input.replaceAll("[<>\"'&]", "")
                   .replaceAll("(?i)javascript:", "")
                   .replaceAll("(?i)on\\w+\\s*=", "")
                   .trim();
    }
}
