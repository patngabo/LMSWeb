<%@include file="include.html"%>
<%@page import="com.gcit.lms.entity.Book"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.gcit.lms.entity.Author"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.service.AdminService"%>

<%
	AdminService adminService = new AdminService();
	List<Author> authors = new ArrayList<>();
	String searchS = request.getParameter("searchString");
	if (searchS == null){
		searchS = "";
	}
    Integer authCount = adminService.getAuthorsCount(searchS);
	int pages = 0;
	if (authCount % 10 > 0) {
		pages = authCount / 10 + 1;
	} else {
		pages = authCount / 10;
	}
	Integer pageNo= (Integer) request.getAttribute("pageNo");
	if (pageNo == null){
		pageNo = 1;
	}
	
	if (request.getAttribute("authors") != null) {
		authors = (List<Author>) (request.getAttribute("authors"));
	} else {
		authors = adminService.getAllAuthors(pageNo, searchS);
	}
%>

<script>
function searchAuthors(){
	$.ajax({
		url: "searchAuthors",
		data:{
			searchString: $('#searchString').val(),
			pageNo: $('#pageNo').val(),
			pages: $('#pages').val()
		}
	}).done(function (data){
		console.log(data);
		$('#viewArea').html(data)
		//alert(data);
	})
}
</script>


<div class="container">
	<!--  jumbotron -->
	<h3>Welcome to GCIT Library Management System</h3>
	<h6>Below is the list of all Authors in featured our Library
		System.</h6>
	${message}
	<div>
		<input type="text" name="searchString" id="searchString"
			placeholder="Author Name" aria-describedby="basic-addon1"
			oninput="searchAuthors()" value=<%=searchS %>> 
			<input type="hidden" name="pageNo" id= "pageNo" value=<%=pageNo%>>
			<input type="hidden" name="pages" id= "pages" value=<%=pages%>>
			
	</div>

	<div id="viewArea">
	
	
	<nav aria-label="Page navigation">
		<ul class="pagination">
			<li><a href="#" aria-label="Previous"> <span
					aria-hidden="true">&laquo;</span>
			</a></li>
			<%
				for(int i=1; i<=pages; i++){
					String search = request.getParameter("searchString");
					if (search == null) {
						  search = "";
						}
			%>
					<li><a href="pageAuthors?pageNo=<%=i%>&searchString=<%=search%>"><%=i%></a></li>
			<%
				}
			%>
			<li><a href="#" aria-label="Next"> <span aria-hidden="true">&raquo;</span>
			</a></li>
		</ul>
	</nav>
	
	
	<table class="table" id="authorsTable">
		<tr>
			<th>Author ID</th>
			<th>Author Name</th>
			<th>Books by Author</th>
			<th>Edit</th>
			<th>Delete</th>
		</tr>
		<%
			for (Author a : authors) {
		%>
		<tr>
			<td><%=authors.indexOf(a) + 1%></td>
			<td><%=a.getAuthorName()%></td>
			<td>
				<%
					for (Book b : a.getBooks()) {
									out.println("'" + b.getTitle() + "'");
								}
				%>
			</td>
			<td><button type="button" class="btn btn-sm btn-primary"
					data-toggle="modal" data-target="#editAuthorModal"
					href="a_editauthor.jsp?authorId=<%=a.getAuthorId()%>">Edit!</button></td>
			<td><button type="button" class="btn btn-sm btn-danger"
					onclick="javascript:location.href='deleteAuthor?authorId=<%=a.getAuthorId()%>'">Delete!</button></td>
		</tr>
		<%
			}
		%>
	</table>
	</div>
	
</div>


<div class="modal fade bs-example-modal-md" tabindex="-1"
	id="editAuthorModal" role="dialog" aria-labelledby="myMediumModalLabel">
	<div class="modal-dialog modal-md" role="document">
		<div class="modal-content"></div>
	</div>
</div>

<script>
	$(document).ready(function() {
		// codes works on all bootstrap modal windows in application
		$('.modal').on('hidden.bs.modal', function(e) {
			$(this).removeData();
		});
	});
</script>