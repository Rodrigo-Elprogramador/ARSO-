package pasarela.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class JwtValidationFilter extends ZuulFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        String uri = RequestContext.getCurrentContext().getRequest().getRequestURI();
        return !uri.startsWith("/auth/")
                && !uri.startsWith("/oauth2/")
                && !uri.startsWith("/login")
                && !uri.startsWith("/error");
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        String token = getToken(context.getRequest());

        if (token != null) {
            try {
                Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token);
            } catch (Exception e) {
                context.setSendZuulResponse(false);
                context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
                context.setResponseBody("Token JWT no valido");
            }
        }

        return null;
    }

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring("Bearer ".length()).trim();
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
