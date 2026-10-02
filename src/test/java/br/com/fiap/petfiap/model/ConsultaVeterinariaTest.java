package br.com.fiap.petfiap.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Testes unitarios do model: sem banco, sem Spring (Aula 15).
public class ConsultaVeterinariaTest {

    private ConsultaVeterinaria consultaDaMimi() {
        return new ConsultaVeterinaria(1, "Mimi", "MEDIO", "Bruno", LocalDateTime.of(2026, 10, 1, 14, 0));
    }

    @Test
    public void deveAcumular50PontosDeFidelidade() {
        // Act
        int pontos = consultaDaMimi().calcularPontosFidelidade();

        // Assert
        assertEquals(50, pontos);
    }

    @Test
    public void deveDurar30Minutos() {
        // Act
        int duracao = consultaDaMimi().getDuracaoMinutos();

        // Assert
        assertEquals(30, duracao);
    }

    @Test
    public void deveCobrar150ReaisQuandoForConsultaDeQualquerPorte() {
        // Arrange
        LocalDateTime data = LocalDateTime.of(2026, 10, 1, 14, 0);
        ConsultaVeterinaria pequeno = new ConsultaVeterinaria(1, "Rex", "PEQUENO", "Ana", data);
        ConsultaVeterinaria medio = new ConsultaVeterinaria(2, "Mimi", "MEDIO", "Bruno", data);
        ConsultaVeterinaria grande = new ConsultaVeterinaria(3, "Thor", "GRANDE", "Carla", data);

        // Act
        double precoPequeno = pequeno.calcularPreco();
        double precoMedio = medio.calcularPreco();
        double precoGrande = grande.calcularPreco();

        // Assert
        assertEquals(150.0, precoPequeno, 0.001);
        assertEquals(150.0, precoMedio, 0.001);
        assertEquals(150.0, precoGrande, 0.001);
    }
}
