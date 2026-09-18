package infrastructure.fixtures;

import api.clients.GraphQLClient;
import api.clients.BookerClient;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class ApiFixtureExtension implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        Class<?> paramType = parameterContext.getParameter().getType();
        return paramType.equals(GraphQLClient.class) || paramType.equals(BookerClient.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        Class<?> paramType = parameterContext.getParameter().getType();

        if (paramType.equals(GraphQLClient.class)) {
            return new GraphQLClient();
        } else if (paramType.equals(BookerClient.class)) {
            return new BookerClient();
        }

        throw new ParameterResolutionException("Unsupported API Client: " + paramType.getName());
    }
}
