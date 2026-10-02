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

| # | Sintoma observado (o que fiz/vi)                                                                                                                                                                                                                                | Causa raiz (arquivo e linha aproximada)                                                                                                                                                                                                                        | Correção aplicada                                                                                                                                                                 | Conceito da disciplina |
|---|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------|
| bug01 | GeradorProtocoloTest.deveManterUmaUnicaInstancia falhou no assertSame (duas instâncias diferentes, @1f7076bc e @71904469) e deveGerarProtocolosSequenciais falhou com expected: <2> but was: <1>. O console imprimiu "GeradorProtocolo criado!" a cada chamada. | eradorProtocolo.java, método getInstancia() (~linha 19): o if (instancia == null) retornava new GeradorProtocolo() sem atribuir o resultado ao campo estático instancia, que ficava sempre null. Cada chamada criava um objeto novo com o contador zerado.     | passei a atribuir a instância ao campo antes de retornar (instancia = new GeradorProtocolo();), e o método sempre devolve o mesmo objeto guardado.                                | Padrão Singleton (Aula 14): construtor privado e atributo estático guardando a instância única (lazy initialization).                       |
| bug02 | AtendimentoFactoryTest.deveCriarTosaQuandoTipoForTosa falhou no assertInstanceOf: Expected: class Tosa, Actual: class Banho. Ao pedir uma Tosa, a factory devolvia um Banho.                                                                                                                                                                                                                                                                | AtendimentoFactory.java, método criar (~linha 17): o case "TOSA" instanciava new Banho(...), provavelmente por cópia e cola do case "BANHO".                                                                                                                   | troquei por new Tosa(...) no case "TOSA", mantendo os mesmos parâmetros.                                                                                                          | Padrão Factory (Aula 14) e polimorfismo. A factory é o único lugar que conhece as subclasses concretas, então um erro ali devolve o tipo errado para todo o sistema, sem erro de compilação.                       |
| bug03 | AtendimentoFactoryTest.devePreencherOsDadosDoPetNaConsulta falhou com Expected: Mimi, Actual: null. Uma consulta criada pela factory ficava com os dados do pet vazios.                                                                                                                                                                                                                                                                | ConsultaVeterinaria.java, construtor de 5 parâmetros (~linha 17): chamava super() sem argumentos, então o construtor vazio de Atendimento rodava e protocolo, pet, porte, tutor, data e status não eram preenchidos. Os parâmetros recebidos eram descartados. | troquei super() por super(protocolo, petNome, petPorte, tutorNome, dataHora), igual ao que Banho e Tosa já faziam.                                                                | Herança e construtores (POO): o construtor da subclasse precisa chamar o construtor correto da superclasse com super(...). Sem isso, o Java chama implicitamente o construtor vazio.                       |
| bug04 | AtendimentoBuilderTest.deveMontarAtendimentoCompleto falhou com Expected: Rex, Actual: null. O atendimento montado pelo Builder ficava sem o nome do pet.                                                                                                                                                                                                                                                                | AtendimentoBuilder.java, método comPet (~linha 24): petNome = petNome; sem o this.. O parâmetro sombreia o atributo, então a atribuição era do parâmetro para ele mesmo e o campo continuava null.                                                             | troquei por this.petNome = petNome;, igual à linha de petPorte logo abaixo.                                                                                                       | Palavra-chave this e sombreamento de variáveis (POO): quando parâmetro e atributo têm o mesmo nome, é o this. que diferencia o campo da classe. Também aparece o padrão Builder (Aula 14), em que cada passo guarda um dado e devolve this.                       |
| bug05 | AtendimentoBuilderTest.deveRecusarMontagemSemNomeDoPet e deveRecusarMontagemSemPorte falharam com Expected IllegalArgumentException to be thrown, but nothing was thrown. O Builder aceitava montar atendimento sem nome ou sem porte do pet. | AtendimentoBuilder.java, método construir (~linha 41): não havia nenhuma validação, e o comentário acima dele delegava a regra ao controller. Um mesmo defeito explicava os dois testes vermelhos. | construir() agora lança IllegalArgumentException com mensagem clara quando petNome ou petPorte forem null ou em branco, antes de chamar a factory. Removi o comentário enganoso.  | Padrão Builder (Aula 14): o objeto só deve ser criado em estado válido, então a validação fica no construir(). Também tratamento de exceções (Aula 11), com falha explícita em vez de objeto inconsistente. |
| bug06 |                                                                                                                                                                                                                                                                 |                                                                                                                                                                                                                                                                |                                                                                                                                                                                   |                        |
| bug07 |                                                                                                                                                                                                                                                                 |                                                                                                                                                                                                                                                                |                                                                                                                                                                                   |                        |
| bug08 |                                                                                                                                                                                                                                                                 |                                                                                                                                                                                                                                                                |                                                                                                                                                                                   |                        |
| bug09 |                                                                                                                                                                                                                                                                 |                                                                                                                                                                                                                                                                |                                                                                                                                                                                   |                        |
| bug10 |                                                                                                                                                                                                                                                                 |                                                                                                                                                                                                                                                                |                                                                                                                                                                                   |                        |
| bug11 |                                                                                                                                                                                                                                                                 |                                                                                                                                                                                                                                                                |                                                                                                                                                                                   |                        |
| bug12 |                                                                                                                                                                                                                                                                 |                                                                                                                                                                                                                                                                |                                                                                                                                                                                   |                        |

## Parte 2 — Ajustes de Clean Code

| # | Onde estava | Qual princípio/boas práticas era violado | O que eu mudei |
|---|---|---|---|
| clean01 | | | |
| clean02 | | | |
| clean03 | | | |
| clean04 | | | |
| clean05 | | | |
| clean06 | | | |

## Parte 3 — Testes novos (regras que estavam sem cobertura)

> Uma linha por teste novo (`test: ...`). "Regra coberta" é o comportamento do
> contrato (seção 3 do enunciado) que o teste protege. Em "Resultado", diga se o
> teste ficou vermelho ao ser escrito (revelou bug — qual?) ou verde de cara
> (regra já estava correta).

| # | Teste escrito (classe.método) | Regra coberta | Resultado ao escrever (vermelho/verde) |
|---|---|---|---|
| teste01 | | | |
| teste02 | | | |
| teste03 | | | |
| teste04 | | | |
| teste05 | | | |
| teste06 | | | |

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
