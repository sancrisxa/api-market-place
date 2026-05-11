package br.com.alura.marketplace.infra.repository;

import br.com.alura.marketplace.domain.entity.Produto;
import br.com.alura.marketplace.infra.config.JpaConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@DataJpaTest
@ContextConfiguration(classes = JpaConfig.class)
class ProdutoRepositoryExtTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ProdutoRepositoryExt produtoRepositoryExt;

    @DisplayName("Quando consultar por nome")
    @Nested
    class FindByNome {

        @DisplayName("Então deve consultar com sucesso")
        @Nested
        class Sucesso {

            @DisplayName("Dado um nome valido, em um cenario onde existe um registro")
            @Test
            void teste1() {

                var produto = Produto.builder().nome("Rodrigo").categoria("Categoria").descricao("Descricao").status(Produto.Status.SOLD).valor(new BigDecimal(1)) .build();

                produtoRepositoryExt.save(produto);

                var nome = "Rodrigo";

                var atual = produtoRepositoryExt.findByNome(nome);

                assertThat(atual).isNotEmpty();
                assertThat(atual.get().getNome()).isEqualTo("Rodrigo");
            }
        }
    }

}