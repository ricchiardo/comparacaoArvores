package Arvores;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

class No {
    int dado;
    No pai;
    No esquerda;
    No direita;
    int cor;
}

public class ArvoreRubroNegra {
    private No raiz;
    private No TNULL;

    public ArvoreRubroNegra() {
        TNULL = new No();
        TNULL.cor = 1;
        raiz = TNULL;
        raiz.pai = TNULL;
    }

    public void inserir(int chave) {
        No no = new No();
        no.dado = chave;
        no.pai = null;
        no.esquerda = TNULL;
        no.direita = TNULL;
        no.cor = 0;

        No y = null;
        No x = this.raiz;

        while (x != TNULL) {
            y = x;
            if (no.dado < x.dado) {
                x = x.esquerda;
            } else {
                x = x.direita;
            }
        }

        no.pai = y;
        if (y == null) {
            raiz = no;
        } else if (no.dado < y.dado) {
            y.esquerda = no;
        } else {
            y.direita = no;
        }

        if (no.pai == null) {
            no.cor = 1;
            return;
        }

        if (no.pai.pai == null) {
            return;
        }

        corrigirInsercao(no);
    }

    private void rotacaoEsquerda(No x) {
        No y = x.direita;
        x.direita = y.esquerda;
        if (y.esquerda != TNULL) {
            y.esquerda.pai = x;
        }
        y.pai = x.pai;
        if (x.pai == null) {
            this.raiz = y;
        } else if (x == x.pai.esquerda) {
            x.pai.esquerda = y;
        } else {
            x.pai.direita = y;
        }
        y.esquerda = x;
        x.pai = y;
    }

    private void rotacaoDireita(No x) {
        No y = x.esquerda;
        x.esquerda = y.direita;
        if (y.direita != TNULL) {
            y.direita.pai = x;
        }
        y.pai = x.pai;
        if (x.pai == null) {
            this.raiz = y;
        } else if (x == x.pai.direita) {
            x.pai.direita = y;
        } else {
            x.pai.esquerda = y;
        }
        y.direita = x;
        x.pai = y;
    }

    private void corrigirInsercao(No k) {
        No u;
        while (k.pai.cor == 0) {
            if (k.pai == k.pai.pai.direita) {
                u = k.pai.pai.esquerda;
                if (u.cor == 0) {
                    u.cor = 1;
                    k.pai.cor = 1;
                    k.pai.pai.cor = 0;
                    k = k.pai.pai;
                } else {
                    if (k == k.pai.esquerda) {
                        k = k.pai;
                        rotacaoDireita(k);
                    }
                    k.pai.cor = 1;
                    k.pai.pai.cor = 0;
                    rotacaoEsquerda(k.pai.pai);
                }
            } else {
                u = k.pai.pai.direita;

                if (u.cor == 0) {
                    u.cor = 1;
                    k.pai.cor = 1;
                    k.pai.pai.cor = 0;
                    k = k.pai.pai;
                } else {
                    if (k == k.pai.direita) {
                        k = k.pai;
                        rotacaoEsquerda(k);
                    }
                    k.pai.cor = 1;
                    k.pai.pai.cor = 0;
                    rotacaoDireita(k.pai.pai);
                }
            }
            if (k == raiz) {
                break;
            }
        }
        raiz.cor = 1;
    }

    private int contar(int chave) {
        return contarRecursivo(raiz, chave);
    }

    private int contarRecursivo(No no, int chave) {
        if (no == TNULL) {
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

    private void remover(int chave) {
        No no = buscarNo(raiz, chave);
        if (no != TNULL) {
            removerNo(no);
        }
    }

    private No buscarNo(No no, int chave) {
        while (no != TNULL && chave != no.dado) {
            if (chave < no.dado) {
                no = no.esquerda;
            } else {
                no = no.direita;
            }
        }
        return no;
    }

    private void removerNo(No no) {
        No y = no;
        No x;
        int corOriginalY = y.cor;

        if (no.esquerda == TNULL) {
            x = no.direita;
            transplantar(no, no.direita);
        } else if (no.direita == TNULL) {
            x = no.esquerda;
            transplantar(no, no.esquerda);
        } else {
            y = minimo(no.direita);
            corOriginalY = y.cor;
            x = y.direita;
            if (y.pai == no) {
                x.pai = y;
            } else {
                transplantar(y, y.direita);
                y.direita = no.direita;
                y.direita.pai = y;
            }
            transplantar(no, y);
            y.esquerda = no.esquerda;
            y.esquerda.pai = y;
            y.cor = no.cor;
        }

        if (corOriginalY == 1) {
            corrigirRemocao(x);
        }
    }

    private No minimo(No no) {
        while (no.esquerda != TNULL) {
            no = no.esquerda;
        }
        return no;
    }

    private void transplantar(No u, No v) {
        if (u.pai == null) {
            raiz = v;
        } else if (u == u.pai.esquerda) {
            u.pai.esquerda = v;
        } else {
            u.pai.direita = v;
        }
        v.pai = u.pai;
    }

    private void corrigirRemocao(No x) {
        while (x != raiz && x.cor == 1) {
            if (x == x.pai.esquerda) {
                No w = x.pai.direita;
                if (w.cor == 0) {
                    w.cor = 1;
                    x.pai.cor = 0;
                    rotacaoEsquerda(x.pai);
                    w = x.pai.direita;
                }
                if (w.esquerda.cor == 1 && w.direita.cor == 1) {
                    w.cor = 0;
                    x = x.pai;
                } else {
                    if (w.direita.cor == 1) {
                        w.esquerda.cor = 1;
                        w.cor = 0;
                        rotacaoDireita(w);
                        w = x.pai.direita;
                    }
                    w.cor = x.pai.cor;
                    x.pai.cor = 1;
                    w.direita.cor = 1;
                    rotacaoEsquerda(x.pai);
                    x = raiz;
                }
            } else {
                No w = x.pai.esquerda;
                if (w.cor == 0) {
                    w.cor = 1;
                    x.pai.cor = 0;
                    rotacaoDireita(x.pai);
                    w = x.pai.esquerda;
                }
                if (w.direita.cor == 1 && w.esquerda.cor == 1) {
                    w.cor = 0;
                    x = x.pai;
                } else {
                    if (w.esquerda.cor == 1) {
                        w.direita.cor = 1;
                        w.cor = 0;
                        rotacaoEsquerda(w);
                        w = x.pai.esquerda;
                    }
                    w.cor = x.pai.cor;
                    x.pai.cor = 1;
                    w.esquerda.cor = 1;
                    rotacaoDireita(x.pai);
                    x = raiz;
                }
            }
        }
        x.cor = 1;
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
            } else if (numeroSorteado % 5 == 0) {
                remover(numeroSorteado);
                System.out.println("Removido da árvore: " + numeroSorteado);
            } else {
                int quantidade = contar(numeroSorteado);
                System.out.println("Número de vezes que " + numeroSorteado + " aparece na árvore: " + quantidade);
            }
        }
    }

    public static void main(String[] args) {
        ArvoreRubroNegra arvore = new ArvoreRubroNegra();

        long inicio = System.currentTimeMillis();

        arvore.lerEInserirDoArquivo("C:/Users/richa/Downloads/dados100_mil_definitive.txt");

        arvore.realizarOperacoesAleatorias(50000);

        long fim = System.currentTimeMillis();
        long tempoTotal = fim - inicio;

        System.out.println("Árvore rubro-negra criada em " + tempoTotal + " milissegundos.");
    }
}
