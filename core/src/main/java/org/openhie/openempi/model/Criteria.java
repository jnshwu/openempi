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
package org.openhie.openempi.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Criteria extends BaseObject
{
	private static final long serialVersionUID = 7534673028743277151L;

	private List<Criterion> criteria;
	private List<OrderCriterion> orderCriteria;
	private boolean lazyIdentifiers = false;

	public Criteria() {
		criteria = new ArrayList<Criterion>();
		orderCriteria = new ArrayList<OrderCriterion>();
	}
	
	public void addCriterion(Criterion criterion) {
		criteria.add(criterion);
	}

	public void addOrderCriterion(OrderCriterion criterion) {
		orderCriteria.add(criterion);
	}
	
	public List<Criterion> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<Criterion> criteria) {
		this.criteria = criteria;
	}
	
	public List<OrderCriterion> getOrderCriteria() {
		return orderCriteria;
	}
	
	public boolean isLazyIdentifiers() {
		return lazyIdentifiers;
	}

	public void setLazyIdentifiers(boolean lazyIdentifiers) {
		this.lazyIdentifiers = lazyIdentifiers;
	}
	
	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Criteria))
			return false;
		Criteria castOther = (Criteria) other;
		return new EqualsBuilder().append(criteria, castOther.criteria).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(criteria).toHashCode();
	}

	@Override
	public String toString() {
		return "Criteria [criteria=" + criteria + ", orderCriteria=" + orderCriteria + "]";
	}
}
