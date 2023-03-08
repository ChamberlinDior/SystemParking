package com.parkit.parkingsystem.integration.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour la classe InteractiveShell.
 */
class InteractiveShellTest {

    private InputStream inputStream;

    /**
     * Initialise le flux d'entrée standard avec une chaîne de caractères.
     * Cette méthode est appelée avant chaque test.
     */
    @BeforeEach
    void setUp() {
        // Mocking the standard input stream with a string
        String input = "1\n2\n3\n";
        inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);
    }

    /**
     * Réinitialise le flux d'entrée standard après chaque test.
     */
    @AfterEach
    void tearDown() {
        // Resetting the standard input stream after each test
        System.setIn(System.in);
    }

    /**
     * Teste si la méthode loadInterface s'exécute sans lever d'exception.
     */
    @Test
    @DisplayName("Test si la méthode loadInterface s'exécute sans lever d'exception")
    void loadInterfaceTest() {
        assertDoesNotThrow(() -> InteractiveShell.loadInterface());
    }

    /**
     * Teste si le lecteur d'entrée fonctionne correctement.
     * Vérifie que la méthode readSelection du InputReaderUtil retourne bien la valeur attendue (1).
     */
    @Test
    @DisplayName("Test si le lecteur d'entrée fonctionne correctement")
    void inputReaderTest() {
        InputReaderUtil inputReaderUtil = new InputReaderUtil();
        int selection = inputReaderUtil.readSelection();
        assertEquals(1, selection);
    }
}
