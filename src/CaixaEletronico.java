public class CaixaEletronico implements ICaixaEletronico {

    private int[][] estoqueCedulas;
    private int cotaMinima;
    private StringBuilder extrato;

    // Construtor
    public CaixaEletronico() {
        this.estoqueCedulas = new int[][] {
            {100, 100}, {50, 200}, {20, 300}, 
            {10, 350}, {5, 450}, {2, 500}
        };
        this.cotaMinima = 0;
        this.extrato = new StringBuilder("=== Extrato de Operações ===\n");
    }

    public StringBuilder getExtrato() { return extrato; }

    @Override
    public String pegaRelatorioCedulas() {
        String relatorio = "--- Relatorio de Cedulas ---\n";
        for (int i = 0; i < estoqueCedulas.length; i++) {
            relatorio = relatorio + "Notas de R$ " + estoqueCedulas[i][0] + ": " + estoqueCedulas[i][1] + " unidades\n";
        }
        return relatorio;
    }

    @Override
    public String pegaValorTotalDisponivel() {
        int total = 0;
        for (int i = 0; i < estoqueCedulas.length; i++) {
            total = total + (estoqueCedulas[i][0] * estoqueCedulas[i][1]);
        }
        return "Valor total disponivel: R$ " + total;
    }

    @Override
    public String reposicaoCedulas(Integer cedula, Integer quantidade) {
        for (int i = 0; i < estoqueCedulas.length; i++) {
            if (estoqueCedulas[i][0] == cedula) {
                estoqueCedulas[i][1] = estoqueCedulas[i][1] + quantidade;
                return "Sucesso: " + quantidade + " notas de R$ " + cedula + " adicionadas.";
            }
        }
        return "Erro: Cédula de R$ " + cedula + " não é válida.";
    }

    @Override
    public String armazenaCotaMinima(Integer minimo) {
        this.cotaMinima = minimo;
        return "Cota mínima atualizada para: R$ " + minimo;
    }

    @Override
    public String sacar(Integer valor) {
        // 1. Verifica se o caixa já está travado pela cota mínima
        int totalNoCaixa = 0;
        for (int i = 0; i < estoqueCedulas.length; i++) {
            totalNoCaixa = totalNoCaixa + (estoqueCedulas[i][0] * estoqueCedulas[i][1]);
        }
        
        // Se após o saque o valor ficar abaixo da cota, bloqueia a operação
        if ((totalNoCaixa - valor) < cotaMinima) {
            return "Caixa Vazio: Chame o Operador";
        }

        // 2. Variáveis para fazer a "simulação" do saque
        int valorRestante = valor;
        int totalNotasUsadas = 0;
        // Vetor temporário para guardar quantas notas vamos tirar de cada linha da matriz
        int[] notasSeparadas = new int[6]; 

        // 3. Lógica para pegar as maiores notas primeiro
        for (int i = 0; i < estoqueCedulas.length; i++) {
            int valorDaNota = estoqueCedulas[i][0];
            int qtdDisponivel = estoqueCedulas[i][1];

            // Se o valor que falta pagar for maior ou igual à nota atual, e tiver nota disponível
            if (valorRestante >= valorDaNota && qtdDisponivel > 0) {
                
                // Descobre quantas notas desse valor precisaria para pagar o restante
                int qtdNecessaria = valorRestante / valorDaNota;
                int qtdParaPegar = 0;

                // Pega o que precisa, ou pega tudo o que tem se não for suficiente
                if (qtdNecessaria <= qtdDisponivel) {
                    qtdParaPegar = qtdNecessaria;
                } else {
                    qtdParaPegar = qtdDisponivel;
                }

                // Atualiza a nossa simulação
                valorRestante = valorRestante - (qtdParaPegar * valorDaNota);
                totalNotasUsadas = totalNotasUsadas + qtdParaPegar;
                notasSeparadas[i] = qtdParaPegar; // Guarda na memória que vamos usar essas notas
            }
        }

        // 4. Validações Finais antes de liberar o dinheiro
        if (valorRestante > 0) {
            return "Saque não realizado por falta de cédulas.";
        }
        
        if (totalNotasUsadas > 30) {
            return "Saque não realizado: limite de 30 cédulas excedido.";
        }

        // 5. Se chegou aqui, o saque deu certo! Vamos descontar da matriz real e montar a mensagem.
        String recibo = "Saque de R$ " + valor + " realizado!\nNotas emitidas:\n";
        
        for (int i = 0; i < estoqueCedulas.length; i++) {
            if (notasSeparadas[i] > 0) {
                // Aqui nós finalmente tiramos as notas da matriz original
                estoqueCedulas[i][1] = estoqueCedulas[i][1] - notasSeparadas[i]; 
                
                // Monta a frase bonitinha para a tela
                recibo = recibo + "- " + notasSeparadas[i] + " nota(s) de R$ " + estoqueCedulas[i][0] + "\n";
            }
        }

        // 6. Guarda o registro no nosso extrato (para mostrar quando clicar em Sair)
        extrato.append("Saque: R$ ").append(valor).append(" | Total de Notas: ").append(totalNotasUsadas).append("\n");

        return recibo;
    }

    public static void main(String arg[]){
        GUI janela = new GUI(CaixaEletronico.class);
        janela.show();
    }
}