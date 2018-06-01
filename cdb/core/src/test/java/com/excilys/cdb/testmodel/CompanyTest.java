package com.excilys.cdb.testmodel;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

/**
 * Unit test for simple App.
 */
public class CompanyTest 
{

    @Test
    public void testEqualsOk()
    {
    	Company c = new Company(1, "blabla");
    	Company c2 = new Company(1, "blabla");
        assertEquals(c,c2);
    }
    
    @Test
    public void testEqualsOkWithDifferentId()
    {
    	Company c = new Company(1, "blabla");
    	Company c2 = new Company(2, "blabla");
        assertEquals(c,c2);
    }
    
}
