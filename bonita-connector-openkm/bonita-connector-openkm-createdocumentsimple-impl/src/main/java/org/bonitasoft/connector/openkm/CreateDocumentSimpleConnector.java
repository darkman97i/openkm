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
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.document.Document;
import org.bonitasoft.engine.bpm.document.DocumentNotFoundException;
import org.bonitasoft.engine.connector.ConnectorValidationException;

import com.openkm.sdk4j.OKMWebservices;
import com.openkm.sdk4j.exception.AccessDeniedException;
import com.openkm.sdk4j.exception.AutomationException;
import com.openkm.sdk4j.exception.DatabaseException;
import com.openkm.sdk4j.exception.ExtensionException;
import com.openkm.sdk4j.exception.FileSizeExceededException;
import com.openkm.sdk4j.exception.ItemExistsException;
import com.openkm.sdk4j.exception.PathNotFoundException;
import com.openkm.sdk4j.exception.RepositoryException;
import com.openkm.sdk4j.exception.UnknowException;
import com.openkm.sdk4j.exception.UnsupportedMimeTypeException;
import com.openkm.sdk4j.exception.UserQuotaExceededException;
import com.openkm.sdk4j.exception.VirusDetectedException;
import com.openkm.sdk4j.exception.WebserviceException;

/**
 * CreateDocumentSimpleConnector
 * 
 * @author jllort
 * 
 */
public class CreateDocumentSimpleConnector extends OpenKMConnector {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	
	// Input parameters
	public static final String FILE_OBJECT = "fileObject";
	public static final String DESTINATION_PATH = "dstPath";
	
	// Vars
	private String fileObject;
	private String dstPath;
	
	@Override
	public void validateInputParameters() throws ConnectorValidationException {
		super.validateInputParameters();
		try {
			fileObject = getFileObject();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException("fileObject type is invalid");
		}
		try {
			dstPath = getDstPath();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException("dstPath type is invalid");
		}
	}
	
	@Override
	protected OpenKMResponse executeAction(OKMWebservices ws) {
		if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Executing CreateDocumentSimpleConnector with params: fileObject:"+fileObject+", dstPath:"+dstPath);
        }
		OpenKMResponse response = new OpenKMResponse();
		InputStream in = null;
		OutputStream out = null;
		File tmpFile = null;
		try {
			ProcessAPI processAPI = getAPIAccessor().getProcessAPI();
			long processInstanceId = getExecutionContext().getProcessInstanceId();
			Document doc = processAPI.getLastDocument(processInstanceId, this.fileObject);
			final byte[] content = processAPI.getDocumentContent(doc.getContentStorageId());
			if (content.length > 0) {
				tmpFile = File.createTempFile("okm", FileUtils.getFileExtension(doc.getContentFileName()));
				String filePath = tmpFile.getCanonicalPath();
				in = new ByteArrayInputStream(content);
				out = new FileOutputStream(filePath);
				IOUtils.copy(in, out);
				
				InputStream is = new FileInputStream(filePath);
				String path = dstPath + doc.getContentFileName();
				ws.createDocumentSimple(path, is);
				
				response.setStatus(true);
				response.setErrorCause("OK");
				response.setErrorMessage("OK");
			}
		} catch (DocumentNotFoundException e) {
			response.setStatus(true);
			response.setErrorCause(e.getClass().getSimpleName());
			response.setErrorMessage("There was an exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
		} catch (IOException e) {
			response.setStatus(true);
			response.setErrorCause(e.getClass().getSimpleName());
			response.setErrorMessage("There was an exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
		} catch (UnsupportedMimeTypeException e) {
			response.setStatus(true);
			response.setErrorCause(e.getClass().getSimpleName());
			response.setErrorMessage("There was an exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
		} catch (FileSizeExceededException e) {
			response.setStatus(true);
			response.setErrorCause(e.getClass().getSimpleName());
			response.setErrorMessage("There was an exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
		} catch (UserQuotaExceededException e) {
			response.setStatus(true);
			response.setErrorCause(e.getClass().getSimpleName());
			response.setErrorMessage("There was an exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
		} catch (VirusDetectedException e) {
			response.setStatus(true);
			response.setErrorCause(e.getClass().getSimpleName());
			response.setErrorMessage("There was an exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
		} catch (ItemExistsException e) {
			response.setStatus(true);
			response.setErrorCause(e.getClass().getSimpleName());
			response.setErrorMessage("There was an exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
		} catch (PathNotFoundException e) {
			response.setStatus(true);
			response.setErrorCause(e.getClass().getSimpleName());
			response.setErrorMessage("There was an exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
		} catch (AccessDeniedException e) {
			response.setStatus(true);
			response.setErrorCause(e.getClass().getSimpleName());
			response.setErrorMessage("There was an exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
		} catch (RepositoryException e) {
			response.setStatus(true);
			response.setErrorCause(e.getClass().getSimpleName());
			response.setErrorMessage("There was an exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
		} catch (DatabaseException e) {
			response.setStatus(true);
			response.setErrorCause(e.getClass().getSimpleName());
			response.setErrorMessage("There was an exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
		} catch (ExtensionException e) {
			response.setStatus(true);
			response.setErrorCause(e.getClass().getSimpleName());
			response.setErrorMessage("There was an exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
		} catch (AutomationException e) {
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
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
			tmpFile.delete();
		}
		return response;
	}
	
	protected final String getFileObject() {
		return (String) getInputParameter(FILE_OBJECT);
	}
	
	protected final String getDstPath() {
		return (String) getInputParameter(DESTINATION_PATH);
	}
}