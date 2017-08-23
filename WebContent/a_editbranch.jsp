<%@page import="com.gcit.lms.entity.Branch"%>
<%@page import="com.gcit.lms.service.AdminService"%>
<%
	Integer branchId = Integer.parseInt(request.getParameter("branchId"));
	AdminService service = new AdminService();
	Branch branch = service.getBranchByPK(branchId);
%>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title" id="myModalLabel">Edit Publisher Details</h4>
</div>
<form action="editBranch" method="post">
	<div class="modal-body">
		<div class = "row">
		Edit Branch Title: <input type="text" name="branchName"
			value="<%=branch.getBranchName()%>"><br /> 
			<input type="hidden" name="branchId" value=<%=branch.getBranchId()%>>
			</div>
			
			
		<div class = "row">
		Edit Branch Address: <input type="text" name="branchAddress"
			value="<%=branch.getBranchAddress()%>"><br /> 
			<input type="hidden" name="branchId" value=<%=branch.getBranchId()%>>
			</div>
			
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		<button type="submit" class="btn btn-primary">Edit Branch</button>
	</div>
</form>