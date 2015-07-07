package org.asuki.model.jackson;

import static java.lang.System.err;
import static java.lang.System.out;

import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;

public class JsonSchemaGeneratorTest {

    private static ObjectMapper createJaxbObjectMapper() {

        // use JAXB annotations
        final AnnotationIntrospector introspector = new JaxbAnnotationIntrospector(
                TypeFactory.defaultInstance());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.getDeserializationConfig().with(introspector);
        mapper.getSerializationConfig().with(introspector);

        return mapper;
    }

    private static void printWithDatabindJsonSchema(
            String fullyQualifiedClassName) {

        final ObjectMapper mapper = createJaxbObjectMapper();

        try {
            com.fasterxml.jackson.databind.jsonschema.JsonSchema jsonSchema = mapper
                    .generateJsonSchema(Class.forName(fullyQualifiedClassName));

            out.println(mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(jsonSchema));
        } catch (ClassNotFoundException e) {
            err.println("Unable to find class " + fullyQualifiedClassName);
        } catch (JsonProcessingException e) {
            err.println("Unable to map JSON: " + e);
        }
    }

    private static void printWithModuleJsonSchema(String fullyQualifiedClassName) {

        final SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
        final ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.acceptJsonFormatVisitor(mapper.constructType(Class
                    .forName(fullyQualifiedClassName)), visitor);

            com.fasterxml.jackson.module.jsonSchema.JsonSchema jsonSchema = visitor
                    .finalSchema();

            out.println(mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(jsonSchema));
        } catch (ClassNotFoundException e) {
            err.println("Unable to find class " + fullyQualifiedClassName);
        } catch (JsonProcessingException e) {
            err.println("Unable to process JSON: " + e);
        }
    }

    @Test
    public void shouldGenerateJsonSchemaFromJaxbAnnotions() {
        final String fullyQualifiedClassName = "org.asuki.model.jackson.generated.Food";

        printWithDatabindJsonSchema(fullyQualifiedClassName);
        printWithModuleJsonSchema(fullyQualifiedClassName);
    }
}
