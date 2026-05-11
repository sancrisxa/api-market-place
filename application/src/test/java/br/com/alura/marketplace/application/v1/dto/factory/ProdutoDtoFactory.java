package br.com.alura.marketplace.application.v1.dto.factory;

import br.com.alura.marketplace.application.v1.dto.FotoDto;
import br.com.alura.marketplace.application.v1.dto.ProdutoDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import static br.com.alura.marketplace.domain.entity.Produto.Status.AVAILABLE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProdutoDtoFactory {


    public static Request criarProdutoDtoRequest() {
        return new Request(ProdutoDto.Request.builder());
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Request {
        private final ProdutoDto.Request.RequestBuilder builder;

        public ProdutoDto.Request comTodosCampos() {
            return builder
                    .nome("Produto Teste")
                    .categoria("Categoria 1")
                    .status(AVAILABLE)
                    .descricao("Descrição do Produto Teste")
                    .valor(new BigDecimal("1.99"))
                    .foto(FotoDto.Request.builder()
                            .fileName("file-name-1.jpg")
                            .base64("Y29udGV1ZG8=")
                            .build())
                    .tag("tag-1")
                    .build();
        }
    }
}