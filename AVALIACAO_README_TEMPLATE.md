# Checkpoint 5 — Bug Hunt PetFiap

> Copie este arquivo para a raiz do seu repositório com o nome **README.md**
> e preencha todas as seções.

## Identificação

**Grupo:** ___

| Integrante | RM     | Turma |
|------------|--------|-------|
| Fernando   | 564297 | 2CCPX |
| Patrick    | 562970 | 2CCPX |
| Pedro      | 565090 | 2CCPX |
| Pietro     | 564345 | 2CCPX |
| Ryan       | 565102 | 2CCPX |
| Samir      | 561562 | 2CCPX |

| Campo | |
|---|---|
| **Total de bugs corrigidos** | ___ / 12 |
| **Total de ajustes de Clean Code** | ___ / 6 |
| **Total de testes novos escritos** | ___ / 6 |
| **Suíte final (Run As → JUnit Test)** | ___ testes, ___ falhas |

---

## Parte 1 — Bugs encontrados

> Uma linha por bug, na ordem em que você os encontrou. Use a numeração dos seus
> commits (`fix: bug01 ...`). Preencha TODAS as colunas — metade da nota está aqui.

| # | Sintoma observado (o que fiz/vi)                                                                                                                                                                                                                                                                   | Causa raiz (arquivo e linha aproximada)                                                                                                                                                                                                                        | Correção aplicada                                                                                                                                                               | Conceito da disciplina |
|---|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------|
| bug01 | GeradorProtocoloTest.deveManterUmaUnicaInstancia falhou no assertSame (duas instâncias diferentes, @1f7076bc e @71904469) e deveGerarProtocolosSequenciais falhou com expected: <2> but was: <1>. O console imprimiu "GeradorProtocolo criado!" a cada chamada.                                    | eradorProtocolo.java, método getInstancia() (~linha 19): o if (instancia == null) retornava new GeradorProtocolo() sem atribuir o resultado ao campo estático instancia, que ficava sempre null. Cada chamada criava um objeto novo com o contador zerado.     | passei a atribuir a instância ao campo antes de retornar (instancia = new GeradorProtocolo();), e o método sempre devolve o mesmo objeto guardado.                              | Padrão Singleton (Aula 14): construtor privado e atributo estático guardando a instância única (lazy initialization).                       |
| bug02 | AtendimentoFactoryTest.deveCriarTosaQuandoTipoForTosa falhou no assertInstanceOf: Expected: class Tosa, Actual: class Banho. Ao pedir uma Tosa, a factory devolvia um Banho.                                                                                                                       | AtendimentoFactory.java, método criar (~linha 17): o case "TOSA" instanciava new Banho(...), provavelmente por cópia e cola do case "BANHO".                                                                                                                   | troquei por new Tosa(...) no case "TOSA", mantendo os mesmos parâmetros.                                                                                                        | Padrão Factory (Aula 14) e polimorfismo. A factory é o único lugar que conhece as subclasses concretas, então um erro ali devolve o tipo errado para todo o sistema, sem erro de compilação.                       |
| bug03 | AtendimentoFactoryTest.devePreencherOsDadosDoPetNaConsulta falhou com Expected: Mimi, Actual: null. Uma consulta criada pela factory ficava com os dados do pet vazios.                                                                                                                            | ConsultaVeterinaria.java, construtor de 5 parâmetros (~linha 17): chamava super() sem argumentos, então o construtor vazio de Atendimento rodava e protocolo, pet, porte, tutor, data e status não eram preenchidos. Os parâmetros recebidos eram descartados. | troquei super() por super(protocolo, petNome, petPorte, tutorNome, dataHora), igual ao que Banho e Tosa já faziam.                                                              | Herança e construtores (POO): o construtor da subclasse precisa chamar o construtor correto da superclasse com super(...). Sem isso, o Java chama implicitamente o construtor vazio.                       |
| bug04 | AtendimentoBuilderTest.deveMontarAtendimentoCompleto falhou com Expected: Rex, Actual: null. O atendimento montado pelo Builder ficava sem o nome do pet.                                                                                                                                          | AtendimentoBuilder.java, método comPet (~linha 24): petNome = petNome; sem o this.. O parâmetro sombreia o atributo, então a atribuição era do parâmetro para ele mesmo e o campo continuava null.                                                             | troquei por this.petNome = petNome;, igual à linha de petPorte logo abaixo.                                                                                                     | Palavra-chave this e sombreamento de variáveis (POO): quando parâmetro e atributo têm o mesmo nome, é o this. que diferencia o campo da classe. Também aparece o padrão Builder (Aula 14), em que cada passo guarda um dado e devolve this.                       |
| bug05 | AtendimentoBuilderTest.deveRecusarMontagemSemNomeDoPet e deveRecusarMontagemSemPorte falharam com Expected IllegalArgumentException to be thrown, but nothing was thrown. O Builder aceitava montar atendimento sem nome ou sem porte do pet.                                                      | AtendimentoBuilder.java, método construir (~linha 41): não havia nenhuma validação, e o comentário acima dele delegava a regra ao controller. Um mesmo defeito explicava os dois testes vermelhos. | construir() agora lança IllegalArgumentException com mensagem clara quando petNome ou petPorte forem null ou em branco, antes de chamar a factory. Removi o comentário enganoso. | Padrão Builder (Aula 14): o objeto só deve ser criado em estado válido, então a validação fica no construir(). Também tratamento de exceções (Aula 11), com falha explícita em vez de objeto inconsistente. |
| bug06 | AgendaServiceTest.deveLancarExcecaoQuandoAtendimentoNaoExiste falhou com Expected AtendimentoNaoEncontradoException to be thrown, but nothing was thrown. Buscar um id inexistente retornava null como se nada tivesse acontecido.                                                                 | AgendaService.java, método buscarPorId (~linhas 37 a 42): o orElseThrow lançava a exceção correta, mas um try/catch (Exception e) genérico a capturava e retornava null. Em cascata, concluir() e cancelar() ficavam sujeitos a NullPointerException, e o controller nunca recebia a exceção para responder 404.                                                                                                                                                                                                                                                               | try/catch, e o método passou a retornar direto repository.findById(id).orElseThrow(...), propagando a AtendimentoNaoEncontradoException para quem chamou.                       | Tratamento de exceções (Aula 11): não capturar Exception genérica nem engolir erros com catch que retorna valor padrão. Exceção unchecked deve propagar até quem sabe tratá-la (aqui, o controller).                       |
| bug07 | AgendaServiceTest.deveRecusarAgendamentoComHorarioJaOcupado falhou com Expected HorarioOcupadoException, Actual NullPointerException (Cannot invoke "Atendimento.getProtocolo()" because "salvo" is null). Um agendamento duplicado para o mesmo pet e horário passava pela verificação de conflito. | AgendaService.java, método agendar (~linha 23): a.getPetNome() == novo.getPetNome() e a.getDataHora() == novo.getDataHora() comparavam referências de objetos (String e LocalDateTime) e não o conteúdo. Com objetos distintos de mesmo valor, o resultado era sempre false, o conflito não era detectado e o código seguia para repository.save.                                                                                                                                                                                                                                                               | substituí os == por Objects.equals(...) nas duas comparações, mantendo o "AGENDADO".equals(a.getStatus()).                                                                                                                                                                                | == vs .equals() (Aula 7): == compara referência e .equals() compara conteúdo. Com literais como "Rex" o == pode "funcionar por sorte" porque o Java reaproveita o mesmo objeto no pool de Strings, mas isso não vale para objetos criados em tempo de execução nem para LocalDateTime.                       |
| bug08 | ao escrever o teste01, calcularPreco() de um Banho PEQUENO retornou 100,0 em vez de 60,0. O bug não aparecia na suíte entregue porque nenhum teste cobria o preço do Banho.                                                                                                                        | Banho.java, método calcularPreco (~linhas 27 a 32): os valores de PEQUENO e GRANDE estavam invertidos (PEQUENO retornava 100 e GRANDE retornava 60), contrariando a tabela do contrato.                                                                                                                                                                                                                                                               | PEQUENO passou a retornar 60,0, MEDIO manteve 80,0 e o caso padrão (GRANDE) passou a retornar 100,0.                                                                                                                                                                                | Regras de negócio no model com polimorfismo (Aula 14): cada subclasse implementa calcularPreco() à sua maneira. Também testes unitários como contrato (Aula 15): o teste novo revelou um erro que nenhum outro teste pegava.                       |
| bug09 | ao escrever o teste02, a duração de uma Tosa veio 30 em vez de 60. Na API, o /resumo de uma tosa também mostraria 30 minutos. Nenhum teste entregue cobria a duração da Tosa.                                                                                                                      | Tosa.java, linha ~40: o método era getDuracaoMinutos(String porte), com um parâmetro a mais que o da superclasse Atendimento. Isso criou uma sobrecarga e não uma sobrescrita, então a chamada sem argumentos usava o método herdado, que retorna 30.                                                                                                                                                                                                                                                               | removi o parâmetro e anotei com @Override: public int getDuracaoMinutos() retornando 60.                                                                                                                                                                                | Sobrescrita vs sobrecarga (Aula 7) e polimorfismo. Na sobrescrita a assinatura é idêntica e a subclasse substitui o comportamento. Na sobrecarga a assinatura muda e o método é outro. A anotação @Override faz o compilador acusar o erro.                       |
| bug10 | ao escrever o teste03, cancelar um atendimento CONCLUIDO não lançou exceção: o status mudava para CANCELADO e o atendimento era salvo. Um serviço já realizado podia ser cancelado.                                                                                                                | Atendimento.java, método cancelar (~linha 63): só fazia status = "CANCELADO", sem verificar o status atual. O concluir() ao lado já tinha essa verificação, então as duas transições estavam inconsistentes.                                                                                                                                                                                                                                                               | adicionei a guarda if (!"AGENDADO".equals(status)) lançando StatusInvalidoException, igual ao concluir(). Só um atendimento AGENDADO pode ser cancelado.                                                                                                                                                                                | Regras de negócio no model e encapsulamento (Aula 14): as transições de status são validadas dentro do próprio objeto. Também exceções customizadas unchecked (Aula 11), com StatusInvalidoException sinalizando a operação inválida.                       |
| bug11 | ao escrever o teste4, agendar um atendimento de ontem não foi recusado. O service consultou o repository e tentou salvar, e no teste isso estourou em NullPointerException. Na API real, um agendamento no passado seria gravado normalmente.                                                      | AgendaService.java, método agendar (~linha 20): não havia nenhuma validação da data/hora antes de consultar e salvar. A regra do contrato (data no passado, IllegalArgumentException, banco nem consultado) não estava implementada.                                                                                                                                                                                                                                                               | adicionei no início do método if (novo.getDataHora().isBefore(LocalDateTime.now())) lançando IllegalArgumentException com mensagem clara, antes de qualquer chamada ao repository.                                                                                                                                                                                | Regras de negócio na camada de serviço (Aulas 13 e 14) e exceções (Aula 11): validar cedo (fail-fast) e lançar a exceção adequada, que o controller traduz em HTTP 400.                       |
| bug12 | a suíte inteira estava verde e nenhum teste apontava o problema. Lendo o código como num code review, vi que a entidade não gera o id. Na API, o POST /api/atendimentos (o curl do enunciado) falha com erro 500 por IdentifierGenerationException, porque o id é null e não há gerador.                                                                                                                                                                                                                                                                                                   | Atendimento.java, campo id (~linhas 14 e 15): só tinha @Id, sem @GeneratedValue. Nada atribuía o identificador antes do repository.save.                                                                                                                                                                                                                                                               | @GeneratedValue(strategy = GenerationType.IDENTITY) ao id, para o banco gerar a chave primária automaticamente.                                                                                                                                                                                | Persistência com Spring Data JPA (Aulas 12 e 13): chave primária com @Id e @GeneratedValue. Mostra também por que teste unitário com mock não substitui rodar a aplicação de verdade.                       |

## Parte 2 — Ajustes de Clean Code

| # | Onde estava | Qual princípio/boas práticas era violado | O que eu mudei |
|---|---|---|---|
| clean01 |AtendimentoFactory.java, método criar (~linha 14): os parâmetros se chamavam p, t, n, po, tu e d. |Nomes significativos (Clean Code). Os nomes não revelam a intenção, e quem lê a assinatura não sabe o que é cada argumento. Com tantos String seguidos, ficava fácil trocar a ordem sem perceber. |renomeei para protocolo, tipo, petNome, petPorte, tutorNome e dataHora, atualizando o switch e as chamadas dos construtores. Nenhum comportamento mudou e a suíte seguiu verde. |
| clean02 |AgendaService.java, método agendar (~linhas 30 e 31): um System.out.println("Recibo: ...") logo depois do save, dentro da regra de negócio. |Saída de depuração no código de produção e mistura de responsabilidades. O service deve agendar e devolver o resultado, não escrever no console. O println também acessava dados do objeto salvo só para imprimir, o que gerava risco de falha: foi ele que estourou o NullPointerException quando o save devolvia null. |removi o println e o método passou a devolver diretamente repository.save(novo). Se for preciso registrar o evento, o certo é um Logger e não System.out. |
| clean03 |GeradorProtocolo.java, construtor privado (~linha 14): System.out.println("GeradorProtocolo criado!"). |Código de depuração esquecido em produção e efeito colateral dentro de construtor. Um construtor deve apenas inicializar o objeto, e escrever no console polui a saída da aplicação e dos testes. |removi o println, e o construtor passou a só inicializar o contador. O comportamento do Singleton não mudou. |
| clean04 |AtendimentoController.java, final da classe (~linhas 106 a 112): o método privado calcularDescontoFidelidade, nunca chamado por ninguém, acompanhado de comentários descrevendo uma funcionalidade "futura". |Código morto e comentários que descrevem planos. Código que não é usado confunde quem lê, dá a falsa impressão de uma regra implementada (desconto de fidelidade) e ainda mistura regra de negócio no controller. O controle de versão serve justamente para guardar histórico e ideias futuras (YAGNI: não implemente o que ainda não é necessário). |removi o método e os comentários do bloco. O comportamento da API não mudou. |
| clean05 |Atendimento.java (construtor, concluir() e cancelar()) e AgendaService.java (agendar): os literais "AGENDADO", "CONCLUIDO" e "CANCELADO" repetidos em vários pontos. |Strings/números mágicos e duplicação (DRY). Um erro de digitação em um dos literais passaria no compilador e quebraria a regra de status em silêncio, e mudar um status exigiria caçar o texto pelo projeto. |criei as constantes STATUS_AGENDADO, STATUS_CONCLUIDO e STATUS_CANCELADO em Atendimento e passei a usá-las no model e no service. Mantive o tipo String para não quebrar a suíte entregue, que compara status como texto. |
| clean06 |AtendimentoController.java, nos métodos agendar, concluir e cancelar: ResponseEntity.status(201) e ResponseEntity.status(409) usados com números literais. |Números mágicos. Quem lê 409 precisa saber de cabeça o que significa, e o valor se repete em vários métodos, sem nome que revele a intenção. |passei a usar HttpStatus.CREATED e HttpStatus.CONFLICT, com o import de org.springframework.http.HttpStatus. O comportamento da API continua o mesmo, mas o código agora se explica sozinho. |

## Parte 3 — Testes novos (regras que estavam sem cobertura)

> Uma linha por teste novo (`test: ...`). "Regra coberta" é o comportamento do
> contrato (seção 3 do enunciado) que o teste protege. Em "Resultado", diga se o
> teste ficou vermelho ao ser escrito (revelou bug — qual?) ou verde de cara
> (regra já estava correta).

| # | Teste escrito (classe.método) | Regra coberta | Resultado ao escrever (vermelho/verde) |
|---|-------------------------------|---------------|----------------------------------------|
| teste01 | BanhoTest.deveCobrarPrecoConformeOPorteQuandoForBanho                              | Banho cobra preço por porte: PEQUENO R$ 60, MEDIO R$ 80, GRANDE R$ 100.              | vermelho (expected: <60.0> but was: <100.0>), revelou o bug08.                                       |
| teste02 | TosaTest.deveDurar60MinutosQuandoForTosa                              | Tosa dura 60 minutos.              | vermelho (expected: <60> but was: <30>), revelou o bug09.                                       |
| teste03 | AgendaServiceTest.deveRecusarCancelamentoQuandoAtendimentoJaConcluido                              | cancelar() de um atendimento CONCLUIDO deve ser recusado com StatusInvalidoException, e nada é salvo.              | vermelho (Expected StatusInvalidoException to be thrown, but nothing was thrown), revelou o bug10.                                       |
| teste04 | AgendaServiceTest.deveRecusarAgendamentoQuandoDataForNoPassado                              | agendar com data/hora no passado deve ser recusado com IllegalArgumentException, sem consultar nem salvar no banco.              | vermelho (esperava IllegalArgumentException mas veio NullPointerException), revelou o bug11.                                       |
| teste05 | ConsultaVeterinariaTest.deveCobrar150ReaisQuandoForConsultaDeQualquerPorte                              | a Consulta tem preço fixo de R$ 150,00, e o porte do pet não altera o valor (PEQUENO, MEDIO e GRANDE).              | verde de cara, a regra já estava correta. O teste passa a proteger contra regressão.                                       |
| teste06 | AgendaServiceTest.deveRecusarConclusaoQuandoAtendimentoEstaCancelado                              | concluir() de um atendimento CANCELADO deve ser recusado com StatusInvalidoException, e nada é salvo.              | verde de cara, a regra já estava correta. O teste passa a proteger contra regressão.                                       |

---

## Parte 4 — Perguntas de reflexão

> Responda com suas palavras, 5 a 10 linhas cada, **usando o código real do
> projeto como exemplo**. Respostas genéricas de tutorial não pontuam.

### 1. A suíte como contrato (Aula 15)
O projeto chegou com 20 testes, 9 vermelhos. Descreva como você usou as
mensagens de falha (ex.: `expected: <Rex> but was: <null>`) para caçar os bugs.
O que a suíte de testes tem de melhor do que testar tudo na mão com curl?

### 2. Mock e injeção de dependência (Aulas 13 a 15)
No `AgendaServiceTest`, o `@Mock` cria um `AtendimentoRepository` falso e o
`@InjectMocks` o injeta no service. Explique a relação disso com o `@Autowired`
que o Spring faz em produção — quem "injeta" em cada mundo, e por que o teste
consegue rodar sem banco e sem subir o Spring?

### 3. `==` vs `.equals()` (Aula 7)
Um dos bugs fazia o agendamento duplicado passar pela verificação de conflito.
Explique por que `==` entre Strings e `LocalDateTime` falhou aqui, por que ele
"funciona por sorte" com literais como `"Rex"`, e o que a sua correção mudou.

### 4. Sobrescrita vs sobrecarga (Aula 7)
Um dos bugs compilava sem nenhum erro: um método parecia sobrescrever
`getDuracaoMinutos`, mas na verdade criava uma assinatura nova. Explique a
diferença entre override e overload nesse caso e por que a anotação `@Override`
teria impedido o bug.

### 5. Singleton manual vs bean do Spring (Aula 14)
O `GeradorProtocolo` é um Singleton escrito à mão e causou um dos bugs.
Explique o que ele garante, qual foi o bug, e por que o `AgendaService`
(`@Service`) não corre o mesmo risco no container do Spring.

### 6. Cobertura de testes: onde parar? (Aula 15)
Dos 6 testes novos que você escreveu, alguns ficaram vermelhos (revelaram
bugs) e outros verdes de cara (regras já corretas). Vale a pena manter os que
ficaram verdes? Em um projeto real com prazo, o que você priorizaria testar:
caminho feliz, caminhos de erro, ou 100% de cobertura? Justifique.

---

## Parte 5 — Espaço livre (opcional)

Alguma dificuldade, dúvida ou comentário sobre o checkpoint?

```

```
