Sistema de Biblioteca - Treze de Maio

Sistema de gerenciamento de biblioteca desenvolvido em JavaFX para controle de acervo, empréstimos e usuários.

Tecnologias

- **Java 21**
- **JavaFX 21.0.6**
- **MySQL 8.0**
- **Maven**

Funcionalidades

- Cadastro e gerenciamento de livros
- Controle de empréstimos e devoluções
- Gestão de usuários
- Cadastro de autores, editoras e categorias
- Controle de acervo (itens doados)
- Sistema de busca

Pré-requisitos

- JDK 21 ou superior
- MySQL Server
- Maven

Configuração

1. Clone o repositório
2. Configure o banco de dados MySQL
3. Configure a classe de conexao usuario e senha
4. Ajuste as credenciais em `src/main/java/org/example/treze/conexao/`
5. Execute: `mvn clean javafx:run`

Estrutura

- `model/` - Entidades do sistema
- `dao/` - Camada de acesso a dados
- `controllers/` - Controladores JavaFX
- `resources/` - Arquivos FXML e CSS

video 
https://youtu.be/VOVFHogJ31Y
