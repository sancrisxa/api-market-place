package br.com.alura.marketplace.iandt.setup;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

public interface WireMockSetup {

    WireMockServer WIRE_MOCK_SERVER = new WireMockServer(9090);


    @BeforeAll
    static void WiewMockBeforeAll() {
        WIRE_MOCK_SERVER.start();
        WireMock.configureFor("localhost", WIRE_MOCK_SERVER.port());
    }

    @AfterEach
    default void wireMockAfterEach() {
        WIRE_MOCK_SERVER.resetAll();
    }
}
