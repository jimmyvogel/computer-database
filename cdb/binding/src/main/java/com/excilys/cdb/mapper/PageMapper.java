package com.excilys.cdb.mapper;

import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.dao.CDBPage;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.model.Computer;

public class PageMapper {

    /**
     * Mapper pour passer d'une page de computer à une page de computer dto.
     * @param page une page de computer
     * @return une page de computer dto.
     */
    public static CDBPage<ComputerDTO> mapPageComputerToDto(CDBPage<Computer> page) {
        List<ComputerDTO> computersDtos = new ArrayList<>();
        for (Computer c : page.getObjects()) {
            computersDtos.add(new ComputerDTO(c));
        }
        CDBPage<ComputerDTO> newpage = new CDBPage<>(page.getLimit(), page.getCount());
        newpage.charge(computersDtos, page.getPageCourante());

        return newpage;
    }

}
