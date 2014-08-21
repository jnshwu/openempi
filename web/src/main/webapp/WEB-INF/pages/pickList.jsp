<%--


    Copyright (C) 2002-2012 "SYSNET International, Inc."
    support@sysnetint.com [http://www.sysnetint.com]

    This file is part of OpenEMPI.

    OpenEMPI is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ include file="/common/taglibs.jsp"%>
<tr>
    <td>
        <select name="<c:out value="${param.leftId}"/>" multiple="multiple"
            onDblClick="moveSelectedOptions(this,$('<c:out value="${param.rightId}"/>'),true)"
            id="<c:out value="${param.leftId}"/>" size="5">
    <c:if test="${leftList != null}">
        <c:forEach var="list" items="${leftList}" varStatus="status">
            <option value="<c:out value="${list.value}"/>">
                <c:out value="${list.label}" escapeXml="false" />
            </option>
        </c:forEach>
    </c:if>
        </select>
    </td>
    <td class="moveOptions">
        <button name="moveRight" id="moveRight<c:out value="${param.listCount}"/>" type="button" 
            onclick="moveSelectedOptions($('<c:out value="${param.leftId}"/>'),$('<c:out value="${param.rightId}"/>'),true)">
            &gt;&gt;</button><br />
        <button name="moveAllRight" id="moveAllRight<c:out value="${param.listCount}"/>" type="button"
            onclick="moveAllOptions($('<c:out value="${param.leftId}"/>'),$('<c:out value="${param.rightId}"/>'),true)">
            All &gt;&gt;</button><br />
        <button name="moveLeft" id="moveLeft<c:out value="${param.listCount}"/>" type="button"
            onclick="moveSelectedOptions($('<c:out value="${param.rightId}"/>'),$('<c:out value="${param.leftId}"/>'),true)">
            &lt;&lt;</button><br />
        <button name="moveAllLeft" id="moveAllLeft<c:out value="${param.listCount}"/>" type="button"
            onclick="moveAllOptions($('<c:out value="${param.rightId}"/>'),$('<c:out value="${param.leftId}"/>'),true)">
            All &lt;&lt;</button>
    </td>
    <td>
        <select name="<c:out value="${param.rightId}"/>" multiple="multiple"
            id="<c:out value="${param.rightId}"/>" size="5">
    <c:if test="${rightList != null}">
        <c:forEach var="list" items="${rightList}" varStatus="status">
            <option value="<c:out value="${list.value}"/>">
                <c:out value="${list.label}" escapeXml="false"/>
            </option>
        </c:forEach>
    </c:if>
        </select>
    </td>
</tr>