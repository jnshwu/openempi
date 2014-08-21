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

<head>
    <title><fmt:message key="display.title"/></title>
    <meta name="heading" content="<fmt:message key='display.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<p>Below is a list of attributes that were gathered in UploadAction.java.</p>

<div class="separator"></div>

<table class="detail" cellpadding="5">
    <tr>
        <th>Friendly Name:</th>
        <td><s:property value="name"/></td>
    </tr>
    <tr>
        <th>Filename:</th>
        <td><s:property value="fileFileName"/></td>
    </tr>
    <tr>
        <th>File content type:</th>
        <td><s:property value="fileContentType"/></td>
    </tr>
    <tr>
        <th>File size:</th>
        <td><s:property value="file.length()"/> bytes</td>
    </tr>
    <tr>
        <th class="tallCell">File Location:</th>
        <td>The file has been written to: <br />
            <a href="<c:out value="${link}"/>">
            <c:out value="${location}" escapeXml="false"/></a>
        </td>
    </tr>
    <tr>
        <td></td>
        <td class="buttonBar">
            <input class="button" type="button" value="Done"
                onclick="location.href='mainMenu.html'" />
            <input class="button" type="button" style="width: 120px" value="Upload Another"
                onclick="location.href='uploadFile!default.html'" />
        </td>
    </tr>
</table>



