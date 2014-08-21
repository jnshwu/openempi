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
<%@ page import="java.util.*,com.opensymphony.clickstream.Clickstream" %>

<%
Map clickstreams = (Map) application.getAttribute("clickstreams");
String showbots = "false";

if (request.getParameter("showbots") != null)
{
    if (request.getParameter("showbots").equals("true"))
        showbots = "true";
    else if (request.getParameter("showbots").equals("both"))
        showbots = "both";
}
%>

<head>
    <title><fmt:message key="clickstreams.title"/></title>
    <meta name="heading" content="<fmt:message key='clickstreams.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<a href="?showbots=false">No Bots</a> |
<a href="?showbots=true">All Bots</a> |
<a href="?showbots=both">Both</a>

<p>
<% if (clickstreams.keySet().size() == 0) { %>
    No clickstreams in progress.
<% } %>

<%
Iterator it = clickstreams.keySet().iterator();
int count = 0;
while (it.hasNext())
{
    String key = (String)it.next();
    Clickstream stream = (Clickstream)clickstreams.get(key);

    if (showbots.equals("false") && stream.isBot())
    {
        continue;
    }
    else if (showbots.equals("true") && !stream.isBot())
    {
        continue;
    }

    count++;
    try {
%>

<%= count %>. <a href="viewstream.jsp?sid=<%= key %>"><b><%= (stream.getHostname() != null && !stream.getHostname().equals("") ? stream.getHostname() : "Stream") %></b></a> [<%= stream.getStream().size() %> reqs]<br />

<%
    }
    catch (Exception e)
    {
%>
    An error occurred - <%= e %><br />
<%
    }
}
%>
