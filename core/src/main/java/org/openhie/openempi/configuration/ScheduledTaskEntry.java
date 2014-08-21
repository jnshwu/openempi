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
package org.openhie.openempi.configuration;

import java.util.concurrent.TimeUnit;

import org.openhie.openempi.model.BaseObject;

public class ScheduledTaskEntry extends BaseObject
{
	private static final long serialVersionUID = 1224297603229522913L;

	public final static int SCHEDULE_ENTRY_TYPE = 0;
	public final static int SCHEDULE_AT_FIXED_RATE_ENTRY_TYPE = 1;
	public final static int SCHEDULE_WITH_FIXED_DELAY_ENTRY_TYPE = 2;
	
	private String taskName;
	private String taskImplementation;
	private long delay;
	private long initialDelay;
	private long period;
	private TimeUnit timeUnit;
	private int scheduleType;
	private Runnable runableTask;
	
	public ScheduledTaskEntry() {
		super();
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskImplementation() {
		return taskImplementation;
	}

	public void setTaskImplementation(String taskImplementation) {
		this.taskImplementation = taskImplementation;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public long getInitialDelay() {
		return initialDelay;
	}

	public void setInitialDelay(long initialDelay) {
		this.initialDelay = initialDelay;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	public int getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(int scheduleType) {
		this.scheduleType = scheduleType;
	}

	public Runnable getRunableTask() {
		return runableTask;
	}

	public void setRunableTask(Runnable runableTask) {
		this.runableTask = runableTask;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((taskImplementation == null) ? 0 : taskImplementation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScheduledTaskEntry other = (ScheduledTaskEntry) obj;
		if (taskImplementation == null) {
			if (other.taskImplementation != null)
				return false;
		} else if (!taskImplementation.equals(other.taskImplementation))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ScheduledTaskEntry [taskName=" + taskName + ", taskImplementation=" + taskImplementation + ", delay="
				+ delay + ", initialDelay=" + initialDelay + ", period=" + period + ", timeUnit=" + timeUnit
				+ ", scheduleType=" + scheduleType + "]";
	}
}
