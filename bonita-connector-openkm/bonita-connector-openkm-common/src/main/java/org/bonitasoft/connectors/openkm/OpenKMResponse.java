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
package org.bonitasoft.connectors.openkm;

import java.io.Serializable;

/**
 * OpenKMResponse
 * 
 * @author jllort
 * 
 */
public class OpenKMResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Boolean status = false;
	private String errorCause = "";
	private String errorMessage = "";
	
	public Boolean getStatus() {
		return status;
	}
	
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	public String getErrorCause() {
		return errorCause;
	}
	
	public void setErrorCause(String errorCause) {
		this.errorCause = errorCause;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("status=").append(status);
		sb.append(", errorCause=").append(errorCause);
		sb.append(", message=").append(errorMessage);
		sb.append("}");
		return sb.toString();
	}
}