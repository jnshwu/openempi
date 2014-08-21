/**
 *
 * Copyright (C) 2002-2012 "SYSNET International, Inc."
 * support@sysnetint.com [http://www.sysnetint.com]
 *
 * This file is part of OpenEMPI.
 *
 * OpenEMPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openhie.openempi.singlebestrecord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SingleBestRecordConfiguration implements Serializable
{
	private static final long serialVersionUID = -1183069022782754301L;

	private String name;
	private String description;
	private List<SingleBestRecordRule> rules = new ArrayList<SingleBestRecordRule>();
	
	public SingleBestRecordConfiguration(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public SingleBestRecordConfiguration() {
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void addRule(SingleBestRecordRule rule) {
		rules.add(rule);
	}
	
	public List<SingleBestRecordRule> getRules() {
		return rules;
	}
	
	public void setRules(List<SingleBestRecordRule> rules) {
		this.rules = rules;
	}

	@Override
	public String toString() {
		return "SingleBestRecordConfiguration [name=" + name + ", description="
				+ description + ", rules=" + rules + "]";
	}
}
