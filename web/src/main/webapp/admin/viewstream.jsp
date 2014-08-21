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
<%@ page import="java.util.*,
                 com.opensymphony.clickstream.Clickstream,
                 com.opensymphony.clickstream.ClickstreamRequest" %>

<%
if (request.getParameter("sid") == null)
{
    response.sendRedirect("clickstreams.jsp");
    return;
}

Map clickstreams = (Map)application.getAttribute("clickstreams");

Clickstream stream = null;

if (clickstreams.get(request.getParameter("sid")) != null)
{
    stream = (Clickstream)clickstreams.get(request.getParameter("sid"));
}

if (stream == null)
{
    response.sendRedirect("clickstreams.jsp");
    return;
}
%>

<head>
  <title>
    <fmt:message key="viewstream.title"/>
  </title>
  <meta name="heading" content="<fmt:message key='viewstream.heading'/>"/>
  <meta name="menu" content="AdminMenu"/>
</head>

<div style="float: right"><a href="clickstreams.jspjsp">All streams</a></div>

<h2>Clickstream for <%= stream.getHostname() %></h2>

<b>Initial Referrer</b>: <a href="<%= stream.getInitialReferrer() %>"><%= stream.getInitialReferrer() %></a><br>
<b>Hostname</b>: <%= stream.getHostname() %><br>
<b>Session ID</b>: <%= request.getParameter("sid") %><br>
<b>Bot</b>: <%= stream.isBot() ? "Yes" : "No" %><br>
<b>Stream Start</b>: <%= stream.getStart() %><br>
<b>Last Request</b>: <%= stream.getLastRequest() %><br>

<% long streamLength = stream.getLastRequest().getTime() - stream.getStart().getTime(); %>
<b>Session Length</b>:
    <%= (streamLength > 3600000 ?
        " " + (streamLength / 3600000) + " hours" : "") +
    (streamLength > 60000 ?
        " " + ((streamLength / 60000) % 60) + " minutes" : "") +
    (streamLength > 1000 ?
        " " + ((streamLength / 1000) % 60) + " seconds" : "") %><br>

<b># of Requests</b>: <%= stream.getStream().size() %>

<p><b>Click stream</b>:</p>

<table border="0" cellpadding="2">
<%
Iterator clickstreamIt = stream.getStream().iterator();

int count = 0;
while (clickstreamIt.hasNext())
{
    count++;
    String click = ((ClickstreamRequest)clickstreamIt.next()).toString();
%>
<tr><td><%= count %>:</td><td><a href="http://<%= click %>"><%= click %></a></td></tr>
<%
}
%>
</table>
