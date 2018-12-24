<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.HashMap" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>AHPPL Reporting Server</title>
<script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

<script type="text/javascript">
	$(document).ready(function(){
		$('.datepicker').datepicker();
	});
</script>
<style>
	.panel-heading
	{
		text-align:center;
	}
/* 	td:first-child */
/* 	{ */
/* 		width: auto !important; */
/* 	} */
	
/* 	td */
/* 	{ */
/* 		width: 100%; */
/* 	} */
	
/* 	.jrPage */
/* 	{ */
/* 		width: 100%; */
/* 	} */
</style>
</head>
<body>
	<%
		if(request.getParameterMap().containsKey("Exception"))
		{
	%>
			<%= request.getAttribute("Exception") %>
	<%
		}
	%>
	<div class="jumbotron" style="text-align: center;">
		<h2>
			<%= request.getAttribute("ReportTitle") %>
		</h2>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
				<div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="panel panel-default">
							<div class="panel-heading">
								<h4>Please Select Report Criteria</h4>
							</div>
							<div class="panel-body">
								<form method="post" action="/TransactionReport/ReportingServlet">
									<div class="row">
										<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
											<div class="form-group">
												<label for="fromCreationDate">
													Select From Date:
												</label>
												<div class="input-group date" id="fromDate"  data-provider="datepicker">
													<input type="text" class="form-control" id="fromCreationDate" />
													<span class="input-group-addon">
								                        <span class="glyphicon glyphicon-calendar"></span>
								                    </span>
												</div>
											</div>
										</div>
										<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
											<div class="form-group">
												<label for="toCreationDate">
													Select To Date:
												</label>
												<div class="input-group date" id="toDate"  data-provider="datepicker">
													<input type="text" class="form-control" data-provider="datepicker" id="toCreationDate" />
													<span class="input-group-addon">
								                        <span class="glyphicon glyphicon-calendar"></span>
								                    </span>
												</div>
											</div>
										</div>
									</div>
									<%@ page import="java.util.Map" %>
									<%@ page import="java.util.ArrayList" %>
									<div class="row">
										<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
											<div class="form-group">
												<label for="Locations_List">Locations:</label>
										      <select name="Location" class="form-control" id="Location">
										      	<option value="">--SELECT--</option>
										        <%
													HashMap Loc_Master = (HashMap)request.getAttribute("Location_Master");
										        	HashMap Lane_Types = (HashMap)request.getAttribute("Lane_Type_Master");
										        	HashMap Lanes = (HashMap)request.getAttribute("Lane_Master");
										        	ArrayList Location_Master_Keys = new ArrayList(Loc_Master.keySet());
										        	ArrayList Location_Master_Values = new ArrayList(Loc_Master.values());
										        	ArrayList Lane_Types_Keys = new ArrayList(Lane_Types.keySet());
										        	ArrayList Lane_Types_Values = new ArrayList(Lane_Types.values());
										        	
										        	ArrayList Lane_Keys = new ArrayList(Lanes.keySet());
										        	ArrayList Lane_Values = new ArrayList(Lanes.values());
										        	
										        	for(int i=0; i < ((HashMap)request.getAttribute("Location_Master")).size(); i++)
										        	{
										        %>
										        		<option value=<%= Location_Master_Keys.get(i) %>><%= Location_Master_Values.get(i) %></option>
										        <%
										        	}
												%>
										      </select>
											</div>
										</div>
										<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
											<div class="form-group">
												<label for="Location_Types_List">Lane Types:</label>
										      <select name="Lane_Type" class="form-control" id="Lane_Type">
										      	<option value="">--SELECT--</option>
										        <%
										        	for(int i=0; i < Lane_Types.size(); i++)
										        	{
										        %>
										        		<option value=<%= Lane_Types_Keys.get(i) %>><%= Lane_Types_Values.get(i) %></option>
										        <%
										        	}
										        %>
										      </select>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
											<div class="form-group">
												<label for="Lanes">Lanes:</label>
										      <select name="Lane" class="form-control" id="Lane">
										      	<option value="">--SELECT--</option>
										        <%
										        	for(int i=0; i < Lanes.size(); i++)
										        	{
										        %>
										        		<option value=<%= Lane_Keys.get(i) %>><%= Lane_Values.get(i) %></option>
										        <%
										        	}
										        %>
										      </select>
											</div>
										</div>
										<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12" style="text-align: right;">
											<input type="hidden" name="ExportOption" value="0" />
											<input type="submit" class="btn btn-default pull-right" name="submit_btn" value="Run" />
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
				<div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs12">
						<div class="panel panel-default">
							<div class="panel-heading">
								<h4>Export Report Data</h4>
							</div>
							<div class="panel-body" style="text-align: center;padding:5%;">
								<div class="row">
									<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12" style="text-align: center;">
										<form method="post" action="/TransactionReport/ReportingServlet">
											<input type="hidden" name="ExportOption" value="1" />
											<button type="submit" class="btn btn-default" id="ExportToPDF" />Export PDF</button>
										</form>						
									</div>
									<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12" style="text-align: center;">
										<form method="post" action="/TransactionReport/ReportingServlet">
											<input type="hidden" name="ExportOption" value="2" />
											<button type="submit" class="btn btn-default" id="ExportAsExcel">Export as Excel</button>
										</form>
									</div>
									<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12" style="text-align: center;">
										<form method="post" action="/TransactionReport/ReportingServlet">
											<input type="hidden" name="ExportOption" value="3" />
											<button type="submit" class="btn btn-default" id="ExportAsExcel">Print Report</button>
										</form>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div id="MainReport">
					<%= request.getAttribute("MainReport") %>
				</div>
			</div>
		</div>
	</div>
</body>
</html>