package io.muserver.rest;

import io.muserver.MuServer;
import io.muserver.UploadedFile;
import io.muserver.openapi.InfoObjectBuilder;
import io.muserver.openapi.LicenseObjectBuilder;
import io.muserver.openapi.OpenAPIObjectBuilder;
import org.example.petstore.resource.PetResource;
import org.example.petstore.resource.PetStoreResource;
import org.example.petstore.resource.UserResource;
import org.example.petstore.resource.VehicleResource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import scaffolding.ServerUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import static io.muserver.openapi.ExternalDocumentationObjectBuilder.externalDocumentationObject;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static scaffolding.ClientUtils.call;
import static scaffolding.ClientUtils.request;

public class OpenApiDocumentorTest {

    @Path("/uploads")
    static class FileUploadResource {
        @POST
        @Consumes(javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA)
        public void create(@FormParam("images") List<UploadedFile> images,
                           @FormParam("oneThing") UploadedFile oneThing,
                           @FormParam("requiredThing") @Required UploadedFile requiredThing) {
        }
    }

    private final MuServer server = ServerUtils.httpsServerForTest()
        .addHandler(RestHandlerBuilder.restHandler(
            new PetResource(), new PetStoreResource(), new UserResource(), new VehicleResource(), new FileUploadResource()
            ).withOpenApiDocument(OpenAPIObjectBuilder.openAPIObject()
                .withInfo(InfoObjectBuilder.infoObject()
                    .withTitle("Mu Server Sample API")
                    .withVersion("1.0")
                    .withLicense(LicenseObjectBuilder.Apache2_0())
                    .withDescription("This is the **description**\n\nWhich is markdown")
                    .withTermsOfService(URI.create("http://swagger.io/terms/"))
                    .build())
                .withExternalDocs(externalDocumentationObject()
                    .withDescription("The swagger version of this API")
                    .withUrl(URI.create("http://petstore.swagger.io"))
                    .build()))
                .withOpenApiJsonUrl("/openapi.json")
                .withOpenApiHtmlUrl("/api.html")
        )
        .start();


    @Test
    public void hasJsonEndpoint() throws IOException {
        try (okhttp3.Response resp = call(request().url(server.uri().resolve("/openapi.json").toString()))) {
            assertThat(resp.code(), is(200));
            assertThat(resp.header("Content-Type"), equalTo("application/json"));
            String responseBody = resp.body().string();
            JSONObject json = new JSONObject(responseBody);
            JSONObject paths = json.getJSONObject("paths");

            JSONObject pet = paths.getJSONObject("/pet");
            assertThat(pet.keySet(), containsInAnyOrder("put", "post"));

            JSONObject download = paths.getJSONObject("/pet/{petId}/download").getJSONObject("get");
            assertThat(download.getBoolean("deprecated"), is(false));
            JSONArray downloadParams = download.getJSONArray("parameters");
            assertThat(downloadParams.length(), is(1));
            JSONObject downloadParam1 = downloadParams.getJSONObject(0);
            assertThat(downloadParam1.getString("name"), equalTo("petId"));
            assertThat(downloadParam1.getString("description"), equalTo("ID of pet that needs to be fetched"));


            JSONObject findByTags = paths.getJSONObject("/pet/findByTags").getJSONObject("get");
            assertThat(findByTags.getBoolean("deprecated"), is(true));
            JSONObject findByTagsParam = findByTags.getJSONArray("parameters").getJSONObject(1);
            assertThat(findByTagsParam.getString("name"), equalTo("tags"));
            assertThat(findByTagsParam.getString("in"), equalTo("query"));
            assertThat(findByTagsParam.getJSONObject("schema").getString("type"), equalTo("string"));


            JSONObject post = paths.getJSONObject("/pet/{petId}")
                .getJSONObject("post");

            JSONArray parameters = post.getJSONArray("parameters");
            assertThat(parameters.length(), is(1));
            JSONObject pathParam = parameters.getJSONObject(0);
            assertThat(pathParam.getString("name"), is("petId"));
            assertThat(pathParam.getBoolean("required"), is(true));
            JSONObject pathParamSchema = pathParam.getJSONObject("schema");
            assertThat(pathParamSchema.has("default"), is(false));
            assertThat(pathParamSchema.getBoolean("nullable"), is(false));
            assertThat(pathParamSchema.getString("format"), is("int64"));
            assertThat(pathParamSchema.getString("type"), is("integer"));

            JSONObject updateByFormData = post
                .getJSONObject("requestBody")
                .getJSONObject("content")
                .getJSONObject("application/x-www-form-urlencoded")
                .getJSONObject("schema");
            assertThat(updateByFormData.has("deprecated"), is(false));
            assertThat(updateByFormData.has("default"), is(false));

            JSONObject updateByFormDataName = updateByFormData.getJSONObject("properties")
                .getJSONObject("name");

            assertThat(updateByFormDataName.getString("type"), is("string"));
            assertThat(updateByFormDataName.getString("description"), is("Updated name of the pet - More details about that"));

        }
    }

    @Test
    public void canGenerateHtml() throws IOException {
        try (okhttp3.Response resp = call(request().url(server.uri().resolve("/api.html").toString()))) {
            assertThat(resp.code(), is(200));
            assertThat(resp.header("Content-Type"), equalTo("text/html;charset=utf-8"));
            String responseBody = resp.body().string();
            File outputFile = new File("target/openapi.html");
            System.out.println("Creating " + outputFile.getCanonicalPath() + " which is the sample API documentation for your viewing pleasure.");
            try (FileWriter fw = new FileWriter(outputFile)) {
                fw.write(responseBody);
            }
        }

    }

    @Test
    public void fileUploadsAreCorrect() throws IOException {
        try (okhttp3.Response resp = call(request().url(server.uri().resolve("/openapi.json").toString()))) {
            assertThat(resp.code(), is(200));
            assertThat(resp.header("Content-Type"), equalTo("application/json"));
            String responseBody = resp.body().string();
            JSONObject json = new JSONObject(responseBody);

            JSONObject params = json.getJSONObject("paths")
                .getJSONObject("/uploads")
                .getJSONObject("post")
                .getJSONObject("requestBody")
                .getJSONObject("content")
                .getJSONObject("multipart/form-data")
                .getJSONObject("schema")
                .getJSONObject("properties");

            JSONObject oneThing = params.getJSONObject("oneThing");
            assertThat(oneThing.getString("type"), is("string"));
            assertThat(oneThing.getString("format"), is("binary"));

            JSONObject images = params.getJSONObject("images");
            assertThat(images.getString("type"), is("array"));
            JSONObject items = images.getJSONObject("items");
            assertThat(items.getString("type"), is("string"));
            assertThat(items.getString("format"), is("binary"));
        }
    }

}
