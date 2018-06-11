package com.excilys.cdb.ui;

import java.util.Set;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.springframework.data.domain.Page;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.webservices.ressources.UrlParams;

public class ClientCompanyWebService extends InstanceWebService {

	private static final String URI = "company";

	public ClientCompanyWebService() {
		super(URI);
	}

	public CompanyDTO get(Long id) {
		webTarget.path(String.valueOf(id));
		return webTarget.request(MediaType.APPLICATION_JSON).get(CompanyDTO.class);
	}

	public Page<CompanyDTO> page(Integer iNumpage, Integer paramLimit) {
		System.out.println(webTarget.getUri());
		webTarget.queryParam(UrlParams.PAGE, String.valueOf(iNumpage));
		webTarget.queryParam(UrlParams.LIMIT, String.valueOf(paramLimit));
		return webTarget.request(MediaType.APPLICATION_JSON).get(new GenericType<Page<CompanyDTO>>() {
		});
	}

	public Page<CompanyDTO> searchPage(String search, Integer iNumpage, Integer paramLimit) {
		webTarget.queryParam(UrlParams.SEARCH, search);
		webTarget.queryParam(UrlParams.PAGE, String.valueOf(iNumpage));
		webTarget.queryParam(UrlParams.LIMIT, String.valueOf(paramLimit));
		return webTarget.request(MediaType.APPLICATION_JSON).get(new GenericType<Page<CompanyDTO>>() {
		});
	}

	public Long delete(Set<Long> deletes) {
		webTarget.queryParam(UrlParams.DELETE_SELECT, deletes);
		return webTarget.request().get(Long.class);
	}

	public Long add(CompanyDTO company) {
		return (Long) webTarget.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(company, MediaType.APPLICATION_JSON)).getEntity();
	}

	public Boolean edit(CompanyDTO company) {
		return (Boolean) webTarget.request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(company, MediaType.APPLICATION_JSON)).getEntity();
	}

}
