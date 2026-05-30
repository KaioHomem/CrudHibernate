# Cadastro em Duas Tabelas — Swing + Hibernate (MySQL)

**Autores:** Kaio Felipe Homem e Jefferson Machado
**Disciplina:** Desenvolvimento de Sistemas Orientados a Objetos — CC3 — 2026/1
**Professor:** Edinilson da Silva Vida

Aplicação desktop em Java que cadastra **Categorias** e **Produtos** relacionados (1:N).
Ao selecionar uma categoria, a tabela de produtos mostra apenas os produtos vinculados a ela.
Também há um `JComboBox` para escolher a categoria ao salvar/alterar um produto.

## Estrutura

```
CrudHibernate/
├── pom.xml
├── script_banco.sql
└── src/main/
    ├── java/br/edu/ifsc/crudhibernate/
    │   ├── Main.java
    │   ├── model/   → Categoria.java, Produto.java
    │   ├── dao/     → CategoriaDAO.java, ProdutoDAO.java
    │   ├── util/    → JPAUtil.java
    │   └── view/    → FrmCategoriaProduto.java
    └── resources/META-INF/persistence.xml
```

## Como executar

1. Tenha o **MySQL** rodando. Ajuste usuário/senha em `src/main/resources/META-INF/persistence.xml`
   (padrão: usuário `root`, senha `root`). O banco `crudhibernate` é criado automaticamente.
2. Pela linha de comando, dentro da pasta `CrudHibernate/`:
   ```
   mvn clean compile
   mvn exec:java
   ```
3. Ou abra como projeto Maven no **NetBeans** e rode a classe `Main`.

> O Hibernate está com `hbm2ddl.auto=update`, então cria as tabelas sozinho.
> Se preferir criar o banco manualmente, rode o `script_banco.sql` e troque para `none`.

## Funcionalidades

- CRUD completo de Categorias e Produtos (salvar, alterar, excluir, listar).
- Relacionamento `@ManyToOne` (Produto → Categoria) via Hibernate/JPA.
- Filtro de produtos por categoria (tabela e combobox).
- Validação: não permite salvar produto sem categoria.
- Métodos de limpeza separados para cada formulário.
