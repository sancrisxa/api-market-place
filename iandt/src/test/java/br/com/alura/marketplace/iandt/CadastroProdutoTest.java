package br.com.alura.marketplace.iandt;

import br.com.alura.marketplace.application.Application;
import br.com.alura.marketplace.application.v1.dto.factory.ProdutoDtoFactory;
import br.com.alura.marketplace.iandt.setup.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.petstore.model.PetDto;
import io.awspring.cloud.s3.S3Template;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = Application.class)
@Testcontainers
public class CadastroProdutoTest  implements LocalStackSetup, WireMockSetup, PostgresSetup, RabbitMQSetup, RedisSetup {

    public final static WireMockServer WIRE_MOCK_SERVER = new WireMockServer(9090);


    @BeforeAll
    static void beforeAll() {
        WIRE_MOCK_SERVER.start();
        WireMock.configureFor("localhost", WIRE_MOCK_SERVER.port());
    }

    @LocalServerPort
    Integer port;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    S3Template s3Template;

    @Value("${aws.s3.bucket.name}")
    String bucketName;

    @BeforeEach
    void beforeEach() {
        RestAssured.baseURI = "http://localhost:" + port + "/api";

        if (s3Template.bucketExists(bucketName)) {
            s3Template.createBucket(bucketName);
        }
    }

    @DisplayName("Quando cadastrar um produto")
    @Nested
    class CadastrarProduto {

        @DisplayName("Então deve cadastrar com sucesso")
        @Nested
        class Sucesso {

            @BeforeEach
            void beforeEach() throws JsonProcessingException {
                var petDto = new PetDto();

                WIRE_MOCK_SERVER.stubFor(post("/petstore/get").willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(objectMapper.writeValueAsString(petDto))));
            }

            @DisplayName("Dado um produto com todos os campos")
            @Test
            void teste1() throws JsonProcessingException {
                var produto = ProdutoDtoFactory.criarProdutoDtoRequest().comTodosCampos();


                var resposta = RestAssured
                        .given()
                        .log()
                        .all()
                        .header("Correlation-Id", "480700dd-1051-44eb-9740-6ad53de322c6")
                        .contentType(ContentType.JSON)
                        .body(objectMapper.writeValueAsString(produto))
                        .post("/v1/produtos")
                        .then()
                        .log()
                        .all()
                        .extract()
                        .response();


                assertThat(resposta.statusCode()).isEqualTo(201);
            }
        }

    }
}
