package auth.filter;


import auth.annotation.RequiereRol;
import auth.service.TokenServices;
import exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Inject
    private TokenServices tokenServices;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Obtener anotación @RequiresRole en método o clase
        Method method = resourceInfo.getResourceMethod();
        RequiereRol annotation = method.getAnnotation(RequiereRol.class);

        if (annotation == null) {
            // Buscar en la clase si no está en el método
            annotation = resourceInfo.getResourceClass().getAnnotation(RequiereRol.class);
        }

        // Si no hay anotación RequiereRol, permitir el acceso sin verificar token
        if (annotation == null) {
            return;
        }

        // Obtener el token del encabezado Authorization
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Token faltante o mal formado");
        }

        String token = authHeader.substring("Bearer".length()).trim();
        Claims claims;
        try {
            claims = tokenServices.parseClaims(token);
        } catch (Exception e) {
            throw new UnauthorizedException("Token inválido");
        }

        // Extraer roles del token
        List<String> roles = claims.get("roles", List.class);

        // Verificar si el usuario tiene los roles requeridos
        String[] requiredRoles = annotation.value();
        boolean autorizado = Arrays.stream(requiredRoles).anyMatch(roles::contains);

        if (!autorizado) {
            throw new ForbiddenException("Acceso denegado: se requiere uno de los roles " + Arrays.toString(requiredRoles));
        }

        // Guardar datos útiles en el contexto si querés reutilizar
        requestContext.setProperty("userId", claims.getSubject());
        requestContext.setProperty("userRoles", roles);
    }
}
