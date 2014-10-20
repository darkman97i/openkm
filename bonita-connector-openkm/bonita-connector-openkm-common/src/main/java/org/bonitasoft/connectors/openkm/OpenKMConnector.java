/**
 *  OpenKM, Open Document Management System (http://www.openkm.com)
 *  Copyright (c) 2006-2014  Paco Avila & Josep Llort
 *
 *  No bytes were intentionally harmed during the development of this application.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.bonitasoft.connectors.openkm;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.engine.connector.AbstractConnector;
import org.bonitasoft.engine.connector.ConnectorException;
import org.bonitasoft.engine.connector.ConnectorValidationException;

import com.openkm.sdk4j.OKMWebservices;
import com.openkm.sdk4j.OKMWebservicesFactory;

/**
 * OpenKMConnector
 * 
 * @author jllort
 *
 */
public abstract class OpenKMConnector extends AbstractConnector {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	
	// global Inputs
	protected static final String HOST = "okmHost";
	protected static final String USER = "okmUser";
	protected static final String PASSWORD = "okmPassword";
	
	// global outputs
	protected static final String STATUS = "okmStatus";
	protected static final String ERROR_CAUSE = "okmErrorCause";
	protected static final String ERROR_MESSAGE = "okmErrorMessage";
	
	// Vars
	private String host;
    private String user;
    private String password;
    private OKMWebservices ws;
    
    @Override
	public void connect() throws ConnectorException{
    	if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Executing OpenKMConnector with params: host:"+host+", user:"+user);
        }
		try {			
			ws = OKMWebservicesFactory.newInstance(host, user, password);
		} catch (Exception e) {
    		getOutputParameters().put(STATUS, String.valueOf(new Boolean(false)));
            getOutputParameters().put(ERROR_CAUSE, e.getClass().getSimpleName());
            getOutputParameters().put(ERROR_MESSAGE, "There was an exception:" + e.getClass().getSimpleName()+ ":" + e.getMessage());
			throw new ConnectorException(e);
		}
	
	}

	@Override
	public void disconnect() throws ConnectorException{
		// Nothing to do here
	}
	
	@Override
	protected void executeBusinessLogic() throws ConnectorException {
		if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Executing Action");
        }
    	try {
    		OpenKMResponse response = executeAction(ws);
    		getOutputParameters().put(STATUS, response.getStatus());
            getOutputParameters().put(ERROR_CAUSE, response.getErrorCause());
            getOutputParameters().put(ERROR_MESSAGE, response.getErrorMessage());
    	} catch (Exception e) {
    		throw new ConnectorException(e);
    	}
    }
    
    /**
     * executeAction
     */
    protected abstract OpenKMResponse executeAction(OKMWebservices ws);
    
	@Override
	public void validateInputParameters() throws ConnectorValidationException {
		try {
			host = getOkmHost();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException("okmHost type is invalid");
		}
		try {
			user = getOkmUser();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException("okmUser type is invalid");
		}
		try {
			password = getOkmPassword();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException("okmPassword type is invalid");
		}
	}
	
	protected final String getOkmHost() {
		return (String) getInputParameter(HOST);
	}

	protected final String getOkmUser() {
		return (String) getInputParameter(USER);
	}

	protected final String getOkmPassword() {
		return (String) getInputParameter(PASSWORD);
	}
}