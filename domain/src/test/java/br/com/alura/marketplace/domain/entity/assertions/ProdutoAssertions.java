package br.com.alura.marketplace.domain.entity.assertions;

import br.com.alura.marketplace.domain.entity.Produto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import static br.com.alura.marketplace.domain.entity.Produto.Status.AVAILABLE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProdutoAssertions {


    private final Produto atual;


    public static ProdutoAssertions afirmaQue_Produro(Produto atual) {
        return new ProdutoAssertions(atual);
    }

    /**
     * @see br.com.alura.marketplace.application.v1.dto.factory.ProdutoDtoFactory
     * .comTodosCampos
     */
    public void foiConvertidoDe_ProdutoDto_Request() {
        assertThat(atual.getProdutoId()).isNull();
        assertThat(atual.getNome()).isEqualTo("Produto Teste");
        assertThat(atual.getCategoria()).isEqualTo("Categoria 1");
        assertThat(atual.getStatus()).isEqualTo(AVAILABLE);
        assertThat(atual.getDescricao()).isEqualTo("Descrição do Produto Teste");
        assertThat(atual.getValor()).isEqualTo(new BigDecimal("1.99"));
        assertThat(atual.getTags().getFirst()).isEqualTo("tag-1");
        assertThat(atual.getPetStorePetId()).isNull();
        assertThat(atual.getCriadoEm()).isNull();
        assertThat(atual.getAtualizadoEm()).isNull();

        var foto = atual.getFotos().getFirst();
        assertThat(foto.getFotoId()).isNull();
        assertThat(foto.getFileName()).isEqualTo("file-name-1.jpg");
        assertThat(foto.getLink()).isNull();
        assertThat(foto.getBase64()).isEqualTo("Y29udGV1ZG8=");
        assertThat(foto.getCriadoEm()).isNull();
        assertThat(foto.getAtualizadoEm()).isNull();
    }


}