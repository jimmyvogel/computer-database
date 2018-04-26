package com.excilys.cdb.daos;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.persistence.Page;

public class PageTest {

    private static final Integer LIMIT = 20;
    private static final Integer NB_ELEMENTS = 600;
    private static final Integer PAGE_COURANTE = 5;

    private Page<Integer> page;

    /**
     * Initialisation des variables de tests.
     */
    @Before
    public void init() {
        page = new Page<Integer>(LIMIT, NB_ELEMENTS);
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < LIMIT; i++) {
            list.add(i);
        }
        page.charge(list, PAGE_COURANTE);
    }
    /**
     * Test de la méthode pageRestantes.
     */
    @Test
    public void testPageRestantes() {
        assertTrue(page.pageRestantes() == 25);
    }

    /**
     * Test de la méthode pageRestantes in list.
     */
    @Test
    public void testPageRestantesInList() {
        List<Integer> list =
                Arrays.asList(PAGE_COURANTE - 2, PAGE_COURANTE - 1,
                        PAGE_COURANTE, PAGE_COURANTE + 1, PAGE_COURANTE + 2);
        assertTrue(page.pageRestantesInList(5).equals(list));
    }

    /**
     * Test de la méthode maxPage.
     */
    @Test
    public void testGetMaxPage() {
        assertTrue(page.getMaxPage() == 30);
    }

}
