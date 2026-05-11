package br.com.alura.marketplace.application.v1.mapper;

import br.com.alura.marketplace.application.v1.dto.factory.ProdutoDtoFactory;
import br.com.alura.marketplace.domain.entity.assertions.ProdutoAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ProdutoDtoMapperTest {

    ProdutoDtoMapper mapper = Mappers.getMapper(ProdutoDtoMapper.class);

    @DisplayName("Quando converter ProdutoDto.Request")
    @Nested
    class Converter {

        @DisplayName("Então deve executar com sucesso")
        @Nested
        class Sucesso {

            @DisplayName("Dado um ProdutoDto.Request com todos os campos")
            @Test
            void teste1() {

                var dto = ProdutoDtoFactory.criarProdutoDtoRequest().comTodosCampos();

                var atual = mapper.converter(dto);


                ProdutoAssertions.afirmaQue_Produro(atual).foiConvertidoDe_ProdutoDto_Request();
            }
        }

    }

}