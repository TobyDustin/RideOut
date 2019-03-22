package org.io.rideout.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

@Path("documentation")
public class DocumentationResource {

    public static final String JSON = "json";
    public static final String YAML = "yaml";


    @GET
    @Produces({MediaType.APPLICATION_JSON, "application/yaml"})
    @Operation(
            summary = "Get OpenAPI documentation",
            tags = {"documentation"},
            description = "Returns OpenAPI documentation in selected format",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OpenAPI documentation", content = @Content(
                            schema = @Schema(type = "string")
                    ))
            }
    )
    public Response getDocs(@Parameter(
            description = "Specifies what format to return",
            example = JSON,
            schema = @Schema(type = "string", allowableValues = {JSON, YAML})
    ) @QueryParam("format") @DefaultValue(JSON) String format) {
        format = format.toLowerCase();
        InputStream input = null;
        if (!format.equals(JSON) && !format.equals(YAML)) throw new BadRequestException();
        String fileName = "docs/openapi." + format;

        input = getClass().getClassLoader().getResourceAsStream(fileName);
        if (input == null) throw new InternalServerErrorException();

        String docs;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, Charset.defaultCharset()))) {
            docs = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        }

        return Response.ok(docs, format.equals(JSON) ? "application/json" : "application/yaml").build();
    }
}
