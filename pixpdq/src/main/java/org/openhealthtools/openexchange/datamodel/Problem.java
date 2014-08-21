/**
 *  Copyright (c) 2009-2011 Misys Open Source Solutions (MOSS) and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    Misys Open Source Solutions - initial API and implementation
 *    -
 */

package org.openhealthtools.openexchange.datamodel;

/**
 *
 * @author : Shantanu Paul
 */
public class Problem {
    private String  probName;
    private String  probCode;
    private String  probCodeSystem;
    private String  probCodeVersion;

    public String getProbName() {
        return probName;
    }

    public void setProbName(String probName) {
        this.probName = probName;
    }

    public String getProbCode() {
        return probCode;
    }

    public void setProbCode(String probCode) {
        this.probCode = probCode;
    }

    public String getProbCodeSystem() {
        return probCodeSystem;
    }

    public void setProbCodeSystem(String probCodeSystem) {
        this.probCodeSystem = probCodeSystem;
    }

    public String getProbCodeVersion() {
        return probCodeVersion;
    }

    public void setProbCodeVersion(String probCodeVersion) {
        this.probCodeVersion = probCodeVersion;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Problem)) return false;

        final Problem problem = (Problem) o;

        if (probCode != null ? !probCode.equals(problem.probCode) : problem.probCode != null) return false;
        if (probCodeSystem != null ? !probCodeSystem.equals(problem.probCodeSystem) : problem.probCodeSystem != null) return false;
        if (probCodeVersion != null ? !probCodeVersion.equals(problem.probCodeVersion) : problem.probCodeVersion != null) return false;
        if (probName != null ? !probName.equals(problem.probName) : problem.probName != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (probName != null ? probName.hashCode() : 0);
        result = 29 * result + (probCode != null ? probCode.hashCode() : 0);
        result = 29 * result + (probCodeSystem != null ? probCodeSystem.hashCode() : 0);
        result = 29 * result + (probCodeVersion != null ? probCodeVersion.hashCode() : 0);
        return result;
    }
}
