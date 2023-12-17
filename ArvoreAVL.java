package Arvores;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

class NoAVL {
    int dado;
    NoAVL esquerda;
    NoAVL direita;
    int altura;

    public NoAVL(int dado) {
        this.dado = dado;
        this.altura = 1;
        this.esquerda = null;
        this.direita = null;
    }
}

public class ArvoreAVL {
    private NoAVL raiz;

    public ArvoreAVL() {
        this.raiz = null;
    }

    private int altura(NoAVL no) {
        if (no == null) {
            return 0;
        }
        return no.altura;
    }

    private int maximo(int a, int b) {
        return (a > b) ? a : b;
    }

    private int calcularBalanceamento(NoAVL no) {
        if (no == null) {
            return 0;
        }
        return altura(no.esquerda) - altura(no.direita);
    }

    private NoAVL rotacaoDireita(NoAVL y) {
        NoAVL x = y.esquerda;
        NoAVL T2 = x.direita;

        x.direita = y;
        y.esquerda = T2;

        y.altura = maximo(altura(y.esquerda), altura(y.direita)) + 1;
        x.altura = maximo(altura(x.esquerda), altura(x.direita)) + 1;

        return x;
    }

    private NoAVL rotacaoEsquerda(NoAVL x) {
        NoAVL y = x.direita;
        NoAVL T2 = y.esquerda;

        y.esquerda = x;
        x.direita = T2;

        x.altura = maximo(altura(x.esquerda), altura(x.direita)) + 1;
        y.altura = maximo(altura(y.esquerda), altura(y.direita)) + 1;

        return y;
    }

    public void inserir(int chave) {
        raiz = inserirRec(raiz, chave);
    }

    private NoAVL inserirRec(NoAVL no, int chave) {
        if (no == null) {
            return new NoAVL(chave);
        }

        if (chave < no.dado) {
            no.esquerda = inserirRec(no.esquerda, chave);
        } else if (chave > no.dado) {
            no.direita = inserirRec(no.direita, chave);
        } else {
            return no; // Duplicatas não são permitidas
        }

        no.altura = 1 + maximo(altura(no.esquerda), altura(no.direita));

        int balanceamento = calcularBalanceamento(no);

        // Casos de desequilíbrio
        // Esquerda-Esquerda
        if (balanceamento > 1 && chave < no.esquerda.dado) {
            return rotacaoDireita(no);
        }
        // Direita-Direita
        if (balanceamento < -1 && chave > no.direita.dado) {
            return rotacaoEsquerda(no);
        }
        // Esquerda-Direita
        if (balanceamento > 1 && chave > no.esquerda.dado) {
            no.esquerda = rotacaoEsquerda(no.esquerda);
            return rotacaoDireita(no);
        }
        // Direita-Esquerda
        if (balanceamento < -1 && chave < no.direita.dado) {
            no.direita = rotacaoDireita(no.direita);
            return rotacaoEsquerda(no);
        }

        return no;
    }

    public void lerEInserirDoArquivo(String nomeArquivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                int numero = Integer.parseInt(linha);
                inserir(numero);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void realizarOperacoesAleatorias(int quantidadeOperacoes) {
        Random random = new Random();

        for (int i = 0; i < quantidadeOperacoes; i++) {
            int numeroSorteado = random.nextInt(19999) - 9999;

            if (numeroSorteado % 3 == 0) {
                inserir(numeroSorteado);
                System.out.println("Inserido na árvore: " + numeroSorteado);
            }
            int quantidade = contar(numeroSorteado);
            System.out.println("Número de vezes que " + numeroSorteado + " aparece na árvore: " + quantidade);
        }
    }

    private int contar(int chave) {
        return contarRecursivo(raiz, chave);
    }

    private int contarRecursivo(NoAVL no, int chave) {
        if (no == null) {
            return 0;
        }

        int quantidade = 0;
        if (chave < no.dado) {
            quantidade += contarRecursivo(no.esquerda, chave);
        } else if (chave > no.dado) {
            quantidade += contarRecursivo(no.direita, chave);
        } else {
            quantidade = 1;
        }

        return quantidade;
    }

    public static void main(String[] args) {
        ArvoreAVL arvore = new ArvoreAVL();

        long inicio = System.currentTimeMillis();

        arvore.lerEInserirDoArquivo("C:/Users/richa/Downloads/dados100_mil_definitive.txt");

        arvore.realizarOperacoesAleatorias(50000);

        long fim = System.currentTimeMillis();
        long tempoTotal = fim - inicio;

        System.out.println("Árvore AVL criada em " + tempoTotal + " milissegundos.");
    }
}
