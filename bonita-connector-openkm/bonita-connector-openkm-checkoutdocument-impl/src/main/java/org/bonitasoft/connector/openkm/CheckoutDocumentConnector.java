/**
 * OpenKM, Open Document Management System (http://www.openkm.com)
 * Copyright (c) 2006-2014 Paco Avila & Josep Llort
 * 
 * No bytes were intentionally harmed during the development of this application.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.bonitasoft.connector.openkm;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.apache.commons.io.IOUtils;
import org.bonitasoft.connectors.openkm.OpenKMConnector;
import org.bonitasoft.connectors.openkm.OpenKMResponse;
import org.bonitasoft.connectors.openkm.util.FileUtils;
import org.bonitasoft.engine.connector.ConnectorValidationException;

import com.openkm.sdk4j.OKMWebservices;
import com.openkm.sdk4j.OKMWebservicesFactory;
import com.openkm.sdk4j.exception.AccessDeniedException;
import com.openkm.sdk4j.exception.DatabaseException;
import com.openkm.sdk4j.exception.WebserviceException;
import com.openkm.sdk4j.exception.LockException;
import com.openkm.sdk4j.exception.PathNotFoundException;
import com.openkm.sdk4j.exception.RepositoryException;
import com.openkm.sdk4j.exception.UnknowException;


public class CheckoutDocumentConnector extends OpenKMConnector {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	
	// Input parameters
	public static final String DOC_ID = "docId";
	
	// Vars
	private String docId;
	
	@Override
	public void validateInputParameters() throws ConnectorValidationException {
		super.validateInputParameters();
		try {
			docId = getDocId();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException("documentName type is invalid");
		}	
	}
	
	@Override
	protected OpenKMResponse executeAction(OKMWebservices ws) {
		if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Executing CheckoutDocumentConnector with params: documentName:"+docId);
        }
		
		OpenKMResponse response = new OpenKMResponse();
	
		try {
			ws.checkout(docId);		
			response.setStatus(true);
			response.setErrorCause("OK");
			response.setErrorMessage("OK");		
		}  catch (AccessDeniedException e) {
			response.setStatus(true);
			response.setErrorCause(e.getClass().getSimpleName());
			response.setErrorMessage("There was an exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
		} catch (RepositoryException e) {
			response.setStatus(true);
			response.setErrorCause(e.getClass().getSimpleName());
			response.setErrorMessage("There was an exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
		} catch (PathNotFoundException e) {
			response.setStatus(true);
			response.setErrorCause(e.getClass().getSimpleName());
			response.setErrorMessage("There was an exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
		} catch (LockException e) {
			response.setStatus(true);
			response.setErrorCause(e.getClass().getSimpleName());
			response.setErrorMessage("There was an exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
		} catch (DatabaseException e) {
			response.setStatus(true);
			response.setErrorCause(e.getClass().getSimpleName());
			response.setErrorMessage("There was an exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
		} catch (UnknowException e) {
			response.setStatus(true);
			response.setErrorCause(e.getClass().getSimpleName());
			response.setErrorMessage("There was an exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
		} catch (WebserviceException e) {
			response.setStatus(true);
			response.setErrorCause(e.getClass().getSimpleName());
			response.setErrorMessage("There was an exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
		}
		
		return response;
	}
	
	protected final String getDocId() {
		return (String) getInputParameter(DOC_ID);
	}
	
}
