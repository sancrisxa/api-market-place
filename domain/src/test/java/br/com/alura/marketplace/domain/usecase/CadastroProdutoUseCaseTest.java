package br.com.alura.marketplace.domain.usecase;

import br.com.alura.marketplace.domain.entity.Produto;
import br.com.alura.marketplace.domain.exception.BusinessException;
import br.com.alura.marketplace.domain.repository.BucketRepository;
import br.com.alura.marketplace.domain.repository.PetStoreRepository;
import br.com.alura.marketplace.domain.repository.ProdutoRepository;
import br.com.alura.marketplace.domain.repository.QueueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.util.ReflectionTestUtils.setField;


@ExtendWith(MockitoExtension.class)
class CadastroProdutoUseCaseTest {

    @InjectMocks
    CadastroProdutoUseCase cadastroProdutoUseCase;

    @Mock
    ProdutoRepository produtoRepository;

    @Mock
    PetStoreRepository petStoreRepository;

    @Mock
    BucketRepository bucketRepository;

    @Mock
    QueueRepository queueRepository;

    @DisplayName("Quando cadastrar produto")
    @Nested
    class Cadastrar {

        @BeforeEach
        void beforeEach() {
            Mockito.lenient().when(petStoreRepository.cadastrarPet(any())).thenAnswer(invocationOnMock -> {
                Produto produto = invocationOnMock.getArgument(0);
                setField(produto, "petStorePetId", 99L);

                return produto;
            });
        }

        @DisplayName("Então deve executar com sucesso")
        @Nested
        class Sucesso {

            @BeforeEach
            void beforeEach() {
                Mockito.when(produtoRepository.save(any())).thenAnswer(invocationOnMock -> {
                    Produto produto = invocationOnMock.getArgument(0);
                    setField(produto, "produtoId", UUID.fromString("58967910-6379-478a-9351-419086c8f94f"));

                    return produto;
                });
            }

            @DisplayName("Dado um produto com todos os campos")
            @Test
            void teste1() {

                var produto = Produto.builder().nome("Nome 1").build();

                var atual = cadastroProdutoUseCase.cadastrar(produto);

                assertThat(atual.getProdutoId()).isEqualTo(UUID.fromString("58967910-6379-478a-9351-419086c8f94f"));

            }

            @DisplayName("Dado um produto com o cargo igual a ${status}")
            @ParameterizedTest
            @EnumSource(Produto.Status.class)
            void teste2(Produto.Status status) {

                var produto = Produto.builder().nome("Nome 1").build();
                setField(produto, "status", status);

                var atual = cadastroProdutoUseCase.cadastrar(produto);

                assertThat(atual.getStatus()).isEqualTo(status);

            }

            @DisplayName("Dado um produto com o cargo igual a ${status}")
            @ParameterizedTest
            @CsvSource(value = {
                    "AVAILABLE | (Disponível)",
                    "PENDING | (Pendente)",
                    "SOLD | (Vendido)",
            }, delimiterString = "|")
            void teste3(Produto.Status status, String descricaoEsperada) {

                var produto = Produto.builder().nome("Nome 1").descricao("Descricao 1").build();
                setField(produto, "status", status);

                var atual = cadastroProdutoUseCase.cadastrar(produto);

                assertThat(atual.getDescricao()).endsWith(descricaoEsperada);

            }
        }

        @DisplayName("Então deve retornar erro")
        @Nested
        class Falha {
            @DisplayName("Dado um produto com o nome que come com -")
            @Test
            void teste1() {

                var produto = Produto.builder().nome("-Nome 1").build();

                var atual = assertThrows(BusinessException.class, () -> cadastroProdutoUseCase.cadastrar(produto));

                assertThat(atual).hasMessage("O nome não pode começar com -");
            }
        }
    }
}