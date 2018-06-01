package com.excilys.cdb.testmodel;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

/**
 * Unit test for simple App.
 */
public class ComputerTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testEqualsOk()
    {
    	Computer c = new Computer("ComputerName", LocalDate.now(), LocalDate.now().plusDays(1), new Company("blabla"));
    	Computer c2 = new Computer("ComputerName", LocalDate.now(), LocalDate.now().plusDays(1), new Company("blabla"));
        assertEquals(c,c2);
    }
    
    @Test
    public void testEqualsOkWithDifferentId()
    {
    	Computer c = new Computer(1, "ComputerName", LocalDate.now(), LocalDate.now().plusDays(1), new Company("blabla"));
    	Computer c2 = new Computer(2, "ComputerName", LocalDate.now(), LocalDate.now().plusDays(1), new Company("blabla"));
        assertEquals(c,c2);
    }
    
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testEqualsFalseCompany()
    {
    	Computer c = new Computer("ComputerName", LocalDate.now(), LocalDate.now().plusDays(1), new Company("blabla"));
    	Computer c2 = new Computer("ComputerName", LocalDate.now(), LocalDate.now().plusDays(1), new Company("blabla2"));
        assertNotEquals(c,c2);
    }
    
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testEqualsFalseDate()
    {
    	Computer c = new Computer("ComputerName", LocalDate.now(), LocalDate.now().plusDays(1), new Company("blabla"));
    	Computer c2 = new Computer("ComputerName", LocalDate.now(), LocalDate.now().plusDays(2), new Company("blabla"));
        assertNotEquals(c,c2);
    }
}
